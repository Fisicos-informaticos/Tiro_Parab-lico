package org.example.coordenadas

import org.example.modelo.Vector2D
import org.example.simulacion.Simulacion

/**
 * Adaptador que expone el offset de cámara de la simulación como transformador.
 * Esto evita que la simulación conozca detalles de UI y sigue DIP.
 */
class TransformadorCoordenadasSimulacion(
    private val simulacion: Simulacion
) : TransformadorCoordenadas {
    override fun ancho(): Double = simulacion.ancho()

    override fun alto(): Double = simulacion.alto()

    override fun offsetCamara(): Vector2D = simulacion.offsetCamara()
}
