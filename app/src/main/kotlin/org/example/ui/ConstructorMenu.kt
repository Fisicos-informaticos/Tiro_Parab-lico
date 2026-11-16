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

    data class EspecificacionMenu(
        private val titulo: String,
        private val subtitulo: String,
        private val ayuda: String,
        private val contenido: Node
    ) {
        fun titulo(): String = titulo
        fun subtitulo(): String = subtitulo
        fun ayuda(): String = ayuda
        fun contenido(): Node = contenido
        fun tieneSubtitulo(): Boolean = subtitulo().isNotBlank()
    }

    data class EspecificacionBotonMenu(
        private val previa: Canvas,
        private val textos: VBox,
        private val ancho: Double,
        private val alCerrarMenus: () -> Unit,
        private val accion: () -> Unit
    ) {
        fun previa(): Canvas = previa
        fun textos(): VBox = textos
        fun ancho(): Double = ancho
        fun ejecutar() {
            accion()
            alCerrarMenus()
        }
    }

    data class EspecificacionTextos(
        private val nombre: String,
        private val datos: String,
        private val extra: String,
        private val anchoExtra: Double? = null
    ) {
        fun nombre(): String = nombre
        fun datos(): String = datos
        fun extra(): String = extra
        fun anchoExtra(): Double? = anchoExtra
        fun detalleCompleto(): String = listOf(datos(), extra()).filter { it.isNotBlank() }.joinToString(" · ")
    }

    fun construir(especificacion: EspecificacionMenu): VBox {
        val menu = VBox(
            14.0,
            Label(especificacion.titulo()).also { it.style = EstilosUI.TITULO },
            etiquetaCentrada(especificacion.subtitulo(), EstilosUI.SUBTITULO),
            especificacion.contenido(),
            etiquetaCentrada(especificacion.ayuda(), EstilosUI.AYUDA)
        )
        decorar(menu, EstilosUI.MENU)
        return menu
    }

    private fun etiquetaCentrada(texto: String, estilo: String) = Label(texto).also {
        it.alignment = Pos.CENTER
        it.style = estilo
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

    fun botonMenu(especificacion: EspecificacionBotonMenu): ToggleButton {
        val boton = ToggleButton()
        boton.graphic = HBox(10.0, especificacion.previa(), especificacion.textos())
        boton.alignment = Pos.CENTER_LEFT
        boton.prefWidth = especificacion.ancho()
        boton.style = EstilosUI.BOTON
        boton.setOnAction { especificacion.ejecutar() }
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

    fun textos(especificacion: EspecificacionTextos): VBox {
        val etiquetaNombre = Label(especificacion.nombre()).also { it.style = EstilosUI.NOMBRE }
        val etiquetaDatos = Label(especificacion.datos()).also { it.style = EstilosUI.DETALLE }
        val etiquetaExtra = Label(especificacion.extra()).also {
            it.style = EstilosUI.AYUDA
            especificacion.anchoExtra()?.let { ancho ->
                it.maxWidth = ancho
                it.isWrapText = true
            }
        }
        return VBox(2.0, etiquetaNombre, etiquetaDatos, etiquetaExtra).also {
            it.isMouseTransparent = true
            it.alignment = Pos.CENTER_LEFT
        }
    }
}
