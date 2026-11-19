package org.example.fisica

import org.example.modelo.Vector2D

class Tierra(
    private val ancho: Double = 1000.0,
    private val alto: Double = 650.0,
    private val gravedad: Vector2D = Vector2D(0.0, 9.81)
) : Ambiente {
    private val densidadAire = 1.225
    private val sueloY: Double = alto - 50.0
    private val techoY: Double = 0.0

    override fun ancho(): Double = ancho

    override fun alto(): Double = alto

    override fun gravedad(): Vector2D = gravedad

    override fun densidadAire(): Double = densidadAire

    override fun sueloY(): Double = sueloY

    override fun techoY(): Double = techoY

    companion object {
        const val GRAVEDAD_PIXELES = 1500.0
    }
}
