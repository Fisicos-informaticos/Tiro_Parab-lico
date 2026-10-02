package org.example.fisica

import org.example.modelo.Pared
import org.example.modelo.Proyectil
import org.example.modelo.Vector2D
import org.example.modelo.colision.Penetracion
import org.example.modelo.colision.Transformaciones
import kotlin.math.ceil
import kotlin.math.exp

class MotorFisico(
    private val ambiente: Ambiente,
    paredes: List<Pared> = emptyList()
) {
    private val listaParedes = paredes.toMutableList()

    val sueloY: Double get() = ambiente.sueloY
    val techoY: Double get() = ambiente.techoY

    fun agregarPared(pared: Pared) {
        listaParedes.add(pared)
    }

    fun removerPared(pared: Pared): Boolean = listaParedes.remove(pared)

    fun limpiarParedes() {
        listaParedes.clear()
    }

    fun obtenerParedes(): List<Pared> = listaParedes.toList()

    fun simularPaso(proyectil: Proyectil, deltaTime: Double) {
        if (deltaTime <= 0.0) return

        val fuerzaTotal = ambiente.gravedad * proyectil.masa + fuerzaArrastre(proyectil)
        val velocidadTrasFuerza = proyectil.velocidad + fuerzaTotal * deltaTime
        val desplazamiento = velocidadTrasFuerza.magnitud * deltaTime
        val cantidadSubpasos = ceil(desplazamiento / MAXIMA_DISTANCIA_POR_SUBPASO)
            .toInt()
            .coerceIn(1, MAXIMOS_SUBPASOS)
        val deltaSubpaso = deltaTime / cantidadSubpasos

        repeat(cantidadSubpasos) {
            proyectil.aplicarFuerza(fuerzaTotal, deltaSubpaso)
            avanzarOrientacion(proyectil, deltaSubpaso)
            resolverColisiones(proyectil, deltaSubpaso)
        }
    }

    /** Freno del aire: proporcional al cuadrado de la rapidez y opuesto al movimiento. */
    private fun fuerzaArrastre(p: Proyectil): Vector2D {
        if (p.coeficienteArrastre <= 0.0) return Vector2D(0.0, 0.0)
        val rapidez = p.velocidad.magnitud
        if (rapidez < UMBRAL_ARRASTRE) return Vector2D(0.0, 0.0)
        return p.velocidad * (-p.coeficienteArrastre * rapidez * p.masa)
    }

    /** Giro propio y orientacion aerodinamica de las formas alargadas. */
    private fun avanzarOrientacion(p: Proyectil, deltaTime: Double) {
        p.angulo += p.velocidadAngular * deltaTime
        if (p.giro <= 0.0 || !p.forma.alineable) return
        if (p.velocidad.magnitud < UMBRAL_ARRASTRE) return

        val tasa = p.giro * TASA_ORIENTACION * deltaTime
        p.velocidadAngular *= exp(-tasa)
        val objetivo = Transformaciones.anguloDe(p.velocidad) -
            Transformaciones.anguloDe(p.forma.ejePrincipal)
        p.angulo += Transformaciones.diferenciaAngular(objetivo, p.angulo) * tasa
    }

    private fun resolverColisiones(p: Proyectil, deltaTime: Double) {
        val dtRozamiento = deltaTime / ITERACIONES_RESOLUCION
        repeat(ITERACIONES_RESOLUCION) {
            for (pared in listaParedes) {
                resolverColisionPared(p, pared)
            }
            resolverColisionSuelo(p, dtRozamiento)
            resolverColisionTecho(p, dtRozamiento)
        }
    }

    private fun resolverColisionPared(p: Proyectil, pared: Pared) {
        val puntoMasCercano = pared.puntoMasCercano(p.posicion)
        val delta = p.posicion - puntoMasCercano
        val distancia = delta.magnitud
        if (distancia > pared.grosor / 2.0 + p.forma.radioEnvoltura + CORRECCION_POSICION) return

        val normal =
            if (distancia > EPSILON) delta.normalizado else normalDeRespaldo(p, pared)
        val cara = puntoMasCercano + normal * (pared.grosor / 2.0)
        val contacto = p.forma.contactoPlano(cara, p.posicion, p.angulo, normal) ?: return

        aplicarImpacto(p, contacto)
        separar(p, contacto)
    }

    private fun resolverColisionSuelo(p: Proyectil, deltaTime: Double) {
        val punto = Vector2D(p.posicion.x, sueloY)
        val contacto = p.forma.contactoPlano(punto, p.posicion, p.angulo, NORMAL_ARRIBA) ?: return

        aplicarImpacto(p, contacto)
        separar(p, contacto)
        aplicarRozamiento(p, contacto.normal, deltaTime)
    }

    private fun resolverColisionTecho(p: Proyectil, deltaTime: Double) {
        val punto = Vector2D(p.posicion.x, techoY)
        val contacto = p.forma.contactoPlano(punto, p.posicion, p.angulo, NORMAL_ABAJO) ?: return

        aplicarImpacto(p, contacto)
        separar(p, contacto)
        aplicarRozamiento(p, contacto.normal, deltaTime)
    }

    private fun normalDeRespaldo(p: Proyectil, pared: Pared): Vector2D {
        val direccion = pared.direccion.normalizado
        if (direccion.magnitud <= EPSILON) return NORMAL_ARRIBA

        val normal = Vector2D(-direccion.y, direccion.x)
        val velocidadNormal = p.velocidad.x * normal.x + p.velocidad.y * normal.y
        return if (velocidadNormal <= 0.0) normal else -normal
    }

    /** Rebote y giro: la velocidad se separa en normal y tangencial al contacto. */
    private fun aplicarImpacto(p: Proyectil, contacto: Penetracion) {
        val normal = contacto.normal
        val tangente = Vector2D(normal.y, -normal.x)
        val velocidadNormal = p.velocidad.x * normal.x + p.velocidad.y * normal.y
        if (velocidadNormal >= 0.0) return

        val velocidadTangente = p.velocidad.x * tangente.x + p.velocidad.y * tangente.y
        val nuevaTangente =
            velocidadTangente * exp(-p.coeficienteFriccion * TIEMPO_ROZAMIENTO_IMPACTO)

        p.velocidad = normal * (-velocidadNormal * p.coeficienteRestitucion) +
            tangente * nuevaTangente
        p.velocidadAngular = -nuevaTangente / p.forma.radioEnvoltura * p.giro
    }

    /** Rozamiento mientras la pelota permanece en contacto con la superficie. */
    private fun aplicarRozamiento(p: Proyectil, normal: Vector2D, deltaTime: Double) {
        val velocidadNormal = p.velocidad.x * normal.x + p.velocidad.y * normal.y
        val tangente = p.velocidad - normal * velocidadNormal
        p.velocidad = normal * velocidadNormal + tangente * exp(-p.coeficienteFriccion * deltaTime)
    }

    private fun separar(p: Proyectil, contacto: Penetracion) {
        p.posicion = p.posicion + contacto.normal * (contacto.profundidad + CORRECCION_POSICION)
    }

    fun estaEnReposo(proyectil: Proyectil): Boolean {
        val limiteSuelo = sueloY - proyectil.radio - 2.0
        return proyectil.posicion.y >= limiteSuelo && proyectil.velocidad.magnitud < 0.5
    }

    companion object {
        private const val MAXIMA_DISTANCIA_POR_SUBPASO = 4.0
        private const val MAXIMOS_SUBPASOS = 256
        private const val ITERACIONES_RESOLUCION = 2
        private const val CORRECCION_POSICION = 0.01
        private const val EPSILON = 1e-9

        /** Rapidez por debajo de la cual el aire y la orientacion no hacen nada. */
        private const val UMBRAL_ARRASTRE = 1.0

        /** Constante de orientacion: como de rapido sigue el vuelo a su forma. */
        private const val TASA_ORIENTACION = 4.0

        /** Tiempo equivalente de rozamiento que aplica cada golpe. */
        private const val TIEMPO_ROZAMIENTO_IMPACTO = 0.05

        private val NORMAL_ARRIBA = Vector2D(0.0, -1.0)
        private val NORMAL_ABAJO = Vector2D(0.0, 1.0)
    }
}
