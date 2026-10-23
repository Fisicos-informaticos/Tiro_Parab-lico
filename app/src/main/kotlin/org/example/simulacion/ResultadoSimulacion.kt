package org.example.simulacion

/**
 * Resumen de un tiro terminado. Es un objeto de solo lectura que el nucleo
 * entrega a quien observa el final de la simulacion, de modo que la interfaz no
 * necesita consultar la [Simulacion] para pintar los resultados.
 */
data class ResultadoSimulacion(
    /** Tiempo total de vuelo en segundos, ya detenido en el instante de reposo. */
    val tiempoTotal: Double,
    /** Distancia horizontal maxima alcanzada desde el punto de lanzamiento, en px. */
    val distanciaX: Double,
    /** Altura maxima alcanzada sobre el suelo, en px. */
    val alturaMaxima: Double
)
