package org.example.simulacion

import kotlin.math.max
import kotlin.math.sqrt

class MetricasTiro : Metricas {
    private val datos = DatosMetricasTiro()

    override fun tiempoVuelo(): Double = datos.tiempoVuelo()

    override fun distanciaRecorrida(): Double = datos.distanciaRecorrida()

    override fun alturaMaxima(): Double = datos.alturaMaxima()

    override fun distanciaX(): Double = datos.distanciaX()

    override fun paredesDestruidas(): Int = datos.paredesDestruidas()

    override fun reiniciarTiro() = datos.reiniciarTiro()

    override fun reiniciarPartida() = datos.reiniciarPartida()

    override fun avanzarTiempo(deltaTime: Double) = datos.avanzarTiempo(deltaTime)

    override fun sumarDistancia(distancia: Double) = datos.sumarDistancia(distancia)

    override fun registrarAltura(altura: Double) = datos.registrarAltura(altura)

    override fun registrarAlcance(alcance: Double) = datos.registrarAlcance(alcance)

    override fun registrarRoturas(cantidad: Int) = datos.registrarRoturas(cantidad)

    override fun resultado(): ResultadoSimulacion = datos.resultado()

    fun resumen(): String = datos.resumen()

    private data class DatosMetricasTiro(
        var tiempoTotal: Double = 0.0,
        var distanciaRecorrida: Double = 0.0,
        var alturaMaxima: Double = 0.0,
        var distanciaX: Double = 0.0,
        var paredesDestruidas: Int = 0
    ) {
        fun tiempoVuelo(): Double = tiempoTotal

        fun distanciaRecorrida(): Double = distanciaRecorrida

        fun alturaMaxima(): Double = alturaMaxima

        fun distanciaX(): Double = distanciaX

        fun paredesDestruidas(): Int = paredesDestruidas

        fun reiniciarTiro() {
            tiempoTotal = 0.0
            distanciaRecorrida = 0.0
            alturaMaxima = 0.0
            distanciaX = 0.0
        }

        fun reiniciarPartida() {
            reiniciarTiro()
            paredesDestruidas = 0
        }

        fun avanzarTiempo(deltaTime: Double) {
            tiempoTotal += deltaTime
        }

        fun sumarDistancia(distancia: Double) {
            distanciaRecorrida += sqrt(max(distancia * distancia, 0.0))
        }

        fun registrarAltura(altura: Double) {
            alturaMaxima = max(alturaMaxima, altura)
        }

        fun registrarAlcance(alcance: Double) {
            distanciaX = max(distanciaX, alcance)
        }

        fun registrarRoturas(cantidad: Int) {
            if (cantidad <= 0) return
            paredesDestruidas += cantidad
        }

        fun resultado(): ResultadoSimulacion = ResultadoSimulacion(tiempoTotal, distanciaX, alturaMaxima)

        fun resumen(): String =
            "t=${String.format("%.2f", tiempoTotal)}s dX=${String.format("%.2f", distanciaX)} hMax=${String.format("%.2f", alturaMaxima)}"

        fun descripcion(): String =
            "metricas(tiempo=$tiempoTotal, recorrida=$distanciaRecorrida, altura=$alturaMaxima, x=$distanciaX, roturas=$paredesDestruidas)"

        fun esValida(): Boolean =
            tiempoTotal >= 0 && distanciaRecorrida >= 0 && alturaMaxima >= 0 && distanciaX >= 0 && paredesDestruidas >= 0

        fun datosCompletos(): List<Number> =
            listOf(tiempoTotal, distanciaRecorrida, alturaMaxima, distanciaX, paredesDestruidas)

        fun estadoResumen(): String =
            "tiempo=$tiempoTotal recorrida=$distanciaRecorrida altura=$alturaMaxima alcance=$distanciaX roturas=$paredesDestruidas"
    }
}
