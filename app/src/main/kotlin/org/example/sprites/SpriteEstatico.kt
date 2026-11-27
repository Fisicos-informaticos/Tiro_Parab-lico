package org.example.sprites

import org.example.grafico.ContextoDibujo
import org.example.grafico.ImagenEnMundo
import org.example.modelo.Vector2D
import java.io.File

class SpriteEstatico(
    ruta: String,
    posicion: Vector2D,
    visible: Boolean = true
) : Sprite {
    private val datos = DatosSpriteEstatico(ruta, posicion, visible)

    override fun ruta(): String = datos.ruta

    override fun posicion(): Vector2D = datos.posicion()

    override fun visible(): Boolean = datos.visible()

    override fun ancho(): Double = datos.ancho

    override fun alto(): Double = datos.alto

    override fun fijarPosicion(nueva: Vector2D) = datos.fijarPosicion(nueva)

    override fun fijarVisible(valor: Boolean) = datos.fijarVisible(valor)

    fun cargar(): Boolean = datos.cargar()

    fun cargar(recurso: String): Boolean = datos.cargar(recurso)

    override fun dibujar(contexto: ContextoDibujo) = datos.dibujar(contexto)

    private data class DatosSpriteEstatico(
        val ruta: String,
        var posicion: Vector2D,
        var visible: Boolean,
        var imagenData: ByteArray? = null,
        val ancho: Double = 32.0,
        val alto: Double = 32.0
    ) {
        fun posicion(): Vector2D = posicion

        fun visible(): Boolean = visible

        fun fijarPosicion(nueva: Vector2D) {
            posicion = nueva
        }

        fun fijarVisible(valor: Boolean) {
            visible = valor
        }

        fun cargar(): Boolean {
            val archivo = File(ruta)
            return if (archivo.exists()) {
                imagenData = archivo.readBytes()
                true
            } else false
        }

        fun cargar(recurso: String): Boolean {
            val stream = SpriteEstatico::class.java.getResourceAsStream(recurso)
            return if (stream != null) {
                imagenData = stream.readBytes()
                true
            } else false
        }

        fun dibujar(contexto: ContextoDibujo) {
            if (!visible) return
            val data = imagenData ?: return
            val stream = data.inputStream()
            val img = javafx.scene.image.Image(stream)
            contexto.dibujarImagenMundo(ImagenEnMundo(img, posicion, ancho, alto))
        }

        fun descripcion(): String =
            "sprite(ruta=$ruta, posicion=$posicion, visible=$visible, ancho=$ancho, alto=$alto, cargado=${imagenData != null})"

        fun esValido(): Boolean = ruta.isNotBlank() && ancho > 0 && alto > 0 && posicion.x().isFinite()

        fun resumen(): String = "$ruta ${posicion.x()},${posicion.y()} ${ancho}x${alto} visible=$visible"
    }
}
