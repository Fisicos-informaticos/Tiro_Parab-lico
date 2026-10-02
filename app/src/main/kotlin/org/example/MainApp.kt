package org.example

import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToggleGroup
import javafx.scene.control.Tooltip
import javafx.scene.input.KeyCode
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.Node
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.stage.Stage
import org.example.grafico.CanvasRenderer
import org.example.grafico.DibujoPelota
import org.example.grafico.PresentadorJuego
import org.example.modelo.TipoPelota
import org.example.modelo.TipoPared
import org.example.modelo.Vector2D
import org.example.simulacion.EstadoSimulacion
import org.example.simulacion.Simulacion

/**
 * Capa JavaFX: construye la ventana, los menus y reenvia los eventos al nucleo.
 * No decide nada del juego; de eso se encarga [Simulacion].
 */
class MainApp : Application() {

    companion object {
        const val ANCHO_VENTANA = 1000.0
        const val ALTO_VENTANA = 650.0

        private const val COLUMNAS_MENU = 2
        private const val ANCHO_BOTON_MENU = 300.0
        private const val ANCHO_BOTON_PARED = 340.0
        private const val ALTO_PREVIA = 44.0
        private const val ESTILO_BOTON =
            "-fx-background-color: #2b3138; -fx-text-fill: white; " +
                "-fx-border-color: #4a515a; -fx-border-radius: 8; -fx-background-radius: 8; " +
                "-fx-padding: 6 8 6 8;"
        private const val ESTILO_BOTON_ACTIVO =
            "-fx-background-color: #3d5a80; -fx-text-fill: white; " +
                "-fx-border-color: #d4e157; -fx-border-width: 2; -fx-border-radius: 8; " +
                "-fx-background-radius: 8; -fx-padding: 5 7 5 7;"
        private const val ESTILO_MENU =
            "-fx-background-color: #20252d; -fx-background-radius: 12; " +
                "-fx-border-color: #6c757d; -fx-border-radius: 12;"
        private const val ESTILO_MENU_FIN =
            "-fx-background-color: #20252d; -fx-background-radius: 12; " +
                "-fx-border-color: #d4e157; -fx-border-radius: 12;"
        private const val ESTILO_TITULO =
            "-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;"
        private const val ESTILO_NOMBRE = "-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;"
        private const val ESTILO_DETALLE = "-fx-text-fill: #c8ced6; -fx-font-size: 11px;"
        private const val ESTILO_SUBTITULO = "-fx-text-fill: #c8ced6; -fx-font-size: 12px;"
        private const val ESTILO_AYUDA = "-fx-text-fill: #9aa4b0; -fx-font-size: 12px;"
        private const val ANCHO_BOTON_ACCION = 200.0
    }

    private val simulacion = Simulacion(ANCHO_VENTANA, ALTO_VENTANA)

    private lateinit var canvas: Canvas
    private lateinit var presentador: PresentadorJuego
    private lateinit var botonPared: Button
    private lateinit var botonTipoPared: Button
    private lateinit var botonLimpiarParedes: Button
    private lateinit var botonPelotas: Button
    private lateinit var botonPausa: Button
    private lateinit var etiquetaPelota: Label
    private lateinit var etiquetaGravedad: Label
    private lateinit var comboGravedad: ComboBox<String>
    private lateinit var menuPelotas: VBox
    private lateinit var menuParedes: VBox
    private lateinit var menuPausa: VBox
    private lateinit var menuFin: VBox
    private lateinit var etiquetaResumenFin: Label

    private val grupoPelotas = ToggleGroup()
    private val grupoParedes = ToggleGroup()
    private val botonesPelota = mutableMapOf<TipoPelota, ToggleButton>()
    private val botonesPared = mutableMapOf<TipoPared, ToggleButton>()

    private var menuPelotasAbierto = false
    private var menuParedesAbierto = false
    private var menuFinAbierto = false
    private var estadoPrevio: EstadoSimulacion? = null

