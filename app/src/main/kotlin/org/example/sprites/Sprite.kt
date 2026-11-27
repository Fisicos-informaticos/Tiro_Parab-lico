package org.example.sprites

import org.example.grafico.ContextoDibujo
import org.example.modelo.Vector2D

interface EstadoSprite {
    fun ruta(): String
    fun ancho(): Double
    fun alto(): Double
    fun posicion(): Vector2D
    fun visible(): Boolean
}

interface ControlSprite {
    fun fijarPosicion(nueva: Vector2D)
    fun fijarVisible(valor: Boolean)
}

interface DibujoSprite {
    fun dibujar(contexto: ContextoDibujo)
}

interface Sprite : EstadoSprite, ControlSprite, DibujoSprite
