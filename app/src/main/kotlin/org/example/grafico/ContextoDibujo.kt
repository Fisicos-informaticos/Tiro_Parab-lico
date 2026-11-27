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
    inicio: Vector2D,
    fin: Vector2D,
    color: String,
    grosor: Double = 2.0
) {
    private val datos = DatosLineaDibujo(inicio, fin, color, grosor)

    fun inicio(): Vector2D = datos.inicio
    fun fin(): Vector2D = datos.fin
    fun color(): String = datos.color
    fun grosor(): Double = datos.grosor
    fun longitud(): Double = datos.longitud()
    fun esCorta(umbral: Double): Boolean = datos.esCorta(umbral)

    private data class DatosLineaDibujo(
        val inicio: Vector2D,
        val fin: Vector2D,
        val color: String,
        val grosor: Double
    ) {
        fun longitud(): Double = inicio.distanciaA(fin)

        fun esCorta(umbral: Double): Boolean = longitud() <= umbral

        fun descripcion(): String = "linea($inicio -> $fin, color=$color, grosor=$grosor)"

        fun esValida(): Boolean =
            color.isNotBlank() && grosor > 0 && inicio !== fin

        fun resumen(): String = "$inicio-$fin $color ${grosor}px ${longitud()}"
    }
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
    imagen: Image,
    posicion: Vector2D,
    ancho: Double,
    alto: Double
) {
    private val datos = DatosImagenEnMundo(imagen, posicion, ancho, alto)

    fun imagen(): Image = datos.imagen
    fun posicion(): Vector2D = datos.posicion
    fun ancho(): Double = datos.ancho
    fun alto(): Double = datos.alto
    fun ocupa(x: Double, y: Double): Boolean = datos.ocupa(x, y)

    private data class DatosImagenEnMundo(
        val imagen: Image,
        val posicion: Vector2D,
        val ancho: Double,
        val alto: Double
    ) {
        fun ocupa(x: Double, y: Double): Boolean =
            x >= posicion.x() && x <= posicion.x() + ancho && y >= posicion.y() && y <= posicion.y() + alto

        fun descripcion(): String =
            "imagen(${imagen.width}x${imagen.height} en $posicion, ${ancho}x${alto})"

        fun esValida(): Boolean = ancho > 0 && alto > 0 && imagen.width > 0 && imagen.height > 0

        fun resumen(): String = "$posicion ${ancho}x${alto} ${imagen.width}x${imagen.height}"
    }
}
