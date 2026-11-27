package org.example.herramientas

import org.example.modelo.Pared
import org.example.modelo.Vector2D

/**
 * Contrato de la herramienta que dibuja paredes arrastrando el puntero. La
 * simulacion depende de esta abstraccion y no de la implementacion concreta.
 */
interface EstadoHerramientaParedes {
    fun paredTemporal(): Pared?
}

interface ActivacionHerramientaParedes {
    fun activar()
    fun desactivar()
}

interface TrazoHerramientaParedes {
    fun iniciar(puntoPantalla: Vector2D)
    fun actualizar(puntoPantalla: Vector2D)
    fun finalizar(puntoPantalla: Vector2D? = null)
    fun cancelar()
}

interface HerramientaParedes :
    EstadoHerramientaParedes,
    ActivacionHerramientaParedes,
    TrazoHerramientaParedes
