package org.example.grafico

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.text.Font
import org.example.modelo.Vector2D

/**
 * Pinta los vectores fisicos de la pelota sobre el mundo: velocidad (azul) y
 * aceleracion de la gravedad (roja). Es su unica responsabilidad, asi que el
 * resto del renderizado no depende de como se construye una flecha ni de la
 * escala fisica que se usa para convertir pixeles/segundo en pixeles.
 */
class VectorRenderer(private val gc: GraphicsContext) : DibujoVectoresFisica {

    /** Dibuja los dos vectores activos desde el centro de la pelota. */
    override fun dibujar(centro: Vector2D, velocidad: Vector2D, gravedad: Vector2D) {
        dibujarVelocidad(centro, velocidad)
        dibujarGravedad(centro, gravedad)
    }

    /** Flecha azul en la direccion del movimiento, proporcional a su rapidez. */
    fun dibujarVelocidad(centro: Vector2D, velocidad: Vector2D) {
        if (velocidad.magnitud() < UMBRAL_NULO) return
        flecha(
            TrazoVector(
                origen = centro,
                magnitud = velocidad,
                escala = ESCALA_VELOCIDAD,
                color = COLOR_VELOCIDAD,
                etiqueta = ETIQUETA_VELOCIDAD
            )
        )
    }

    /** Flecha roja vertical hacia abajo, proporcional a la gravedad activa. */
    fun dibujarGravedad(centro: Vector2D, gravedad: Vector2D) {
        if (gravedad.magnitud() < UMBRAL_NULO) return
        flecha(
            TrazoVector(
                origen = centro,
                magnitud = gravedad,
                escala = ESCALA_GRAVEDAD,
                color = COLOR_GRAVEDAD,
                etiqueta = ETIQUETA_GRAVEDAD
            )
        )
    }

    /**
     * Convierte una magnitud fisica a pixeles y la pinta como flecha. La longitud
     * es proporcional al valor fisico hasta [LARGO_MAXIMO], tope que evita que una
     * flecha gigante salga del lienzo.
     */
    private fun flecha(trazo: TrazoVector) {
        val geometria = geometria(trazo) ?: return
        dibujarLinea(trazo, geometria)
        dibujarCabeza(trazo, geometria)
        pintarEtiqueta(trazo, geometria)
    }

    private fun geometria(trazo: TrazoVector): GeometriaFlecha? {
        val largo = (trazo.magnitud().magnitud() * trazo.escala()).coerceAtMost(LARGO_MAXIMO)
        if (largo < LARGO_MINIMO) return null

        val direccion = trazo.magnitud().normalizado()
        return GeometriaFlecha(
            punta = trazo.origen() + direccion * largo,
            direccion = direccion,
            perpendicular = Vector2D(-direccion.y(), direccion.x()),
            cabeza = (largo * FRACCION_CABEZA).coerceAtMost(CABEZA_MAXIMA),
            largo = largo
        )
    }

    private fun dibujarLinea(trazo: TrazoVector, geometria: GeometriaFlecha) {
        gc.stroke = trazo.color()
        gc.lineWidth = GROSOR_LINEA
        gc.setLineDashes()
        gc.strokeLine(
            trazo.origen().x(),
            trazo.origen().y(),
            geometria.punta().x(),
            geometria.punta().y()
        )
    }

    private fun dibujarCabeza(trazo: TrazoVector, geometria: GeometriaFlecha) {
        val puntosX = puntosCabezaX(geometria)
        val puntosY = puntosCabezaY(geometria)
        gc.fill = trazo.color()
        gc.fillPolygon(puntosX, puntosY, 3)
    }

    private fun puntosCabezaX(geometria: GeometriaFlecha): DoubleArray {
        val punta = geometria.punta()
        val direccion = geometria.direccion()
        val perpendicular = geometria.perpendicular()
        val baseX = punta.x() - direccion.x() * geometria.cabeza()
        return doubleArrayOf(
            punta.x(),
            baseX + perpendicular.x() * ANCHO_CABEZA,
            baseX - perpendicular.x() * ANCHO_CABEZA
        )
    }

    private fun puntosCabezaY(geometria: GeometriaFlecha): DoubleArray {
        val punta = geometria.punta()
        val direccion = geometria.direccion()
        val perpendicular = geometria.perpendicular()
        val baseY = punta.y() - direccion.y() * geometria.cabeza()
        return doubleArrayOf(
            punta.y(),
            baseY + perpendicular.y() * ANCHO_CABEZA,
            baseY - perpendicular.y() * ANCHO_CABEZA
        )
    }

    /** Rotulo del vector a un lado del eje, para no tapar la punta. */
    private fun pintarEtiqueta(trazo: TrazoVector, geometria: GeometriaFlecha) {
        val punto = trazo.origen() + geometria.direccion() *
            (geometria.largo() * POSICION_ETIQUETA)
        gc.fill = trazo.color()
        gc.font = Font.font("Monospace", TAMANO_ETIQUETA)
        gc.fillText(
            trazo.etiqueta(),
            punto.x() + geometria.perpendicular().x() * DESPLAZAMIENTO_ETIQUETA,
            punto.y() + geometria.perpendicular().y() * DESPLAZAMIENTO_ETIQUETA
        )
    }

    private class TrazoVector(
        private val origen: Vector2D,
        private val magnitud: Vector2D,
        private val escala: Double,
        private val color: Color,
        private val etiqueta: String
    ) {
        fun origen(): Vector2D = origen
        fun magnitud(): Vector2D = magnitud
        fun escala(): Double = escala
        fun color(): Color = color
        fun etiqueta(): String = etiqueta
    }

    private class GeometriaFlecha(
        private val punta: Vector2D,
        private val direccion: Vector2D,
        private val perpendicular: Vector2D,
        private val cabeza: Double,
        private val largo: Double
    ) {
        fun punta(): Vector2D = punta
        fun direccion(): Vector2D = direccion
        fun perpendicular(): Vector2D = perpendicular
        fun cabeza(): Double = cabeza
        fun largo(): Double = largo
    }

    companion object {
        /** Pixeles de flecha por pixeles/segundo de rapidez. */
        private const val ESCALA_VELOCIDAD = 0.3

        /** Pixeles de flecha por pixeles/segundo^2 de gravedad. */
        private const val ESCALA_GRAVEDAD = 0.06

        /** Techo grafico de las flechas para que sigan cabiendo en pantalla. */
        private const val LARGO_MAXIMO = 240.0
        private const val LARGO_MINIMO = 6.0

        /** Rapidez o gravedad con la que ya no merece pena pintar nada. */
        private const val UMBRAL_NULO = 1e-6

        private const val GROSOR_LINEA = 3.0
        private const val FRACCION_CABEZA = 0.3
        private const val CABEZA_MAXIMA = 16.0
        private const val ANCHO_CABEZA = 6.0

        private const val POSICION_ETIQUETA = 0.55
        private const val DESPLAZAMIENTO_ETIQUETA = 10.0
        private const val TAMANO_ETIQUETA = 13.0

        private val COLOR_VELOCIDAD = Color.web("#1e90ff")
        private val COLOR_GRAVEDAD = Color.web("#e53935")
        private const val ETIQUETA_VELOCIDAD = "V"
        private const val ETIQUETA_GRAVEDAD = "g"
    }
}
