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
class VectorRenderer(private val gc: GraphicsContext) {

    /** Dibuja los dos vectores activos desde el centro de la pelota. */
    fun dibujar(centro: Vector2D, velocidad: Vector2D, gravedad: Vector2D) {
        dibujarVelocidad(centro, velocidad)
        dibujarGravedad(centro, gravedad)
    }

    /** Flecha azul en la direccion del movimiento, proporcional a su rapidez. */
    fun dibujarVelocidad(centro: Vector2D, velocidad: Vector2D) {
        if (velocidad.magnitud() < UMBRAL_NULO) return
        flecha(centro, velocidad, ESCALA_VELOCIDAD, COLOR_VELOCIDAD, ETIQUETA_VELOCIDAD)
    }

    /** Flecha roja vertical hacia abajo, proporcional a la gravedad activa. */
    fun dibujarGravedad(centro: Vector2D, gravedad: Vector2D) {
        if (gravedad.magnitud() < UMBRAL_NULO) return
        flecha(centro, gravedad, ESCALA_GRAVEDAD, COLOR_GRAVEDAD, ETIQUETA_GRAVEDAD)
    }

    /**
     * Convierte una magnitud fisica a pixeles y la pinta como flecha desde
     * [origen]. La longitud es proporcional al valor fisico hasta [LARGO_MAXIMO],
     * tope que evita que una flecha gigante salga del lienzo.
     */
    private fun flecha(
        origen: Vector2D,
        magnitud: Vector2D,
        escala: Double,
        color: Color,
        etiqueta: String
    ) {
        val largo = (magnitud.magnitud() * escala).coerceAtMost(LARGO_MAXIMO)
        if (largo < LARGO_MINIMO) return

        val direccion = magnitud.normalizado()
        val perpendicular = Vector2D(-direccion.y(), direccion.x())
        val punta = origen + direccion * largo
        val cabeza = (largo * FRACCION_CABEZA).coerceAtMost(CABEZA_MAXIMA)

        gc.stroke = color
        gc.lineWidth = GROSOR_LINEA
        gc.setLineDashes()
        gc.strokeLine(origen.x(), origen.y(), punta.x(), punta.y())

        gc.fill = color
        gc.fillPolygon(
            doubleArrayOf(
                punta.x(),
                punta.x() - direccion.x() * cabeza + perpendicular.x() * ANCHO_CABEZA,
                punta.x() - direccion.x() * cabeza - perpendicular.x() * ANCHO_CABEZA
            ),
            doubleArrayOf(
                punta.y(),
                punta.y() - direccion.y() * cabeza + perpendicular.y() * ANCHO_CABEZA,
                punta.y() - direccion.y() * cabeza - perpendicular.y() * ANCHO_CABEZA
            ),
            3
        )

        pintarEtiqueta(
            etiqueta,
            origen + direccion * (largo * POSICION_ETIQUETA),
            perpendicular,
            color
        )
    }

    /** Rotulo del vector a un lado del eje, para no tapar la punta. */
    private fun pintarEtiqueta(
        texto: String,
        punto: Vector2D,
        perpendicular: Vector2D,
        color: Color
    ) {
        gc.fill = color
        gc.font = Font.font("Monospace", TAMANO_ETIQUETA)
        gc.fillText(
            texto,
            punto.x() + perpendicular.x() * DESPLAZAMIENTO_ETIQUETA,
            punto.y() + perpendicular.y() * DESPLAZAMIENTO_ETIQUETA
        )
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
