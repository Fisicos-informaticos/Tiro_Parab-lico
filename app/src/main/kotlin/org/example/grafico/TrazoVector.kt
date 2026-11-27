package org.example.grafico

import javafx.scene.paint.Color
import org.example.modelo.Vector2D

internal class TrazoVector(
    origen: Vector2D,
    magnitud: Vector2D,
    escala: Double,
    color: Color,
    etiqueta: String
) {
    private val datos = DatosTrazoVector(origen, magnitud, escala, color, etiqueta)

    fun origen(): Vector2D = datos.origen
    fun magnitud(): Vector2D = datos.magnitud
    fun escala(): Double = datos.escala
    fun color(): Color = datos.color
    fun etiqueta(): String = datos.etiqueta
    fun esNulo(umbral: Double = 1e-6): Boolean = datos.esNulo(umbral)

    private data class DatosTrazoVector(
        val origen: Vector2D,
        val magnitud: Vector2D,
        val escala: Double,
        val color: Color,
        val etiqueta: String
    ) {
        fun esNulo(umbral: Double = 1e-6): Boolean = magnitud.magnitud() < umbral

        fun componentes(): List<Any> = listOf(origen, magnitud, escala, color, etiqueta)

        fun escalado(): Double = magnitud.magnitud() * escala

        fun resumen(): String =
            "trazo(origen=$origen, magnitud=$magnitud, escala=$escala, color=$color, etiqueta=$etiqueta)"

        fun esValido(): Boolean =
            escala > 0 && etiqueta.isNotBlank() && magnitud !== origen && color.opacity > 0
    }
}
