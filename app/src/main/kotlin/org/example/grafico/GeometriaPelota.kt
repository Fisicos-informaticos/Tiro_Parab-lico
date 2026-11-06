package org.example.grafico

import javafx.scene.canvas.GraphicsContext
import org.example.modelo.Vector2D
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

internal object GeometriaPelota {

    fun trazar(gc: GraphicsContext, puntos: List<Vector2D>) {
        gc.beginPath()
        if (puntos.isNotEmpty()) {
            gc.moveTo(puntos[0].x(), puntos[0].y())
            for (i in 1 until puntos.size) {
                gc.lineTo(puntos[i].x(), puntos[i].y())
            }
            gc.closePath()
        }
    }

    fun poligonoLocal(lados: Int, radio: Double, anguloInicial: Double): List<Vector2D> =
        (0 until lados).map { i ->
            val angulo = anguloInicial + 2.0 * PI * i / lados
            Vector2D(cos(angulo) * radio, sin(angulo) * radio)
        }
}