    /** Con un menu abierto el mundo no responde al raton. */
    private val interaccionBloqueada: Boolean
        get() = simulacion.pausado || menuPelotasAbierto || menuParedesAbierto || menuFinAbierto

    private val enVuelo: Boolean
        get() = simulacion.estado is EstadoSimulacion.EnVuelo

    override fun start(stage: Stage) {
        canvas = Canvas(ANCHO_VENTANA, ALTO_VENTANA)
        canvas.graphicsContext2D.isImageSmoothing = true

        presentador = PresentadorJuego(CanvasRenderer(canvas.graphicsContext2D, ANCHO_VENTANA, ALTO_VENTANA))

        val controles = crearControles()
        controles.maxWidth = Region.USE_PREF_SIZE
        controles.maxHeight = Region.USE_PREF_SIZE
        controles.isPickOnBounds = false

        crearMenuPelotas()
        crearMenuParedes()
        crearMenuPausa()
        crearMenuFin()

        val root = StackPane(canvas, controles, menuPelotas, menuParedes, menuPausa, menuFin)
        StackPane.setAlignment(controles, Pos.TOP_RIGHT)
        StackPane.setMargin(controles, Insets(10.0))
        for (menu in listOf(menuPelotas, menuParedes, menuPausa, menuFin)) {
            StackPane.setAlignment(menu, Pos.CENTER)
        }

        val scene = Scene(root, ANCHO_VENTANA, ALTO_VENTANA)
        configurarEventos(scene)
        actualizarControles()
        configurarBucleDeJuego()

        stage.title = "Simulador de Tiro Parabolico"
        stage.scene = scene
        stage.isResizable = false
        stage.show()
    }

    // ---- Panel de controles -----------------------------------------------------------

    private fun crearControles(): VBox {
        botonPared = Button("Anadir pared")
        botonTipoPared = Button("Pared: ${simulacion.tipoPared.nombre}")
        botonLimpiarParedes = Button("Limpiar paredes")
        botonPelotas = Button("Pelotas")
        botonPausa = Button("Pausa")
        etiquetaPelota = Label("Pelota: ${simulacion.tipoPelota.nombre}")
        etiquetaGravedad = Label("Gravedad: ${"%.2f".format(simulacion.factorGravedad)} g")
        comboGravedad = ComboBox<String>()
        comboGravedad.setItems(FXCollections.observableArrayList(Simulacion.GRAVEDADES.keys))
        comboGravedad.value = Simulacion.GRAVEDAD_POR_DEFECTO
        comboGravedad.tooltip = Tooltip("Elige la gravedad con la que se simula el tiro")

        botonPared.setOnAction { conAccion { simulacion.alternarModoParedes() } }
        botonTipoPared.setOnAction { alternarMenuParedes() }
        botonLimpiarParedes.setOnAction { conAccion { simulacion.limpiarParedes() } }
        botonPelotas.setOnAction { alternarMenuPelotas() }
        botonPausa.setOnAction { alternarPausa() }
        comboGravedad.setOnAction { conAccion { simulacion.aplicarGravedad(comboGravedad.value) } }

        return VBox(
            8.0,
            botonPared, botonTipoPared, botonLimpiarParedes, botonPelotas, botonPausa,
            etiquetaPelota, etiquetaGravedad, comboGravedad, Label("Arrastra en el mundo para dibujar")
        )
    }

    /** Los botones del panel respetan el mismo bloqueo que el raton. */
    private fun conAccion(accion: () -> Unit) {
        if (interaccionBloqueada || enVuelo) return
        accion()
        actualizarControles()
    }

    // ---- Menus -----------------------------------------------------------------------

    private fun crearMenuPelotas() {
        menuPelotas = construirMenu(
            titulo = "ELIGE TU PELOTA",
            subtitulo = "Cada tipo tiene su masa, rebote, rozamiento y forma de colision",
            ayuda = "Pulsa B o ESC para cerrar",
            contenido = rejillaTipos(TipoPelota.entries.toList()) { crearBotonPelota(it) }
        )

        grupoPelotas.selectedToggleProperty().addListener { _, _, _ -> marcarSeleccion(botonesPelota) }
        grupoPelotas.selectToggle(botonesPelota[simulacion.tipoPelota])
    }

