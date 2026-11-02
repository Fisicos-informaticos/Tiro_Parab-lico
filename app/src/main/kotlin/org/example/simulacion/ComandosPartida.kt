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
    private val sesion: SesionPartida,
    private val mecanica: MecanicaTiro,
    private val edicion: GestorEdicion,
    private val metricas: MetricasTiro,
    private val efectos: RegistroEfectos,
    private val superficies: SuperficiesLanzamiento,
    private val controladorCamara: ControladorCamara,
    private val motor: PhysicsEngine,
    private val fabricaModos: FabricaModos,
    private val fabricaAmbiente: FabricaAmbiente,
    private val ancho: Double,
    private val alto: Double,
    private val alCambiarEstado: (EstadoSimulacion) -> Unit
) {
    fun seleccionarPelota(tipo: TipoPelota) {
        if (!sesion.estado.permiteEditar()) return
        mecanica.cambiarPelota(tipo)
    }

    fun aplicarGravedad(nombre: String?) {
        val factor = Gravedades.factorDe(nombre) ?: return
        if (factor == sesion.factorGravedad) return
        sesion.factorGravedad = factor
        motor.cambiarAmbiente(crearAmbiente())
        reiniciarSiNoVuela()
    }

    fun seleccionarModo(nombre: String?) {
        val nuevo = fabricaModos.crear(nombre)
        if (nuevo.nombre() == sesion.modo.nombre()) return
        sesion.modo = nuevo
        reiniciarSiNoVuela()
    }

    fun nuevaPartida() {
        sesion.pausado = false
        edicion.desactivar()
        mecanica.reiniciar()
        controladorCamara.reiniciar()
        metricas.reiniciarPartida()
        efectos.limpiar()
        superficies.limpiar()
    }

    fun volverAlMenu() {
        nuevaPartida()
        alCambiarEstado(EstadoSimulacion.PantallaInicio)
    }

    private fun reiniciarSiNoVuela() {
        if (!sesion.estado.estaEnVuelo()) mecanica.reiniciar()
    }

    private fun crearAmbiente(): Ambiente {
        val gravedad = Vector2D(0.0, Tierra.GRAVEDAD_PIXELES * sesion.factorGravedad)
        return fabricaAmbiente.crear(ancho, alto, gravedad)
    }
}
