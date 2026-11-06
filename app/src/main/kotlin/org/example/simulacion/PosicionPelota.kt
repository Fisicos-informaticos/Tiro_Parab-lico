package org.example.simulacion

import org.example.coordenadas.Camara
import org.example.coordenadas.TransformadorCoordenadas
import org.example.fisica.PhysicsEngine
import org.example.modelo.Vector2D
import org.example.modos.Preparacion

/** Calcula apoyos, alturas y la vista del mundo que necesita el modo activo. */
class PosicionPelota(
    private val sesion: SesionJuego,
    private val motor: PhysicsEngine,
    private val superficies: SuperficiesApoyo,
    private val transformador: TransformadorCoordenadas,
    private val camara: Camara,
    private val alto: Double
) : UbicadorPelota {
    override fun aMundo(punto: Vector2D): Vector2D = transformador.pantallaAMundo(punto)

    override fun preparacionPara(puntoMundo: Vector2D): Preparacion {
        val pelota = sesion.pelota()
        val radio = pelota.radio()
        val offsetY = camara.offsetCamara().y()
        val minY = maxOf(motor.techoY(), offsetY) + radio
        val maxY = maxOf(minY, minOf(motor.sueloY(), offsetY + alto) - radio)
        return Preparacion(
            punto = puntoMundo,
            origen = pelota.posicion(),
            apoyoY = superficies.yDeApoyo(puntoMundo.x(), radio, motor.sueloY()),
            sueloY = motor.sueloY(),
            minY = minY,
            maxY = maxY
        )
    }

    override fun alturaActual(): Double {
        val pelota = sesion.pelota()
        return (motor.sueloY() - pelota.radio() - SuperficiesLanzamiento.ALTURA_APOYO_PELOTA -
            pelota.posicion().y()).coerceAtLeast(0.0)
    }

    override fun apoyarEnElSueloOSuperficie() {
        val pelota = sesion.pelota()
        val y = superficies.yDeApoyo(sesion.posicionOrigen().x(), pelota.radio(), motor.sueloY())
        sesion.reubicarPelota(Vector2D(sesion.posicionOrigen().x(), y))
    }
}
