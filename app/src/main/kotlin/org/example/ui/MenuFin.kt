package org.example.ui

import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.VBox

/** Menu de fin de tiro con el resumen de la partida. */
internal class MenuFin(
    private val escucha: UIEventListener,
    private val alSalir: () -> Unit
) {
    private val etiquetaResumen = Label().also {
        it.alignment = Pos.CENTER
        it.style = EstilosUI.SUBTITULO
    }

    private val menu: VBox = crear()

    fun menu(): VBox = menu

    fun mostrarResumen(texto: String) {
        etiquetaResumen.text = texto
    }

    private fun crear(): VBox {
        val titulo = Label("LA PELOTA HA CAIDO").also { it.style = EstilosUI.TITULO }
        val reiniciar = boton("Reiniciar") { escucha.reiniciar() }
        val salir = boton("Salir del simulador") { alSalir() }

        return VBox(14.0, titulo, etiquetaResumen, reiniciar, salir)
            .also { ConstructorMenu.decorar(it, EstilosUI.MENU_FIN) }
    }

    private fun boton(texto: String, accion: () -> Unit): Button =
        Button(texto).also {
            it.prefWidth = ConstructorMenu.ANCHO_BOTON_ACCION
            it.setOnAction { accion() }
        }
}
