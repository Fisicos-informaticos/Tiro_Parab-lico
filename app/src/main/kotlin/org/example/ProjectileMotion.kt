package org.example

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class ProjectileMotion(
    private val initialVelocity: Double,
    private val angleDegrees: Double,
    private val gravity: Double = 9.81,
) {
    private val angleRad: Double = Math.toRadians(angleDegrees)

    private val vx: Double = initialVelocity * cos(angleRad)
    private val vy: Double = initialVelocity * sin(angleRad)

    val tiempoDeVuelo: Double
        get() = 2 * vy / gravity

    val alturaMaxima: Double
        get() = (vy * vy) / (2 * gravity)

    val alcanceMaximo: Double
        get() = vx * tiempoDeVuelo

    fun posicionEn(t: Double): Pair<Double, Double> {
        val x = vx * t
        val y = vy * t - 0.5 * gravity * t * t
        return x to y
    }

    fun alcanceHastaAltura(alturaObjetivo: Double): Double? {
        val disc = vy * vy - 2 * gravity * alturaObjetivo
        if (disc < 0) return null
        val t1 = (vy - sqrt(disc)) / gravity
        val t2 = (vy + sqrt(disc)) / gravity
        val t = if (alturaObjetivo > 0) t2 else t1
        return vx * t
    }

    fun datosCompletos(): String {
        return """
            |Velocidad inicial : $initialVelocity m/s
            |Angulo            : $angleDegrees grados
            |Componente Vx     : ${"%.2f".format(vx)} m/s
            |Componente Vy     : ${"%.2f".format(vy)} m/s
            |Tiempo de vuelo   : ${"%.2f".format(tiempoDeVuelo)} s
            |Altura maxima     : ${"%.2f".format(alturaMaxima)} m
            |Alcance maximo    : ${"%.2f".format(alcanceMaximo)} m
        """.trimMargin()
    }

    fun trayectoria(pasos: Int = 10): List<Pair<Double, Double>> {
        if (pasos <= 0) return emptyList()
        val dt = tiempoDeVuelo / pasos
        return (0..pasos).map { posicionEn(it * dt) }
    }
}

const val GRAVEDAD = 9.81
