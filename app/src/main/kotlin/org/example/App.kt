package org.example

import org.example.modelo.TipoPelota
import org.example.modelo.TipoPared
import org.example.simulacion.FabricaSimulacion
import org.example.simulacion.Simulacion

/**
 * Segunda presentacion del mismo nucleo: la consola. Sirve para comprobar que
 * [Simulacion] no depende de ninguna ventana.
 */
class App {

    private val simulacion = FabricaSimulacion.crear()
    private val entrada = EntradaConsola()
    private val presentador = PresentadorConsola(simulacion)

    fun iniciar() {
        println("=== SIMULADOR DE TIRO PARABOLICO (consola) ===")

        do {
            presentador.mostrarEstado()
            presentador.mostrarNuevaSimulacion()
            simulacion.reiniciar()

            prepararSimulacion()
            presentador.lanzar(
                entrada.leerDouble("Angulo de lanzamiento (grados)", 45.0),
                entrada.leerDouble("Fuerza (0-${Simulacion.MAX_FUERZA.toInt()})", 220.0)
            )
            presentador.repetirTiros()
        } while (entrada.preguntar("Ejecutar otra simulacion? (s/n): "))

        println("\nFin del programa.")
    }

    private fun prepararSimulacion() {
        val tipoPelota = entrada.elegir("Pelota", TipoPelota.entries, { it.nombre() })
        simulacion.seleccionarPelota(tipoPelota)
        val tipoPared = entrada.elegir("Pared", TipoPared.entries, { it.nombre() })
        simulacion.seleccionarTipoPared(tipoPared)
    }

    companion object {
        /** Punto de entrada de la presentacion por consola. */
        @JvmStatic
        fun main(args: Array<String>) {
            App().iniciar()
        }
    }
}