    private fun crearMenuParedes() {
        menuParedes = construirMenu(
            titulo = "ELIGE EL TIPO DE PARED",
            subtitulo = "Cada material aguanta distinto y solo lo rompen ciertas pelotas",
            ayuda = "Pulsa W o ESC para cerrar",
            contenido = rejillaTipos(TipoPared.entries.toList()) { crearBotonPared(it) }
        )

        grupoParedes.selectedToggleProperty().addListener { _, _, _ -> marcarSeleccion(botonesPared) }
        grupoParedes.selectToggle(botonesPared[simulacion.tipoPared])
    }

    private fun crearMenuPausa() {
        val titulo = Label("PAUSA").also { it.style = ESTILO_TITULO }
        val continuar = Button("Continuar").also { it.prefWidth = ANCHO_BOTON_ACCION }
        val reiniciar = Button("Reiniciar tiro").also { it.prefWidth = ANCHO_BOTON_ACCION }
        val salir = Button("Salir del simulador").also { it.prefWidth = ANCHO_BOTON_ACCION }
        continuar.setOnAction { alternarPausa() }
        reiniciar.setOnAction { reiniciarJuego() }
        salir.setOnAction { Platform.exit() }

        menuPausa = VBox(14.0, titulo, continuar, reiniciar, salir)
        decorarMenu(menuPausa, ESTILO_MENU)
    }

    private fun crearMenuFin() {
        val titulo = Label("LA PELOTA HA CAIDO").also { it.style = ESTILO_TITULO }
        etiquetaResumenFin = Label().also {
            it.alignment = Pos.CENTER
            it.style = ESTILO_SUBTITULO
        }
        val reiniciar = Button("Reiniciar").also { it.prefWidth = ANCHO_BOTON_ACCION }
        val salir = Button("Salir del simulador").also { it.prefWidth = ANCHO_BOTON_ACCION }
        reiniciar.setOnAction { reiniciarJuego() }
        salir.setOnAction { Platform.exit() }

        menuFin = VBox(14.0, titulo, etiquetaResumenFin, reiniciar, salir)
        decorarMenu(menuFin, ESTILO_MENU_FIN)
    }

    private fun construirMenu(
        titulo: String,
        subtitulo: String,
        ayuda: String,
        contenido: Node
    ): VBox {
        val etiquetaTitulo = Label(titulo).also { it.style = ESTILO_TITULO }
        val etiquetaSubtitulo = Label(subtitulo).also {
            it.alignment = Pos.CENTER
            it.style = ESTILO_SUBTITULO
        }
        val etiquetaAyuda = Label(ayuda).also {
            it.alignment = Pos.CENTER
            it.style = ESTILO_AYUDA
        }

        val menu = VBox(14.0, etiquetaTitulo, etiquetaSubtitulo, contenido, etiquetaAyuda)
        decorarMenu(menu, ESTILO_MENU)
        return menu
    }

    private fun decorarMenu(menu: VBox, estilo: String) {
        menu.alignment = Pos.CENTER
        menu.padding = Insets(20.0)
        menu.maxWidth = Region.USE_PREF_SIZE
        menu.maxHeight = Region.USE_PREF_SIZE
        menu.isPickOnBounds = true
        menu.style = estilo
        menu.isVisible = false
    }

