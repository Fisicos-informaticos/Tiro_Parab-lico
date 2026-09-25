package org.example.fisica

import org.example.modelo.Vector2D

class Tierra(
    override val ancho: Double = 1000.0,
    override val alto: Double = 650.0,
    override val gravedad: Vector2D = Vector2D(0.0, 9.81)
) : Ambiente {
    companion object {
        const val GRAVEDAD_PIXELES = 1500.0
    }

    override val densidadAire = 1.225
    override val sueloY: Double = alto - 50.0
    override val techoY: Double = 0.0
}
