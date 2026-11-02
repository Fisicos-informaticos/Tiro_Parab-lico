package org.example.simulacion

import org.example.coordenadas.Camara
import org.example.coordenadas.TransformadorCoordenadas
import org.example.entrada.InputHandler
import org.example.entrada.InputListener
import org.example.fisica.Ambiente
import org.example.fisica.FabricaAmbiente
import org.example.fisica.FabricaMotorFisico
import org.example.fisica.Tierra
import org.example.fisica.PhysicsEngine
import org.example.herramientas.HerramientaParedes
import org.example.modelo.Pared
import org.example.modelo.Pelota
import org.example.modelo.TipoPelota
import org.example.modelo.TipoPared
import org.example.modelo.Vector2D
import org.example.modelo.creadores.CreadorParedes
import org.example.modos.FabricaModos
import org.example.modos.LecturaModo
import org.example.modos.ModoSimulacion

/**
 * Nucleo del simulador: coordina las reglas del tiro, la fisica, la camara y el
 * estado de la partida sin conocer ninguna presentacion concreta.
 */
class Simulacion(
    private val ancho: Double,
    private val alto: Double,
    private val entrada: InputHandler,
    private val camara: Camara,
    private val motor: PhysicsEngine,
    private val creadorParedes: CreadorParedes,
    private val transformador: TransformadorCoordenadas,
    private val herramientaCreacionParedes: HerramientaParedes,
    private val fabricaAmbiente: FabricaAmbiente,
    private val fabricaMotorFisico: FabricaMotorFisico,
    private val fabricaModos: FabricaModos
) : InputListener {

    constructor(
        ancho: Double = 1000.0,
        alto: Double = 650.0,
        entrada: InputHandler? = null
    ) : this(ancho, alto, FabricaSimulacion.componentes(ancho, alto, entrada))

    private constructor(
        ancho: Double,
        alto: Double,
        componentes: ComponentesSimulacion
    ) : this(
        ancho, alto, componentes.entrada, componentes.camara, componentes.motor,
        componentes.creadorParedes, componentes.transformador, componentes.herramienta,
        componentes.fabricaAmbiente, componentes.fabricaMotorFisico, componentes.fabricaModos
    )

    companion object {
        const val DT_FISICA = 1.0 / 60.0
        const val MAX_FUERZA = 500.0
        const val LONGITUD_MINIMA_PARED = 12.0
        const val GRAVEDAD_POR_DEFECTO = Gravedades.POR_DEFECTO
        const val DURACION_ROTURA = 0.45

        fun gravedades(): List<String> = Gravedades.nombres()

        fun factorGravedadDe(nombre: String): Double? = Gravedades.factorDe(nombre)

        fun factorGravedadPorDefecto(): Double = Gravedades.factorPorDefecto()
    }

    private val sesion = SesionPartida(alto, TipoPelota.porDefecto(), fabricaModos.porDefecto())
    private val metricas = MetricasTiro()
    private val efectos = RegistroEfectos()
    private val superficies = SuperficiesLanzamiento()
    private val controladorCamara = ControladorCamara(camara, ancho, alto)
    private val edicion = GestorEdicion(herramientaCreacionParedes, motor, superficies)
    private val previsualizador = PrevisualizadorTrayectoria(fabricaMotorFisico) { crearAmbiente() }
    private val observadores = RegistroObservadoresFin()
    private val integrador = IntegradorVuelo(motor, metricas, efectos)
    private val posicionPelota = PosicionPelota(sesion, motor, superficies, transformador, camara, alto)
    private val mecanica = MecanicaTiro(sesion, entrada, metricas, integrador, posicionPelota) {
        terminarVuelo()
    }
    private val comandosEdicion = ComandosEdicion(
        sesion, edicion, entrada, motor, creadorParedes, superficies,
        alReiniciar = { reiniciar() },
        alActivarHerramienta = { sesion.estado.alActivarHerramienta(this) },
        alAbrirEdicion = { sesion.estado.alAbrirEdicion(this) }
    )
    private val comandosPartida = ComandosPartida(
        sesion, mecanica, edicion, metricas, efectos, superficies, controladorCamara,
        motor, fabricaModos, fabricaAmbiente, ancho, alto
    ) { sesion.estado = it }

    init {
        seleccionarTipoPared(TipoPared.porDefecto())
    }

    fun ancho(): Double = ancho
    fun alto(): Double = alto
    fun entrada(): InputHandler = entrada
    fun estado(): EstadoSimulacion = sesion.estado
    fun pausado(): Boolean = sesion.pausado
    fun modoParedes(): Boolean = edicion.modoParedes()
    fun modoSuperficies(): Boolean = edicion.modoSuperficies()
    fun tipoPelota(): TipoPelota = sesion.tipoPelota
    fun tipoPared(): TipoPared = creadorParedes.tipoParedActual()
    fun factorGravedad(): Double = sesion.factorGravedad
    fun modo(): ModoSimulacion = sesion.modo
    fun nombresModos(): List<String> = fabricaModos.nombres()
    fun pelota(): Pelota = sesion.pelota
    fun offsetCamara(): Vector2D = camara.offsetCamara()
    fun tiempoVuelo(): Double = metricas.tiempoVuelo()
    fun distanciaRecorrida(): Double = metricas.distanciaRecorrida()
    fun alturaMaxima(): Double = metricas.alturaMaxima()
    fun distanciaX(): Double = metricas.distanciaX()
    fun paredesDestruidas(): Int = metricas.paredesDestruidas()
    fun paredTemporal(): Pared? = edicion.paredTemporal()
    fun paredes(): List<Pared> = motor.obtenerParedes()
    fun superficiesLanzamientoLista(): List<Pared> = superficies.lista()
    fun efectos(): List<EfectoRotura> = efectos.lista()
    fun sueloY(): Double = motor.sueloY()

    fun gravedad(): Vector2D = Vector2D(0.0, Tierra.GRAVEDAD_PIXELES * sesion.factorGravedad)

    override fun pulsar(punto: Vector2D) {
        if (edicion.activaCon(sesion.estado)) edicion.iniciar(punto)
        else sesion.estado.alPulsar(this, punto)
    }

    override fun arrastrar(punto: Vector2D) {
        if (edicion.activaCon(sesion.estado)) edicion.actualizar(punto)
        else sesion.estado.alArrastrar(this, punto)
    }

    override fun soltar(punto: Vector2D) {
        if (edicion.activaCon(sesion.estado)) edicion.finalizar(punto)
        else sesion.estado.alSoltar(this, punto)
    }

    fun actualizar(deltaTime: Double) {
        if (sesion.pausado) return
        controladorCamara.actualizar(deltaTime, entrada, sesion.pelota)
        efectos.avanzar(deltaTime)
        sesion.estado.alActualizar(this, deltaTime)
    }

    fun alternarPausa() = comandosEdicion.alternarPausa()
    fun alternarModoParedes() = comandosEdicion.alternarModoParedes()
    fun alternarModoSuperficies() = comandosEdicion.alternarModoSuperficies()
    fun agregarSuperficieLanzamiento(pared: Pared) = comandosEdicion.agregarSuperficieLanzamiento(pared)
    fun limpiarSuperficiesLanzamiento() = comandosEdicion.limpiarSuperficiesLanzamiento()
    fun seleccionarPelota(tipo: TipoPelota) = comandosPartida.seleccionarPelota(tipo)
    fun seleccionarTipoPared(tipo: TipoPared) = comandosEdicion.seleccionarTipoPared(tipo)
    fun aplicarGravedad(nombre: String?) = comandosPartida.aplicarGravedad(nombre)
    fun seleccionarModo(nombre: String?) = comandosPartida.seleccionarModo(nombre)
    fun agregarPared(pared: Pared) = comandosEdicion.agregarPared(pared)
    fun limpiarParedes() = comandosEdicion.limpiarParedes()
    fun abrirEdicion() = comandosEdicion.abrirEdicion()
    fun reiniciar() = mecanica.reiniciar()
    fun nuevaPartida() = comandosPartida.nuevaPartida()
    fun volverAlMenu() = comandosPartida.volverAlMenu()

    fun agregarObservadorFinSimulacion(observador: ObservadorFinSimulacion) =
        observadores.agregar(observador)

    fun quitarObservadorFinSimulacion(observador: ObservadorFinSimulacion) =
        observadores.quitar(observador)

    fun resultadoFinal(): ResultadoSimulacion = metricas.resultado()

    fun rompeParedSeleccionada(): Boolean = tipoPared().registraDanio(sesion.tipoPelota)

    fun etiquetaColision(): String = sesion.pelota.forma().tipo().etiqueta()

    fun lecturaModo(): LecturaModo = LecturaModo(
        posicion = sesion.pelota.posicion(),
        velocidad = sesion.pelota.velocidad(),
        distancia = metricas.distanciaRecorrida(),
        altura = posicionPelota.alturaActual(),
        tiempo = metricas.tiempoVuelo(),
        angulo = entrada.anguloLanzamiento(),
        fuerza = entrada.fuerzaLanzamiento()
    )

    fun previsualizarTrayectoria(): List<Vector2D> {
        if (!sesion.estado.permitePrevisualizar()) return emptyList()
        val condicion = sesion.modo.condicionDePrevia(entrada, sesion.pelota.posicion()) ?: return emptyList()
        return previsualizador.previsualizar(condicion, sesion.tipoPelota, motor.obtenerParedes())
    }

    internal fun cambiarA(nuevo: EstadoSimulacion) {
        sesion.estado = nuevo
    }

    internal fun colocarPelotaParaLanzar(punto: Vector2D) = mecanica.colocarPelotaParaLanzar(punto)
    internal fun iniciarApuntado(punto: Vector2D) = mecanica.iniciarApuntado(punto)
    internal fun continuarApuntado(punto: Vector2D) = mecanica.continuarApuntado(punto)
    internal fun lanzarPelota() = mecanica.lanzarPelota()
    internal fun avanzarVuelo(deltaTime: Double) = mecanica.avanzarVuelo(deltaTime)

    private fun terminarVuelo() {
        edicion.desactivar()
        entrada.reiniciar()
        cambiarA(EstadoSimulacion.EnSuelo)
        observadores.notificar(resultadoFinal())
    }

    private fun crearAmbiente(): Ambiente = fabricaAmbiente.crear(ancho, alto, gravedad())
}
