package org.example.simulacion

import org.example.coordenadas.Camara
import org.example.coordenadas.TransformadorCoordenadas
import org.example.entrada.InputHandler
import org.example.fisica.Ambiente
import org.example.fisica.FabricaAmbiente
import org.example.fisica.FabricaMotorFisico
import org.example.fisica.PhysicsEngine
import org.example.herramientas.HerramientaParedes
import org.example.modelo.creadores.CreadorParedes
import org.example.modos.FabricaModos

internal class ComponentesFisica(
    val camara: Camara,
    val transformador: TransformadorCoordenadas,
    val ambiente: Ambiente,
    val fabricaAmbiente: FabricaAmbiente,
    val fabricaMotorFisico: FabricaMotorFisico,
    val motor: PhysicsEngine,
    val creadorParedes: CreadorParedes,
    val herramienta: HerramientaParedes
)

internal class ComponentesPartida(
    val sesion: SesionJuego,
    val metricas: Metricas,
    val efectos: Efectos,
    val superficies: SuperficiesApoyo,
    val fabricaModos: FabricaModos
)

internal class ServiciosCableados(
    val controladorCamara: ControlCamara,
    val edicion: EditorEscenario,
    val previsualizador: Previsualizador,
    val observadores: ObservadoresFin,
    val posicionPelota: UbicadorPelota,
    val mecanica: Mecanica
)

internal class ComandosCableados(
    val edicion: ComandosEditor,
    val partida: ComandosJuego
)

internal class ComponentesControl(
    val entrada: InputHandler,
    val servicios: ServiciosCableados,
    val comandos: ComandosCableados
)

class ComponentesSimulacion internal constructor(
    internal val fisica: ComponentesFisica,
    internal val partida: ComponentesPartida,
    internal val control: ComponentesControl
) {
    fun crearSimulacion(ancho: Double, alto: Double): Simulacion = Simulacion(ancho, alto, this)
}
