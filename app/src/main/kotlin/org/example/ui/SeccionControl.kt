package org.example.ui

import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.layout.Region
import javafx.scene.layout.VBox

/**
 * Bloque titulado del panel lateral. Agrupa un conjunto de controles afines bajo
 * una etiqueta y una linea divisoria, de modo que la maquetacion quede ordenada
 * por secciones logicas.
 */
class SeccionControl(titulo: String, contenido: List<Node>) {

    private val raiz: VBox = VBox(8.0).apply {
        val etiqueta = Label(titulo).apply { style = EstilosUI.TITULO_SECCION }
        val divisor = Region().apply {
            style = EstilosUI.DIVISOR
            prefHeight = 1.0
            maxWidth = Double.MAX_VALUE
        }
        children.addAll(etiqueta, divisor)
        children.addAll(contenido)
    }

    /** Contenedor estilizado que se inserta en la escena. */
    fun raiz(): VBox = raiz
}
