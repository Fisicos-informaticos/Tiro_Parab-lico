package org.example.grafico

import javafx.scene.image.Image
import org.example.modelo.Vector2D

interface ContextoDibujo {
    fun ancho(): Double
    fun alto(): Double
    fun offsetCamara(): Vector2D
    fun fijarOffsetCamara(nuevo: Vector2D)
    fun dibujarLinea(linea: LineaDibujo)
    fun dibujarTexto(texto: TextoDibujo)
    fun dibujarImagenMundo(imagen: ImagenEnMundo)
}

class LineaDibujo(
    private val inicio: Vector2D,
    private val fin: Vector2D,
    private val color: String,
    private val grosor: Double = 2.0
) {
    fun inicio(): Vector2D = inicio
    fun fin(): Vector2D = fin
    fun color(): String = color
    fun grosor(): Double = grosor
    fun longitud(): Double = inicio.distanciaA(fin)
    fun esCorta(umbral: Double): Boolean = longitud() <= umbral
}

class TextoDibujo(
    private val texto: String,
    private val posicion: Vector2D,
    private val tamano: Double = 14.0
) {
    fun texto(): String = texto
    fun posicion(): Vector2D = posicion
    fun tamano(): Double = tamano
    fun en(nuevaPosicion: Vector2D): TextoDibujo = TextoDibujo(texto, nuevaPosicion, tamano)
    fun conTamano(nuevoTamano: Double): TextoDibujo = TextoDibujo(texto, posicion, nuevoTamano)
}

class ImagenEnMundo(
    private val imagen: Image,
    private val posicion: Vector2D,
    private val ancho: Double,
    private val alto: Double
) {
    fun imagen(): Image = imagen
    fun posicion(): Vector2D = posicion
    fun ancho(): Double = ancho
    fun alto(): Double = alto
    fun ocupa(x: Double, y: Double): Boolean = x >= posicion.x() && x <= posicion.x() + ancho && y >= posicion.y() && y <= posicion.y() + alto
}
