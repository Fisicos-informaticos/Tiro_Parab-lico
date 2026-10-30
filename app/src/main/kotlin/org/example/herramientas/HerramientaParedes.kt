package org.example.herramientas

import org.example.modelo.Pared
import org.example.modelo.Vector2D

/**
 * Contrato de la herramienta que dibuja paredes arrastrando el puntero. La
 * simulacion depende de esta abstraccion y no de la implementacion concreta.
 */
interface HerramientaParedes {

    fun paredTemporal(): Pared?

    fun activar()

    fun desactivar()

    fun iniciar(puntoPantalla: Vector2D)

    fun actualizar(puntoPantalla: Vector2D)

    fun finalizar(puntoPantalla: Vector2D? = null)

    fun cancelar()
}
