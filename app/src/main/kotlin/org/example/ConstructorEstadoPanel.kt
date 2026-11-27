package org.example

import org.example.simulacion.LecturaConfiguracionJuego
import org.example.simulacion.LecturaEscenarioJuego
import org.example.simulacion.LecturaEstadoJuego
import org.example.simulacion.LecturaPanelJuego
import org.example.ui.EstadoPanel
import org.example.EstadoMenus

/** Traduce la simulacion y los menus a la instantanea que consume el panel. */
class ConstructorEstadoPanel(
    private val simulacion: LecturaPanelJuego,
    private val menus: EstadoMenus
) {
    fun crear(): EstadoPanel = EstadoPanel(
        pausado = simulacion.pausado(),
        enVuelo = menus.enVuelo(),
        enPantallaInicio = simulacion.estado().esPantallaInicio(),
        modoParedes = simulacion.modoParedes(),
        modoSuperficies = simulacion.modoSuperficies(),
        menuParedesAbierto = menus.menuParedesAbierto(),
        menuPelotasAbierto = menus.menuPelotasAbierto(),
        menuFinAbierto = menus.menuFinAbierto(),
        nombreTipoPared = simulacion.tipoPared().nombre(),
        nombrePelota = simulacion.tipoPelota().nombre(),
        factorGravedad = simulacion.factorGravedad(),
        nombreModo = simulacion.modo().nombre(),
        ayudaModo = simulacion.modo().ayuda(),
        hayParedes = simulacion.paredes().isNotEmpty(),
        haySuperficies = simulacion.superficiesLanzamientoLista().isNotEmpty()
    )
}
