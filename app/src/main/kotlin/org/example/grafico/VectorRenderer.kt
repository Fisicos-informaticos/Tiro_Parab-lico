package org.example.grafico

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import org.example.modelo.Vector2D

class VectorRenderer(private val gc: GraphicsContext) : DibujoVectoresFisica {

    override fun dibujar(centro: Vector2D, velocidad: Vector2D, gravedad: Vector2D) {
        dibujarVelocidad(centro, velocidad)
        dibujarGravedad(centro, gravedad)
    }

    fun dibujarVelocidad(centro: Vector2D, velocidad: Vector2D) {
        if (velocidad.magnitud() < UMBRAL_NULO) return
        flecha(TrazoVector(centro, velocidad, ESCALA_VELOCIDAD, COLOR_VELOCIDAD, ETIQUETA_VELOCIDAD))
    }

    fun dibujarGravedad(centro: Vector2D, gravedad: Vector2D) {
        if (gravedad.magnitud() < UMBRAL_NULO) return
        flecha(TrazoVector(centro, gravedad, ESCALA_GRAVEDAD, COLOR_GRAVEDAD, ETIQUETA_GRAVEDAD))
    }

    private fun flecha(trazo: TrazoVector) {
        val g = geometria(trazo) ?: return
        DibujadorFlecha.dibujar(gc, trazo, g)
    }

    private fun geometria(trazo: TrazoVector): FlechaGeometria? {
        val largo = (trazo.magnitud().magnitud() * trazo.escala()).coerceAtMost(LARGO_MAXIMO)
        if (largo < LARGO_MINIMO) return null
        val dir = trazo.magnitud().normalizado()
        return FlechaGeometria(
            punta = trazo.origen() + dir * largo,
            direccion = dir,
            perpendicular = Vector2D(-dir.y(), dir.x()),
            cabeza = (largo * FRACCION_CABEZA).coerceAtMost(CABEZA_MAXIMA),
            largo = largo
        )
    }

    companion object {
        private const val ESCALA_VELOCIDAD = 0.3
        private const val ESCALA_GRAVEDAD = 0.06
        private const val LARGO_MAXIMO = 240.0
        private const val LARGO_MINIMO = 6.0
        private const val UMBRAL_NULO = 1e-6
        private const val FRACCION_CABEZA = 0.3
        private const val CABEZA_MAXIMA = 16.0
        private val COLOR_VELOCIDAD = Color.web("#1e90ff")
        private val COLOR_GRAVEDAD = Color.web("#e53935")
        private const val ETIQUETA_VELOCIDAD = "V"
        private const val ETIQUETA_GRAVEDAD = "g"
    }
}
