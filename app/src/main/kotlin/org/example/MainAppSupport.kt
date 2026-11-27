package org.example

import javafx.application.Platform
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import org.example.grafico.FabricaRenderer
import org.example.grafico.PintorApuntado
import org.example.grafico.PintorInformacion
import org.example.grafico.PintorMundo
import org.example.grafico.PresentadorJuego
import org.example.simulacion.FabricaSimulacion
import org.example.simulacion.Simulacion
import org.example.sprites.FabricaSpriteEstatica
import org.example.sprites.SpriteManager
import org.example.ui.ConfiguracionPanelSimulacion
import org.example.ui.ControlEventosUI
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

internal data class ConfiguracionInterfaz(
    val ancho: Double,
    val alto: Double
) {
    fun crearCanvas(): Canvas = Canvas(ancho, alto).also {
        it.graphicsContext2D.isImageSmoothing = true
    }

    fun crearEscena(root: Parent): Scene = Scene(root, ancho, alto)
}

internal interface NucleoUi {
    fun crearPresentador(): PresentadorJuego
    fun crearUiRefs(): UiRefs
    fun crearEscenaRoot(refs: UiRefs): Scene
    fun enlazarEventos(scene: Scene, refs: UiRefs)
}

internal interface FabricaComponentesUi {
    fun crearPresentador(canvas: Canvas): PresentadorJuego
    fun crearPanelControl(simulacion: Simulacion): PanelControl
    fun crearMenus(simulacion: Simulacion): MenusJuego
    fun crearEscenaRoot(canvas: Canvas, refs: UiRefs): Scene
    fun crearGestorMenus(simulacion: Simulacion, menus: MenusJuego): GestorMenus
    fun crearDetectorFin(simulacion: Simulacion, gestor: GestorMenus, menus: MenusJuego): DetectorFinDeVuelo
    fun crearConstructorEstado(simulacion: Simulacion, gestor: GestorMenus): ConstructorEstadoPanel
    fun crearControladorEventos(
        simulacion: Simulacion,
        gestor: GestorMenus,
        canvas: Canvas,
        scene: Scene
    )
}

internal class EnsambladorInterfaz(private val nucleo: NucleoUi) : NucleoUi by nucleo {
    constructor(
        app: ControlEventosUI,
        simulacion: Simulacion,
        configuracion: ConfiguracionInterfaz
    ) : this(
        NucleoUiPredeterminada(
            simulacion,
            configuracion,
            FabricaComponentesUiPredeterminada(app, configuracion)
        )
    )
}

internal class FabricaControlesUi(
    private val app: ControlEventosUI,
    private val configuracion: ConfiguracionInterfaz
) {
    fun crearPanelControl(simulacion: Simulacion): PanelControl = PanelControl(
        app,
        app,
        app,
        ConfiguracionPanelSimulacion(
            modos = simulacion.nombresModos(),
            modoInicial = simulacion.modo().nombre(),
            gravedades = Simulacion.gravedades(),
            gravedadInicial = Simulacion.GRAVEDAD_POR_DEFECTO
        )
    )

    fun crearMenus(simulacion: Simulacion): MenusJuego = MenusJuego(
        acciones = app,
        configuracion = app,
        tipoPelotaActual = { simulacion.tipoPelota() },
        tipoParedActual = { simulacion.tipoPared() },
        alCerrarMenus = { app.cerrarMenusDeSeleccion() },
        alSalir = { Platform.exit() }
    )
}

internal class FabricaEscenaPrincipal(
    private val configuracion: ConfiguracionInterfaz
) {
    fun crear(canvas: Canvas, refs: UiRefs): Scene {
        val nodos = (listOf(canvas, refs.panelControl.raiz()) + refs.menus.todos()).toTypedArray()
        val root = StackPane(*nodos)
        StackPane.setAlignment(refs.panelControl.raiz(), Pos.TOP_RIGHT)
        StackPane.setMargin(refs.panelControl.raiz(), Insets(12.0))
        for (menu in refs.menus.todos()) {
            StackPane.setAlignment(menu, Pos.CENTER)
        }
        return configuracion.crearEscena(root)
    }
}

