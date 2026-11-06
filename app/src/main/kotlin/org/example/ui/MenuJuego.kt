package org.example.ui

import javafx.scene.layout.VBox

/**
 * Contrato de los menus que necesita el coordinador de la partida. La logica de
 * menus depende de esta abstraccion y no de la construccion JavaFX concreta.
 */
interface MenuJuego {
    fun menuPelotas(): VBox
    fun menuParedes(): VBox
    fun menuPausa(): VBox
    fun menuFin(): VBox
    fun marcarPelotaActual()
    fun marcarParedActual()
    fun mostrarResumenFin(texto: String)
}
