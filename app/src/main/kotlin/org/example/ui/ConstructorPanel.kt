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

/** Piezas de maquetacion compartidas por las secciones del panel lateral. */
internal object ConstructorPanel {
    fun seccion(titulo: String, contenido: List<Node>): VBox =
        SeccionControl(titulo, contenido).raiz()

    fun campo(titulo: String, control: Node): VBox =
        VBox(4.0).apply {
            val etiqueta = Label(titulo).apply { style = EstilosUI.CAPTION }
            children.addAll(etiqueta, control)
        }

    fun fila(vararg nodos: Region): HBox =
        HBox(8.0).apply {
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

    fun combo(
        opciones: List<String>,
        seleccionada: String,
        ayuda: String,
        alCambiar: (String?) -> Unit
    ): ComboBox<String> = ComboBox<String>().apply {
        items = FXCollections.observableArrayList(opciones)
        value = seleccionada
        tooltip = Tooltip(ayuda)
        maxWidth = Double.MAX_VALUE
        setOnAction { alCambiar(value) }
    }

    fun etiquetaDetalle(): Label = Label().also { it.style = EstilosUI.DETALLE }

    fun etiquetaAyuda(ancho: Double): Label = Label().also {
        it.style = EstilosUI.AYUDA
        it.isWrapText = true
        it.maxWidth = ancho
    }
}