internal class FabricaComponentesUiPredeterminada(
    private val app: ControlEventosUI,
    private val configuracion: ConfiguracionInterfaz
) : FabricaComponentesUi {
    private val fabricaControles = FabricaControlesUi(app, configuracion)
    private val fabricaEscena = FabricaEscenaPrincipal(configuracion)

    override fun crearPresentador(canvas: Canvas): PresentadorJuego {
        val renderer = FabricaRenderer.crear(canvas.graphicsContext2D, configuracion.ancho, configuracion.alto)
        val sprites = SpriteManager(FabricaSpriteEstatica())
        return PresentadorJuego(renderer, sprites, PintorMundo(renderer, sprites), PintorApuntado(renderer), PintorInformacion(renderer))
    }

    override fun crearPanelControl(simulacion: Simulacion): PanelControl =
        fabricaControles.crearPanelControl(simulacion)

    override fun crearMenus(simulacion: Simulacion): MenusJuego =
        fabricaControles.crearMenus(simulacion)

    override fun crearEscenaRoot(canvas: Canvas, refs: UiRefs): Scene =
        fabricaEscena.crear(canvas, refs)

    override fun crearGestorMenus(simulacion: Simulacion, menus: MenusJuego) =
        GestorMenus(simulacion, menus)

    override fun crearDetectorFin(
        simulacion: Simulacion,
        gestor: GestorMenus,
        menus: MenusJuego
    ) = DetectorFinDeVuelo(simulacion, gestor, menus)

    override fun crearConstructorEstado(simulacion: Simulacion, gestor: GestorMenus) =
        ConstructorEstadoPanel(simulacion, gestor)

    override fun crearControladorEventos(
        simulacion: Simulacion,
        gestor: GestorMenus,
        canvas: Canvas,
        scene: Scene
    ) {
        ControladorEventosUI(simulacion, gestor, app).enlazar(canvas, scene)
    }
}

internal class NucleoUiPredeterminada(
    private val simulacion: Simulacion,
    private val configuracion: ConfiguracionInterfaz,
    private val fabrica: FabricaComponentesUi
) : NucleoUi {
    private val canvas = configuracion.crearCanvas()

    override fun crearPresentador(): PresentadorJuego = fabrica.crearPresentador(canvas)

    override fun crearUiRefs(): UiRefs {
        val panelControl = fabrica.crearPanelControl(simulacion)
        val menus = fabrica.crearMenus(simulacion)
        val gestorMenus = fabrica.crearGestorMenus(simulacion, menus)
        val detectorFin = fabrica.crearDetectorFin(simulacion, gestorMenus, menus)
        val constructorEstado = fabrica.crearConstructorEstado(simulacion, gestorMenus)
        return UiRefs(crearPresentador(), panelControl, menus, gestorMenus, detectorFin, constructorEstado)
    }

    override fun crearEscenaRoot(refs: UiRefs): Scene {
        return fabrica.crearEscenaRoot(canvas, refs)
    }

    override fun enlazarEventos(scene: Scene, refs: UiRefs) =
        fabrica.crearControladorEventos(simulacion, refs.gestorMenus, canvas, scene)
}

internal class EjecutorAcciones(
    private val simulacion: Simulacion,
    private val gestorMenus: GestorMenus,
    private val detectorFin: DetectorFinDeVuelo,
    private val actualizar: () -> Unit
) {
    fun conBloqueo(accion: () -> Unit) {
        if (gestorMenus.interaccionBloqueada() || gestorMenus.enVuelo()) return
        accion()
        actualizar()
    }

    fun reiniciarPartida() {
        gestorMenus.cerrarTodosLosMenus()
        gestorMenus.cerrarMenuFin()
        simulacion.nuevaPartida()
        detectorFin.reiniciarSeguimiento()
        actualizar()
    }

    fun volverAlMenu() {
        gestorMenus.cerrarTodosLosMenus()
        gestorMenus.cerrarMenuFin()
        simulacion.volverAlMenu()
        detectorFin.reiniciarSeguimiento()
        actualizar()
    }

    fun alternarPausa() {
        if (gestorMenus.menuFinAbierto() || !simulacion.estado().permitePausa()) return
        simulacion.alternarPausa()
        if (simulacion.pausado()) gestorMenus.cerrarTodosLosMenus()
        actualizar()
    }

    fun alternarMenuPelotas() {
        gestorMenus.alternarMenuPelotas()
        actualizar()
    }

    fun alternarMenuParedes() {
        gestorMenus.alternarMenuParedes()
        actualizar()
    }
}

