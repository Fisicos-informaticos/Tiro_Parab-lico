package org.example.simulacion

import org.example.modelo.Pared
import org.example.modelo.TipoPared

internal class SimulacionEdicion(
    private val edicion: EditorEscenario,
    private val comandosEdicion: ComandosEditor,
    private val mecanica: Mecanica,
    private val entrada: org.example.entrada.InputHandler,
    private val observadores: ObservadoresFin,
    private val sesion: EstadoSesionJuego
) {
    fun modoParedes(): Boolean = edicion.modoParedes()
    fun modoSuperficies(): Boolean = edicion.modoSuperficies()
    fun paredTemporal(): Pared? = edicion.paredTemporal()
    fun alternarPausa() { comandosEdicion.alternarPausa() }

    fun alternarModoParedes(onActivar: () -> Unit) {
        if (comandosEdicion.alternarModoParedes()) onActivar()
    }

    fun alternarModoSuperficies(onActivar: () -> Unit) {
        if (comandosEdicion.alternarModoSuperficies()) onActivar()
    }

    fun agregarSuperficieLanzamiento(pared: Pared) = comandosEdicion.agregarSuperficieLanzamiento(pared)
    fun limpiarSuperficiesLanzamiento() { comandosEdicion.limpiarSuperficiesLanzamiento() }
    fun seleccionarTipoPared(tipo: TipoPared) = comandosEdicion.seleccionarTipoPared(tipo)
    fun agregarPared(pared: Pared) = comandosEdicion.agregarPared(pared)
    fun limpiarParedes() = comandosEdicion.limpiarParedes()

    fun abrirEdicion(onAbrir: () -> Unit) {
        comandosEdicion.abrirEdicion()
        onAbrir()
    }

    fun avanzarVuelo(deltaTime: Double, onTerminar: () -> Unit): Boolean {
        if (mecanica.avanzarVuelo(deltaTime)) {
            edicion.desactivar()
            entrada.reiniciar()
            onTerminar()
            return true
        }
        return false
    }
}
