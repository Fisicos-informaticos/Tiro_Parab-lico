package org.example.fisica

import org.example.modelo.Vector2D

/**
 * Crea el ambiente fisico con las dimensiones y la gravedad pedidas. La
 * simulacion depende de esta abstraccion para no instanciar [Tierra] por su cuenta.
 */
interface FabricaAmbiente {

    fun crear(ancho: Double, alto: Double, gravedad: Vector2D): Ambiente
}

/** Implementacion por defecto: un mundo terrestre con la gravedad indicada. */
class FabricaTierra : FabricaAmbiente {

    override fun crear(ancho: Double, alto: Double, gravedad: Vector2D): Ambiente =
        Tierra(ancho, alto, gravedad)
}
