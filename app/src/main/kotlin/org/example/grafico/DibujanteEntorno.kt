package org.example.grafico

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import org.example.modelo.Vector2D

/**
 * Pinta el decorado del mundo (suelo, cesped, arboles, nubes y metas) usando la
 * camara como unica referencia de desplazamiento. CanvasRenderer solo le delega.
 */
class DibujanteEntorno(
    private val gc: GraphicsContext,
    private val ancho: Double,
    private val alto: Double
) {

    private val colorSuelo = Color.web("#4a7c3f")
    private val colorCesped = Color.web("#5cb85c")
    private val colorCespedOscuro = Color.web("#3f9142")
    private val colorCespedClaro = Color.web("#7fd07f")
    private val colorMeta = Color.web("#e74c3c")
    private val sueloY: Double get() = alto - 50.0

    fun dibujar(camara: Vector2D) {
        val ySuelo = sueloY - camara.y()
        if (ySuelo < alto) {
            gc.fill = colorSuelo
            val desde = Math.max(ySuelo, 0.0)
            gc.fillRect(0.0, desde, ancho, alto - desde)
        }

        val margen = MARGEN_VISIBILIDAD
        val desde = -camara.x() - margen
        val hasta = ancho - camara.x() + margen

        dibujarPasto(desde, hasta, ySuelo)
        dibujarArboles(desde, hasta, camara)
        dibujarNubes(desde, hasta, camara)
        dibujarMetas(desde, hasta, ySuelo)
    }

    /** Briznas cada GeneradorEntorno.ANCHO_BRIZNA: el cesped no se interrumpe. */
    private fun dibujarPasto(desde: Double, hasta: Double, yBase: Double) {
        if (yBase > alto + 30.0) return

        for (brizna in GeneradorEntorno.pasto(desde, hasta)) {
            gc.fill = when {
                brizna.brillo() < 0.4 -> colorCespedOscuro
                brizna.brillo() > 0.78 -> colorCespedClaro
                else -> colorCesped
            }
            gc.fillOval(brizna.x(), yBase - brizna.alto(), brizna.ancho(), brizna.alto() + 3.0)
        }
    }

    private fun dibujarArboles(desde: Double, hasta: Double, camara: Vector2D) {
        if (sueloY - camara.y() > alto + 220.0) return

        for (arbol in GeneradorEntorno.arboles(desde, hasta)) {
            dibujarArbol(arbol.x(), sueloY, arbol.escala(), arbol.claro(), camara)
        }
    }

    private fun dibujarNubes(desde: Double, hasta: Double, camara: Vector2D) {
        for (nube in GeneradorEntorno.nubes(desde, hasta)) {
            dibujarNube(nube.x(), nube.y(), nube.escala(), nube.opacidad(), camara)
        }
    }

    /** La meta se repite cada celda y siempre apoya en el suelo. */
    private fun dibujarMetas(desde: Double, hasta: Double, ySuelo: Double) {
        if (ySuelo > alto + 110.0) return

        for (meta in GeneradorEntorno.metas(desde, hasta)) {
            val yPoste = ySuelo - 78.0
            gc.stroke = colorMeta
            gc.lineWidth = 4.0
            gc.setLineDashes()
            gc.strokeLine(meta.x(), yPoste, meta.x(), ySuelo)

            gc.fill = colorMeta
            gc.fillRect(meta.x() + 2.0, yPoste - 4.0, 30.0, 17.0)
            gc.lineWidth = 2.0
            gc.strokeRect(meta.x() + 2.0, yPoste - 4.0, 30.0, 17.0)
        }
    }

    private fun dibujarArbol(
        xMundo: Double,
        sueloMundo: Double,
        escala: Double,
        clara: Boolean,
        camara: Vector2D
    ) {
        val x = xMundo - camara.x()
        val yBase = sueloMundo - camara.y()
        val tronco = 45.0 * escala
        val copa = if (clara) 58.0 * escala else 48.0 * escala

        gc.fill = Color.web("#7B4A21")
        gc.fillRect(x - 4.5 * escala, yBase - tronco, 9.0 * escala, tronco + 2.0)

        gc.fill = Color.web("#1b5e20")
        gc.fillOval(x - copa / 2.0, yBase - tronco - copa * 0.52, copa, copa * 0.78)
        gc.fill = if (clara) Color.web("#43a047") else Color.web("#2e7d32")
        gc.fillOval(x - copa * 0.33, yBase - tronco - copa * 0.68, copa * 0.64, copa * 0.54)
        gc.fill = Color.web("#66bb6a")
        gc.fillOval(x - copa * 0.28, yBase - tronco - copa * 0.74, copa * 0.4, copa * 0.3)
    }

    private fun dibujarNube(
        xMundo: Double,
        yMundo: Double,
        escala: Double,
        opacidad: Double,
        camara: Vector2D
    ) {
        val x = xMundo - camara.x()
        val y = yMundo - camara.y()
        val ancho = 92.0 * escala
        val alto = 30.0 * escala

        gc.fill = Color.color(1.0, 1.0, 1.0, opacidad)
        gc.fillOval(x, y, ancho, alto)
        gc.fillOval(x + ancho * 0.28, y - alto * 0.5, ancho * 0.7, alto * 1.16)
        gc.fillOval(x - ancho * 0.14, y + alto * 0.14, ancho * 0.6, alto * 0.86)
    }

    private companion object {
        /** Margen extra que se genera a los lados para no cortar el decorado. */
        const val MARGEN_VISIBILIDAD = 120.0
    }
}
