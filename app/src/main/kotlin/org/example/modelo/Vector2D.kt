package org.example.modelo

/**
 * Vector inmutable del plano. La representacion interna queda oculta: solo se
 * puede consultar a traves de metodos y se opera con los operadores del tipo.
 */
class Vector2D(private val x: Double, private val y: Double) {
    fun x(): Double = x
    fun y(): Double = y

    operator fun plus(other: Vector2D) = Vector2D(x + other.x, y + other.y)
    operator fun minus(other: Vector2D) = Vector2D(x - other.x, y - other.y)
    operator fun times(scalar: Double) = Vector2D(x * scalar, y * scalar)
    operator fun div(scalar: Double) = Vector2D(x / scalar, y / scalar)
    operator fun unaryMinus() = Vector2D(-x, -y)

    fun magnitud(): Double = kotlin.math.sqrt(x * x + y * y)

    fun normalizado(): Vector2D {
        val m = magnitud()
        return if (m > 0.0) this / m else Vector2D(0.0, 0.0)
    }

    fun conX(nuevoX: Double): Vector2D = Vector2D(nuevoX, y)

    fun conY(nuevoY: Double): Vector2D = Vector2D(x, nuevoY)

    fun distanciaA(other: Vector2D): Double = (this - other).magnitud()

    override fun equals(other: Any?): Boolean =
        other is Vector2D && x == other.x && y == other.y

    override fun hashCode(): Int = 31 * x.hashCode() + y.hashCode()

    override fun toString(): String = "Vector2D(x=$x, y=$y)"
}
