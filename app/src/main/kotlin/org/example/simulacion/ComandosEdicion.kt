package org.example.simulacion

import org.example.entrada.InputHandler
import org.example.fisica.PhysicsEngine
import org.example.modelo.Pared
import org.example.modelo.TipoPared
import org.example.modelo.creadores.CreadorParedes

/** Comandos de pausa y edicion del escenario. */
class ComandosEdicion(
    private val sesion: SesionJuego,
    private val edicion: EditorEscenario,
    private val entrada: InputHandler,
    private val motor: PhysicsEngine,
    private val creadorParedes: CreadorParedes,
    private val superficies: SuperficiesApoyo
) : ComandosEditor {
    override fun alternarPausa() {
        if (!sesion.estado().permitePausa()) return
        if (sesion.alternarPausa()) desactivarEntrada()
    }

    override fun alternarModoParedes(): Boolean = alternarHerramienta(edicion::alternarParedes)

    override fun alternarModoSuperficies(): Boolean = alternarHerramienta(edicion::alternarSuperficies)

    override fun agregarSuperficieLanzamiento(pared: Pared) {
        if (!sesion.estado().permiteEditar()) return
        superficies.agregar(pared)
    }

    override fun limpiarSuperficiesLanzamiento(): Boolean {
        if (!sesion.estado().permiteEditar()) return false
        superficies.limpiar()
        return true
    }

    override fun seleccionarTipoPared(tipo: TipoPared) {
        if (!sesion.estado().permiteEditar()) return
        creadorParedes.seleccionarTipoPared(tipo)
        edicion.cancelarDibujo()
    }

    override fun agregarPared(pared: Pared) {
        if (!sesion.estado().estaEnVuelo()) motor.agregarPared(pared)
    }

    override fun limpiarParedes() {
        if (!sesion.estado().permiteEditar()) return
        motor.limpiarParedes()
    }

    override fun abrirEdicion() {
        sesion.reanudar()
        edicion.desactivar()
        entrada.reiniciar()
    }

    private fun alternarHerramienta(alternar: () -> Boolean): Boolean {
        if (sesion.pausado() || !sesion.estado().permiteEditar()) return false
        return alternar()
    }

    private fun desactivarEntrada() {
        edicion.desactivar()
        entrada.reiniciar()
    }
}
