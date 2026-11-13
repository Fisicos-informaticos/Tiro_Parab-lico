package org.example.ui

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.canvas.Canvas
import javafx.scene.control.Label
import javafx.scene.control.ToggleButton
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.scene.paint.Color

/** Piezas compartidas por los menus de seleccion y de estado. */
internal object ConstructorMenu {
    const val COLUMNAS_MENU = 2
    const val ANCHO_BOTON_MENU = 300.0
    const val ANCHO_BOTON_PARED = 340.0
    const val ALTO_PREVIA = 44.0
    const val ANCHO_BOTON_ACCION = 200.0

    fun construir(
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
        decorar(menu, EstilosUI.MENU)
        return menu
    }

    fun decorar(menu: VBox, estilo: String) {
        menu.alignment = Pos.CENTER
        menu.padding = Insets(20.0)
        menu.maxWidth = Region.USE_PREF_SIZE
        menu.maxHeight = Region.USE_PREF_SIZE
        menu.isPickOnBounds = true
        menu.style = estilo
        menu.isVisible = false
    }

    fun <T> rejillaTipos(tipos: List<T>, crearBoton: (T) -> ToggleButton): GridPane {
        val rejilla = GridPane()
        rejilla.hgap = 10.0
        rejilla.vgap = 10.0
        tipos.forEachIndexed { indice, tipo ->
            rejilla.add(crearBoton(tipo), indice % COLUMNAS_MENU, indice / COLUMNAS_MENU)
        }
        return rejilla
    }

    fun botonMenu(
        previa: Canvas,
        textos: VBox,
        ancho: Double,
        alCerrarMenus: () -> Unit,
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

    fun previaCanvas(dibujar: (javafx.scene.canvas.GraphicsContext) -> Unit): Canvas {
        val canvas = Canvas(120.0, ALTO_PREVIA)
        canvas.isMouseTransparent = true
        dibujar(canvas.graphicsContext2D)
        return canvas
    }

    fun colorDe(valor: Int): Color =
        Color.rgb((valor shr 16) and 0xFF, (valor shr 8) and 0xFF, valor and 0xFF)

    fun textos(
        nombre: String,
        datos: String,
        extra: String,
        anchoExtra: Double? = null
    ): VBox {
        val etiquetaNombre = Label(nombre).also { it.style = EstilosUI.NOMBRE }
        val etiquetaDatos = Label(datos).also { it.style = EstilosUI.DETALLE }
        val etiquetaExtra = Label(extra).also {
            it.style = EstilosUI.AYUDA
            if (anchoExtra != null) {
                it.maxWidth = anchoExtra
                it.isWrapText = true
            }
        }
        return VBox(2.0, etiquetaNombre, etiquetaDatos, etiquetaExtra).also {
            it.isMouseTransparent = true
            it.alignment = Pos.CENTER_LEFT
        }
    }
}
