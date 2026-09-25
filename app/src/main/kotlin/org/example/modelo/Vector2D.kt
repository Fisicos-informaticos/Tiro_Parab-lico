package org.example.modelo

data class Vector2D(val x: Double, val y: Double) {
    operator fun plus(other: Vector2D) = Vector2D(x + other.x, y + other.y)
    operator fun minus(other: Vector2D) = Vector2D(x - other.x, y - other.y)
    operator fun times(scalar: Double) = Vector2D(x * scalar, y * scalar)
    operator fun div(scalar: Double) = Vector2D(x / scalar, y / scalar)

    val magnitud: Double get() = kotlin.math.sqrt(x * x + y * y)
    val normalizado: Vector2D
        get() {
            val m = magnitud
            return if (m > 0.0) this / m else Vector2D(0.0, 0.0)
        }

    fun distanciaA(other: Vector2D): Double = (this - other).magnitud
}
