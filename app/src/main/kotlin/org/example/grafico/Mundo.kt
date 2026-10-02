package org.example.grafico

import org.example.modelo.Pared
import org.example.modelo.Proyectil
import org.example.modelo.Vector2D

/** Lo que se pinta en el mundo de la partida: decorado, paredes y proyectil. */
interface Mundo {
    fun limpiar()
    fun dibujarEntorno()
    fun dibujarPared(pared: Pared)
    fun dibujarParedTemporal(pared: Pared)
    fun dibujarProyectil(proyectil: Proyectil)
    fun dibujarPuntoLanzamiento(posicion: Vector2D, radio: Double)
    fun dibujarLineaDireccion(inicio: Vector2D, fin: Vector2D)
}