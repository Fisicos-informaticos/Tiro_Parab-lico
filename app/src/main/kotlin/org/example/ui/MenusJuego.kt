package org.example.ui

import javafx.scene.layout.VBox
import org.example.modelo.TipoPelota
import org.example.modelo.TipoPared

/**
 * Coordina los menus de la partida. Cada menu concreto vive en su propia clase
 * para que la seleccion, la pausa y el fin de tiro evolucionen por separado.
 */
class MenusJuego(
    acciones: AccionesJuego,
    configuracion: ConfiguracionJuego,
    tipoPelotaActual: () -> TipoPelota,
    tipoParedActual: () -> TipoPared,
    alCerrarMenus: () -> Unit,
    alSalir: () -> Unit
) : MenuJuego {
    private val datos = DatosMenusJuego(
        MenuPelotas(configuracion, tipoPelotaActual, alCerrarMenus),
        MenuParedes(configuracion, tipoParedActual, alCerrarMenus),
        MenuPausa(acciones, alSalir),
        MenuFin(acciones, alSalir)
    )

    override fun menuPelotas(): VBox = datos.menuPelotas.menu()

    override fun menuParedes(): VBox = datos.menuParedes.menu()

    override fun menuPausa(): VBox = datos.menuPausa.menu()

    override fun menuFin(): VBox = datos.menuFin.menu()

    override fun marcarPelotaActual() = datos.marcarPelotaActual()

    override fun marcarParedActual() = datos.marcarParedActual()

    override fun mostrarResumenFin(texto: String) = datos.mostrarResumenFin(texto)

    fun todos(): List<VBox> = datos.menus()

    private data class DatosMenusJuego(
        val menuPelotas: MenuPelotas,
        val menuParedes: MenuParedes,
        val menuPausa: MenuPausa,
        val menuFin: MenuFin
    ) {
        fun marcarPelotaActual() = menuPelotas.marcarActual()

        fun marcarParedActual() = menuParedes.marcarActual()

        fun mostrarResumenFin(texto: String) = menuFin.mostrarResumen(texto)

        fun descripcion(): String =
            "menus(pelotas=${menuPelotas.javaClass.simpleName}, paredes=${menuParedes.javaClass.simpleName}, pausa=${menuPausa.javaClass.simpleName}, fin=${menuFin.javaClass.simpleName})"

        fun esValido(): Boolean =
            menuPelotas.javaClass !== menuParedes.javaClass && menuParedes.javaClass !== menuPausa.javaClass && menuPausa.javaClass !== menuFin.javaClass

        fun menus(): List<VBox> = listOf(menuPelotas.menu(), menuParedes.menu(), menuPausa.menu(), menuFin.menu())

        fun sincronizar(): String {
            marcarPelotaActual()
            marcarParedActual()
            return resumen()
        }

        fun resumen(): String =
            "${menuPelotas.javaClass.simpleName}/${menuParedes.javaClass.simpleName}/${menuPausa.javaClass.simpleName}/${menuFin.javaClass.simpleName}"

        fun estado(): Map<String, Any> = mapOf(
            "pelotas" to menuPelotas,
            "paredes" to menuParedes,
            "pausa" to menuPausa,
            "fin" to menuFin
        )
    }
}
