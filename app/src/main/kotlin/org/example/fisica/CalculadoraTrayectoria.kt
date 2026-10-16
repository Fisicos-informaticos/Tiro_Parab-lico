package org.example.fisica

import org.example.modelo.Vector2D
import kotlin.math.cos
import kotlin.math.sin

object CalculadoraTrayectoria {

    fun calcularVelocidadInicial(v0: Double, anguloGrados: Double): Vector2D {
        val rad = Math.toRadians(anguloGrados)
        return Vector2D(cos(rad) * v0, -sin(rad) * v0)
    }

    fun calcularAlcanceMaximo(v0: Double, gravedad: Double): Double {
        return v0 * v0 / gravedad
    }

    fun calcularAlturaMaxima(v0: Double, anguloGrados: Double, gravedad: Double): Double {
        val rad = Math.toRadians(anguloGrados)
        val vy = sin(rad) * v0
        return (vy * vy) / (2.0 * gravedad)
    }

    fun calcularTiempoVuelo(v0: Double, anguloGrados: Double, gravedad: Double): Double {
        val rad = Math.toRadians(anguloGrados)
        val vy = sin(rad) * v0
        return (2.0 * vy) / gravedad
    }
}
