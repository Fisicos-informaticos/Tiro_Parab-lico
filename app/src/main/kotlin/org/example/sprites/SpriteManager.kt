package org.example.sprites

import org.example.grafico.ContextoDibujo
import org.example.modelo.Vector2D

class SpriteManager(
    private val fabrica: FabricaSprite
) : GestionSprites {

    private val sprites = mutableListOf<Sprite>()

    override fun agregar(sprite: Sprite) {
        sprites.add(sprite)
    }

    override fun remover(sprite: Sprite) {
        sprites.remove(sprite)
    }

    override fun limpiar() {
        sprites.clear()
    }

    override fun obtenerSprites(): List<Sprite> = sprites.toList()

    override fun crearDecoracion(
        ruta: String,
        posicion: Vector2D,
        escala: Double
    ): Sprite = fabrica.crear(ruta, posicion)

    override fun dibujarTodos(contexto: ContextoDibujo) {
        for (sprite in sprites) {
            sprite.dibujar(contexto)
        }
    }

    override fun actualizarPosicion(sprite: Sprite, nuevaPosicion: Vector2D) {
        sprite.fijarPosicion(nuevaPosicion)
    }
}
