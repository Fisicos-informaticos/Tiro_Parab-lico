package org.example

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.layout.StackPane
import org.example.ui.MenusJuego
import org.example.ui.PanelControl

/** Monta el arbol JavaFX de la partida y coloca el panel y los menus. */
internal class EscenaJuego(
    private val canvas: Canvas,
    private val panelControl: PanelControl,
    private val menus: MenusJuego
) {
    fun crear(): Scene {
        val root = StackPane(
            canvas,
            panelControl.raiz(),
            menus.menuPelotas(),
            menus.menuParedes(),
            menus.menuPausa(),
            menus.menuFin()
        )
        StackPane.setAlignment(panelControl.raiz(), Pos.TOP_RIGHT)
        StackPane.setMargin(panelControl.raiz(), Insets(12.0))
        for (menu in menusTodos()) {
            StackPane.setAlignment(menu, Pos.CENTER)
        }
        return Scene(root, MainApp.ANCHO_VENTANA, MainApp.ALTO_VENTANA)
    }

    private fun menusTodos() = listOf(
        menus.menuPelotas(),
        menus.menuParedes(),
        menus.menuPausa(),
        menus.menuFin()
    )
}
