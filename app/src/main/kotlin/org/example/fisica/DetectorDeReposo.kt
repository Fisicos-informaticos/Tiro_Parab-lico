package org.example.fisica

import org.example.modelo.Pared
import org.example.modelo.Proyectil

/**
 * Contrato de la regla que decide cuando un proyectil ha terminado su recorrido.
 * El motor depende de esta abstraccion en lugar de instanciar un detector concreto.
 */
interface DetectorDeReposo {

    fun estaEnReposo(proyectil: Proyectil, paredes: List<Pared>): Boolean

    /** El ambiente activo cambio (por ejemplo, al elegir otra gravedad). */
    fun cambiarAmbiente(ambiente: Ambiente)
}

/** Fabrica de detectores para no acoplar el motor a una implementacion concreta. */
object FabricaDetectoresReposo {

    fun crear(ambiente: Ambiente): DetectorDeReposo = DetectorReposo(ambiente)
}
