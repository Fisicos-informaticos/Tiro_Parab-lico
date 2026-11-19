package org.example.ui

import javafx.scene.layout.VBox
import org.example.modelo.TipoPelota
import org.example.modelo.TipoPared

/**
 * Coordina los menus de la partida. Cada menu concreto vive en su propia clase
 * para que la seleccion, la pausa y el fin de tiro evolucionen por separado.
 */
class MenusJuego(
    escucha: UIEventListener,
    tipoPelotaActual: () -> TipoPelota,
    tipoParedActual: () -> TipoPared,
    alCerrarMenus: () -> Unit,
    alSalir: () -> Unit
) : MenuJuego {

    private val menuPelotas = MenuPelotas(escucha, tipoPelotaActual, alCerrarMenus)
    private val menuParedes = MenuParedes(escucha, tipoParedActual, alCerrarMenus)
    private val menuPausa = MenuPausa(escucha, alSalir)
    private val menuFin = MenuFin(escucha, alSalir)

    override fun menuPelotas(): VBox = menuPelotas.menu()

    override fun menuParedes(): VBox = menuParedes.menu()

    override fun menuPausa(): VBox = menuPausa.menu()

    override fun menuFin(): VBox = menuFin.menu()

    override fun marcarPelotaActual() = menuPelotas.marcarActual()

    override fun marcarParedActual() = menuParedes.marcarActual()

    override fun mostrarResumenFin(texto: String) = menuFin.mostrarResumen(texto)
}
