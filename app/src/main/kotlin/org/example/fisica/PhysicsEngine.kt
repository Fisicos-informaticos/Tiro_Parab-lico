package org.example.fisica

import org.example.modelo.Pared
import org.example.modelo.Proyectil

/**
 * Contrato del motor de fisica. La simulacion y las herramientas dependen de
 * esta abstraccion (DIP) y no de una implementacion concreta.
 */
interface PhysicsEngine {
    fun sueloY(): Double
    fun techoY(): Double

    fun agregarPared(pared: Pared)
    fun removerPared(pared: Pared): Boolean
    fun quitarPared(pared: Pared)
    fun limpiarParedes()
    fun obtenerParedes(): List<Pared>

    /** Avanza el proyectil y devuelve las paredes que ha roto en este paso. */
    fun simularPaso(proyectil: Proyectil, deltaTime: Double): List<Pared>

    fun estaEnReposo(proyectil: Proyectil): Boolean

    /** Cambia el ambiente activo sin perder las paredes ya colocadas. */
    fun cambiarAmbiente(ambiente: Ambiente)
}
