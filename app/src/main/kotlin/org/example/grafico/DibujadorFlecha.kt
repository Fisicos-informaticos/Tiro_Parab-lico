package org.example.grafico

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.text.Font
import org.example.modelo.Vector2D

internal object DibujadorFlecha {
    fun dibujar(gc: GraphicsContext, trazo: TrazoVector, g: FlechaGeometria) {
        gc.stroke = trazo.color()
        gc.lineWidth = 3.0
        gc.setLineDashes()
        gc.strokeLine(trazo.origen().x(), trazo.origen().y(), g.punta().x(), g.punta().y())
        gc.fill = trazo.color()
        gc.fillPolygon(cabezaX(g), cabezaY(g), 3)
        val p = trazo.origen() + g.direccion() * (g.largo() * 0.55)
        gc.fill = trazo.color()
        gc.font = Font.font("Monospace", 13.0)
        gc.fillText(trazo.etiqueta(), p.x() + g.perpendicular().x() * 10.0, p.y() + g.perpendicular().y() * 10.0)
    }

    private fun cabezaX(g: FlechaGeometria): DoubleArray {
        val baseX = g.punta().x() - g.direccion().x() * g.cabeza()
        return doubleArrayOf(g.punta().x(), baseX + g.perpendicular().x() * 6.0, baseX - g.perpendicular().x() * 6.0)
    }

    private fun cabezaY(g: FlechaGeometria): DoubleArray {
        val baseY = g.punta().y() - g.direccion().y() * g.cabeza()
        return doubleArrayOf(g.punta().y(), baseY + g.perpendicular().y() * 6.0, baseY - g.perpendicular().y() * 6.0)
    }
}
