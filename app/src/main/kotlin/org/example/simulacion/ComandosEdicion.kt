package org.example.simulacion

import org.example.entrada.InputHandler
import org.example.fisica.GestionParedesFisicas
import org.example.modelo.Pared
import org.example.modelo.TipoPared
import org.example.modelo.creadores.CreadorParedes

/** Comandos de pausa y edicion del escenario. */
class ComandosEdicion(
    private val sesion: EstadoSesionJuego,
    private val edicion: EditorEscenario,
    private val entrada: InputHandler,
    private val motor: GestionParedesFisicas,
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
        if (!permiteEditar()) return
        superficies.agregar(pared)
    }

    override fun limpiarSuperficiesLanzamiento(): Boolean {
        if (!permiteEditar()) return false
        superficies.limpiar()
        return true
    }

    override fun seleccionarTipoPared(tipo: TipoPared) {
        if (!permiteEditar()) return
        creadorParedes.seleccionarTipoPared(tipo)
        edicion.cancelarDibujo()
    }

    override fun agregarPared(pared: Pared) {
        if (!sesion.estado().estaEnVuelo()) motor.agregarPared(pared)
    }

    override fun limpiarParedes() {
        if (!permiteEditar()) return
        motor.limpiarParedes()
    }

    override fun abrirEdicion() {
        sesion.reanudar()
        edicion.desactivar()
        entrada.reiniciar()
    }

    private fun alternarHerramienta(alternar: () -> Boolean): Boolean {
        if (sesion.pausado() || !permiteEditar()) return false
        return alternar()
    }

    private fun permiteEditar(): Boolean = sesion.estado().permiteEditar()

    private fun desactivarEntrada() {
        edicion.desactivar()
        entrada.reiniciar()
    }
}
