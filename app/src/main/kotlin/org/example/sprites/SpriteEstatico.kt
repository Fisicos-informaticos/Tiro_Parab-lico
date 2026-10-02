package org.example.sprites

import org.example.grafico.ContextoDibujo
import org.example.modelo.Vector2D
import java.io.File

class SpriteEstatico(
    override val ruta: String,
    override var posicion: Vector2D,
    override var visible: Boolean = true
) : Sprite {

    private var imagenData: ByteArray? = null

    override val ancho: Double
        get() = 32.0
    override val alto: Double
        get() = 32.0

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
        contexto.dibujarImagenMundo(img, posicion.x, posicion.y, ancho, alto)
    }
}
