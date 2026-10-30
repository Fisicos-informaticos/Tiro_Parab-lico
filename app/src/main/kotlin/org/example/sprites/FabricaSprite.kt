package org.example.sprites

import org.example.modelo.Vector2D

/**
 * Crea sprites decorativos. El gestor de sprites depende de esta abstraccion
 * para no instanciar [SpriteEstatico] internamente.
 */
interface FabricaSprite {

    fun crear(ruta: String, posicion: Vector2D): Sprite
}

/** Implementacion por defecto: sprite estatico cargado desde la ruta indicada. */
class FabricaSpriteEstatica : FabricaSprite {

    override fun crear(ruta: String, posicion: Vector2D): Sprite =
        SpriteEstatico(ruta, posicion).also { it.cargar() }
}
