package org.example.simulacion

import org.example.coordenadas.Camara
import org.example.coordenadas.TransformadorCoordenadas
import org.example.entrada.InputHandler
import org.example.fisica.Ambiente
import org.example.modelo.Proyectil
import org.example.modelo.Vector2D
import org.example.fisica.FabricaAmbiente
import org.example.fisica.FabricaMotorFisico
import org.example.fisica.PhysicsEngine
import org.example.herramientas.HerramientaParedes
import org.example.modelo.creadores.CreadorParedes
import org.example.modos.FabricaModos

internal class ComponentesFisica(
    camara: Camara,
    transformador: TransformadorCoordenadas,
    ambiente: Ambiente,
    fabricaAmbiente: FabricaAmbiente,
    fabricaMotorFisico: FabricaMotorFisico,
    motor: PhysicsEngine,
    creadorParedes: CreadorParedes,
    herramienta: HerramientaParedes
) {
    private val datos = DatosComponentesFisica(
        camara,
        transformador,
        ambiente,
        fabricaAmbiente,
        fabricaMotorFisico,
        motor,
        creadorParedes,
        herramienta
    )

    fun camara(): Camara = datos.camara
    fun transformador(): TransformadorCoordenadas = datos.transformador
    fun ambiente(): Ambiente = datos.ambiente
    fun fabricaAmbiente(): FabricaAmbiente = datos.fabricaAmbiente
    fun fabricaMotorFisico(): FabricaMotorFisico = datos.fabricaMotorFisico
    fun motor(): PhysicsEngine = datos.motor
    fun creadorParedes(): CreadorParedes = datos.creadorParedes
    fun herramienta(): HerramientaParedes = datos.herramienta
    fun describir(): String = datos.describir()

    private data class DatosComponentesFisica(
        val camara: Camara,
        val transformador: TransformadorCoordenadas,
        val ambiente: Ambiente,
        val fabricaAmbiente: FabricaAmbiente,
        val fabricaMotorFisico: FabricaMotorFisico,
        val motor: PhysicsEngine,
        val creadorParedes: CreadorParedes,
        val herramienta: HerramientaParedes
    ) {
        fun describir(): String =
            "fisica(camara=${camara.javaClass.simpleName}, motor=${motor.javaClass.simpleName}, herramienta=${herramienta.javaClass.simpleName})"

        fun dependencias(): List<Any> =
            listOf(
                camara,
                transformador,
                ambiente,
                fabricaAmbiente,
                fabricaMotorFisico,
                motor,
                creadorParedes,
                herramienta
            )

        fun esCompleta(): Boolean =
            camara.javaClass !== transformador.javaClass && ambiente.javaClass !== motor.javaClass && fabricaAmbiente.javaClass !== fabricaMotorFisico.javaClass && creadorParedes.javaClass !== herramienta.javaClass

        fun resumen(): String = dependencias().joinToString("/") { it.javaClass.simpleName }
    }
}

internal class ComponentesPartida(
    sesion: SesionJuego,
    metricas: Metricas,
    efectos: Efectos,
    superficies: SuperficiesApoyo,
    fabricaModos: FabricaModos
) {
    private val datos = DatosComponentesPartida(sesion, metricas, efectos, superficies, fabricaModos)

    fun sesion(): SesionJuego = datos.sesion
    fun metricas(): Metricas = datos.metricas
    fun efectos(): Efectos = datos.efectos
    fun superficies(): SuperficiesApoyo = datos.superficies
    fun fabricaModos(): FabricaModos = datos.fabricaModos
    fun describir(): String = datos.describir()

    private data class DatosComponentesPartida(
        val sesion: SesionJuego,
        val metricas: Metricas,
        val efectos: Efectos,
        val superficies: SuperficiesApoyo,
        val fabricaModos: FabricaModos
    ) {
        fun describir(): String =
            "partida(sesion=${sesion.javaClass.simpleName}, metricas=${metricas.javaClass.simpleName}, efectos=${efectos.javaClass.simpleName}, superficies=${superficies.javaClass.simpleName}, modos=${fabricaModos.javaClass.simpleName})"

        fun dependencias(): List<Any> = listOf(sesion, metricas, efectos, superficies, fabricaModos)

        fun estado(): String = dependencias().joinToString("/") { it.javaClass.simpleName }

        fun esCompleta(): Boolean =
            sesion.javaClass !== metricas.javaClass && metricas.javaClass !== efectos.javaClass && efectos.javaClass !== superficies.javaClass && superficies.javaClass !== fabricaModos.javaClass
    }
}

