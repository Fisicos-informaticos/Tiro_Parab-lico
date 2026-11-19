package org.example.grafico

import javafx.scene.paint.Color
import kotlin.math.abs

internal object PaletaPelota {

    fun color(valor: Int): Color =
        Color.rgb((valor shr 16) and 0xFF, (valor shr 8) and 0xFF, valor and 0xFF)

    fun tono(valor: Int, cantidad: Double): Color {
        val destino = if (cantidad >= 0.0) 255.0 else 0.0
        val mezcla = abs(cantidad).coerceIn(0.0, 1.0)
        val rojo = (valor shr 16) and 0xFF
        val verde = (valor shr 8) and 0xFF
        val azul = valor and 0xFF
        return Color.rgb(
            (rojo + (destino - rojo) * mezcla).toInt().coerceIn(0, 255),
            (verde + (destino - verde) * mezcla).toInt().coerceIn(0, 255),
            (azul + (destino - azul) * mezcla).toInt().coerceIn(0, 255)
        )
    }
}
