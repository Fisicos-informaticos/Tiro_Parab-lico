package org.example

import javafx.application.Platform
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.layout.StackPane
import org.example.grafico.FabricaRenderer
import org.example.grafico.PintorApuntado
import org.example.grafico.PintorInformacion
import org.example.grafico.PintorMundo
import org.example.grafico.PresentadorJuego
import org.example.modelo.TipoPelota
import org.example.modelo.TipoPared
import org.example.simulacion.Simulacion
import org.example.sprites.FabricaSpriteEstatica
import org.example.sprites.SpriteManager
import org.example.ui.ConfiguracionPanelSimulacion
import org.example.ui.ControladorEventosUI
import org.example.ui.MenusJuego
import org.example.ui.PanelControl

internal data class UiRefs(
    val presentador: PresentadorJuego,
    val panelControl: PanelControl,
    val menus: MenusJuego,
    val gestorMenus: GestorMenus,
    val detectorFin: DetectorFinDeVuelo,
    val constructorEstado: ConstructorEstadoPanel
)

internal fun crearCanvas(ancho: Double, alto: Double): Canvas = Canvas(ancho, alto).also {
    it.graphicsContext2D.isImageSmoothing = true
}

internal fun crearPresentador(canvas: Canvas, ancho: Double, alto: Double): PresentadorJuego {
    val renderer = FabricaRenderer.crear(canvas.graphicsContext2D, ancho, alto)
    val sprites = SpriteManager(FabricaSpriteEstatica())
    return PresentadorJuego(renderer, sprites, PintorMundo(renderer, sprites), PintorApuntado(renderer), PintorInformacion(renderer))
}

internal fun crearUiRefs(simulacion: Simulacion, app: MainApp): UiRefs {
    val panelControl = PanelControl(app, ConfiguracionPanelSimulacion(
        modos = simulacion.nombresModos(),
        modoInicial = simulacion.modo().nombre(),
        gravedades = Simulacion.gravedades(),
        gravedadInicial = Simulacion.GRAVEDAD_POR_DEFECTO
    ))
    val menus = MenusJuego(
        escucha = app,
        tipoPelotaActual = { simulacion.tipoPelota() },
        tipoParedActual = { simulacion.tipoPared() },
        alCerrarMenus = { app.cerrarMenusDeSeleccionPublico() },
        alSalir = { Platform.exit() }
    )
    val gestorMenus = GestorMenus(simulacion, menus)
    val detectorFin = DetectorFinDeVuelo(simulacion, gestorMenus, menus)
    val constructorEstado = ConstructorEstadoPanel(simulacion, gestorMenus)
    return UiRefs(crearPresentador(app.canvasRef, MainApp.ANCHO_VENTANA, MainApp.ALTO_VENTANA), panelControl, menus, gestorMenus, detectorFin, constructorEstado)
}

internal fun crearEscenaRoot(canvas: Canvas, panelControl: PanelControl, menus: MenusJuego, ancho: Double, alto: Double): Scene {
    val root = StackPane(canvas, panelControl.raiz(), menus.menuPelotas(), menus.menuParedes(), menus.menuPausa(), menus.menuFin())
    StackPane.setAlignment(panelControl.raiz(), Pos.TOP_RIGHT)
    StackPane.setMargin(panelControl.raiz(), Insets(12.0))
    for (menu in listOf(menus.menuPelotas(), menus.menuParedes(), menus.menuPausa(), menus.menuFin())) {
        StackPane.setAlignment(menu, Pos.CENTER)
    }
    return Scene(root, ancho, alto)
}

internal fun enlazarEventos(canvas: Canvas, scene: Scene, simulacion: Simulacion, gestorMenus: GestorMenus, app: MainApp) {
    ControladorEventosUI(simulacion, gestorMenus, app).enlazar(canvas, scene)
}

internal fun ejecutarAccionConBloqueo(simulacion: Simulacion, gestorMenus: GestorMenus, actualizar: () -> Unit, accion: () -> Unit) {
    if (gestorMenus.interaccionBloqueada() || gestorMenus.enVuelo()) return
    accion()
    actualizar()
}

internal fun reiniciarPartida(simulacion: Simulacion, gestorMenus: GestorMenus, detectorFin: DetectorFinDeVuelo, actualizar: () -> Unit) {
    gestorMenus.cerrarTodosLosMenus()
    gestorMenus.cerrarMenuFin()
    simulacion.nuevaPartida()
    detectorFin.reiniciarSeguimiento()
    actualizar()
}

internal fun volverAlMenuApp(simulacion: Simulacion, gestorMenus: GestorMenus, detectorFin: DetectorFinDeVuelo, actualizar: () -> Unit) {
    gestorMenus.cerrarTodosLosMenus()
    gestorMenus.cerrarMenuFin()
    simulacion.volverAlMenu()
    detectorFin.reiniciarSeguimiento()
    actualizar()
}

internal fun alternarPausaApp(simulacion: Simulacion, gestorMenus: GestorMenus, actualizar: () -> Unit) {
    if (gestorMenus.menuFinAbierto() || !simulacion.estado().permitePausa()) return
    simulacion.alternarPausa()
    if (simulacion.pausado()) gestorMenus.cerrarTodosLosMenus()
    actualizar()
}

internal fun alternarMenuPelotasApp(gestorMenus: GestorMenus, actualizar: () -> Unit) {
    gestorMenus.alternarMenuPelotas()
    actualizar()
}

internal fun alternarMenuParedesApp(gestorMenus: GestorMenus, actualizar: () -> Unit) {
    gestorMenus.alternarMenuParedes()
    actualizar()
}
