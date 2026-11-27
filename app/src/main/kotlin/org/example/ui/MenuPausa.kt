package org.example.ui

import javafx.scene.control.Label
import javafx.scene.layout.VBox

/** Menu que se muestra mientras la partida esta en pausa. */
internal class MenuPausa(
    private val escucha: AccionesJuego,
    private val alSalir: () -> Unit
) {
    private val menu: VBox = crear()

    fun menu(): VBox = menu

    private fun crear(): VBox {
        val titulo = Label("PAUSA").also { it.style = EstilosUI.TITULO }
        val continuar = ConstructorMenu.botonAccion("Continuar") { escucha.alternarPausa() }
        val reiniciar = ConstructorMenu.botonAccion("Reiniciar tiro") { escucha.reiniciar() }
        val salir = ConstructorMenu.botonAccion("Salir del simulador") { alSalir() }

        return VBox(14.0, titulo, continuar, reiniciar, salir)
            .also { ConstructorMenu.decorar(it, EstilosUI.MENU) }
    }

}
