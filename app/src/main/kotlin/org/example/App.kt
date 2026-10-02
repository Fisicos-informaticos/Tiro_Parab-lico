package org.example

import org.example.modelo.TipoPelota
import org.example.modelo.TipoPared
import org.example.modelo.Vector2D
import org.example.simulacion.EstadoSimulacion
import org.example.simulacion.Simulacion
import kotlin.math.cos
import kotlin.math.sin

/**
 * Segunda presentacion del mismo nucleo: la consola. Sirve para comprobar que
 * [Simulacion] no depende de ninguna ventana.
 */
class App {

    private val simulacion = Simulacion()

    fun iniciar() {
        println("=== SIMULADOR DE TIRO PARABOLICO (consola) ===")

        do {
            mostrarEstado()
            println("\n--- Nueva simulacion ---")
            simulacion.reiniciar()

            val tipoPelota = elegir("Pelota", TipoPelota.entries, { it.nombre })
            simulacion.seleccionarPelota(tipoPelota)
            val tipoPared = elegir("Pared", TipoPared.entries, { it.nombre })
            simulacion.seleccionarTipoPared(tipoPared)

            val angulo = leerDouble("Angulo de lanzamiento (grados)", 45.0)
            val fuerza = leerDouble("Fuerza (0-${Simulacion.MAX_FUERZA.toInt()})", 220.0)

            lanzar(angulo, fuerza)
            repetirTiros()
        } while (preguntar("Ejecutar otra simulacion? (s/n): "))

        println("\nFin del programa.")
    }

    /**
     * Lanza hacia arriba a la derecha con el angulo (sobre la horizontal) y la
     * fuerza pedidos. El vector de tiro es el contrario al arrastre, asi que
     * aqui se arrastra hacia abajo a la izquierda.
     */
    private fun lanzar(anguloGrados: Double, fuerza: Double) {
        val origen = simulacion.pelota.posicion - simulacion.offsetCamara
        val rad = Math.toRadians(anguloGrados)
        val destino = origen + Vector2D(-cos(rad) * fuerza, sin(rad) * fuerza)

        simulacion.pulsar(origen)
        simulacion.arrastrar(destino)
        simulacion.soltar(destino)
    }

    private fun repetirTiros() {
        val pasos = (1.0 / Simulacion.DT_FISICA).toInt()
        var impresos = 0
        while (simulacion.estado is EstadoSimulacion.EnVuelo) {
            simulacion.actualizar(Simulacion.DT_FISICA)
            if (impresos++ % pasos == 0) mostrarVuelo()
        }
        mostrarResumen()
    }

    private fun mostrarVuelo() {
        val pelota = simulacion.pelota
        println(
            "  t=${"%.1f".format(simulacion.tiempoVuelo)}s  " +
                "x=${"%.1f".format(pelota.posicion.x)}  y=${"%.1f".format(pelota.posicion.y)}  " +
                "v=${"%.1f".format(pelota.velocidad.magnitud)}"
        )
    }

    private fun mostrarResumen() {
        println("\n--- Resultados ---")
        println("Pelota: ${simulacion.tipoPelota.nombre} (${simulacion.etiquetaColision()})")
        println("Pared: ${simulacion.tipoPared.nombre} · la rompe: ${rompe()}")
        println("Distancia: ${"%.1f".format(simulacion.distanciaRecorrida)} px")
        println("Tiempo: ${"%.2f".format(simulacion.tiempoVuelo)} s")
        println("Paredes colocadas: ${simulacion.paredes.size} · rotas: ${simulacion.paredesDestruidas}")
    }

    private fun mostrarEstado() {
        println()
        println("Estado: ${simulacion.estado.nombre}")
    }

    private fun rompe(): String =
        if (simulacion.rompeParedSeleccionada()) "si" else "no"

    private fun <T> elegir(que: String, opciones: List<T>, texto: (T) -> String): T {
        println("$que disponibles:")
        opciones.forEachIndexed { indice, opcion ->
            println("  ${indice + 1}) ${texto(opcion)}")
        }
        print("$que (1-${opciones.size}): ")
        val indice = readlnOrNull()?.trim()?.toIntOrNull() ?: 1
        return opciones.getOrNull(indice - 1) ?: opciones.first()
    }

    private fun leerDouble(mensaje: String, valorDefecto: Double): Double {
        print("$mensaje (valor por defecto: $valorDefecto): ")
        val entrada = readlnOrNull()?.trim()
        return entrada?.toDoubleOrNull() ?: valorDefecto
    }

    private fun preguntar(mensaje: String): Boolean {
        print(mensaje)
        return readlnOrNull()?.trim()?.lowercase() == "s"
    }
}

private fun main() {
    App().iniciar()
}