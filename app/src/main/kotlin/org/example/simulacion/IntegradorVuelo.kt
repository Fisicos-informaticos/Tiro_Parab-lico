package org.example.simulacion

import org.example.fisica.PhysicsEngine
import org.example.modelo.Pelota
import org.example.modelo.Vector2D
import kotlin.math.min

/**
 * Avanza el proyectil en pasos pequenos, actualiza las metricas y acumula las
 * roturas. Devuelve true cuando el tiro debe cerrarse.
 */
class IntegradorVuelo(
    private val motor: PhysicsEngine,
    private val metricas: MetricasTiro,
    private val efectos: RegistroEfectos
) {

    fun avanzar(
        pelota: Pelota,
        deltaTime: Double,
        posicionOrigen: Vector2D,
        alturaActual: () -> Double
    ): Boolean {
        var restante = deltaTime
        while (restante > PASO_MINIMO) {
            val paso = min(restante, Simulacion.DT_FISICA)
            val antes = pelota.posicion()
            registrarPaso(pelota, paso, antes, posicionOrigen, alturaActual)
            restante -= paso

            // El reloj se detiene en el instante exacto en que la pelota para.
            if (motor.estaEnReposo(pelota)) return true
        }
        return metricas.tiempoVuelo() >= TIEMPO_MAXIMO_VUELO
    }

    private fun registrarPaso(
        pelota: Pelota,
        paso: Double,
        posicionAnterior: Vector2D,
        posicionOrigen: Vector2D,
        alturaActual: () -> Double
    ) {
        val paredesRotas = motor.simularPaso(pelota, paso)
        metricas.registrarRoturas(paredesRotas.size)
        efectos.agregarRoturas(paredesRotas, Simulacion.DURACION_ROTURA)

        metricas.sumarDistancia(pelota.posicion().distanciaA(posicionAnterior))
        metricas.avanzarTiempo(paso)
        metricas.registrarAltura(alturaActual())
        metricas.registrarAlcance(pelota.posicion().x() - posicionOrigen.x())
    }

    private companion object {
        const val TIEMPO_MAXIMO_VUELO = 30.0

        /** Resto de tiempo por debajo del cual ya no se da un paso mas. */
        const val PASO_MINIMO = 1e-9
    }
}
