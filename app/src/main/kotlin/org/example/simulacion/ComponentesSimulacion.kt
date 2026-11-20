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
    private val camara: Camara,
    private val transformador: TransformadorCoordenadas,
    private val ambiente: Ambiente,
    private val fabricaAmbiente: FabricaAmbiente,
    private val fabricaMotorFisico: FabricaMotorFisico,
    private val motor: PhysicsEngine,
    private val creadorParedes: CreadorParedes,
    private val herramienta: HerramientaParedes
) {
    fun camara(): Camara = camara
    fun transformador(): TransformadorCoordenadas = transformador
    fun ambiente(): Ambiente = ambiente
    fun fabricaAmbiente(): FabricaAmbiente = fabricaAmbiente
    fun fabricaMotorFisico(): FabricaMotorFisico = fabricaMotorFisico
    fun motor(): PhysicsEngine = motor
    fun creadorParedes(): CreadorParedes = creadorParedes
    fun herramienta(): HerramientaParedes = herramienta
    fun describir(): String = "fisica(campos=${8})"
}

internal class ComponentesPartida(
    private val sesion: SesionJuego,
    private val metricas: Metricas,
    private val efectos: Efectos,
    private val superficies: SuperficiesApoyo,
    private val fabricaModos: FabricaModos
) {
    fun sesion(): SesionJuego = sesion
    fun metricas(): Metricas = metricas
    fun efectos(): Efectos = efectos
    fun superficies(): SuperficiesApoyo = superficies
    fun fabricaModos(): FabricaModos = fabricaModos
    fun describir(): String = "partida(campos=${5})"
}

internal class ServiciosCableados(
    private val controladorCamara: ControlCamara,
    private val edicion: EditorEscenario,
    private val previsualizador: Previsualizador,
    private val observadores: ObservadoresFin,
    private val posicionPelota: UbicadorPelota,
    private val mecanica: Mecanica
) {
    fun controladorCamara(): ControlCamara = controladorCamara
    fun edicion(): EditorEscenario = edicion
    fun previsualizador(): Previsualizador = previsualizador
    fun observadores(): ObservadoresFin = observadores
    fun posicionPelota(): UbicadorPelota = posicionPelota
    fun mecanica(): Mecanica = mecanica
    fun describir(): String = "servicios(campos=${6})"
}

internal class ComandosCableados(
    private val edicion: ComandosEditor,
    private val partida: ComandosJuego
) {
    fun edicion(): ComandosEditor = edicion
    fun partida(): ComandosJuego = partida
}

internal class ComponentesControl(
    private val entrada: InputHandler,
    private val servicios: ServiciosCableados,
    private val comandos: ComandosCableados
) {
    fun entrada(): InputHandler = entrada
    fun servicios(): ServiciosCableados = servicios
    fun comandos(): ComandosCableados = comandos
}

class ComponentesSimulacion internal constructor(
    private val fisica: ComponentesFisica,
    private val partida: ComponentesPartida,
    private val control: ComponentesControl
) {
    internal fun fisica(): ComponentesFisica = fisica
    internal fun partida(): ComponentesPartida = partida
    internal fun control(): ComponentesControl = control
    fun crearSimulacion(ancho: Double, alto: Double): Simulacion = Simulacion(ancho, alto, this)
    fun resumen(): String = listOf(fisica.describir(), partida.describir(), control.servicios().describir()).joinToString("; ")
}
