package org.example

import org.example.simulacion.Simulacion
import org.example.ui.EstadoPanel

/** Traduce la simulacion y los menus a la instantanea que consume el panel. */
class ConstructorEstadoPanel(
    private val simulacion: Simulacion,
    private val menus: GestorMenus
) {
    fun crear(): EstadoPanel = EstadoPanel(
        pausado = simulacion.pausado(),
        enVuelo = menus.enVuelo,
        enPantallaInicio = simulacion.estado().esPantallaInicio(),
        modoParedes = simulacion.modoParedes(),
        modoSuperficies = simulacion.modoSuperficies(),
        menuParedesAbierto = menus.menuParedesAbierto,
        menuPelotasAbierto = menus.menuPelotasAbierto,
        menuFinAbierto = menus.menuFinAbierto,
        nombreTipoPared = simulacion.tipoPared().nombre(),
        nombrePelota = simulacion.tipoPelota().nombre(),
        factorGravedad = simulacion.factorGravedad(),
        nombreModo = simulacion.modo().nombre(),
        ayudaModo = simulacion.modo().ayuda(),
        hayParedes = simulacion.paredes().isNotEmpty(),
        haySuperficies = simulacion.superficiesLanzamientoLista().isNotEmpty()
    )
}
