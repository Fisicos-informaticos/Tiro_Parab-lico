package org.example.fisica

import org.example.modelo.Pared
import org.example.modelo.ProyectilFisico
import org.example.modelo.ProyectilVisual

/**
 * Contrato del motor de fisica. La simulacion y las herramientas dependen de
 * esta abstraccion (DIP) y no de una implementacion concreta.
 */
interface LimitesFisicos {
    fun sueloY(): Double
    fun techoY(): Double
}

interface GestionParedesFisicas {
    fun agregarPared(pared: Pared)
    fun removerPared(pared: Pared): Boolean
    fun quitarPared(pared: Pared)
    fun limpiarParedes()
    fun obtenerParedes(): List<Pared>
}

interface CicloFisico {
    /** Avanza el proyectil y devuelve las paredes que ha roto en este paso. */
    fun simularPaso(proyectil: ProyectilFisico, deltaTime: Double): List<Pared>
    fun estaEnReposo(proyectil: ProyectilVisual): Boolean
}

interface ConfiguracionFisica {
    /** Cambia el ambiente activo sin perder las paredes ya colocadas. */
    fun cambiarAmbiente(ambiente: Ambiente)
}

interface PhysicsEngine :
    LimitesFisicos,
    GestionParedesFisicas,
    CicloFisico,
    ConfiguracionFisica
