package org.example.simulacion

import org.example.coordenadas.Camara
import org.example.coordenadas.TransformadorCoordenadas
import org.example.fisica.PhysicsEngine
import org.example.modelo.Vector2D
import org.example.modos.Preparacion

/** Calcula apoyos, alturas y la vista del mundo que necesita el modo activo. */
class PosicionPelota(
    private val sesion: SesionPartida,
    private val motor: PhysicsEngine,
    private val superficies: SuperficiesLanzamiento,
    private val transformador: TransformadorCoordenadas,
    private val camara: Camara,
    private val alto: Double
) {
    fun aMundo(punto: Vector2D): Vector2D = transformador.pantallaAMundo(punto)

    fun preparacionPara(puntoMundo: Vector2D): Preparacion {
        val radio = sesion.pelota.radio()
        val offsetY = camara.offsetCamara().y()
        val minY = maxOf(motor.techoY(), offsetY) + radio
        val maxY = maxOf(minY, minOf(motor.sueloY(), offsetY + alto) - radio)
        return Preparacion(
            punto = puntoMundo,
            origen = sesion.pelota.posicion(),
            apoyoY = superficies.yDeApoyo(puntoMundo.x(), radio, motor.sueloY()),
            sueloY = motor.sueloY(),
            minY = minY,
            maxY = maxY
        )
    }

    fun alturaActual(): Double =
        (motor.sueloY() - sesion.pelota.radio() - SuperficiesLanzamiento.ALTURA_APOYO_PELOTA -
            sesion.pelota.posicion().y()).coerceAtLeast(0.0)

    fun apoyarEnElSueloOSuperficie() {
        val y = superficies.yDeApoyo(sesion.posicionOrigen.x(), sesion.pelota.radio(), motor.sueloY())
        sesion.posicionOrigen = Vector2D(sesion.posicionOrigen.x(), y)
        sesion.pelota.fijarPosicion(sesion.posicionOrigen)
    }
}
