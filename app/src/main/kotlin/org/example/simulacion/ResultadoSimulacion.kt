package org.example.simulacion

class ResultadoSimulacion(
    private val tiempoTotal: Double,
    private val distanciaX: Double,
    private val alturaMaxima: Double
) {
    fun tiempoTotal(): Double = tiempoTotal
    fun distanciaX(): Double = distanciaX
    fun alturaMaxima(): Double = alturaMaxima
    fun tiempoTexto(): String = String.format("%.2f", tiempoTotal)
    fun distanciaTexto(): String = String.format("%.2f", distanciaX)
    fun alturaTexto(): String = String.format("%.2f", alturaMaxima)

    fun formatearLinea(): String = "t=${tiempoTexto()}s x=${distanciaTexto()} h=${alturaTexto()}"
}
