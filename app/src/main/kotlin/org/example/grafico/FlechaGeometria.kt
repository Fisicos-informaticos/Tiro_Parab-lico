package org.example.grafico

import org.example.modelo.Vector2D

internal class FlechaGeometria(
    private val punta: Vector2D,
    private val direccion: Vector2D,
    private val perpendicular: Vector2D,
    private val cabeza: Double,
    private val largo: Double
) {
    fun punta(): Vector2D = punta
    fun direccion(): Vector2D = direccion
    fun perpendicular(): Vector2D = perpendicular
    fun cabeza(): Double = cabeza
    fun largo(): Double = largo
    fun esVisible(minLargo: Double): Boolean = largo >= minLargo
}
