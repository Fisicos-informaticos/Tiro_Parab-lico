package org.example

import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.application.Platform
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.input.KeyCode
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import org.example.grafico.CanvasRenderer
import org.example.grafico.PresentadorJuego
import org.example.grafico.VectorRenderer
import org.example.modelo.TipoPelota
import org.example.modelo.TipoPared
import org.example.modelo.Vector2D
import org.example.simulacion.EstadoSimulacion
import org.example.simulacion.FabricaSimulacion
import org.example.simulacion.Simulacion
import org.example.sprites.FabricaSpriteEstatica
import org.example.sprites.SpriteManager
import org.example.ui.EstadoPanel
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
    private lateinit var menuPelotas: VBox
    private lateinit var menuParedes: VBox
    private lateinit var menuPausa: VBox
    private lateinit var menuFin: VBox

    private var menuPelotasAbierto = false
    private var menuParedesAbierto = false
    private var menuFinAbierto = false
    private var estadoPrevio: EstadoSimulacion? = null

    /** Con un menu abierto el mundo no responde al raton. */
    private val interaccionBloqueada: Boolean
        get() = simulacion.pausado() || menuPelotasAbierto || menuParedesAbierto || menuFinAbierto

    private val enVuelo: Boolean
        get() = simulacion.estado().estaEnVuelo()

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
        menuPelotas = menus.menuPelotas
        menuParedes = menus.menuParedes
        menuPausa = menus.menuPausa
        menuFin = menus.menuFin
    }

    private fun crearEscena(): Scene {
        val root = StackPane(canvas, panelControl.raiz(), menuPelotas, menuParedes, menuPausa, menuFin)
        StackPane.setAlignment(panelControl.raiz(), Pos.TOP_RIGHT)
        StackPane.setMargin(panelControl.raiz(), Insets(12.0))
        for (menu in listOf(menuPelotas, menuParedes, menuPausa, menuFin)) {
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

    // ---- Acciones que dispara el panel de control -------------------------------------

    override fun alternarModoParedes() = conAccion { simulacion.alternarModoParedes() }

    override fun alternarMenuParedes() {
        if (menuParedesAbierto) {
            cerrarMenuParedes()
        } else {
            abrirMenuParedes()
        }
        actualizarControles()
    }

    override fun limpiarParedes() = conAccion { simulacion.limpiarParedes() }

    override fun alternarModoSuperficies() = conAccion { simulacion.alternarModoSuperficies() }

    override fun limpiarSuperficiesLanzamiento() =
        conAccion { simulacion.limpiarSuperficiesLanzamiento() }

    override fun alternarMenuPelotas() {
        if (menuPelotasAbierto) {
            cerrarMenuPelotas()
        } else {
            abrirMenuPelotas()
        }
        actualizarControles()
    }

    override fun alternarPausa() {
        if (menuFinAbierto || !simulacion.estado().permitePausa()) return

        simulacion.alternarPausa()
        if (simulacion.pausado()) cerrarTodosLosMenus()
        actualizarControles()
    }

    override fun reiniciar() = reiniciarJuego()

    override fun volverAlMenu() {
        cerrarTodosLosMenus()
        cerrarMenuFin()
        simulacion.volverAlMenu()
        estadoPrevio = simulacion.estado()
        actualizarControles()
    }

    override fun seleccionarModo(nombre: String?) = conAccion { simulacion.seleccionarModo(nombre) }

    override fun aplicarGravedad(nombre: String?) = conAccion { simulacion.aplicarGravedad(nombre) }

    override fun seleccionarPelota(tipo: TipoPelota) = simulacion.seleccionarPelota(tipo)

    override fun seleccionarTipoPared(tipo: TipoPared) = simulacion.seleccionarTipoPared(tipo)

    /** Los botones del panel respetan el mismo bloqueo que el raton. */
    private fun conAccion(accion: () -> Unit) {
        if (interaccionBloqueada || enVuelo) return
        accion()
        actualizarControles()
    }

    private fun cerrarMenusDeSeleccion() {
        cerrarTodosLosMenus()
        actualizarControles()
    }

    // ---- Eventos ---------------------------------------------------------------------

    private fun configurarEventos(scene: Scene) {
        canvas.setOnMousePressed { e ->
            if (interaccionBloqueada) return@setOnMousePressed
            simulacion.pulsar(Vector2D(e.x, e.y))
            actualizarControles()
        }
        canvas.setOnMouseDragged { e ->
            if (interaccionBloqueada) return@setOnMouseDragged
            simulacion.arrastrar(Vector2D(e.x, e.y))
        }
        canvas.setOnMouseReleased { e ->
            if (interaccionBloqueada) return@setOnMouseReleased
            simulacion.soltar(Vector2D(e.x, e.y))
            actualizarControles()
        }

        scene.setOnKeyPressed { e ->
            when (e.code) {
                KeyCode.P -> alternarModoParedes()
                KeyCode.B -> alternarMenuPelotas()
                KeyCode.W -> alternarMenuParedes()
                KeyCode.R -> if (menuFinAbierto) reiniciarJuego()
                KeyCode.Q -> if (menuFinAbierto) Platform.exit()
                KeyCode.ESCAPE -> cerrarMenusConEscape()
                else -> { }
            }
        }
    }

    private fun cerrarMenusConEscape() {
        when {
            menuPelotasAbierto -> cerrarMenuPelotas()
            menuParedesAbierto -> cerrarMenuParedes()
            menuFinAbierto -> cerrarMenuFin()
            simulacion.pausado() -> simulacion.alternarPausa()
            simulacion.modoParedes() -> simulacion.alternarModoParedes()
            else -> { }
        }
        actualizarControles()
    }

    // ---- Estado de los menus ---------------------------------------------------------

    private fun abrirMenuPelotas() {
        if (enVuelo || menuFinAbierto) return

        simulacion.abrirEdicion()
        cerrarMenuParedes()
        menuPelotasAbierto = true
        menus.marcarPelotaActual()
        menuPelotas.isVisible = true
    }

    private fun abrirMenuParedes() {
        if (enVuelo || menuFinAbierto) return

        simulacion.abrirEdicion()
        cerrarMenuPelotas()
        menuParedesAbierto = true
        menus.marcarParedActual()
        menuParedes.isVisible = true
    }

    private fun cerrarMenuPelotas() {
        menuPelotasAbierto = false
        menuPelotas.isVisible = false
    }

    private fun cerrarMenuParedes() {
        menuParedesAbierto = false
        menuParedes.isVisible = false
    }

    private fun cerrarMenuFin() {
        menuFinAbierto = false
        menuFin.isVisible = false
    }

    private fun cerrarTodosLosMenus() {
        cerrarMenuPelotas()
        cerrarMenuParedes()
    }

    private fun reiniciarJuego() {
        cerrarTodosLosMenus()
        cerrarMenuFin()
        simulacion.nuevaPartida()
        estadoPrevio = simulacion.estado()
        actualizarControles()
    }

    private fun actualizarControles() {
        menuPelotas.isVisible = menuPelotasAbierto
        menuParedes.isVisible = menuParedesAbierto
        menuPausa.isVisible = simulacion.pausado()
        menuFin.isVisible = menuFinAbierto && !simulacion.pausado()
        panelControl.refrescar(estadoPanel())
    }

    /** Resumen de solo lectura con el que el panel decide textos y bloqueos. */
    private fun estadoPanel(): EstadoPanel = EstadoPanel(
        pausado = simulacion.pausado(),
        enVuelo = enVuelo,
        enPantallaInicio = simulacion.estado().esPantallaInicio(),
        modoParedes = simulacion.modoParedes(),
        modoSuperficies = simulacion.modoSuperficies(),
        menuParedesAbierto = menuParedesAbierto,
        menuPelotasAbierto = menuPelotasAbierto,
        menuFinAbierto = menuFinAbierto,
        nombreTipoPared = simulacion.tipoPared().nombre(),
        nombrePelota = simulacion.tipoPelota().nombre(),
        factorGravedad = simulacion.factorGravedad(),
        nombreModo = simulacion.modo().nombre(),
        ayudaModo = simulacion.modo().ayuda(),
        hayParedes = simulacion.paredes().isNotEmpty(),
        haySuperficies = simulacion.superficiesLanzamientoLista().isNotEmpty()
    )

    // ---- Bucle -----------------------------------------------------------------------

    private fun configurarBucleDeJuego() {
        object : AnimationTimer() {
            private var ultimoTiempo = 0L

            override fun handle(now: Long) {
                if (ultimoTiempo == 0L) {
                    ultimoTiempo = now
                    return
                }

                val deltaSegundos = (now - ultimoTiempo) / 1_000_000_000.0
                ultimoTiempo = now

                simulacion.actualizar(deltaSegundos.coerceAtMost(0.05))
                detectarFinDeVuelo()
                presentador.pintar(simulacion)
                actualizarControles()
            }
        }.start()
    }

    /** El menu final se abre justo al terminar el tiro, no en cada fotograma. */
    private fun detectarFinDeVuelo() {
        val estado = simulacion.estado()
        if (estado === estadoPrevio) return

        estadoPrevio = estado
        menuFinAbierto = estado.esEnSuelo()
        if (menuFinAbierto) {
            menus.mostrarResumenFin(
                "Distancia: ${"%.2f".format(simulacion.distanciaRecorrida())} px · " +
                    "Tiempo: ${"%.2f".format(simulacion.tiempoVuelo())} s"
            )
        }
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
