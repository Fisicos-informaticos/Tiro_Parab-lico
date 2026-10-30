package org.example.ui

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.canvas.Canvas
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToggleGroup
import javafx.scene.control.Tooltip
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import org.example.grafico.DibujoPelota
import org.example.modelo.TipoPelota
import org.example.modelo.TipoPared
import org.example.modelo.Vector2D

/**
 * Construye y mantiene los menus de seleccion de material y de pausa. La
 * seleccion viaja al juego solo por [UIEventListener], de modo que esta clase
 * no conoce la simulacion concreta.
 */
class MenusJuego(
    private val escucha: UIEventListener,
    private val tipoPelotaActual: () -> TipoPelota,
    private val tipoParedActual: () -> TipoPared,
    private val alCerrarMenus: () -> Unit,
    private val alSalir: () -> Unit
) {

    private val grupoPelotas = ToggleGroup()
    private val grupoParedes = ToggleGroup()
    private val botonesPelota = mutableMapOf<TipoPelota, ToggleButton>()
    private val botonesPared = mutableMapOf<TipoPared, ToggleButton>()
    private val etiquetaResumenFin = Label().also {
        it.alignment = Pos.CENTER
        it.style = EstilosUI.SUBTITULO
    }

    val menuPelotas: VBox = crearMenuPelotas()
    val menuParedes: VBox = crearMenuParedes()
    val menuPausa: VBox = crearMenuPausa()
    val menuFin: VBox = crearMenuFin()

    init {
        grupoPelotas.selectedToggleProperty().addListener { _, _, _ -> marcarSeleccion(botonesPelota) }
        grupoParedes.selectedToggleProperty().addListener { _, _, _ -> marcarSeleccion(botonesPared) }
    }

    fun marcarPelotaActual() {
        grupoPelotas.selectToggle(botonesPelota[tipoPelotaActual()])
    }

    fun marcarParedActual() {
        grupoParedes.selectToggle(botonesPared[tipoParedActual()])
    }

    fun mostrarResumenFin(texto: String) {
        etiquetaResumenFin.text = texto
    }

    private fun crearMenuPelotas(): VBox {
        val menu = construirMenu(
            titulo = "ELIGE TU PELOTA",
            subtitulo = "Cada tipo tiene su masa, rebote, rozamiento y forma de colision",
            ayuda = "Pulsa B o ESC para cerrar",
            contenido = rejillaTipos(TipoPelota.entries.toList()) { crearBotonPelota(it) }
        )
        grupoPelotas.selectToggle(botonesPelota[tipoPelotaActual()])
        marcarSeleccion(botonesPelota)
        return menu
    }

    private fun crearMenuParedes(): VBox {
        val menu = construirMenu(
            titulo = "ELIGE EL TIPO DE PARED",
            subtitulo = "Cada material aguanta distinto y solo lo rompen ciertas pelotas",
            ayuda = "Pulsa W o ESC para cerrar",
            contenido = rejillaTipos(TipoPared.entries.toList()) { crearBotonPared(it) }
        )
        grupoParedes.selectToggle(botonesPared[tipoParedActual()])
        marcarSeleccion(botonesPared)
        return menu
    }

    private fun crearMenuPausa(): VBox {
        val titulo = Label("PAUSA").also { it.style = EstilosUI.TITULO }
        val continuar = Button("Continuar").also { it.prefWidth = ANCHO_BOTON_ACCION }
        val reiniciar = Button("Reiniciar tiro").also { it.prefWidth = ANCHO_BOTON_ACCION }
        val salir = Button("Salir del simulador").also { it.prefWidth = ANCHO_BOTON_ACCION }
        continuar.setOnAction { escucha.alternarPausa() }
        reiniciar.setOnAction { escucha.reiniciar() }
        salir.setOnAction { alSalir() }

        return VBox(14.0, titulo, continuar, reiniciar, salir).also { decorarMenu(it, EstilosUI.MENU) }
    }

    private fun crearMenuFin(): VBox {
        val titulo = Label("LA PELOTA HA CAIDO").also { it.style = EstilosUI.TITULO }
        val reiniciar = Button("Reiniciar").also { it.prefWidth = ANCHO_BOTON_ACCION }
        val salir = Button("Salir del simulador").also { it.prefWidth = ANCHO_BOTON_ACCION }
        reiniciar.setOnAction { escucha.reiniciar() }
        salir.setOnAction { alSalir() }

        return VBox(14.0, titulo, etiquetaResumenFin, reiniciar, salir)
            .also { decorarMenu(it, EstilosUI.MENU_FIN) }
    }

    private fun construirMenu(
        titulo: String,
        subtitulo: String,
        ayuda: String,
        contenido: Node
    ): VBox {
        val etiquetaTitulo = Label(titulo).also { it.style = EstilosUI.TITULO }
        val etiquetaSubtitulo = Label(subtitulo).also {
            it.alignment = Pos.CENTER
            it.style = EstilosUI.SUBTITULO
        }
        val etiquetaAyuda = Label(ayuda).also {
            it.alignment = Pos.CENTER
            it.style = EstilosUI.AYUDA
        }

        val menu = VBox(14.0, etiquetaTitulo, etiquetaSubtitulo, contenido, etiquetaAyuda)
        decorarMenu(menu, EstilosUI.MENU)
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
        val escala = (ALTO_PREVIA * 0.42) / forma.radioEnvoltura()
        val previa = previaCanvas {
            DibujoPelota.dibujar(it, tipo, forma, 0.0, Vector2D(60.0, ALTO_PREVIA / 2.0), escala)
        }

        val nombre = Label(tipo.nombre()).also { it.style = EstilosUI.NOMBRE }
        val datos = Label("masa ${"%.2f".format(tipo.masa())} kg · rebote ${"%.2f".format(tipo.coeficienteRestitucion())}")
            .also { it.style = EstilosUI.DETALLE }
        val rozamiento = Label("rozamiento ${"%.1f".format(tipo.coeficienteFriccion())} · ${forma.tipo().etiqueta()}")
            .also { it.style = EstilosUI.AYUDA }

        val textos = VBox(2.0, nombre, datos, rozamiento).also {
            it.isMouseTransparent = true
            it.alignment = Pos.CENTER_LEFT
        }

        val boton = botonMenu(previa, textos, ANCHO_BOTON_MENU) { escucha.seleccionarPelota(tipo) }
        boton.tooltip = Tooltip(tipo.descripcion())
        botonesPelota[tipo] = boton
        return boton
    }

    private fun crearBotonPared(tipo: TipoPared): ToggleButton {
        val previa = previaCanvas { gc ->
            val yCentro = ALTO_PREVIA / 2.0
            gc.stroke = colorDe(tipo.colorBorde())
            gc.lineWidth = tipo.grosor() + 2.0
            gc.strokeLine(4.0, yCentro, 116.0, yCentro)
            gc.stroke = colorDe(tipo.color())
            gc.lineWidth = tipo.grosor()
            gc.strokeLine(4.0, yCentro, 116.0, yCentro)
            if (tipo.indestructible()) {
                gc.stroke = Color.web("#f1c40f")
                gc.lineWidth = 2.0
                gc.strokeLine(60.0, 6.0, 60.0, ALTO_PREVIA - 6.0)
            }
        }

        val nombre = Label(tipo.nombre()).also { it.style = EstilosUI.NOMBRE }
        val datos = Label("grosor ${tipo.grosor().toInt()} px · aguanta ${textoResistencia(tipo)}")
            .also { it.style = EstilosUI.DETALLE }
        val rompe = Label(tipo.textoRompible()).also {
            it.style = EstilosUI.AYUDA
            it.maxWidth = ANCHO_BOTON_PARED
            it.isWrapText = true
        }

        val textos = VBox(2.0, nombre, datos, rompe).also {
            it.isMouseTransparent = true
            it.alignment = Pos.CENTER_LEFT
        }

        val boton = botonMenu(previa, textos, ANCHO_BOTON_PARED) { escucha.seleccionarTipoPared(tipo) }
        boton.tooltip = Tooltip(tipo.descripcion())
        botonesPared[tipo] = boton
        return boton
    }

    private fun colorDe(valor: Int): Color =
        Color.rgb((valor shr 16) and 0xFF, (valor shr 8) and 0xFF, valor and 0xFF)

    private fun textoResistencia(tipo: TipoPared): String =
        if (tipo.indestructible()) "todo" else "${tipo.resistencia().toInt()} de energia"

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
        boton.style = EstilosUI.BOTON
        boton.setOnAction {
            accion()
            alCerrarMenus()
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
            boton.style = if (boton.isSelected) EstilosUI.BOTON_ACTIVO else EstilosUI.BOTON
        }
    }

    private companion object {
        const val COLUMNAS_MENU = 2
        const val ANCHO_BOTON_MENU = 300.0
        const val ANCHO_BOTON_PARED = 340.0
        const val ALTO_PREVIA = 44.0
        const val ANCHO_BOTON_ACCION = 200.0
    }
}
