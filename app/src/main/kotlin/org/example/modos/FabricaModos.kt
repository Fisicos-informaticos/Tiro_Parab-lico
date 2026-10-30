package org.example.modos

/**
 * Registro de modos de simulacion. El nucleo depende de esta abstraccion en
 * lugar de llamar directamente al catalogo concreto.
 */
interface FabricaModos {

    fun nombres(): List<String>

    fun crear(nombre: String?): ModoSimulacion

    fun porDefecto(): ModoSimulacion
}
