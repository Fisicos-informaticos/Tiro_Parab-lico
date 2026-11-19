package org.example.ui

import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.layout.VBox

/** Controles del modo, la gravedad y el avance de la simulacion. */
internal class PanelSimulacion(
    escucha: UIEventListener,
    configuracion: ConfiguracionPanelSimulacion
) {
    private val comboModo: ComboBox<String> = ConstructorPanel.combo(
        ConstructorPanel.EspecificacionCombo(
            opciones = configuracion.modos(),
            seleccionada = configuracion.modoInicialValido(),
            ayuda = "Elige el modo de simulacion",
            alCambiar = { escucha.seleccionarModo(it) }
        )
    )

    private val comboGravedad: ComboBox<String> = ConstructorPanel.combo(
        ConstructorPanel.EspecificacionCombo(
            opciones = configuracion.gravedades(),
            seleccionada = configuracion.gravedadInicialValida(),
            ayuda = "Elige la gravedad con la que se simula el tiro",
            alCambiar = { escucha.aplicarGravedad(it) }
        )
    )

    private val botonPausa = Button().also {
        ConstructorPanel.prepararBoton(it) { escucha.alternarPausa() }
    }
    private val botonReiniciar = Button("Reiniciar").also {
        ConstructorPanel.prepararBoton(it) { escucha.reiniciar() }
    }

    private val etiquetaGravedad = ConstructorPanel.etiquetaDetalle()
    private val etiquetaAyuda = ConstructorPanel.etiquetaAyuda(PanelControl.ANCHO_PANEL)

    private val seccion: VBox = crear()

    fun seccion(): VBox = seccion

    fun refrescar(estado: EstadoPanel) {
        botonPausa.text = if (estado.pausado()) "Continuar" else "Pausa"
        botonPausa.isDisable = estado.enPantallaInicio()
        botonReiniciar.isDisable = estado.enVuelo() || estado.enPantallaInicio()

        comboModo.isDisable = estado.bloqueado()
        if (comboModo.value != estado.nombreModo()) comboModo.value = estado.nombreModo()
        comboGravedad.isDisable = estado.interaccionBloqueada()

        etiquetaGravedad.text = "Gravedad actual: ${estado.gravedadTexto()} g"
        etiquetaAyuda.text = estado.ayudaModo()
    }

    private fun crear(): VBox = ConstructorPanel.seccion(
        "SIMULACIÓN",
        listOf(
            ConstructorPanel.campo("Modo de simulación", comboModo),
            ConstructorPanel.campo("Gravedad", comboGravedad),
            etiquetaGravedad,
            ConstructorPanel.fila(botonPausa, botonReiniciar),
            etiquetaAyuda
        )
    )
}
