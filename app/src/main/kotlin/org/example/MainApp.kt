package org.example

import javafx.application.Application
import javafx.scene.canvas.Canvas
import javafx.stage.Stage
import org.example.modelo.TipoPelota
import org.example.modelo.TipoPared
import org.example.simulacion.FabricaSimulacion
import org.example.ui.ControlEventosUI

class MainApp : Application(), ControlEventosUI {
    companion object {
        const val ANCHO_VENTANA = 1000.0
        const val ALTO_VENTANA = 650.0
    }

    private val simulacion = FabricaSimulacion.crear(ANCHO_VENTANA, ALTO_VENTANA)
    private lateinit var canvas: Canvas
    private lateinit var presentador: org.example.grafico.PresentadorJuego
    private lateinit var panelControl: org.example.ui.PanelControl
    private lateinit var menus: org.example.ui.MenusJuego
    private lateinit var gestorMenus: GestorMenus
    private lateinit var detectorFin: DetectorFinDeVuelo
    private lateinit var constructorEstado: ConstructorEstadoPanel

    internal val canvasRef: Canvas get() = canvas

    override fun start(stage: Stage) {
        canvas = crearCanvas(ANCHO_VENTANA, ALTO_VENTANA)
        val refs = crearUiRefs(simulacion, this)
        presentador = refs.presentador
        panelControl = refs.panelControl
        menus = refs.menus
        gestorMenus = refs.gestorMenus
        detectorFin = refs.detectorFin
        constructorEstado = refs.constructorEstado
        val scene = crearEscenaRoot(canvas, panelControl, menus, ANCHO_VENTANA, ALTO_VENTANA)
        enlazarEventos(canvas, scene, simulacion, gestorMenus, this)
        actualizarControles()
        BucleJuego { delta ->
            simulacion.actualizar(delta)
            detectorFin.detectar()
            presentador.pintar(simulacion)
            actualizarControles()
        }.iniciar()
        stage.title = "Simulador de Tiro Parabolico"
        stage.scene = scene
        stage.isResizable = false
        stage.show()
    }

    fun cerrarMenusDeSeleccionPublico() {
        gestorMenus.cerrarTodosLosMenus()
        actualizarControles()
    }

    override fun alternarModoParedes() = ejecutarAccionConBloqueo(simulacion, gestorMenus, ::actualizarControles) { simulacion.alternarModoParedes() }
    override fun alternarMenuParedes() = alternarMenuParedesApp(gestorMenus, ::actualizarControles)
    override fun limpiarParedes() = ejecutarAccionConBloqueo(simulacion, gestorMenus, ::actualizarControles) { simulacion.limpiarParedes() }
    override fun alternarModoSuperficies() = ejecutarAccionConBloqueo(simulacion, gestorMenus, ::actualizarControles) { simulacion.alternarModoSuperficies() }
    override fun limpiarSuperficiesLanzamiento() = ejecutarAccionConBloqueo(simulacion, gestorMenus, ::actualizarControles) { simulacion.limpiarSuperficiesLanzamiento() }
    override fun alternarMenuPelotas() = alternarMenuPelotasApp(gestorMenus, ::actualizarControles)
    override fun alternarPausa() = alternarPausaApp(simulacion, gestorMenus, ::actualizarControles)
    override fun reiniciar() = reiniciarPartida(simulacion, gestorMenus, detectorFin, ::actualizarControles)
    override fun volverAlMenu() = volverAlMenuApp(simulacion, gestorMenus, detectorFin, ::actualizarControles)
    override fun seleccionarModo(nombre: String?) = ejecutarAccionConBloqueo(simulacion, gestorMenus, ::actualizarControles) { simulacion.seleccionarModo(nombre) }
    override fun aplicarGravedad(nombre: String?) = ejecutarAccionConBloqueo(simulacion, gestorMenus, ::actualizarControles) { simulacion.aplicarGravedad(nombre) }
    override fun seleccionarPelota(tipo: TipoPelota) = simulacion.seleccionarPelota(tipo)
    override fun seleccionarTipoPared(tipo: TipoPared) = simulacion.seleccionarTipoPared(tipo)

    override fun actualizarControles() {
        gestorMenus.sincronizarVisibilidad()
        panelControl.refrescar(constructorEstado.crear())
    }
}
