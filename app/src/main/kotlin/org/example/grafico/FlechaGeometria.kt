package org.example.grafico

import org.example.modelo.Vector2D

internal class FlechaGeometria(
    punta: Vector2D,
    direccion: Vector2D,
    perpendicular: Vector2D,
    cabeza: Double,
    largo: Double
) {
    private val datos = DatosFlechaGeometria(punta, direccion, perpendicular, cabeza, largo)

    fun punta(): Vector2D = datos.punta
    fun direccion(): Vector2D = datos.direccion
    fun perpendicular(): Vector2D = datos.perpendicular
    fun cabeza(): Double = datos.cabeza
    fun largo(): Double = datos.largo
    fun esVisible(minLargo: Double): Boolean = datos.esVisible(minLargo)

    private data class DatosFlechaGeometria(
        val punta: Vector2D,
        val direccion: Vector2D,
        val perpendicular: Vector2D,
        val cabeza: Double,
        val largo: Double
    ) {
        fun esVisible(minLargo: Double): Boolean = largo >= minLargo

        fun puntos(): List<Vector2D> = listOf(punta, direccion, perpendicular)

        fun triangulo(): List<Any> = listOf(punta, direccion, perpendicular, cabeza, largo)

        fun resumen(): String =
            "flecha(punta=$punta, direccion=$direccion, perpendicular=$perpendicular, cabeza=$cabeza, largo=$largo)"

        fun esValida(): Boolean =
            largo > 0 && cabeza > 0 && direccion !== perpendicular && punta !== direccion
    }
}
