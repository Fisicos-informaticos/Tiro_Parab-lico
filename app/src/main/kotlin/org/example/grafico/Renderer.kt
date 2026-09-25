package org.example.grafico

import org.example.modelo.Pared
import org.example.modelo.Proyectil
import org.example.modelo.EstadoSimulacion
import org.example.modelo.Vector2D

interface Renderer {
    val ancho: Double
    val alto: Double
    var offsetCamara: Vector2D

    fun limpiar()
    fun dibujarEntorno()
    fun dibujarPantallaInicio()
    fun dibujarPared(pared: Pared)
    fun dibujarParedTemporal(inicio: Vector2D, fin: Vector2D, grosor: Double)
    fun dibujarProyectil(proyectil: Proyectil)
    fun dibujarPuntoLanzamiento(posicion: Vector2D, radio: Double)
    fun dibujarLineaDireccion(inicio: Vector2D, fin: Vector2D)
    fun dibujarLinea(inicio: Vector2D, fin: Vector2D, color: String, grosor: Double = 2.0)
    fun dibujarTexto(texto: String, x: Double, y: Double, tamano: Double = 14.0)
    fun dibujarRectangulo(x: Double, y: Double, ancho: Double, alto: Double, color: String)
    fun dibujarImagen(imagen: Any, x: Double, y: Double, ancho: Double, alto: Double)
    fun dibujarImagenMundo(imagen: Any, x: Double, y: Double, ancho: Double, alto: Double)
    fun presentar()

    fun dibujarUI(
        estado: EstadoSimulacion,
        velocidad: Vector2D?,
        posicion: Vector2D?,
        angulo: Double?,
        distanciaRecorrida: Double?,
        cantidadParedes: Int = 0,
        modoParedes: Boolean = false
    )
}
