package org.example.ui

import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.VBox

/** Controles de seleccion de la pelota actual. */
internal class PanelPelotas(escucha: AccionesJuego) {
    private val boton = Button().also {
        ConstructorPanel.prepararBoton(it) { escucha.alternarMenuPelotas() }
    }
    private val etiqueta = ConstructorPanel.etiquetaDetalle()

    private val seccion: VBox = ConstructorPanel.seccion("PELOTAS", listOf(boton, etiqueta))

    fun seccion(): VBox = seccion

    fun refrescar(estado: EstadoPanel) {
        boton.text = if (estado.menuPelotasAbierto()) "Cerrar pelotas" else "Pelotas"
        boton.isDisable = estado.bloqueado()
        etiqueta.text = "Actual: ${estado.nombrePelota()}"
    }
}
