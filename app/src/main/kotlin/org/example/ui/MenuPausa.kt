package org.example.ui

import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.VBox

/** Menu que se muestra mientras la partida esta en pausa. */
internal class MenuPausa(
    private val escucha: UIEventListener,
    private val alSalir: () -> Unit
) {
    private val menu: VBox = crear()

    fun menu(): VBox = menu

    private fun crear(): VBox {
        val titulo = Label("PAUSA").also { it.style = EstilosUI.TITULO }
        val continuar = boton("Continuar") { escucha.alternarPausa() }
        val reiniciar = boton("Reiniciar tiro") { escucha.reiniciar() }
        val salir = boton("Salir del simulador") { alSalir() }

        return VBox(14.0, titulo, continuar, reiniciar, salir)
            .also { ConstructorMenu.decorar(it, EstilosUI.MENU) }
    }

    private fun boton(texto: String, accion: () -> Unit): Button =
        Button(texto).also {
            it.prefWidth = ConstructorMenu.ANCHO_BOTON_ACCION
            it.setOnAction { accion() }
        }
}
