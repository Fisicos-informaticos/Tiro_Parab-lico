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
            brizna.dibujar(gc, yBase)
        }
    }

    private fun dibujarArboles(desde: Double, hasta: Double, camara: Vector2D) {
        if (sueloY - camara.y() > alto + 220.0) return

        for (arbol in GeneradorEntorno.arboles(desde, hasta)) {
            arbol.dibujar(gc, sueloY, camara)
        }
    }

    private fun dibujarNubes(desde: Double, hasta: Double, camara: Vector2D) {
        for (nube in GeneradorEntorno.nubes(desde, hasta)) {
            nube.dibujar(gc, camara)
        }
    }

    /** La meta se repite cada celda y siempre apoya en el suelo. */
    private fun dibujarMetas(desde: Double, hasta: Double, ySuelo: Double) {
        if (ySuelo > alto + 110.0) return

        for (meta in GeneradorEntorno.metas(desde, hasta)) {
            meta.dibujar(gc, ySuelo)
        }
    }

    private companion object {
        /** Margen extra que se genera a los lados para no cortar el decorado. */
        const val MARGEN_VISIBILIDAD = 120.0
    }
}