internal class ServiciosCableados(
    controladorCamara: ControlCamara,
    edicion: EditorEscenario,
    previsualizador: Previsualizador,
    observadores: ObservadoresFin,
    posicionPelota: UbicadorPelota,
    mecanica: Mecanica
) {
    private val datos = DatosServiciosCableados(
        controladorCamara,
        edicion,
        previsualizador,
        observadores,
        posicionPelota,
        mecanica
    )

    fun controladorCamara(): ControlCamara = datos.controladorCamara
    fun edicion(): EditorEscenario = datos.edicion
    fun previsualizador(): Previsualizador = datos.previsualizador
    fun observadores(): ObservadoresFin = datos.observadores
    fun posicionPelota(): UbicadorPelota = datos.posicionPelota
    fun mecanica(): Mecanica = datos.mecanica
    fun describir(): String = datos.describir()

    fun edicionActivaCon(estado: EstadoSimulacion): Boolean = datos.edicion.activaCon(estado)
    fun iniciarEdicion(punto: Vector2D) = datos.edicion.iniciar(punto)
    fun actualizarEdicion(punto: Vector2D) = datos.edicion.actualizar(punto)
    fun finalizarEdicion(punto: Vector2D) = datos.edicion.finalizar(punto)
    fun actualizarCamara(deltaTime: Double, entrada: InputHandler, pelota: Proyectil) =
        datos.controladorCamara.actualizar(deltaTime, entrada, pelota)
    fun observadoresAgregar(observador: ObservadorFinSimulacion) = datos.observadores.agregar(observador)
    fun observadoresQuitar(observador: ObservadorFinSimulacion) = datos.observadores.quitar(observador)
    fun notificarFin(resultado: ResultadoSimulacion) = datos.observadores.notificar(resultado)
    fun colocarPelotaParaLanzar(punto: Vector2D) = datos.mecanica.colocarPelotaParaLanzar(punto)
    fun iniciarApuntado(punto: Vector2D) = datos.mecanica.iniciarApuntado(punto)
    fun continuarApuntado(punto: Vector2D) = datos.mecanica.continuarApuntado(punto)
    fun lanzarPelota() = datos.mecanica.lanzarPelota()

    private data class DatosServiciosCableados(
        val controladorCamara: ControlCamara,
        val edicion: EditorEscenario,
        val previsualizador: Previsualizador,
        val observadores: ObservadoresFin,
        val posicionPelota: UbicadorPelota,
        val mecanica: Mecanica
    ) {
        fun describir(): String =
            "servicios(camara=${controladorCamara.javaClass.simpleName}, edicion=${edicion.javaClass.simpleName}, previsualizador=${previsualizador.javaClass.simpleName}, observadores=${observadores.javaClass.simpleName}, posicion=${posicionPelota.javaClass.simpleName}, mecanica=${mecanica.javaClass.simpleName})"

        fun dependencias(): List<Any> =
            listOf(controladorCamara, edicion, previsualizador, observadores, posicionPelota, mecanica)

        fun estado(): String = dependencias().joinToString("/") { it.javaClass.simpleName }

        fun esCompleto(): Boolean =
            controladorCamara.javaClass !== edicion.javaClass && edicion.javaClass !== previsualizador.javaClass && previsualizador.javaClass !== observadores.javaClass &&
                observadores.javaClass !== posicionPelota.javaClass && posicionPelota.javaClass !== mecanica.javaClass
    }
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
