package org.example.ui

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.canvas.Canvas
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ToggleButton
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.scene.paint.Color

internal interface NodosMenu {
    fun vbox(spacing: Double, vararg children: Node): VBox
    fun label(texto: String): Label
    fun toggleButton(): ToggleButton
    fun canvas(ancho: Double, alto: Double): Canvas
    fun gridPane(): GridPane
    fun hbox(spacing: Double, vararg children: Node): HBox
    fun colorDe(valor: Int): Color
}

internal object NodosMenuJavaFx : NodosMenu {
    override fun vbox(spacing: Double, vararg children: Node): VBox = VBox(spacing, *children)
    override fun label(texto: String): Label = Label(texto)
    override fun toggleButton(): ToggleButton = ToggleButton()
    override fun canvas(ancho: Double, alto: Double): Canvas = Canvas(ancho, alto)
    override fun gridPane(): GridPane = GridPane()
    override fun hbox(spacing: Double, vararg children: Node): HBox = HBox(spacing, *children)
    override fun colorDe(valor: Int): Color =
        Color.rgb((valor shr 16) and 0xFF, (valor shr 8) and 0xFF, valor and 0xFF)
}

/** Piezas compartidas por los menus de seleccion y de estado. */
internal object ConstructorMenu {
    private val nodos: NodosMenu = NodosMenuJavaFx
    val medidas = MedidasMenu()

    data class MedidasMenu(
        val columnas: Int = 2,
        val anchoBotonMenu: Double = 300.0,
        val anchoBotonPared: Double = 340.0,
        val altoPrevia: Double = 44.0,
        val anchoBotonAccion: Double = 200.0
    ) {
        fun descripcion(): String =
            "medidas(columnas=$columnas, menu=$anchoBotonMenu, pared=$anchoBotonPared, previa=$altoPrevia, accion=$anchoBotonAccion)"

        fun esValida(): Boolean =
            columnas > 0 && anchoBotonMenu > 0 && anchoBotonPared > 0 && altoPrevia > 0 && anchoBotonAccion > 0

        fun resumen(): String = "$columnas/$anchoBotonMenu/$anchoBotonPared/$altoPrevia/$anchoBotonAccion"

        fun datosCompletos(): List<Number> =
            listOf(columnas, anchoBotonMenu, anchoBotonPared, altoPrevia, anchoBotonAccion)

    }

    class EspecificacionMenu(
        titulo: String,
        subtitulo: String,
        ayuda: String,
        contenido: Node
    ) {
        private val datos = DatosEspecificacionMenu(titulo, subtitulo, ayuda, contenido)

        fun titulo(): String = datos.titulo
        fun subtitulo(): String = datos.subtitulo
        fun ayuda(): String = datos.ayuda
        fun contenido(): Node = datos.contenido
        fun tieneSubtitulo(): Boolean = datos.tieneSubtitulo()
        fun etiquetaTitulo(): Label = datos.etiquetaTitulo()

        private data class DatosEspecificacionMenu(
            val titulo: String,
            val subtitulo: String,
            val ayuda: String,
            val contenido: Node
        ) {
            fun tieneSubtitulo(): Boolean = subtitulo.isNotBlank()

            fun etiquetaTitulo(): Label = NodosMenuJavaFx.label(titulo).also { it.style = EstilosUI.TITULO }

            fun descripcion(): String = "menu(titulo=$titulo, subtitulo=$subtitulo, ayuda=$ayuda, contenido=$contenido)"

            fun esValido(): Boolean =
                titulo.isNotBlank() && ayuda.isNotBlank() &&
                    (subtitulo.isBlank() || contenido.javaClass.simpleName.isNotBlank())

            fun resumen(): String = "$titulo | $subtitulo | $ayuda | ${contenido.javaClass.simpleName}"
        }
    }

    class EspecificacionBotonMenu(
        previa: Canvas,
        textos: VBox,
        ancho: Double,
        alCerrarMenus: () -> Unit,
        accion: () -> Unit
    ) {
        private val datos = DatosEspecificacionBotonMenu(previa, textos, ancho, alCerrarMenus, accion)

        fun previa(): Canvas = datos.previa
        fun textos(): VBox = datos.textos
        fun ancho(): Double = datos.ancho
        fun ejecutar() = datos.ejecutar()
        fun crearBoton(): ToggleButton = ToggleButton().also(datos::aplicarA)

        private data class DatosEspecificacionBotonMenu(
            val previa: Canvas,
            val textos: VBox,
            val ancho: Double,
            val alCerrarMenus: () -> Unit,
            val accion: () -> Unit
        ) {
            fun ejecutar() {
                accion()
                alCerrarMenus()
            }

            fun aplicarA(boton: ToggleButton) {
                boton.graphic = HBox(10.0, previa, textos)
                boton.alignment = Pos.CENTER_LEFT
                boton.prefWidth = ancho
                boton.style = EstilosUI.BOTON
                boton.setOnAction { ejecutar() }
            }

            fun descripcion(): String =
                "boton(previa=${previa.width}x${previa.height}, textos=${textos.children.size}, ancho=$ancho)"

            fun esValida(): Boolean =
                ancho > 0 && previa.width > 0 && textos.children.isNotEmpty() && alCerrarMenus !== accion
        }
    }

