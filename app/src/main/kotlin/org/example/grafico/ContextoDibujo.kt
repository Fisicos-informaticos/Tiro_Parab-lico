package org.example.grafico

import javafx.scene.image.Image
import org.example.modelo.Vector2D

/**
 * Puerto minimo de dibujo para lo que solo necesita pintar encima del mundo
 * (sprites, estelas, textos). Los decorados dependen de esta interface y no de
 * la pantalla completa. La camara se consulta y se fija con metodos para no
 * exponer estado mutable.
 */
interface ContextoDibujo {
    fun ancho(): Double
    fun alto(): Double
    fun offsetCamara(): Vector2D
    fun fijarOffsetCamara(nuevo: Vector2D)

    fun dibujarLinea(linea: LineaDibujo)
    fun dibujarTexto(texto: TextoDibujo)
    fun dibujarImagenMundo(imagen: ImagenEnMundo)
}

data class LineaDibujo(
    val inicio: Vector2D,
    val fin: Vector2D,
    val color: String,
    val grosor: Double = 2.0
)

data class TextoDibujo(
    val texto: String,
    val posicion: Vector2D,
    val tamano: Double = 14.0
)

data class ImagenEnMundo(
    val imagen: Image,
    val posicion: Vector2D,
    val ancho: Double,
    val alto: Double
)
