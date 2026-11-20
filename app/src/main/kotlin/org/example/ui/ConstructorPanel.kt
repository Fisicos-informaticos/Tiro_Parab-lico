package org.example.ui

import javafx.collections.FXCollections
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.Tooltip
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.layout.VBox

internal object ConstructorPanel {
    data class EspecificacionCombo(
        private val opciones: List<String>,
        private val seleccionada: String,
        private val ayuda: String,
        private val alCambiar: (String?) -> Unit
    ) {
        fun opciones(): List<String> = opciones
        fun seleccionada(): String = seleccionada
        fun ayuda(): String = ayuda
        fun alCambiar(valor: String?) = alCambiar.invoke(valor)
        fun seleccionInicial(): String = if (seleccionada() in opciones()) seleccionada() else opciones().firstOrNull().orEmpty()
        fun crearComboBox(): ComboBox<String> = combo(this)
    }

    fun seccion(titulo: String, contenido: List<Node>): VBox = SeccionControl(titulo, contenido).raiz()
    fun campo(titulo: String, control: Node): VBox = VBox(4.0).apply {
        children.addAll(Label(titulo).apply { style = EstilosUI.CAPTION }, control)
    }
    fun fila(vararg nodos: Region): HBox = HBox(8.0).apply {
        for (nodo in nodos) {
            nodo.maxWidth = Double.MAX_VALUE
            HBox.setHgrow(nodo, Priority.ALWAYS)
            children.add(nodo)
        }
    }
    fun prepararBoton(boton: Button, accion: () -> Unit) {
        boton.apply {
            style = EstilosUI.BOTON
            maxWidth = Double.MAX_VALUE
            setOnAction { accion() }
        }
    }
    fun combo(especificacion: EspecificacionCombo): ComboBox<String> = ComboBox<String>().apply {
        items = FXCollections.observableArrayList(especificacion.opciones())
        value = especificacion.seleccionInicial()
        tooltip = Tooltip(especificacion.ayuda())
        maxWidth = Double.MAX_VALUE
        setOnAction { especificacion.alCambiar(value) }
    }
    fun etiquetaDetalle(): Label = Label().also { it.style = EstilosUI.DETALLE }
    fun etiquetaAyuda(ancho: Double): Label = Label().also {
        it.style = EstilosUI.AYUDA
        it.isWrapText = true
        it.maxWidth = ancho
    }
}
