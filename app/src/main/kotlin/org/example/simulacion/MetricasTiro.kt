package org.example.simulacion

import kotlin.math.max

/**
 * Acumula las metricas de un tiro y sabe resumirlas al terminar. Mantiene el
 * registro separado del estado de la simulacion para que cada clase tenga una
 * unica responsabilidad.
 */
class MetricasTiro {

    private var tiempoVuelo: Double = 0.0
    private var distanciaRecorrida: Double = 0.0
    private var alturaMaxima: Double = 0.0
    private var distanciaX: Double = 0.0
    private var paredesDestruidas: Int = 0

    fun tiempoVuelo(): Double = tiempoVuelo

    fun distanciaRecorrida(): Double = distanciaRecorrida

    fun alturaMaxima(): Double = alturaMaxima

    fun distanciaX(): Double = distanciaX

    fun paredesDestruidas(): Int = paredesDestruidas

    /** Deja las metricas del tiro a cero, conservando el total de la partida. */
    fun reiniciarTiro() {
        tiempoVuelo = 0.0
        distanciaRecorrida = 0.0
        alturaMaxima = 0.0
        distanciaX = 0.0
    }

    /** Empieza una partida nueva: tambien borra las paredes destruidas. */
    fun reiniciarPartida() {
        reiniciarTiro()
        paredesDestruidas = 0
    }

    fun avanzarTiempo(deltaTime: Double) {
        tiempoVuelo += deltaTime
    }

    fun sumarDistancia(distancia: Double) {
        distanciaRecorrida += distancia
    }

    fun registrarAltura(altura: Double) {
        alturaMaxima = max(alturaMaxima, altura)
    }

    fun registrarAlcance(alcance: Double) {
        distanciaX = max(distanciaX, alcance)
    }

    fun registrarRoturas(cantidad: Int) {
        paredesDestruidas += cantidad
    }

    fun resultado(): ResultadoSimulacion = ResultadoSimulacion(
        tiempoTotal = tiempoVuelo,
        distanciaX = distanciaX,
        alturaMaxima = alturaMaxima
    )
}
