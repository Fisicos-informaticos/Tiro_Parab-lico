package org.example.sprites

import org.example.grafico.ContextoDibujo
import org.example.modelo.Vector2D

interface Sprite {
    fun ruta(): String
    fun ancho(): Double
    fun alto(): Double
    fun posicion(): Vector2D
    fun visible(): Boolean

    fun fijarPosicion(nueva: Vector2D)
    fun fijarVisible(valor: Boolean)

    fun dibujar(contexto: ContextoDibujo)
}
