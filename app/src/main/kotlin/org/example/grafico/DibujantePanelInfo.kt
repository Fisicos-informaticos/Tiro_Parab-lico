package org.example.grafico

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.text.Font

class DibujantePanelInfo(
    private val gc: GraphicsContext,
    private val colorTexto: Color
) {
    fun dibujar(lineas: List<String>) {
        gc.fill = Color.color(0.0, 0.0, 0.0, 0.6)
        gc.fillRoundRect(10.0, 10.0, 290.0, 24.0 + lineas.size * 18.0, 10.0, 10.0)

        gc.fill = colorTexto
        gc.font = Font.font("Monospace", 14.0)

        var yTexto = 30.0
        for (linea in lineas) {
            gc.fillText(linea, 20.0, yTexto)
            yTexto += 18.0
        }
    }
}
