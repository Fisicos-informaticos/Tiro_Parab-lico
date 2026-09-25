package org.example.sprites

import org.example.grafico.Renderer
import org.example.modelo.Vector2D

class SpriteManager {
    private val sprites = mutableListOf<Sprite>()

    fun agregar(sprite: Sprite) {
        sprites.add(sprite)
    }

    fun remover(sprite: Sprite) {
        sprites.remove(sprite)
    }

    fun limpiar() {
        sprites.clear()
    }

    fun obtenerSprites(): List<Sprite> = sprites.toList()

    fun crearDecoracion(
        ruta: String,
        posicion: Vector2D,
        escala: Double = 1.0
    ): SpriteEstatico {
        val sprite = SpriteEstatico(ruta, posicion)
        sprite.cargar()
        return sprite
    }

    fun dibujarTodos(renderer: Renderer) {
        for (sprite in sprites) {
            sprite.dibujar(renderer)
        }
    }

    fun actualizarPosicion(sprite: Sprite, nuevaPosicion: Vector2D) {
        sprite.posicion = nuevaPosicion
    }
}
