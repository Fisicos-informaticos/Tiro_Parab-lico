package org.example.simulacion

import org.example.fisica.PhysicsEngine
import org.example.modelo.Proyectil
import org.example.modelo.Vector2D
import kotlin.math.min

/**
 * Avanza el proyectil en pasos pequenos, actualiza las metricas y acumula las
 * roturas. Devuelve true cuando el tiro debe cerrarse.
 */
class IntegradorVuelo(
    private val motor: PhysicsEngine,
    private val metricas: Metricas,
    private val efectos: Efectos
) : Integrador {

    override fun avanzar(contexto: ContextoVuelo): Boolean {
        var restante = contexto.deltaTime()
        while (restante > PASO_MINIMO) {
            val paso = min(restante, Simulacion.DT_FISICA)
            registrarPaso(PasoIntegracion(contexto, paso, contexto.proyectil().posicion()))
            restante -= paso

            if (motor.estaEnReposo(contexto.proyectil())) return true
        }
        return metricas.tiempoVuelo() >= TIEMPO_MAXIMO_VUELO
    }

    private fun registrarPaso(paso: PasoIntegracion) {
        paso.aplicar(motor, metricas, efectos)
    }

    private companion object {
        const val TIEMPO_MAXIMO_VUELO = 30.0

        const val PASO_MINIMO = 1e-9
    }
}

private class PasoIntegracion(
    private val contexto: ContextoVuelo,
    private val deltaTime: Double,
    private val posicionAnterior: Vector2D
) {
    fun aplicar(motor: PhysicsEngine, metricas: Metricas, efectos: Efectos) {
        val pelota = contexto.proyectil()
        val paredesRotas = motor.simularPaso(pelota, deltaTime)
        metricas.registrarRoturas(paredesRotas.size)
        efectos.agregarRoturas(paredesRotas, Simulacion.DURACION_ROTURA)
        metricas.sumarDistancia(pelota.posicion().distanciaA(posicionAnterior))
        metricas.avanzarTiempo(deltaTime)
        metricas.registrarAltura(contexto.alturaActual())
        metricas.registrarAlcance(contexto.alcanceActual())
    }
}
