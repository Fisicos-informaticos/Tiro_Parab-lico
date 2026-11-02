package org.example

import javafx.application.Application
import javafx.application.Platform
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.input.KeyCode
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import org.example.grafico.CanvasRenderer
import org.example.grafico.PresentadorJuego
import org.example.grafico.VectorRenderer
import org.example.modelo.TipoPelota
import org.example.modelo.TipoPared
import org.example.modelo.Vector2D
import org.example.simulacion.FabricaSimulacion
import org.example.simulacion.Simulacion
import org.example.sprites.FabricaSpriteEstatica
import org.example.sprites.SpriteManager
import org.example.ui.MenusJuego
import org.example.ui.PanelControl
import org.example.ui.UIEventListener

/**
 * Capa JavaFX: construye la ventana, los menus y reenvia los eventos al nucleo.
 * No decide nada del juego; de eso se encarga [Simulacion].
 */
class MainApp : Application(), UIEventListener {

    companion object {
        const val ANCHO_VENTANA = 1000.0
        const val ALTO_VENTANA = 650.0
    }

    private val simulacion = FabricaSimulacion.crear(ANCHO_VENTANA, ALTO_VENTANA)

    private lateinit var canvas: Canvas
    private lateinit var presentador: PresentadorJuego
    private lateinit var panelControl: PanelControl
    private lateinit var menus: MenusJuego
    private lateinit var gestorMenus: GestorMenus
    private lateinit var detectorFin: DetectorFinDeVuelo
    private lateinit var constructorEstado: ConstructorEstadoPanel

    override fun start(stage: Stage) {
        canvas = crearCanvas()
        presentador = crearPresentador()
        panelControl = crearPanelControl()
        crearMenus()

        val scene = crearEscena()
        configurarEventos(scene)
        actualizarControles()
        configurarBucleDeJuego()
        configurarVentana(stage, scene)
    }

    private fun crearCanvas(): Canvas = Canvas(ANCHO_VENTANA, ALTO_VENTANA).also {
        it.graphicsContext2D.isImageSmoothing = true
    }

    private fun crearPresentador(): PresentadorJuego {
        val renderer = CanvasRenderer(
            canvas.graphicsContext2D,
            ANCHO_VENTANA,
            ALTO_VENTANA,
            VectorRenderer(canvas.graphicsContext2D)
        )
        return PresentadorJuego(renderer, SpriteManager(FabricaSpriteEstatica()))
    }

    private fun crearPanelControl(): PanelControl = PanelControl(
        this,
        simulacion.nombresModos(),
        simulacion.modo().nombre(),
        Simulacion.gravedades(),
        Simulacion.GRAVEDAD_POR_DEFECTO
    )

    private fun crearMenus() {
        menus = MenusJuego(
            escucha = this,
            tipoPelotaActual = { simulacion.tipoPelota() },
            tipoParedActual = { simulacion.tipoPared() },
            alCerrarMenus = ::cerrarMenusDeSeleccion,
            alSalir = { Platform.exit() }
        )
        gestorMenus = GestorMenus(simulacion, menus)
        detectorFin = DetectorFinDeVuelo(simulacion, gestorMenus, menus)
        constructorEstado = ConstructorEstadoPanel(simulacion, gestorMenus)
    }

    private fun crearEscena(): Scene {
        val root = StackPane(
            canvas, panelControl.raiz(), menus.menuPelotas,
            menus.menuParedes, menus.menuPausa, menus.menuFin
        )
        StackPane.setAlignment(panelControl.raiz(), Pos.TOP_RIGHT)
        StackPane.setMargin(panelControl.raiz(), Insets(12.0))
        for (menu in listOf(menus.menuPelotas, menus.menuParedes, menus.menuPausa, menus.menuFin)) {
            StackPane.setAlignment(menu, Pos.CENTER)
        }
        return Scene(root, ANCHO_VENTANA, ALTO_VENTANA)
    }

    private fun configurarVentana(stage: Stage, scene: Scene) {
        stage.title = "Simulador de Tiro Parabolico"
        stage.scene = scene
        stage.isResizable = false
        stage.show()
    }

    override fun alternarModoParedes() = conAccion { simulacion.alternarModoParedes() }

    override fun alternarMenuParedes() {
        gestorMenus.alternarMenuParedes()
        actualizarControles()
    }

    override fun limpiarParedes() = conAccion { simulacion.limpiarParedes() }

    override fun alternarModoSuperficies() = conAccion { simulacion.alternarModoSuperficies() }

