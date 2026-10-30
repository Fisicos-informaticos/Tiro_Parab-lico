package org.example.fisica

import org.example.modelo.Pared
import org.example.modelo.Proyectil
import org.example.modelo.Vector2D
import org.example.modelo.colision.Penetracion
import kotlin.math.exp

/**
 * Resuelve los contactos del proyectil con paredes, suelo y techo. Se mantiene
 * separado del motor para que este solo se ocupe de integrar las fuerzas.
 */
class ResolutorColisiones(
    private val paredes: MutableList<Pared>,
    private var ambiente: Ambiente
) {

    fun cambiarAmbiente(ambiente: Ambiente) {
        this.ambiente = ambiente
    }

    fun resolver(proyectil: Proyectil, deltaTime: Double): List<Pared> {
        val dtRozamiento = deltaTime / ITERACIONES_RESOLUCION
        val paredesRotas = mutableListOf<Pared>()
        repeat(ITERACIONES_RESOLUCION) {
            for (pared in paredes.toList()) {
                if (!resolverColisionPared(proyectil, pared)) continue

                paredes.removeAll { it === pared }
                paredesRotas.add(pared)
            }
            resolverColisionSuelo(proyectil, dtRozamiento)
            resolverColisionTecho(proyectil, dtRozamiento)
        }
        return paredesRotas
    }

    /** Devuelve true si el golpe deja la pared rota: entonces no hay rebote. */
    private fun resolverColisionPared(p: Proyectil, pared: Pared): Boolean {
        val puntoMasCercano = pared.puntoMasCercano(p.posicion())
        val delta = p.posicion() - puntoMasCercano
        val distancia = delta.magnitud()
        if (distancia > pared.grosor() / 2.0 + p.forma().radioEnvoltura() + CORRECCION_POSICION) return false

        val normal =
            if (distancia > EPSILON) delta.normalizado() else normalDeRespaldo(p, pared)
        val cara = puntoMasCercano + normal * (pared.grosor() / 2.0)
        val contacto = p.forma().contactoPlano(cara, p.posicion(), p.angulo(), normal) ?: return false

        val velocidadNormal = p.velocidad().x() * normal.x() + p.velocidad().y() * normal.y()
        if (velocidadNormal < 0.0 && pared.registrarImpacto(energiaDeImpacto(p, velocidadNormal), p.tipo())) {
            return true
        }

        aplicarImpacto(p, contacto)
        separar(p, contacto)
        return false
    }

    /** Energia que el golpe deposita en el muro: solo cuenta la componente normal. */
    private fun energiaDeImpacto(p: Proyectil, velocidadNormal: Double): Double =
        0.5 * p.masa() * velocidadNormal * velocidadNormal

    private fun resolverColisionSuelo(p: Proyectil, deltaTime: Double) {
        val punto = Vector2D(p.posicion().x(), ambiente.sueloY())
        val contacto = p.forma().contactoPlano(punto, p.posicion(), p.angulo(), NORMAL_ARRIBA) ?: return

        aplicarImpacto(p, contacto)
        separar(p, contacto)
        aplicarRozamiento(p, contacto.normal(), deltaTime)
    }

    private fun resolverColisionTecho(p: Proyectil, deltaTime: Double) {
        val punto = Vector2D(p.posicion().x(), ambiente.techoY())
        val contacto = p.forma().contactoPlano(punto, p.posicion(), p.angulo(), NORMAL_ABAJO) ?: return

        aplicarImpacto(p, contacto)
        separar(p, contacto)
        aplicarRozamiento(p, contacto.normal(), deltaTime)
    }

    private fun normalDeRespaldo(p: Proyectil, pared: Pared): Vector2D {
        val direccion = pared.direccion().normalizado()
        if (direccion.magnitud() <= EPSILON) return NORMAL_ARRIBA

        val normal = Vector2D(-direccion.y(), direccion.x())
        val velocidadNormal = p.velocidad().x() * normal.x() + p.velocidad().y() * normal.y()
        return if (velocidadNormal <= 0.0) normal else -normal
    }

    /** Rebote y giro: la velocidad se separa en normal y tangencial al contacto. */
    private fun aplicarImpacto(p: Proyectil, contacto: Penetracion) {
        val normal = contacto.normal()
        val tangente = Vector2D(normal.y(), -normal.x())
        val velocidadNormal = p.velocidad().x() * normal.x() + p.velocidad().y() * normal.y()
        if (velocidadNormal >= 0.0) return

        val velocidadTangente = p.velocidad().x() * tangente.x() + p.velocidad().y() * tangente.y()
        val nuevaTangente =
            velocidadTangente * exp(-p.coeficienteFriccion() * TIEMPO_ROZAMIENTO_IMPACTO)

        // Golpes flojos no rebotan: sin esto la gravedad realimenta micro-rebotes
        // que dejan a la pelota botando para siempre y el tiro nunca termina.
        val reboteNormal =
            if (-velocidadNormal < UMBRAL_REBOTE) 0.0
            else -velocidadNormal * p.coeficienteRestitucion()

        p.fijarVelocidad(normal * reboteNormal + tangente * nuevaTangente)
        p.fijarVelocidadAngular(-nuevaTangente / p.forma().radioEnvoltura() * p.giro())
    }

    /** Rozamiento mientras la pelota permanece en contacto con la superficie. */
    private fun aplicarRozamiento(p: Proyectil, normal: Vector2D, deltaTime: Double) {
        val velocidadNormal = p.velocidad().x() * normal.x() + p.velocidad().y() * normal.y()
        val tangente = p.velocidad() - normal * velocidadNormal
        p.fijarVelocidad(normal * velocidadNormal + tangente * exp(-p.coeficienteFriccion() * deltaTime))
    }

    private fun separar(p: Proyectil, contacto: Penetracion) {
        p.fijarPosicion(contacto.separarDesde(p.posicion(), CORRECCION_POSICION))
    }

    private companion object {
        const val ITERACIONES_RESOLUCION = 2
        const val CORRECCION_POSICION = 0.01
        const val EPSILON = 1e-9
        const val UMBRAL_REBOTE = 40.0
        const val TIEMPO_ROZAMIENTO_IMPACTO = 0.05

        val NORMAL_ARRIBA = Vector2D(0.0, -1.0)
        val NORMAL_ABAJO = Vector2D(0.0, 1.0)
    }
}
