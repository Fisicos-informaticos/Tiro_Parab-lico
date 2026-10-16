package org.example.grafico

import org.example.modelo.Pared
import org.example.modelo.Proyectil
import org.example.modelo.Vector2D

/** Lo que se pinta en el mundo de la partida: decorado, paredes, proyectil y vectores. */
interface Mundo {
    fun limpiar()
    fun dibujarEntorno()
    fun dibujarPared(pared: Pared)
    fun dibujarParedTemporal(pared: Pared)
    fun dibujarProyectil(proyectil: Proyectil)
    fun dibujarPuntoLanzamiento(posicion: Vector2D, radio: Double)
    fun dibujarLineaDireccion(inicio: Vector2D, fin: Vector2D)

    /** Vectores de fisica (velocidad y gravedad) saliendo del centro de la pelota. */
    fun dibujarVectoresFisica(centro: Vector2D, velocidad: Vector2D, gravedad: Vector2D)
}