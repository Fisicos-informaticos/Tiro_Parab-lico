package org.example.fisica

import org.example.modelo.Vector2D

interface Ambiente {
    fun gravedad(): Vector2D
    fun densidadAire(): Double
    fun ancho(): Double
    fun alto(): Double
    fun sueloY(): Double
    fun techoY(): Double
}
