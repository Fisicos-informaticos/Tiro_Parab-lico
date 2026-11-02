package org.example

import org.example.simulacion.Simulacion
import org.example.ui.MenusJuego

/** Decide que menus estan abiertos y mantiene su visibilidad sincronizada. */
class GestorMenus(
    private val simulacion: Simulacion,
    private val menus: MenusJuego
) {
    var menuPelotasAbierto = false
        private set
    var menuParedesAbierto = false
        private set
    var menuFinAbierto = false
        private set

    /** Con un menu abierto el mundo no responde al raton. */
    val interaccionBloqueada: Boolean
        get() = simulacion.pausado() || menuPelotasAbierto || menuParedesAbierto || menuFinAbierto

    val enVuelo: Boolean
        get() = simulacion.estado().estaEnVuelo()

    fun alternarMenuPelotas() {
        if (menuPelotasAbierto) cerrarMenuPelotas() else abrirMenuPelotas()
    }

    fun alternarMenuParedes() {
        if (menuParedesAbierto) cerrarMenuParedes() else abrirMenuParedes()
    }

    fun abrirMenuPelotas() {
        if (!puedeAbrirMenu()) return
        simulacion.abrirEdicion()
        cerrarMenuParedes()
        menuPelotasAbierto = true
        menus.marcarPelotaActual()
    }

    fun abrirMenuParedes() {
        if (!puedeAbrirMenu()) return
        simulacion.abrirEdicion()
        cerrarMenuPelotas()
        menuParedesAbierto = true
        menus.marcarParedActual()
    }

    fun abrirMenuFin() {
        menuFinAbierto = true
    }

    fun cerrarMenuFin() {
        menuFinAbierto = false
    }

    fun cerrarTodosLosMenus() {
        cerrarMenuPelotas()
        cerrarMenuParedes()
    }

    /** Cierra el menu que corresponda a ESC. Devuelve true si cerro alguno. */
    fun cerrarConEscape(): Boolean = when {
        menuPelotasAbierto -> cerrarY(::cerrarMenuPelotas)
        menuParedesAbierto -> cerrarY(::cerrarMenuParedes)
        menuFinAbierto -> cerrarY(::cerrarMenuFin)
        else -> false
    }

    fun sincronizarVisibilidad() {
        menus.menuPelotas.isVisible = menuPelotasAbierto
        menus.menuParedes.isVisible = menuParedesAbierto
        menus.menuPausa.isVisible = simulacion.pausado()
        menus.menuFin.isVisible = menuFinAbierto && !simulacion.pausado()
    }

    private fun puedeAbrirMenu(): Boolean = !enVuelo && !menuFinAbierto

    private fun cerrarMenuPelotas() {
        menuPelotasAbierto = false
    }

    private fun cerrarMenuParedes() {
        menuParedesAbierto = false
    }

    private fun cerrarY(cerrar: () -> Unit): Boolean {
        cerrar()
        return true
    }
}
