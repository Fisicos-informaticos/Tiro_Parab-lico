package org.example.fisica

import org.example.modelo.Pared
import org.example.modelo.Proyectil
import org.example.modelo.Vector2D
import kotlin.math.ceil

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

        val fuerzaGravedad = ambiente.gravedad * proyectil.masa
        val velocidadTrasFuerza = proyectil.velocidad + fuerzaGravedad * deltaTime
        val desplazamiento = velocidadTrasFuerza.magnitud * deltaTime
        val cantidadSubpasos = ceil(desplazamiento / MAXIMA_DISTANCIA_POR_SUBPASO)
            .toInt()
            .coerceIn(1, MAXIMOS_SUBPASOS)
        val deltaSubpaso = deltaTime / cantidadSubpasos

        repeat(cantidadSubpasos) {
            proyectil.aplicarFuerza(fuerzaGravedad, deltaSubpaso)
            resolverColisiones(proyectil)
        }
    }

    private fun resolverColisiones(proyectil: Proyectil) {
        repeat(ITERACIONES_RESOLUCION) {
            for (pared in listaParedes) {
                resolverColisionPared(proyectil, pared)
            }
            resolverColisionSuelo(proyectil)
            resolverColisionTecho(proyectil)
        }
    }

    private fun resolverColisionPared(p: Proyectil, pared: Pared) {
        val puntoMasCercano = pared.puntoMasCercano(p.posicion)
        val desplazamientoCentro = p.posicion - puntoMasCercano
        val distancia = desplazamientoCentro.magnitud
        val distanciaLimite = p.radio + pared.grosor / 2.0
        if (distancia >= distanciaLimite) return

        val normal = if (distancia > EPSILON) {
            desplazamientoCentro.normalizado
        } else {
            normalDeRespaldo(p, pared)
        }
        val velocidadNormal = p.velocidad.x * normal.x + p.velocidad.y * normal.y
        if (velocidadNormal < 0.0) {
            val correccionVelocidad = normal * ((1.0 + p.coeficienteRestitucion) * velocidadNormal)
            p.velocidad = p.velocidad - correccionVelocidad
        }

        p.posicion = puntoMasCercano + normal * (distanciaLimite + CORRECCION_POSICION)
    }

    private fun normalDeRespaldo(p: Proyectil, pared: Pared): Vector2D {
        val direccion = pared.direccion.normalizado
        if (direccion.magnitud <= EPSILON) return Vector2D(0.0, -1.0)

        val normal = Vector2D(-direccion.y, direccion.x)
        val velocidadNormal = p.velocidad.x * normal.x + p.velocidad.y * normal.y
        return if (velocidadNormal <= 0.0) normal else normal * -1.0
    }

    private fun resolverColisionSuelo(p: Proyectil) {
        val limite = sueloY - p.radio
        if (p.posicion.y < limite) return

        p.posicion = Vector2D(p.posicion.x, limite - CORRECCION_POSICION)
        if (p.velocidad.y > 0.0) {
            p.velocidad = Vector2D(p.velocidad.x, -p.velocidad.y * p.coeficienteRestitucion)
        }
    }

    private fun resolverColisionTecho(p: Proyectil) {
        val limite = techoY + p.radio
        if (p.posicion.y > limite) return

        p.posicion = Vector2D(p.posicion.x, limite + CORRECCION_POSICION)
        if (p.velocidad.y < 0.0) {
            p.velocidad = Vector2D(p.velocidad.x, -p.velocidad.y * p.coeficienteRestitucion)
        }
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
    }
}
