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
    override val ancho: Double get() = simulacion.ancho
    override val alto: Double get() = simulacion.alto
    override val offsetCamara: Vector2D get() = simulacion.offsetCamara
}
