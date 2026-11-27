package org.example.ui

import org.example.modelo.TipoPelota
import org.example.modelo.TipoPared

/**
 * Contrato de comunicacion entre la interfaz y el motor del juego.
 *
 * La vista (panel de control) depende solo de esta abstraccion y nunca de
 * [org.example.simulacion.Simulacion]; quien la implementa es el que conoce la
 * logica concreta (DIP). Asi se pueden cambiar ambas partes por separado.
 */
interface AccionesJuego {
    fun alternarPausa()
    fun reiniciar()
    fun volverAlMenu()
    fun alternarMenuParedes()
    fun alternarMenuPelotas()
}

interface ConfiguracionJuego {
    fun seleccionarModo(nombre: String?)
    fun aplicarGravedad(nombre: String?)
    fun seleccionarPelota(tipo: TipoPelota)
    fun seleccionarTipoPared(tipo: TipoPared)
}

interface EdicionEscenario {
    fun alternarModoParedes()
    fun limpiarParedes()
    fun alternarModoSuperficies()
    fun limpiarSuperficiesLanzamiento()
}
