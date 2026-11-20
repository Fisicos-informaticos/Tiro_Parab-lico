package org.example.simulacion

import org.example.modelo.TipoPelota

internal class SimulacionAcciones(
    private val comandosPartida: ComandosJuego,
    private val mecanica: Mecanica
) {
    fun seleccionarPelota(tipo: TipoPelota) = comandosPartida.seleccionarPelota(tipo)
    fun aplicarGravedad(nombre: String?) = comandosPartida.aplicarGravedad(nombre)
    fun seleccionarModo(nombre: String?) = comandosPartida.seleccionarModo(nombre)
    fun reiniciar() = mecanica.reiniciar()
    fun nuevaPartida() = comandosPartida.nuevaPartida()
    fun volverAlMenu() = comandosPartida.volverAlMenu()
}
