package org.example.ui

import javafx.scene.control.Button
import javafx.scene.layout.VBox

/** Controles de paredes y superficies de lanzamiento. */
internal class PanelObstaculos(
    private val acciones: AccionesJuego,
    private val edicion: EdicionEscenario
) {
    private val botonAnadirPared = Button().also {
        ConstructorPanel.prepararBoton(it) { edicion.alternarModoParedes() }
    }
    private val botonTipoPared = Button().also {
        ConstructorPanel.prepararBoton(it) { acciones.alternarMenuParedes() }
    }
    private val botonLimpiarParedes = Button("Limpiar paredes").also {
        ConstructorPanel.prepararBoton(it) { edicion.limpiarParedes() }
    }
    private val botonSuperficie = Button().also {
        ConstructorPanel.prepararBoton(it) { edicion.alternarModoSuperficies() }
    }
    private val botonLimpiarSuperficies = Button("Limpiar superficies").also {
        ConstructorPanel.prepararBoton(it) { edicion.limpiarSuperficiesLanzamiento() }
    }

    private val seccion: VBox = ConstructorPanel.seccion(
        "OBSTÁCULOS",
        listOf(
            botonAnadirPared,
            botonTipoPared,
            botonLimpiarParedes,
            botonSuperficie,
            botonLimpiarSuperficies
        )
    )

    fun seccion(): VBox = seccion

    fun refrescar(estado: EstadoPanel) {
        botonAnadirPared.text = if (estado.modoParedes()) "Cancelar pared" else "Añadir pared"
        botonAnadirPared.isDisable = estado.bloqueado()

        botonTipoPared.text =
            if (estado.menuParedesAbierto()) "Cerrar paredes" else "Pared: ${estado.nombreTipoPared()}"
        botonTipoPared.isDisable = estado.bloqueado()
        botonLimpiarParedes.isDisable = estado.bloqueado() || !estado.hayParedes()

        botonSuperficie.text =
            if (estado.modoSuperficies()) "Cancelar superficie" else "Superficie de lanzamiento"
        botonSuperficie.isDisable = estado.bloqueado()
        botonLimpiarSuperficies.isDisable = estado.bloqueado() || !estado.haySuperficies()
    }
}
