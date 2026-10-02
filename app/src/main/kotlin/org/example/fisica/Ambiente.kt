package org.example.fisica

import org.example.modelo.Vector2D

interface Ambiente {
    val gravedad: Vector2D
    val densidadAire: Double
    val ancho: Double
    val alto: Double
    val sueloY: Double
    val techoY: Double
}
