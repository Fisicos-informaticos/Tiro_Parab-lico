package org.example.grafico

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import org.example.modelo.Vector2D

private val CESPED = Color.web("#5cb85c")
private val CESPED_OSCURO = Color.web("#3f9142")
private val CESPED_CLARO = Color.web("#7fd07f")
private val META = Color.web("#e74c3c")
private val TRONCO = Color.web("#7B4A21")
private val COPA_SOMBRA = Color.web("#1b5e20")
private val COPA_CLARA = Color.web("#43a047")
private val COPA_MEDIA = Color.web("#2e7d32")
private val COPA_BRILLO = Color.web("#66bb6a")

data class Brizna(private val x: Double, private val alto: Double, private val ancho: Double, private val brillo: Double) {
    fun x(): Double = x
    fun alto(): Double = alto
    fun ancho(): Double = ancho
    fun brillo(): Double = brillo
    fun dibujar(gc: GraphicsContext, yBase: Double) {
        gc.fill = when { brillo < 0.4 -> CESPED_OSCURO; brillo > 0.78 -> CESPED_CLARO; else -> CESPED }
        gc.fillOval(x, yBase - alto, ancho, alto + 3.0)
    }
    fun esVisible(desde: Double, hasta: Double): Boolean = x >= desde - 10 && x <= hasta + 10
}

data class Arbol(private val x: Double, private val escala: Double, private val claro: Boolean) {
    fun x(): Double = x
    fun escala(): Double = escala
    fun claro(): Boolean = claro
    fun dibujar(gc: GraphicsContext, sueloMundo: Double, camara: Vector2D) {
        val x = x - camara.x()
        val yBase = sueloMundo - camara.y()
        val tronco = 45.0 * escala
        val copa = if (claro) 58.0 * escala else 48.0 * escala
        gc.fill = TRONCO
        gc.fillRect(x - 4.5 * escala, yBase - tronco, 9.0 * escala, tronco + 2.0)
        gc.fill = COPA_SOMBRA
        gc.fillOval(x - copa / 2.0, yBase - tronco - copa * 0.52, copa, copa * 0.78)
        gc.fill = if (claro) COPA_CLARA else COPA_MEDIA
        gc.fillOval(x - copa * 0.33, yBase - tronco - copa * 0.68, copa * 0.64, copa * 0.54)
        gc.fill = COPA_BRILLO
        gc.fillOval(x - copa * 0.28, yBase - tronco - copa * 0.74, copa * 0.4, copa * 0.3)
    }
}

data class Nube(private val x: Double, private val y: Double, private val escala: Double, private val opacidad: Double) {
    fun x(): Double = x
    fun y(): Double = y
    fun escala(): Double = escala
    fun opacidad(): Double = opacidad
    fun dibujar(gc: GraphicsContext, camara: Vector2D) {
        val x = x - camara.x()
        val y = y - camara.y()
        val ancho = 92.0 * escala
        val alto = 30.0 * escala
        gc.fill = Color.color(1.0, 1.0, 1.0, opacidad)
        gc.fillOval(x, y, ancho, alto)
        gc.fillOval(x + ancho * 0.28, y - alto * 0.5, ancho * 0.7, alto * 1.16)
        gc.fillOval(x - ancho * 0.14, y + alto * 0.14, ancho * 0.6, alto * 0.86)
    }
}

data class Meta(private val x: Double) {
    fun x(): Double = x
    fun dibujar(gc: GraphicsContext, ySuelo: Double) {
        val yPoste = ySuelo - 78.0
        gc.stroke = META
        gc.lineWidth = 4.0
        gc.setLineDashes()
        gc.strokeLine(x, yPoste, x, ySuelo)
        gc.fill = META
        gc.fillRect(x + 2.0, yPoste - 4.0, 30.0, 17.0)
        gc.lineWidth = 2.0
        gc.strokeRect(x + 2.0, yPoste - 4.0, 30.0, 17.0)
    }
}
