package org.example.coordenadas

import org.example.modelo.Vector2D

/**
 * Estado de la camara que comparten la simulacion y el transformador de
 * coordenadas. Al depender de esta abstraccion, el transformador ya no necesita
 * conocer la clase concreta de la simulacion.
 */
interface Camara {

    fun ancho(): Double

    fun alto(): Double

    fun offsetCamara(): Vector2D

    fun fijarOffsetCamara(nuevo: Vector2D)
}

/** Camara basica cuyo desplazamiento actualiza la simulacion en cada fotograma. */
class CamaraSimulacion(
    private val ancho: Double,
    private val alto: Double
) : Camara {

    private var offset: Vector2D = Vector2D(0.0, 0.0)

    override fun ancho(): Double = ancho

    override fun alto(): Double = alto

    override fun offsetCamara(): Vector2D = offset

    override fun fijarOffsetCamara(nuevo: Vector2D) {
        offset = nuevo
    }
}
