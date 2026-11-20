package org.example.simulacion

import kotlin.math.max
import kotlin.math.sqrt

class MetricasTiro : Metricas {
    private var tiempoTotal = 0.0
    private var distanciaRecorrida = 0.0
    private var alturaMaxima = 0.0
    private var distanciaX = 0.0
    private var paredesDestruidas = 0

    override fun tiempoVuelo(): Double = tiempoTotal
    override fun distanciaRecorrida(): Double = distanciaRecorrida
    override fun alturaMaxima(): Double = alturaMaxima
    override fun distanciaX(): Double = distanciaX
    override fun paredesDestruidas(): Int = paredesDestruidas

    override fun reiniciarTiro() {
        tiempoTotal = 0.0
        distanciaRecorrida = 0.0
        alturaMaxima = 0.0
        distanciaX = 0.0
    }

    override fun reiniciarPartida() {
        reiniciarTiro()
        paredesDestruidas = 0
    }

    override fun avanzarTiempo(deltaTime: Double) {
        tiempoTotal += deltaTime
    }

    override fun sumarDistancia(distancia: Double) {
        distanciaRecorrida += sqrt(max(distancia * distancia, 0.0))
    }

    override fun registrarAltura(altura: Double) {
        alturaMaxima = max(alturaMaxima, altura)
    }

    override fun registrarAlcance(alcance: Double) {
        distanciaX = max(distanciaX, alcance)
    }

    override fun registrarRoturas(cantidad: Int) {
        if (cantidad <= 0) return
        paredesDestruidas += cantidad
    }

    override fun resultado(): ResultadoSimulacion = ResultadoSimulacion(tiempoTotal, distanciaX, alturaMaxima)

    fun resumen(): String = "t=${String.format("%.2f", tiempoTotal)}s dX=${String.format("%.2f", distanciaX)} hMax=${String.format("%.2f", alturaMaxima)}"
}