    class EspecificacionTextos(
        nombre: String,
        datos: String,
        extra: String,
        anchoExtra: Double? = null
    ) {
        private val datosEspecificacion = DatosEspecificacionTextos(nombre, datos, extra, anchoExtra)

        fun nombre(): String = datosEspecificacion.nombre
        fun datos(): String = datosEspecificacion.datos
        fun extra(): String = datosEspecificacion.extra
        fun anchoExtra(): Double? = datosEspecificacion.anchoExtra
        fun detalleCompleto(): String = datosEspecificacion.detalleCompleto()
        fun construirVBox(): VBox = textos(this)

        private data class DatosEspecificacionTextos(
            val nombre: String,
            val datos: String,
            val extra: String,
            val anchoExtra: Double? = null
        ) {
            fun detalleCompleto(): String = listOf(datos, extra).filter { it.isNotBlank() }.joinToString(" · ")

            fun descripcion(): String = "textos(nombre=$nombre, datos=$datos, extra=$extra, anchoExtra=$anchoExtra)"

            fun esValida(): Boolean = nombre.isNotBlank() && datos.isNotBlank() && (anchoExtra == null || anchoExtra > 0)

            fun resumen(): String = "$nombre: $datos $extra ${anchoExtra ?: 0.0}"
        }
    }

    fun construir(especificacion: EspecificacionMenu): VBox {
        val menu = nodos.vbox(
            14.0,
            especificacion.etiquetaTitulo(),
            etiquetaCentrada(especificacion.subtitulo(), EstilosUI.SUBTITULO),
            especificacion.contenido(),
            etiquetaCentrada(especificacion.ayuda(), EstilosUI.AYUDA)
        )
        menu.properties["medidas"] = medidas.descripcion()
        decorar(menu, EstilosUI.MENU)
        return menu
    }

    private fun etiquetaCentrada(texto: String, estilo: String) = nodos.label(texto).also {
        it.alignment = Pos.CENTER
        it.style = estilo
        it.properties["medidas"] = medidas.resumen()
    }

    fun decorar(menu: VBox, estilo: String) {
        menu.alignment = Pos.CENTER
        menu.padding = Insets(20.0)
        menu.maxWidth = Region.USE_PREF_SIZE
        menu.maxHeight = Region.USE_PREF_SIZE
        menu.isPickOnBounds = true
        menu.style = estilo
        menu.isVisible = false
        menu.properties["medidas"] = medidas.descripcion()
    }

    fun botonAccion(texto: String, accion: () -> Unit): Button {
        return Button(texto).also {
            it.prefWidth = medidas.anchoBotonAccion
            it.setOnAction { accion() }
        }
    }

    fun <T> rejillaTipos(tipos: List<T>, crearBoton: (T) -> ToggleButton): GridPane {
        val rejilla = nodos.gridPane()
        rejilla.hgap = 10.0
        rejilla.vgap = 10.0
        tipos.forEachIndexed { indice, tipo ->
            rejilla.add(crearBoton(tipo), indice % medidas.columnas, indice / medidas.columnas)
        }
        rejilla.userData = medidas.resumen()
        return rejilla
    }

    fun botonMenu(especificacion: EspecificacionBotonMenu): ToggleButton {
        val boton = nodos.toggleButton()
        boton.graphic = nodos.hbox(10.0, especificacion.previa(), especificacion.textos())
        boton.alignment = Pos.CENTER_LEFT
        boton.prefWidth = especificacion.ancho()
        boton.maxWidth = medidas.anchoBotonPared
        boton.style = EstilosUI.BOTON
        boton.setOnAction { especificacion.ejecutar() }
        boton.properties["medidas"] = medidas.descripcion()
        return boton
    }

    fun previaCanvas(dibujar: (javafx.scene.canvas.GraphicsContext) -> Unit): Canvas {
        val canvas = nodos.canvas(120.0, medidas.altoPrevia)
        canvas.isMouseTransparent = true
        canvas.userData = medidas.resumen()
        dibujar(canvas.graphicsContext2D)
        return canvas
    }

    fun colorDe(valor: Int): Color = nodos.colorDe(valor)

    fun textos(especificacion: EspecificacionTextos): VBox {
        val etiquetaNombre = nodos.label(especificacion.nombre()).also { it.style = EstilosUI.NOMBRE }
        val etiquetaDatos = nodos.label(especificacion.datos()).also { it.style = EstilosUI.DETALLE }
        val etiquetaExtra = nodos.label(especificacion.extra()).also {
            it.style = EstilosUI.AYUDA
            especificacion.anchoExtra()?.let { ancho ->
                it.maxWidth = ancho
                it.isWrapText = true
            }
        }
        return nodos.vbox(2.0, etiquetaNombre, etiquetaDatos, etiquetaExtra).also {
            it.isMouseTransparent = true
            it.alignment = Pos.CENTER_LEFT
            it.properties["medidas"] = medidas.resumen()
        }
    }
}