    private fun crearBotonPelota(tipo: TipoPelota): ToggleButton {
        val forma = tipo.crearForma()
        val escala = (ALTO_PREVIA * 0.42) / forma.radioEnvoltura
        val previa = previaCanvas { DibujoPelota.dibujar(it, tipo, forma, 0.0, Vector2D(60.0, ALTO_PREVIA / 2.0), escala) }

        val nombre = Label(tipo.nombre).also { it.style = ESTILO_NOMBRE }
        val datos = Label("masa ${"%.2f".format(tipo.masa)} kg · rebote ${"%.2f".format(tipo.coeficienteRestitucion)}")
            .also { it.style = ESTILO_DETALLE }
        val rozamiento = Label("rozamiento ${"%.1f".format(tipo.coeficienteFriccion)} · ${forma.tipo.etiqueta}")
            .also { it.style = ESTILO_AYUDA }

        val textos = VBox(2.0, nombre, datos, rozamiento).also {
            it.isMouseTransparent = true
            it.alignment = Pos.CENTER_LEFT
        }

        val boton = botonMenu(previa, textos, ANCHO_BOTON_MENU) { simulacion.seleccionarPelota(tipo) }
        boton.tooltip = Tooltip(tipo.descripcion)
        botonesPelota[tipo] = boton
        return boton
    }

    private fun crearBotonPared(tipo: TipoPared): ToggleButton {
        val previa = previaCanvas { gc ->
            val yCentro = ALTO_PREVIA / 2.0
            gc.stroke = colorDe(tipo.colorBorde)
            gc.lineWidth = tipo.grosor + 2.0
            gc.strokeLine(4.0, yCentro, 116.0, yCentro)
            gc.stroke = colorDe(tipo.color)
            gc.lineWidth = tipo.grosor
            gc.strokeLine(4.0, yCentro, 116.0, yCentro)
            if (tipo.indestructible) {
                gc.stroke = Color.web("#f1c40f")
                gc.lineWidth = 2.0
                gc.strokeLine(60.0, 6.0, 60.0, ALTO_PREVIA - 6.0)
            }
        }

        val nombre = Label(tipo.nombre).also { it.style = ESTILO_NOMBRE }
        val datos = Label("grosor ${tipo.grosor.toInt()} px · aguanta ${textoResistencia(tipo)}")
            .also { it.style = ESTILO_DETALLE }
        val rompe = Label(tipo.textoRompible).also {
            it.style = ESTILO_AYUDA
            it.maxWidth = ANCHO_BOTON_PARED
            it.isWrapText = true
        }

        val textos = VBox(2.0, nombre, datos, rompe).also {
            it.isMouseTransparent = true
            it.alignment = Pos.CENTER_LEFT
        }

        val boton = botonMenu(previa, textos, ANCHO_BOTON_PARED) { simulacion.seleccionarTipoPared(tipo) }
        boton.tooltip = Tooltip(tipo.descripcion)
        botonesPared[tipo] = boton
        return boton
    }

    private fun colorDe(valor: Int): Color =
        Color.rgb((valor shr 16) and 0xFF, (valor shr 8) and 0xFF, valor and 0xFF)

    private fun textoResistencia(tipo: TipoPared): String =
        if (tipo.indestructible) "todo" else "${tipo.resistencia.toInt()} de energia"

    private fun previaCanvas(dibujar: (javafx.scene.canvas.GraphicsContext) -> Unit): Canvas {
        val canvas = Canvas(120.0, ALTO_PREVIA)
        canvas.isMouseTransparent = true
        dibujar(canvas.graphicsContext2D)
        return canvas
    }

    private fun botonMenu(
        previa: Canvas,
        textos: VBox,
        ancho: Double,
        accion: () -> Unit
    ): ToggleButton {
        val boton = ToggleButton()
        boton.graphic = HBox(10.0, previa, textos)
        boton.alignment = Pos.CENTER_LEFT
        boton.prefWidth = ancho
        boton.style = ESTILO_BOTON
        boton.setOnAction {
            accion()
            cerrarTodosLosMenus()
            actualizarControles()
        }
        return boton
    }

    private fun <T> rejillaTipos(tipos: List<T>, crearBoton: (T) -> ToggleButton): GridPane {
        val rejilla = GridPane()
        rejilla.hgap = 10.0
        rejilla.vgap = 10.0
        tipos.forEachIndexed { indice, tipo ->
            rejilla.add(crearBoton(tipo), indice % COLUMNAS_MENU, indice / COLUMNAS_MENU)
        }
        return rejilla
    }

