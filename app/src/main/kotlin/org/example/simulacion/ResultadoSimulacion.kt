package org.example.simulacion

class ResultadoSimulacion(
    tiempoTotal: Double,
    distanciaX: Double,
    alturaMaxima: Double
) {
    private val datos = DatosResultadoSimulacion(tiempoTotal, distanciaX, alturaMaxima)

    fun tiempoTotal(): Double = datos.tiempoTotal
    fun distanciaX(): Double = datos.distanciaX
    fun alturaMaxima(): Double = datos.alturaMaxima
    fun tiempoTexto(): String = datos.tiempoTexto()
    fun distanciaTexto(): String = datos.distanciaTexto()
    fun alturaTexto(): String = datos.alturaTexto()
    fun formatearLinea(): String = datos.formatearLinea()

    private data class DatosResultadoSimulacion(
        val tiempoTotal: Double,
        val distanciaX: Double,
        val alturaMaxima: Double
    ) {
        fun tiempoTexto(): String = String.format("%.2f", tiempoTotal)

        fun distanciaTexto(): String = String.format("%.2f", distanciaX)

        fun alturaTexto(): String = String.format("%.2f", alturaMaxima)

        fun formatearLinea(): String = "t=${tiempoTexto()}s x=${distanciaTexto()} h=${alturaTexto()}"

        fun descripcion(): String =
            "resultado(tiempo=$tiempoTotal, distanciaX=$distanciaX, alturaMaxima=$alturaMaxima)"

        fun esValido(): Boolean = tiempoTotal >= 0 && distanciaX >= 0 && alturaMaxima >= 0

        fun resumen(): String = "$tiempoTotal s | $distanciaX px | $alturaMaxima px"

        fun datosCompletos(): List<Double> = listOf(tiempoTotal, distanciaX, alturaMaxima)

        fun esCero(): Boolean = tiempoTotal == 0.0 && distanciaX == 0.0 && alturaMaxima == 0.0

        fun valoraciones(): String =
            "${tiempoTexto()}s/${distanciaTexto()}px/${alturaTexto()}px (tiempo=$tiempoTotal, x=$distanciaX, h=$alturaMaxima)"
    }
}
