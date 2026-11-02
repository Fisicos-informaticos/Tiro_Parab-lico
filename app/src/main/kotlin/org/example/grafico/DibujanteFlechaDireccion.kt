package org.example.grafico

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import org.example.modelo.Vector2D
import kotlin.math.sqrt

class DibujanteFlechaDireccion(
    private val gc: GraphicsContext,
    private val colorLinea: Color
) {
    fun dibujar(inicio: Vector2D, fin: Vector2D) {
        gc.stroke = colorLinea
        gc.lineWidth = 3.0
        gc.setLineDashes(8.0, 4.0)
        gc.strokeLine(inicio.x(), inicio.y(), fin.x(), fin.y())
        gc.setLineDashes()
        dibujarPuntaFlecha(fin, inicio)
    }

    private fun dibujarPuntaFlecha(fin: Vector2D, inicio: Vector2D) {
        val dx = fin.x() - inicio.x()
        val dy = fin.y() - inicio.y()
        val len = sqrt(dx * dx + dy * dy)
        if (len < 1.0) return

        val nx = dx / len
        val ny = dy / len

        gc.fill = colorLinea
        gc.fillPolygon(
            doubleArrayOf(fin.x(), fin.x() - ny * 8.0 - nx * 12.0, fin.x() + ny * 8.0 - nx * 12.0),
            doubleArrayOf(fin.y(), fin.y() + nx * 8.0 - ny * 12.0, fin.y() - nx * 8.0 - ny * 12.0),
            3
        )
    }
}
