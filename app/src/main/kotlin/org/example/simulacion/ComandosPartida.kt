package org.example.simulacion

import org.example.fisica.Ambiente
import org.example.fisica.FabricaAmbiente
import org.example.fisica.PhysicsEngine
import org.example.fisica.Tierra
import org.example.modelo.TipoPelota
import org.example.modelo.Vector2D
import org.example.modos.FabricaModos

/** Comandos que cambian el escenario o reinician la partida. */
class ComandosPartida(
    private val sesion: SesionJuego,
    private val mecanica: Mecanica,
    private val edicion: EditorEscenario,
    private val metricas: Metricas,
    private val efectos: Efectos,
    private val superficies: SuperficiesApoyo,
    private val controladorCamara: ControlCamara,
    private val motor: PhysicsEngine,
    private val fabricaModos: FabricaModos,
    private val fabricaAmbiente: FabricaAmbiente,
    private val ancho: Double,
    private val alto: Double
) : ComandosJuego {
    override fun seleccionarPelota(tipo: TipoPelota) {
        if (!sesion.estado().permiteEditar()) return
        mecanica.cambiarPelota(tipo)
    }

    override fun aplicarGravedad(nombre: String?) {
        val factor = Gravedades.factorDe(nombre) ?: return
        if (factor == sesion.factorGravedad()) return
        sesion.cambiarGravedad(factor)
        motor.cambiarAmbiente(crearAmbiente())
        reiniciarSiNoVuela()
    }

    override fun seleccionarModo(nombre: String?) {
        val nuevo = fabricaModos.crear(nombre)
        if (nuevo.nombre() == sesion.modo().nombre()) return
        sesion.cambiarModo(nuevo)
        reiniciarSiNoVuela()
    }

    override fun nuevaPartida() {
        sesion.reanudar()
        edicion.desactivar()
        mecanica.reiniciar()
        controladorCamara.reiniciar()
        metricas.reiniciarPartida()
        efectos.limpiar()
        superficies.limpiar()
    }

    override fun volverAlMenu() {
        nuevaPartida()
        sesion.cambiarEstado(EstadoSimulacion.PantallaInicio)
    }

    private fun reiniciarSiNoVuela() {
        if (!sesion.estado().estaEnVuelo()) mecanica.reiniciar()
    }

    private fun crearAmbiente(): Ambiente {
        val gravedad = Vector2D(0.0, Tierra.GRAVEDAD_PIXELES * sesion.factorGravedad())
        return fabricaAmbiente.crear(ancho, alto, gravedad)
    }
}
