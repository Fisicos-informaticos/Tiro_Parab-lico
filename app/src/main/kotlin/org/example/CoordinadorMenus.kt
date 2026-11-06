package org.example

/**
 * Contrato de coordinacion de menus que consumen el detector de fin de vuelo y
 * el panel de estado. Depender de esta interfaz evita acoplarlos a la clase
 * concreta que mantiene la visibilidad en JavaFX.
 */
interface CoordinadorMenus {
    fun menuPelotasAbierto(): Boolean
    fun menuParedesAbierto(): Boolean
    fun menuFinAbierto(): Boolean
    fun interaccionBloqueada(): Boolean
    fun enVuelo(): Boolean
    fun alternarMenuPelotas()
    fun alternarMenuParedes()
    fun abrirMenuPelotas()
    fun abrirMenuParedes()
    fun abrirMenuFin()
    fun cerrarMenuFin()
    fun cerrarTodosLosMenus()
    fun cerrarConEscape(): Boolean
    fun sincronizarVisibilidad()
}
