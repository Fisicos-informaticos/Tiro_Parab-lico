package org.example

/**
 * Contrato de coordinacion de menus que consumen el detector de fin de vuelo y
 * el panel de estado. Depender de esta interfaz evita acoplarlos a la clase
 * concreta que mantiene la visibilidad en JavaFX.
 */
interface EstadoMenus {
    fun menuPelotasAbierto(): Boolean
    fun menuParedesAbierto(): Boolean
    fun menuFinAbierto(): Boolean
    fun interaccionBloqueada(): Boolean
    fun enVuelo(): Boolean
}

interface SeleccionMenus {
    fun alternarMenuPelotas()
    fun alternarMenuParedes()
    fun abrirMenuPelotas()
    fun abrirMenuParedes()
}

interface ControlCicloMenus {
    fun abrirMenuFin()
    fun cerrarMenuFin()
    fun cerrarTodosLosMenus()
    fun cerrarConEscape(): Boolean
    fun sincronizarVisibilidad()
}

interface CoordinadorMenus : EstadoMenus, SeleccionMenus, ControlCicloMenus

interface ControladorMenusRoles : EstadoMenus, ControlCicloMenus
