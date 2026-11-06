package org.example

import org.example.simulacion.ContextoJuego
import org.example.ui.MenuJuego

/** Abre o cierra el menu final justo cuando cambia el estado de la partida. */
class DetectorFinDeVuelo(
    private val simulacion: ContextoJuego,
    private val menus: CoordinadorMenus,
    private val menusJuego: MenuJuego
) {
    private var estadoPrevio = simulacion.estado()

    fun detectar() {
        val estado = simulacion.estado()
        if (estado === estadoPrevio) return
        estadoPrevio = estado

        if (!estado.esEnSuelo()) {
            menus.cerrarMenuFin()
            return
        }
        menus.abrirMenuFin()
        menusJuego.mostrarResumenFin(
            "Distancia: ${"%.2f".format(simulacion.distanciaRecorrida())} px · " +
                "Tiempo: ${"%.2f".format(simulacion.tiempoVuelo())} s"
        )
    }

    fun reiniciarSeguimiento() {
        estadoPrevio = simulacion.estado()
    }
}
