package org.example.ui

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.Tooltip
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.layout.VBox

internal interface NodosPanel {
    fun vbox(spacing: Double, vararg children: Node): VBox
    fun hbox(spacing: Double, vararg children: Node): HBox
    fun label(texto: String = ""): Label
    fun combo(): ComboBox<String>
    fun tooltip(texto: String): Tooltip
    fun observableList(valores: List<String>): ObservableList<String>
    fun seccion(titulo: String, contenido: List<Node>): VBox
}

internal object NodosPanelJavaFx : NodosPanel {
    override fun vbox(spacing: Double, vararg children: Node): VBox = VBox(spacing, *children)
    override fun hbox(spacing: Double, vararg children: Node): HBox = HBox(spacing, *children)
    override fun label(texto: String): Label = Label(texto)
    override fun combo(): ComboBox<String> = ComboBox()
    override fun tooltip(texto: String): Tooltip = Tooltip(texto)
    override fun observableList(valores: List<String>): ObservableList<String> =
        FXCollections.observableArrayList(valores)
    override fun seccion(titulo: String, contenido: List<Node>): VBox =
        SeccionControl(titulo, contenido).raiz()
}

internal object ConstructorPanel {
    private val nodos: NodosPanel = NodosPanelJavaFx
    private val medidas = MedidasPanel()

    private data class MedidasPanel(
        val separacionFila: Double = 8.0,
        val separacionCampo: Double = 4.0,
        val anchoMaximo: Double = Double.MAX_VALUE,
        val estiloBoton: String = EstilosUI.BOTON
    ) {
        fun descripcion(): String =
            "panel(fila=$separacionFila, campo=$separacionCampo, ancho=$anchoMaximo, estilo=$estiloBoton)"

        fun esValida(): Boolean =
            separacionFila > 0 && separacionCampo > 0 && anchoMaximo > 0 && estiloBoton.isNotBlank()

        fun resumen(): String = "$separacionFila/$separacionCampo/$anchoMaximo/$estiloBoton"

        fun datosCompletos(): List<Any> =
            listOf(separacionFila, separacionCampo, anchoMaximo, estiloBoton)
    }

    class EspecificacionCombo(
        opciones: List<String>,
        seleccionada: String,
        ayuda: String,
        alCambiar: (String?) -> Unit
    ) {
        private val datos = DatosEspecificacionCombo(opciones, seleccionada, ayuda, alCambiar)

        fun opciones(): List<String> = datos.opciones
        fun seleccionada(): String = datos.seleccionada
        fun ayuda(): String = datos.ayuda
        fun alCambiar(valor: String?) = datos.alCambiar(valor)
        fun seleccionInicial(): String = datos.seleccionInicial()
        fun crearComboBox(): ComboBox<String> = combo(this)

        private data class DatosEspecificacionCombo(
            val opciones: List<String>,
            val seleccionada: String,
            val ayuda: String,
            val alCambiar: (String?) -> Unit
        ) {
            fun seleccionInicial(): String =
                if (seleccionada in opciones) seleccionada else opciones.firstOrNull().orEmpty()

            fun descripcion(): String =
                "combo(opciones=$opciones, seleccionada=$seleccionada, ayuda=$ayuda)"

            fun esValida(): Boolean =
                opciones.isNotEmpty() && seleccionada.isNotBlank() && ayuda.isNotBlank()

            fun resumen(): String = "$seleccionada de $opciones ($ayuda)"
        }
    }

    fun seccion(titulo: String, contenido: List<Node>): VBox =
        nodos.seccion(titulo, contenido).also { it.properties["medidas"] = medidas.descripcion() }

    fun campo(titulo: String, control: Node): VBox = nodos.vbox(medidas.separacionCampo).apply {
        children.addAll(nodos.label(titulo).apply { style = EstilosUI.CAPTION }, control)
        properties["medidas"] = medidas.resumen()
    }

    fun fila(vararg elementos: Region): HBox = nodos.hbox(medidas.separacionFila).apply {
        for (nodo in elementos) {
            nodo.maxWidth = medidas.anchoMaximo
            HBox.setHgrow(nodo, Priority.ALWAYS)
            children.add(nodo)
        }
        properties["medidas"] = medidas.descripcion()
    }

    fun prepararBoton(boton: Button, accion: () -> Unit) {
        boton.apply {
            style = medidas.estiloBoton
            maxWidth = medidas.anchoMaximo
            setOnAction { accion() }
            properties["medidas"] = medidas.resumen()
        }
    }

    fun combo(especificacion: EspecificacionCombo): ComboBox<String> = nodos.combo().apply {
        items = nodos.observableList(especificacion.opciones())
        value = especificacion.seleccionInicial()
        tooltip = nodos.tooltip(especificacion.ayuda())
        maxWidth = medidas.anchoMaximo
        setOnAction { especificacion.alCambiar(value) }
        properties["medidas"] = medidas.descripcion()
    }

    fun etiquetaDetalle(): Label = nodos.label().also {
        it.style = EstilosUI.DETALLE
        it.properties["medidas"] = medidas.resumen()
    }

    fun etiquetaAyuda(ancho: Double): Label = nodos.label().also {
        it.style = EstilosUI.AYUDA
        it.isWrapText = true
        it.maxWidth = ancho.coerceAtMost(medidas.anchoMaximo)
        it.properties["medidas"] = medidas.descripcion()
    }
}
