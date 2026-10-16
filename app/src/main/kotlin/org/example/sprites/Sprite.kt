package org.example.sprites

import org.example.grafico.ContextoDibujo
import org.example.modelo.Vector2D

interface Sprite {
    val ruta: String
    val ancho: Double
    val alto: Double
    var posicion: Vector2D
    var visible: Boolean

    fun dibujar(contexto: ContextoDibujo)
}
