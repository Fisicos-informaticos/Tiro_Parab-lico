package org.example.simulacion

import org.example.coordenadas.Camara
import org.example.coordenadas.CamaraSimulacion
import org.example.coordenadas.TransformadorCoordenadas
import org.example.coordenadas.TransformadorCoordenadasSimulacion
import org.example.entrada.DragInputHandler
import org.example.entrada.InputHandler
import org.example.fisica.FabricaAmbiente
import org.example.fisica.FabricaMotorFisico
import org.example.fisica.FabricaMotorFisicoPredeterminada
import org.example.fisica.FabricaTierra
import org.example.fisica.PhysicsEngine
import org.example.fisica.Tierra
import org.example.herramientas.HerramientaCreacionParedes
import org.example.herramientas.HerramientaParedes
import org.example.modelo.TipoPelota
import org.example.modelo.TipoPared
import org.example.modelo.Vector2D
import org.example.modelo.creadores.ConstructorParedes
import org.example.modelo.creadores.CreadorParedes
import org.example.modos.CatalogoModos
import org.example.modos.FabricaModos

/**
 * Punto de composicion del nucleo. Es el unico lugar que conoce las clases
 * concretas y se encarga de construirlas para que [Simulacion] reciba todo a
 * traves de sus interfaces.
 */
object FabricaSimulacion {

    fun crear(ancho: Double = 1000.0, alto: Double = 650.0): Simulacion =
        Simulacion(ancho, alto, componentes(ancho, alto))

    fun componentes(
        ancho: Double,
        alto: Double,
        entrada: InputHandler? = null
    ): ComponentesSimulacion {
        val camara = CamaraSimulacion(ancho, alto)
        val transformador = TransformadorCoordenadasSimulacion(camara)
        val fabricaAmbiente = FabricaTierra()
        val ambiente = fabricaAmbiente.crear(ancho, alto, gravedadPorDefecto())
        val fabricaMotorFisico = FabricaMotorFisicoPredeterminada()
        val motor = fabricaMotorFisico.crear(ambiente)
        val creadorParedes = ConstructorParedes(motor, TipoPared.porDefecto())
        val herramienta = HerramientaCreacionParedes(
            transformador,
            creadorParedes,
            Simulacion.LONGITUD_MINIMA_PARED
        )
        val entradaFinal = entrada ?: DragInputHandler()
        val fabricaModos = CatalogoModos

        val sesion = SesionPartida(alto, TipoPelota.porDefecto(), fabricaModos.porDefecto())
        val metricas = MetricasTiro()
        val efectos = RegistroEfectos()
        val superficies = SuperficiesLanzamiento()
        val controladorCamara = ControladorCamara(camara, ancho, alto)
        val edicion = GestorEdicion(herramienta, motor, superficies)
        val previsualizador = PrevisualizadorTrayectoria(fabricaMotorFisico) {
            fabricaAmbiente.crear(
                ancho,
                alto,
                Vector2D(0.0, Tierra.GRAVEDAD_PIXELES * sesion.factorGravedad())
            )
        }
        val observadores = RegistroObservadoresFin()
        val integrador = IntegradorVuelo(motor, metricas, efectos)
        val posicionPelota = PosicionPelota(sesion, motor, superficies, transformador, camara, alto)
        val mecanica = MecanicaTiro(sesion, entradaFinal, metricas, integrador, posicionPelota)
        val comandosEdicion = ComandosEdicion(
            sesion, edicion, entradaFinal, motor, creadorParedes, superficies
        )
        val comandosPartida = ComandosPartida(
            sesion, mecanica, edicion, metricas, efectos, superficies, controladorCamara,
            motor, fabricaModos, fabricaAmbiente, ancho, alto
        )

        return ComponentesSimulacion(
            entrada = entradaFinal,
            camara = camara,
            motor = motor,
            creadorParedes = creadorParedes,
            transformador = transformador,
            herramienta = herramienta,
            fabricaAmbiente = fabricaAmbiente,
            fabricaMotorFisico = fabricaMotorFisico,
            fabricaModos = fabricaModos,
            sesion = sesion,
            metricas = metricas,
            efectos = efectos,
            superficies = superficies,
            controladorCamara = controladorCamara,
            edicion = edicion,
            previsualizador = previsualizador,
            observadores = observadores,
            posicionPelota = posicionPelota,
            mecanica = mecanica,
            comandosEdicion = comandosEdicion,
            comandosPartida = comandosPartida
        )
    }

    private fun gravedadPorDefecto(): Vector2D = Vector2D(
        0.0,
        Tierra.GRAVEDAD_PIXELES * Simulacion.factorGravedadPorDefecto()
    )
}

/**
 * Dependencias ya cableadas que comparten la construccion de conveniencia y la
 * fabrica. Su estado es privado y solo se entrega a traves de metodos, de modo
 * que el punto de composicion tampoco expone campos manipulables.
 */
class ComponentesSimulacion(
    private val entrada: InputHandler,
    private val camara: Camara,
    private val motor: PhysicsEngine,
    private val creadorParedes: CreadorParedes,
    private val transformador: TransformadorCoordenadas,
    private val herramienta: HerramientaParedes,
    private val fabricaAmbiente: FabricaAmbiente,
    private val fabricaMotorFisico: FabricaMotorFisico,
    private val fabricaModos: FabricaModos,
    private val sesion: SesionJuego,
    private val metricas: Metricas,
    private val efectos: Efectos,
    private val superficies: SuperficiesApoyo,
    private val controladorCamara: ControlCamara,
    private val edicion: EditorEscenario,
    private val previsualizador: Previsualizador,
    private val observadores: ObservadoresFin,
    private val posicionPelota: UbicadorPelota,
    private val mecanica: Mecanica,
    private val comandosEdicion: ComandosEditor,
    private val comandosPartida: ComandosJuego
) {
    fun entrada(): InputHandler = entrada

    fun camara(): Camara = camara

    fun motor(): PhysicsEngine = motor

    fun creadorParedes(): CreadorParedes = creadorParedes

    fun transformador(): TransformadorCoordenadas = transformador

    fun herramienta(): HerramientaParedes = herramienta

    fun fabricaAmbiente(): FabricaAmbiente = fabricaAmbiente

    fun fabricaMotorFisico(): FabricaMotorFisico = fabricaMotorFisico

    fun fabricaModos(): FabricaModos = fabricaModos

    fun sesion(): SesionJuego = sesion

    fun metricas(): Metricas = metricas

    fun efectos(): Efectos = efectos

    fun superficies(): SuperficiesApoyo = superficies

    fun controladorCamara(): ControlCamara = controladorCamara

    fun edicion(): EditorEscenario = edicion

    fun previsualizador(): Previsualizador = previsualizador

    fun observadores(): ObservadoresFin = observadores

    fun posicionPelota(): UbicadorPelota = posicionPelota

    fun mecanica(): Mecanica = mecanica

    fun comandosEdicion(): ComandosEditor = comandosEdicion

    fun comandosPartida(): ComandosJuego = comandosPartida
}
