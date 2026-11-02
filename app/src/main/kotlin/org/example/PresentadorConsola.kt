package org.example

import org.example.modelo.Vector2D
import org.example.simulacion.Simulacion
import kotlin.math.cos
import kotlin.math.sin

class PresentadorConsola(
    private val simulacion: Simulacion
) {
    fun mostrarEstado() {
        println()
        println("Estado: ${simulacion.estado().nombre()}")
    }

    fun mostrarNuevaSimulacion() {
        println("\n--- Nueva simulacion ---")
    }

    fun lanzar(anguloGrados: Double, fuerza: Double) {
        val origen = simulacion.pelota().posicion() - simulacion.offsetCamara()
        val rad = Math.toRadians(anguloGrados)
        val destino = origen + Vector2D(-cos(rad) * fuerza, sin(rad) * fuerza)

        simulacion.pulsar(origen)
        simulacion.arrastrar(destino)
        simulacion.soltar(destino)
    }

    fun repetirTiros() {
        val pasos = (1.0 / Simulacion.DT_FISICA).toInt()
        var impresos = 0
        while (simulacion.estado().estaEnVuelo()) {
            simulacion.actualizar(Simulacion.DT_FISICA)
            if (impresos++ % pasos == 0) mostrarVuelo()
        }
        mostrarResumen()
    }

    private fun mostrarVuelo() {
        val pelota = simulacion.pelota()
        println(
            "  t=${"%.1f".format(simulacion.tiempoVuelo())}s  " +
                "x=${"%.1f".format(pelota.posicion().x())}  y=${"%.1f".format(pelota.posicion().y())}  " +
                "v=${"%.1f".format(pelota.velocidad().magnitud())}"
        )
    }

    private fun mostrarResumen() {
        println("\n--- Resultados ---")
        println("Pelota: ${simulacion.tipoPelota().nombre()} (${simulacion.etiquetaColision()})")
        println("Pared: ${simulacion.tipoPared().nombre()} · la rompe: ${rompe()}")
        println("Distancia: ${"%.1f".format(simulacion.distanciaRecorrida())} px")
        println("Tiempo: ${"%.2f".format(simulacion.tiempoVuelo())} s")
        println("Paredes colocadas: ${simulacion.paredes().size} · rotas: ${simulacion.paredesDestruidas()}")
    }

    private fun rompe(): String =
        if (simulacion.rompeParedSeleccionada()) "si" else "no"
}
