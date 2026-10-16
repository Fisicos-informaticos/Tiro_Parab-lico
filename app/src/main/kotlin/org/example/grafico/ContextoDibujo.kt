package org.example.grafico

import org.example.modelo.Vector2D

/**
 * Puerto minimo de dibujo para lo que solo necesita pintar encima del mundo
 * (sprites, estelas, textos). Los decorados dependen de esta interface y no de
 * la pantalla completa.
 */
interface ContextoDibujo {
    val ancho: Double
    val alto: Double
    var offsetCamara: Vector2D

    fun dibujarLinea(inicio: Vector2D, fin: Vector2D, color: String, grosor: Double = 2.0)
    fun dibujarTexto(texto: String, x: Double, y: Double, tamano: Double = 14.0)
    fun dibujarImagenMundo(imagen: Any, x: Double, y: Double, ancho: Double, alto: Double)
}