    private fun marcarSeleccion(botones: Map<*, ToggleButton>) {
        for (boton in botones.values) {
            boton.style = if (boton.isSelected) ESTILO_BOTON_ACTIVO else ESTILO_BOTON
        }
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
                KeyCode.P -> conAccion { simulacion.alternarModoParedes() }
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
            simulacion.pausado -> simulacion.alternarPausa()
            simulacion.modoParedes -> simulacion.alternarModoParedes()
            else -> { }
        }
        actualizarControles()
    }

    /** La pausa vive en el nucleo, aqui solo se refleja con el menu y el boton. */
    private fun alternarPausa() {
        if (menuFinAbierto || simulacion.estado is EstadoSimulacion.PantallaInicio) return

        simulacion.alternarPausa()
        if (simulacion.pausado) cerrarTodosLosMenus()
        actualizarControles()
    }

    // ---- Estado de los menus ---------------------------------------------------------

    private fun alternarMenuPelotas() {
        if (menuPelotasAbierto) {
            cerrarMenuPelotas()
        } else {
            abrirMenuPelotas()
        }
        actualizarControles()
    }

    private fun abrirMenuPelotas() {
        if (enVuelo || menuFinAbierto) return

        simulacion.abrirEdicion()
        cerrarMenuParedes()
        menuPelotasAbierto = true
        grupoPelotas.selectToggle(botonesPelota[simulacion.tipoPelota])
        menuPelotas.isVisible = true
    }

    private fun cerrarMenuPelotas() {
        menuPelotasAbierto = false
        menuPelotas.isVisible = false
    }

    private fun alternarMenuParedes() {
        if (menuParedesAbierto) {
            cerrarMenuParedes()
        } else {
            abrirMenuParedes()
        }
        actualizarControles()
    }

    private fun abrirMenuParedes() {
        if (enVuelo || menuFinAbierto) return

        simulacion.abrirEdicion()
        cerrarMenuPelotas()
        menuParedesAbierto = true
        grupoParedes.selectToggle(botonesPared[simulacion.tipoPared])
        menuParedes.isVisible = true
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
        estadoPrevio = simulacion.estado
        actualizarControles()
    }

    private fun actualizarControles() {
        botonPared.text = if (simulacion.modoParedes) "Cancelar pared" else "Anadir pared"
        botonPared.isDisable = interaccionBloqueada || enVuelo
        botonTipoPared.text =
            if (menuParedesAbierto) "Cerrar paredes" else "Pared: ${simulacion.tipoPared.nombre}"
        botonTipoPared.isDisable = interaccionBloqueada || enVuelo
        botonLimpiarParedes.isDisable = interaccionBloqueada || enVuelo || simulacion.paredes.isEmpty()
        botonPelotas.text = if (menuPelotasAbierto) "Cerrar pelotas" else "Pelotas"
        botonPelotas.isDisable = interaccionBloqueada || enVuelo
        botonPausa.text = if (simulacion.pausado) "Continuar" else "Pausa"
        botonPausa.isDisable = simulacion.estado is EstadoSimulacion.PantallaInicio
        etiquetaPelota.text = "Pelota: ${simulacion.tipoPelota.nombre}"
        etiquetaGravedad.text = "Gravedad: ${"%.2f".format(simulacion.factorGravedad)} g"
        comboGravedad.isDisable = interaccionBloqueada
        menuPelotas.isVisible = menuPelotasAbierto
        menuParedes.isVisible = menuParedesAbierto
        menuPausa.isVisible = simulacion.pausado
        menuFin.isVisible = menuFinAbierto && !simulacion.pausado
    }

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
        val estado = simulacion.estado
        if (estado === estadoPrevio) return

        estadoPrevio = estado
        menuFinAbierto = estado is EstadoSimulacion.EnSuelo
        if (menuFinAbierto) {
            etiquetaResumenFin.text =
                "Distancia: ${"%.2f".format(simulacion.distanciaRecorrida)} px · " +
                    "Tiempo: ${"%.2f".format(simulacion.tiempoVuelo)} s"
        }
    }
}

fun main() {
    Application.launch(MainApp::class.java)
}