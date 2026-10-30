package org.example.simulacion

/**
 * Resumen de un tiro terminado. Encapsula sus metricas y sabe presentarlas en
 * texto, de modo que la interfaz no necesita manipular los numeros.
 */
class ResultadoSimulacion(
    /** Tiempo total de vuelo en segundos, ya detenido en el instante de reposo. */
    private val tiempoTotal: Double,
    /** Distancia horizontal maxima alcanzada desde el punto de lanzamiento, en px. */
    private val distanciaX: Double,
    /** Altura maxima alcanzada sobre el suelo, en px. */
    private val alturaMaxima: Double
) {
    fun tiempoTotal(): Double = tiempoTotal

    fun distanciaX(): Double = distanciaX

    fun alturaMaxima(): Double = alturaMaxima

    fun tiempoTexto(): String = String.format("%.2f", tiempoTotal)

    fun distanciaTexto(): String = String.format("%.2f", distanciaX)

    fun alturaTexto(): String = String.format("%.2f", alturaMaxima)
}
