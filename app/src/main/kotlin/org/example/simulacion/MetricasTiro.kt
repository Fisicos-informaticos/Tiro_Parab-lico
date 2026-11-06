package org.example.simulacion

import kotlin.math.max

/**
 * Acumula las metricas de un tiro y sabe resumirlas al terminar. Mantiene el
 * registro separado del estado de la simulacion para que cada clase tenga una
 * unica responsabilidad.
 */
class MetricasTiro : Metricas {

    private var tiempoVuelo: Double = 0.0
    private var distanciaRecorrida: Double = 0.0
    private var alturaMaxima: Double = 0.0
    private var distanciaX: Double = 0.0
    private var paredesDestruidas: Int = 0

    override fun tiempoVuelo(): Double = tiempoVuelo

    override fun distanciaRecorrida(): Double = distanciaRecorrida

    override fun alturaMaxima(): Double = alturaMaxima

    override fun distanciaX(): Double = distanciaX

    override fun paredesDestruidas(): Int = paredesDestruidas

    /** Deja las metricas del tiro a cero, conservando el total de la partida. */
    override fun reiniciarTiro() {
        tiempoVuelo = 0.0
        distanciaRecorrida = 0.0
        alturaMaxima = 0.0
        distanciaX = 0.0
    }

    /** Empieza una partida nueva: tambien borra las paredes destruidas. */
    override fun reiniciarPartida() {
        reiniciarTiro()
        paredesDestruidas = 0
    }

    override fun avanzarTiempo(deltaTime: Double) {
        tiempoVuelo += deltaTime
    }

    override fun sumarDistancia(distancia: Double) {
        distanciaRecorrida += distancia
    }

    override fun registrarAltura(altura: Double) {
        alturaMaxima = max(alturaMaxima, altura)
    }

    override fun registrarAlcance(alcance: Double) {
        distanciaX = max(distanciaX, alcance)
    }

    override fun registrarRoturas(cantidad: Int) {
        paredesDestruidas += cantidad
    }

    override fun resultado(): ResultadoSimulacion = ResultadoSimulacion(
        tiempoTotal = tiempoVuelo,
        distanciaX = distanciaX,
        alturaMaxima = alturaMaxima
    )
}
