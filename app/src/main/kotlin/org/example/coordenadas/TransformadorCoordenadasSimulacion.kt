package org.example.coordenadas

import org.example.modelo.Vector2D

/**
 * Adaptador que expone el estado de la camara como transformador de coordenadas.
 * Depende de [Camara], no de la simulacion concreta, de modo que ambas piezas
 * pueden evolucionar por separado (DIP).
 */
class TransformadorCoordenadasSimulacion(
    private val camara: Camara
) : TransformadorCoordenadas {
    override fun ancho(): Double = camara.ancho()

    override fun alto(): Double = camara.alto()

    override fun offsetCamara(): Vector2D = camara.offsetCamara()
}
