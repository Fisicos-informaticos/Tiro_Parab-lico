package org.example.coordenadas

import org.example.modelo.Vector2D

/**
 * Interfaz para convertir entre coordenadas de pantalla y mundo.
 * Esto permite separar la lógica de transformación de la simulación (SRP).
 */
interface TransformadorCoordenadas {
    val ancho: Double
    val alto: Double
    val offsetCamara: Vector2D

    fun pantallaAMundo(punto: Vector2D): Vector2D = punto + offsetCamara

    fun mundoAPantalla(punto: Vector2D): Vector2D = punto - offsetCamara
}
