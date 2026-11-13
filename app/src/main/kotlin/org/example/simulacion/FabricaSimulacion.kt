package org.example.simulacion

import org.example.coordenadas.Camara
import org.example.coordenadas.CamaraSimulacion
import org.example.coordenadas.TransformadorCoordenadas
import org.example.coordenadas.TransformadorCoordenadasSimulacion
import org.example.entrada.DragInputHandler
import org.example.entrada.InputHandler
import org.example.fisica.Ambiente
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
        componentes(ancho, alto).crearSimulacion(ancho, alto)

    fun componentes(
        ancho: Double,
        alto: Double,
        entrada: InputHandler? = null
    ): ComponentesSimulacion {
        val entradaFinal = entrada ?: DragInputHandler()
        val fisica = crearFisica(ancho, alto)
        val partida = crearPartida(alto, fisica)
        val servicios = crearServicios(fisica, partida, ancho, alto, entradaFinal)
        val comandos = crearComandos(fisica, partida, servicios, ancho, alto, entradaFinal)

        return ComponentesSimulacion(
            entrada = entradaFinal,
            camara = fisica.camara,
            motor = fisica.motor,
            creadorParedes = fisica.creadorParedes,
            transformador = fisica.transformador,
            herramienta = fisica.herramienta,
            fabricaAmbiente = fisica.fabricaAmbiente,
            fabricaMotorFisico = fisica.fabricaMotorFisico,
            fabricaModos = partida.fabricaModos,
            sesion = partida.sesion,
            metricas = partida.metricas,
            efectos = partida.efectos,
            superficies = partida.superficies,
            controladorCamara = servicios.controladorCamara,
            edicion = servicios.edicion,
            previsualizador = servicios.previsualizador,
            observadores = servicios.observadores,
            posicionPelota = servicios.posicionPelota,
            mecanica = servicios.mecanica,
            comandosEdicion = comandos.edicion,
            comandosPartida = comandos.partida
        )
    }

    private fun crearFisica(ancho: Double, alto: Double): FisicaCableada {
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
        return FisicaCableada(
            camara,
            transformador,
            ambiente,
            fabricaAmbiente,
            fabricaMotorFisico,
            motor,
            creadorParedes,
            herramienta
        )
    }

    private fun crearPartida(alto: Double, fisica: FisicaCableada): PartidaCableada {
        val fabricaModos = CatalogoModos
        return PartidaCableada(
            sesion = SesionPartida(alto, TipoPelota.porDefecto(), fabricaModos.porDefecto()),
            metricas = MetricasTiro(),
            efectos = RegistroEfectos(),
            superficies = SuperficiesLanzamiento(),
            fabricaModos = fabricaModos
        )
    }

    private fun crearServicios(
        fisica: FisicaCableada,
        partida: PartidaCableada,
        ancho: Double,
        alto: Double,
        entrada: InputHandler
    ): ServiciosCableados {
        val controladorCamara = ControladorCamara(fisica.camara, ancho, alto)
        val edicion = GestorEdicion(fisica.herramienta, fisica.motor, partida.superficies)
        val previsualizador = PrevisualizadorTrayectoria(fisica.fabricaMotorFisico) {
            fisica.fabricaAmbiente.crear(
                ancho,
                alto,
                Vector2D(0.0, Tierra.GRAVEDAD_PIXELES * partida.sesion.factorGravedad())
            )
        }
        val integrador = IntegradorVuelo(fisica.motor, partida.metricas, partida.efectos)
        val posicionPelota = PosicionPelota(
            partida.sesion,
            fisica.motor,
            partida.superficies,
            fisica.transformador,
            fisica.camara,
            alto
        )
        val mecanica = MecanicaTiro(
            partida.sesion,
            entrada,
            partida.metricas,
            integrador,
            posicionPelota
        )
        return ServiciosCableados(
            controladorCamara,
            edicion,
            previsualizador,
            RegistroObservadoresFin(),
            posicionPelota,
            mecanica
        )
    }

    private fun crearComandos(
        fisica: FisicaCableada,
        partida: PartidaCableada,
        servicios: ServiciosCableados,
        ancho: Double,
        alto: Double,
        entrada: InputHandler
    ): ComandosCableados = ComandosCableados(
        edicion = ComandosEdicion(
            partida.sesion,
            servicios.edicion,
            entrada,
            fisica.motor,
            fisica.creadorParedes,
            partida.superficies
        ),
        partida = ComandosPartida(
            partida.sesion,
            servicios.mecanica,
            servicios.edicion,
            partida.metricas,
            partida.efectos,
            partida.superficies,
            servicios.controladorCamara,
            fisica.motor,
            partida.fabricaModos,
            fisica.fabricaAmbiente,
            ancho,
            alto
        )
    )

    private fun gravedadPorDefecto(): Vector2D = Vector2D(
        0.0,
        Tierra.GRAVEDAD_PIXELES * Simulacion.factorGravedadPorDefecto()
    )

    private class FisicaCableada(
        val camara: Camara,
        val transformador: TransformadorCoordenadas,
        val ambiente: Ambiente,
        val fabricaAmbiente: FabricaAmbiente,
        val fabricaMotorFisico: FabricaMotorFisico,
        val motor: PhysicsEngine,
        val creadorParedes: CreadorParedes,
        val herramienta: HerramientaParedes
    )

    private class PartidaCableada(
        val sesion: SesionJuego,
        val metricas: Metricas,
        val efectos: Efectos,
        val superficies: SuperficiesApoyo,
        val fabricaModos: FabricaModos
    )

    private class ServiciosCableados(
        val controladorCamara: ControlCamara,
        val edicion: EditorEscenario,
        val previsualizador: Previsualizador,
        val observadores: ObservadoresFin,
        val posicionPelota: UbicadorPelota,
        val mecanica: Mecanica
    )

    private class ComandosCableados(
        val edicion: ComandosEditor,
        val partida: ComandosJuego
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
    /** Ensambla el nucleo ya cableado sin que la fabrica conozca su constructor. */
    fun crearSimulacion(ancho: Double, alto: Double): Simulacion = Simulacion(ancho, alto, this)

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