    override fun limpiarSuperficiesLanzamiento() =
        conAccion { simulacion.limpiarSuperficiesLanzamiento() }

    override fun alternarMenuPelotas() {
        gestorMenus.alternarMenuPelotas()
        actualizarControles()
    }

    override fun alternarPausa() {
        if (gestorMenus.menuFinAbierto || !simulacion.estado().permitePausa()) return
        simulacion.alternarPausa()
        if (simulacion.pausado()) gestorMenus.cerrarTodosLosMenus()
        actualizarControles()
    }

    override fun reiniciar() = reiniciarJuego()

    override fun volverAlMenu() {
        cerrarMenusYDetector()
        simulacion.volverAlMenu()
        detectorFin.reiniciarSeguimiento()
        actualizarControles()
    }

    override fun seleccionarModo(nombre: String?) = conAccion { simulacion.seleccionarModo(nombre) }

    override fun aplicarGravedad(nombre: String?) = conAccion { simulacion.aplicarGravedad(nombre) }

    override fun seleccionarPelota(tipo: TipoPelota) = simulacion.seleccionarPelota(tipo)

    override fun seleccionarTipoPared(tipo: TipoPared) = simulacion.seleccionarTipoPared(tipo)

    /** Los botones del panel respetan el mismo bloqueo que el raton. */
    private fun conAccion(accion: () -> Unit) {
        if (gestorMenus.interaccionBloqueada || gestorMenus.enVuelo) return
        accion()
        actualizarControles()
    }

    private fun cerrarMenusDeSeleccion() {
        gestorMenus.cerrarTodosLosMenus()
        actualizarControles()
    }

    private fun configurarEventos(scene: Scene) {
        configurarRatones()
        configurarTeclado(scene)
    }

    private fun configurarRatones() {
        canvas.setOnMousePressed { e ->
            if (gestorMenus.interaccionBloqueada) return@setOnMousePressed
            simulacion.pulsar(Vector2D(e.x, e.y))
            actualizarControles()
        }
        canvas.setOnMouseDragged { e ->
            if (gestorMenus.interaccionBloqueada) return@setOnMouseDragged
            simulacion.arrastrar(Vector2D(e.x, e.y))
        }
        canvas.setOnMouseReleased { e ->
            if (gestorMenus.interaccionBloqueada) return@setOnMouseReleased
            simulacion.soltar(Vector2D(e.x, e.y))
            actualizarControles()
        }
    }

    private fun configurarTeclado(scene: Scene) {
        scene.setOnKeyPressed { e ->
            when (e.code) {
                KeyCode.P -> alternarModoParedes()
                KeyCode.B -> alternarMenuPelotas()
                KeyCode.W -> alternarMenuParedes()
                KeyCode.R -> if (gestorMenus.menuFinAbierto) reiniciarJuego()
                KeyCode.Q -> if (gestorMenus.menuFinAbierto) Platform.exit()
                KeyCode.ESCAPE -> cerrarMenusConEscape()
                else -> { }
            }
        }
    }

    private fun cerrarMenusConEscape() {
        if (!gestorMenus.cerrarConEscape()) cerrarPausaOHerramienta()
        actualizarControles()
    }

    private fun cerrarPausaOHerramienta() {
        when {
            simulacion.pausado() -> simulacion.alternarPausa()
            simulacion.modoParedes() -> simulacion.alternarModoParedes()
            else -> { }
        }
    }

    private fun reiniciarJuego() {
        cerrarMenusYDetector()
        simulacion.nuevaPartida()
        detectorFin.reiniciarSeguimiento()
        actualizarControles()
    }

    private fun cerrarMenusYDetector() {
        gestorMenus.cerrarTodosLosMenus()
        gestorMenus.cerrarMenuFin()
    }

    private fun actualizarControles() {
        gestorMenus.sincronizarVisibilidad()
        panelControl.refrescar(constructorEstado.crear())
    }

    private fun configurarBucleDeJuego() {
        BucleJuego { delta ->
            simulacion.actualizar(delta)
            detectorFin.detectar()
            presentador.pintar(simulacion)
            actualizarControles()
        }.iniciar()
    }
}

/**
 * Punto de entrada de la presentacion JavaFX. Vive en un [object] para que la
 * logica quede encapsulada y no haya funciones sueltas en el archivo.
 */
object LanzadorJuego {
    @JvmStatic
    fun main(args: Array<String>) {
        Application.launch(MainApp::class.java)
    }
}
