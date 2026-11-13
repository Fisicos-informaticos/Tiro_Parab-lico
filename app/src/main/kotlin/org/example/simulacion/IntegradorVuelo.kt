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
        var restante = contexto.deltaTime
        while (restante > PASO_MINIMO) {
            val paso = min(restante, Simulacion.DT_FISICA)
            registrarPaso(PasoIntegracion(contexto, paso, contexto.proyectil.posicion()))
            restante -= paso

            // El reloj se detiene en el instante exacto en que la pelota para.
            if (motor.estaEnReposo(contexto.proyectil)) return true
        }
        return metricas.tiempoVuelo() >= TIEMPO_MAXIMO_VUELO
    }

    private fun registrarPaso(paso: PasoIntegracion) {
        val contexto = paso.contexto
        val pelota = contexto.proyectil
        val paredesRotas = motor.simularPaso(pelota, paso.deltaTime)
        metricas.registrarRoturas(paredesRotas.size)
        efectos.agregarRoturas(paredesRotas, Simulacion.DURACION_ROTURA)

        metricas.sumarDistancia(pelota.posicion().distanciaA(paso.posicionAnterior))
        metricas.avanzarTiempo(paso.deltaTime)
        metricas.registrarAltura(contexto.alturaActual())
        metricas.registrarAlcance(pelota.posicion().x() - contexto.posicionOrigen.x())
    }

    private companion object {
        const val TIEMPO_MAXIMO_VUELO = 30.0

        /** Resto de tiempo por debajo del cual ya no se da un paso mas. */
        const val PASO_MINIMO = 1e-9
    }
}

private data class PasoIntegracion(
    val contexto: ContextoVuelo,
    val deltaTime: Double,
    val posicionAnterior: Vector2D
)
