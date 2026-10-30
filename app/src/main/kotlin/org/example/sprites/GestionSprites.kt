package org.example.sprites

import org.example.grafico.ContextoDibujo
import org.example.modelo.Vector2D

/**
 * Contrato del gestor de sprites que usa la presentacion. El presentador
 * depende de esta abstraccion y no de la implementacion concreta.
 */
interface GestionSprites {

    fun agregar(sprite: Sprite)

    fun remover(sprite: Sprite)

    fun limpiar()

    fun obtenerSprites(): List<Sprite>

    fun crearDecoracion(ruta: String, posicion: Vector2D, escala: Double = 1.0): Sprite

    fun dibujarTodos(contexto: ContextoDibujo)

    fun actualizarPosicion(sprite: Sprite, nuevaPosicion: Vector2D)
}
