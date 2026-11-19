package org.example

import org.example.simulacion.ContextoJuego
import org.example.ui.MenuJuego

/** Decide que menus estan abiertos y mantiene su visibilidad sincronizada. */
class GestorMenus(
    private val simulacion: ContextoJuego,
    private val menus: MenuJuego
) : CoordinadorMenus {
    private var menuPelotasAbierto: Boolean = false
    private var menuParedesAbierto: Boolean = false
    private var menuFinAbierto: Boolean = false

    override fun menuPelotasAbierto(): Boolean = menuPelotasAbierto

    override fun menuParedesAbierto(): Boolean = menuParedesAbierto

    override fun menuFinAbierto(): Boolean = menuFinAbierto

    /** Con un menu abierto el mundo no responde al raton. */
    override fun interaccionBloqueada(): Boolean =
        simulacion.pausado() || menuPelotasAbierto || menuParedesAbierto || menuFinAbierto

    override fun enVuelo(): Boolean = simulacion.estado().estaEnVuelo()

    override fun alternarMenuPelotas() {
        if (menuPelotasAbierto) cerrarMenuPelotas() else abrirMenuPelotas()
    }

    override fun alternarMenuParedes() {
        if (menuParedesAbierto) cerrarMenuParedes() else abrirMenuParedes()
    }

    override fun abrirMenuPelotas() {
        if (!puedeAbrirMenu()) return
        simulacion.abrirEdicion()
        cerrarMenuParedes()
        menuPelotasAbierto = true
        menus.marcarPelotaActual()
    }

    override fun abrirMenuParedes() {
        if (!puedeAbrirMenu()) return
        simulacion.abrirEdicion()
        cerrarMenuPelotas()
        menuParedesAbierto = true
        menus.marcarParedActual()
    }

    override fun abrirMenuFin() {
        menuFinAbierto = true
    }

    override fun cerrarMenuFin() {
        menuFinAbierto = false
    }

    override fun cerrarTodosLosMenus() {
        cerrarMenuPelotas()
        cerrarMenuParedes()
    }

    /** Cierra el menu que corresponda a ESC. Devuelve true si cerro alguno. */
    override fun cerrarConEscape(): Boolean = when {
        menuPelotasAbierto -> cerrarY(::cerrarMenuPelotas)
        menuParedesAbierto -> cerrarY(::cerrarMenuParedes)
        menuFinAbierto -> cerrarY(::cerrarMenuFin)
        else -> false
    }

    override fun sincronizarVisibilidad() {
        menus.menuPelotas().isVisible = menuPelotasAbierto
        menus.menuParedes().isVisible = menuParedesAbierto
        menus.menuPausa().isVisible = simulacion.pausado()
        menus.menuFin().isVisible = menuFinAbierto && !simulacion.pausado()
    }

    private fun puedeAbrirMenu(): Boolean = !enVuelo() && !menuFinAbierto

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
