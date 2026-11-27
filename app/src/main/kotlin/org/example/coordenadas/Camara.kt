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
    ancho: Double,
    alto: Double
) : Camara {
    private val datos = DatosCamaraSimulacion(ancho, alto)

    override fun ancho(): Double = datos.ancho

    override fun alto(): Double = datos.alto

    override fun offsetCamara(): Vector2D = datos.offsetCamara()

    override fun fijarOffsetCamara(nuevo: Vector2D) = datos.fijarOffsetCamara(nuevo)

    private data class DatosCamaraSimulacion(
        val ancho: Double,
        val alto: Double
    ) {
        private var offset: Vector2D = Vector2D(0.0, 0.0)

        fun offsetCamara(): Vector2D = offset

        fun fijarOffsetCamara(nuevo: Vector2D) {
            offset = nuevo
        }

        fun descripcion(): String = "camara(${ancho}x${alto}, offset=$offset)"

        fun esValida(): Boolean = ancho > 0 && alto > 0 && offset.magnitud() >= 0

        fun resumen(): String = "${ancho}x${alto} offset=${offset.x()},${offset.y()}"
    }
}
