package org.example.grafico

import javafx.scene.paint.Color
import org.example.modelo.Vector2D

internal class TrazoVector(
    private val origen: Vector2D,
    private val magnitud: Vector2D,
    private val escala: Double,
    private val color: Color,
    private val etiqueta: String
) {
    fun origen(): Vector2D = origen
    fun magnitud(): Vector2D = magnitud
    fun escala(): Double = escala
    fun color(): Color = color
    fun etiqueta(): String = etiqueta
    fun esNulo(umbral: Double = 1e-6): Boolean = magnitud.magnitud() < umbral
}