internal class SincronizadorControles(
    private val gestorMenus: GestorMenus,
    private val panelControl: PanelControl,
    private val constructorEstado: ConstructorEstadoPanel
) {
    fun actualizar() {
        gestorMenus.sincronizarVisibilidad()
        panelControl.refrescar(constructorEstado.crear())
    }
}

internal class CicloSimulacion(
    private val simulacion: Simulacion,
    private val detectorFin: DetectorFinDeVuelo,
    private val presentador: PresentadorJuego,
    private val sincronizador: SincronizadorControles
) {
    fun alCuadro(delta: Double) {
        simulacion.actualizar(delta)
        detectorFin.detectar()
        presentador.pintar(simulacion)
        sincronizador.actualizar()
    }
}

internal class ArquitecturaAplicacion(
    private val escucha: ControlEventosUI,
    ancho: Double,
    alto: Double
) {
    private val simulacion = FabricaSimulacion.crear(ancho, alto)
    private val configuracion = ConfiguracionInterfaz(ancho, alto)
    private val interfaz = EnsambladorInterfaz(escucha, simulacion, configuracion)
    private lateinit var refs: UiRefs
    private lateinit var acciones: EjecutorAcciones
    private lateinit var sincronizador: SincronizadorControles
    private lateinit var ciclo: CicloSimulacion

    fun iniciar(stage: Stage) {
        refs = interfaz.crearUiRefs()
        inicializarColaboradores()
        val scene = interfaz.crearEscenaRoot(refs)
        interfaz.enlazarEventos(scene, refs)
        configurarEscena(stage, scene)
        sincronizador.actualizar()
        BucleJuego(ciclo::alCuadro).iniciar()
    }

    private fun inicializarColaboradores() {
        acciones = EjecutorAcciones(simulacion, refs.gestorMenus, refs.detectorFin, ::actualizarControles)
        sincronizador = SincronizadorControles(refs.gestorMenus, refs.panelControl, refs.constructorEstado)
        ciclo = CicloSimulacion(simulacion, refs.detectorFin, refs.presentador, sincronizador)
    }

    private fun configurarEscena(stage: Stage, scene: Scene) {
        stage.title = "Simulador de Tiro Parabolico"
        stage.scene = scene
        stage.isResizable = false
        stage.show()
    }

    fun actualizarControles() = sincronizador.actualizar()

    fun cerrarMenusDeSeleccion() {
        refs.gestorMenus.cerrarTodosLosMenus()
        actualizarControles()
    }

    fun alternarModoParedes() = acciones.conBloqueo { simulacion.alternarModoParedes() }
    fun alternarMenuParedes() = acciones.alternarMenuParedes()
    fun limpiarParedes() = acciones.conBloqueo { simulacion.limpiarParedes() }
    fun alternarModoSuperficies() = acciones.conBloqueo { simulacion.alternarModoSuperficies() }
    fun limpiarSuperficiesLanzamiento() = acciones.conBloqueo { simulacion.limpiarSuperficiesLanzamiento() }
    fun alternarMenuPelotas() = acciones.alternarMenuPelotas()
    fun alternarPausa() = acciones.alternarPausa()
    fun reiniciar() = acciones.reiniciarPartida()
    fun volverAlMenu() = acciones.volverAlMenu()
    fun seleccionarModo(nombre: String?) = acciones.conBloqueo { simulacion.seleccionarModo(nombre) }
    fun aplicarGravedad(nombre: String?) = acciones.conBloqueo { simulacion.aplicarGravedad(nombre) }
    fun seleccionarPelota(tipo: org.example.modelo.TipoPelota) = simulacion.seleccionarPelota(tipo)
    fun seleccionarTipoPared(tipo: org.example.modelo.TipoPared) = simulacion.seleccionarTipoPared(tipo)
}
