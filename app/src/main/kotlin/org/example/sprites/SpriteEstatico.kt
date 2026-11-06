package org.example.sprites

import org.example.grafico.ContextoDibujo
import org.example.modelo.Vector2D
import java.io.File

class SpriteEstatico(
    private val ruta: String,
    private var posicion: Vector2D,
    private var visible: Boolean = true
) : Sprite {

    private var imagenData: ByteArray? = null
    private val ancho = 32.0
    private val alto = 32.0

    override fun ruta(): String = ruta

    override fun posicion(): Vector2D = posicion

    override fun visible(): Boolean = visible

    override fun ancho(): Double = ancho

    override fun alto(): Double = alto

    override fun fijarPosicion(nueva: Vector2D) {
        posicion = nueva
    }

    override fun fijarVisible(valor: Boolean) {
        visible = valor
    }

    fun cargar(): Boolean {
        val archivo = File(ruta)
        return if (archivo.exists()) {
            imagenData = archivo.readBytes()
            true
        } else {
            false
        }
    }

    fun cargar(recurso: String): Boolean {
        val stream = javaClass.getResourceAsStream(recurso)
        return if (stream != null) {
            imagenData = stream.readBytes()
            true
        } else {
            false
        }
    }

    override fun dibujar(contexto: ContextoDibujo) {
        if (!visible) return
        val data = imagenData ?: return
        val stream = data.inputStream()
        val img = javafx.scene.image.Image(stream)
        contexto.dibujarImagenMundo(img, posicion.x(), posicion.y(), ancho, alto)
    }
}
