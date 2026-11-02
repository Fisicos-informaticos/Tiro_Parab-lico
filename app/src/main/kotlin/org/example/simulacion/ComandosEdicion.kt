package org.example.simulacion

import org.example.entrada.InputHandler
import org.example.fisica.PhysicsEngine
import org.example.modelo.Pared
import org.example.modelo.TipoPared
import org.example.modelo.creadores.CreadorParedes

/** Comandos de pausa y edicion del escenario. */
class ComandosEdicion(
    private val sesion: SesionPartida,
    private val edicion: GestorEdicion,
    private val entrada: InputHandler,
    private val motor: PhysicsEngine,
    private val creadorParedes: CreadorParedes,
    private val superficies: SuperficiesLanzamiento,
    private val alReiniciar: () -> Unit,
    private val alActivarHerramienta: () -> Unit,
    private val alAbrirEdicion: () -> Unit
) {
    fun alternarPausa() {
        if (!sesion.estado.permitePausa()) return
        sesion.pausado = !sesion.pausado
        if (sesion.pausado) desactivarEntrada()
    }

    fun alternarModoParedes() = alternarHerramienta(edicion::alternarParedes)

    fun alternarModoSuperficies() = alternarHerramienta(edicion::alternarSuperficies)

    private fun alternarHerramienta(alternar: () -> Boolean) {
        if (sesion.pausado || !sesion.estado.permiteEditar()) return
        if (alternar()) alActivarHerramienta()
    }

    fun agregarSuperficieLanzamiento(pared: Pared) {
        if (!sesion.estado.permiteEditar()) return
        superficies.agregar(pared)
    }

    fun limpiarSuperficiesLanzamiento() {
        if (!sesion.estado.permiteEditar()) return
        superficies.limpiar()
        alReiniciar()
    }

    fun seleccionarTipoPared(tipo: TipoPared) {
        if (!sesion.estado.permiteEditar()) return
        creadorParedes.seleccionarTipoPared(tipo)
        edicion.cancelarDibujo()
    }

    fun agregarPared(pared: Pared) {
        if (!sesion.estado.estaEnVuelo()) motor.agregarPared(pared)
    }

    fun limpiarParedes() {
        if (!sesion.estado.permiteEditar()) return
        motor.limpiarParedes()
    }

    fun abrirEdicion() {
        sesion.pausado = false
        edicion.desactivar()
        entrada.reiniciar()
        alAbrirEdicion()
    }

    private fun desactivarEntrada() {
        edicion.desactivar()
        entrada.reiniciar()
    }
}
