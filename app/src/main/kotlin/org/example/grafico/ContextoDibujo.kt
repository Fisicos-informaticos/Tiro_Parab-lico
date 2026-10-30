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

    fun dibujarLinea(inicio: Vector2D, fin: Vector2D, color: String, grosor: Double = 2.0)
    fun dibujarTexto(texto: String, x: Double, y: Double, tamano: Double = 14.0)
    fun dibujarImagenMundo(imagen: Image, x: Double, y: Double, ancho: Double, alto: Double)
}
