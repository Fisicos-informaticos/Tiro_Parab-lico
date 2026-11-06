package org.example.simulacion

import org.example.coordenadas.Camara
import org.example.entrada.InputHandler
import org.example.entrada.InputListener
import org.example.fisica.PhysicsEngine
import org.example.fisica.Tierra
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
 * estado de la partida sin conocer ninguna presentacion concreta. Todas sus
 * dependencias se reciben ya construidas a traves de puertos (DIP); la unica
 * clase que conoce implementaciones concretas es [FabricaSimulacion].
 */
class Simulacion(
    private val ancho: Double,
    private val alto: Double,
    private val entrada: InputHandler,
    private val camara: Camara,
    private val motor: PhysicsEngine,
    private val creadorParedes: CreadorParedes,
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
) : InputListener, ContextoJuego {

    constructor(
        ancho: Double = 1000.0,
        alto: Double = 650.0,
        entrada: InputHandler? = null
    ) : this(ancho, alto, FabricaSimulacion.componentes(ancho, alto, entrada))

    internal constructor(
        ancho: Double,
        alto: Double,
        componentes: ComponentesSimulacion
    ) : this(
        ancho, alto, componentes.entrada(), componentes.camara(), componentes.motor(),
        componentes.creadorParedes(), componentes.fabricaModos(), componentes.sesion(),
        componentes.metricas(), componentes.efectos(), componentes.superficies(),
        componentes.controladorCamara(), componentes.edicion(), componentes.previsualizador(),
        componentes.observadores(), componentes.posicionPelota(), componentes.mecanica(),
        componentes.comandosEdicion(), componentes.comandosPartida()
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

    init {
        seleccionarTipoPared(TipoPared.porDefecto())
    }

    override fun ancho(): Double = ancho
    override fun alto(): Double = alto
    override fun entrada(): InputHandler = entrada
    override fun estado(): EstadoSimulacion = sesion.estado()
    override fun pausado(): Boolean = sesion.pausado()
    override fun modoParedes(): Boolean = edicion.modoParedes()
    override fun modoSuperficies(): Boolean = edicion.modoSuperficies()
    override fun tipoPelota(): TipoPelota = sesion.tipoPelota()
    override fun tipoPared(): TipoPared = creadorParedes.tipoParedActual()
    override fun factorGravedad(): Double = sesion.factorGravedad()
    override fun modo(): ModoSimulacion = sesion.modo()
    override fun nombresModos(): List<String> = fabricaModos.nombres()
    override fun pelota(): Pelota = sesion.pelota()
    override fun offsetCamara(): Vector2D = camara.offsetCamara()
    override fun tiempoVuelo(): Double = metricas.tiempoVuelo()
    override fun distanciaRecorrida(): Double = metricas.distanciaRecorrida()
    override fun alturaMaxima(): Double = metricas.alturaMaxima()
    override fun distanciaX(): Double = metricas.distanciaX()
    override fun paredesDestruidas(): Int = metricas.paredesDestruidas()
    override fun paredTemporal(): Pared? = edicion.paredTemporal()
    override fun paredes(): List<Pared> = motor.obtenerParedes()
    override fun superficiesLanzamientoLista(): List<Pared> = superficies.lista()
    override fun efectos(): List<EfectoRotura> = efectos.lista()
    override fun sueloY(): Double = motor.sueloY()

    override fun gravedad(): Vector2D = Vector2D(0.0, Tierra.GRAVEDAD_PIXELES * sesion.factorGravedad())

    override fun pulsar(punto: Vector2D) {
        if (edicion.activaCon(sesion.estado())) edicion.iniciar(punto)
        else sesion.estado().alPulsar(this, punto)
    }

    override fun arrastrar(punto: Vector2D) {
        if (edicion.activaCon(sesion.estado())) edicion.actualizar(punto)
        else sesion.estado().alArrastrar(this, punto)
    }

    override fun soltar(punto: Vector2D) {
        if (edicion.activaCon(sesion.estado())) edicion.finalizar(punto)
        else sesion.estado().alSoltar(this, punto)
    }

    override fun actualizar(deltaTime: Double) {
        if (sesion.pausado()) return
        controladorCamara.actualizar(deltaTime, entrada, sesion.pelota())
        efectos.avanzar(deltaTime)
        sesion.estado().alActualizar(this, deltaTime)
    }

    override fun alternarPausa() = comandosEdicion.alternarPausa()

    override fun alternarModoParedes() {
        if (comandosEdicion.alternarModoParedes()) sesion.estado().alActivarHerramienta(this)
    }

    override fun alternarModoSuperficies() {
        if (comandosEdicion.alternarModoSuperficies()) sesion.estado().alActivarHerramienta(this)
    }

    override fun agregarSuperficieLanzamiento(pared: Pared) = comandosEdicion.agregarSuperficieLanzamiento(pared)

    override fun limpiarSuperficiesLanzamiento() {
        if (comandosEdicion.limpiarSuperficiesLanzamiento()) reiniciar()
    }

    override fun seleccionarPelota(tipo: TipoPelota) = comandosPartida.seleccionarPelota(tipo)
    override fun seleccionarTipoPared(tipo: TipoPared) = comandosEdicion.seleccionarTipoPared(tipo)
    override fun aplicarGravedad(nombre: String?) = comandosPartida.aplicarGravedad(nombre)
    override fun seleccionarModo(nombre: String?) = comandosPartida.seleccionarModo(nombre)
    override fun agregarPared(pared: Pared) = comandosEdicion.agregarPared(pared)
    override fun limpiarParedes() = comandosEdicion.limpiarParedes()

    override fun abrirEdicion() {
        comandosEdicion.abrirEdicion()
        sesion.estado().alAbrirEdicion(this)
    }

    override fun reiniciar() = mecanica.reiniciar()
    override fun nuevaPartida() = comandosPartida.nuevaPartida()
    override fun volverAlMenu() = comandosPartida.volverAlMenu()

    override fun agregarObservadorFinSimulacion(observador: ObservadorFinSimulacion) =
        observadores.agregar(observador)

    override fun quitarObservadorFinSimulacion(observador: ObservadorFinSimulacion) =
        observadores.quitar(observador)

    override fun resultadoFinal(): ResultadoSimulacion = metricas.resultado()

    override fun rompeParedSeleccionada(): Boolean = tipoPared().registraDanio(sesion.tipoPelota())

    override fun etiquetaColision(): String = sesion.pelota().forma().tipo().etiqueta()

    override fun lecturaModo(): LecturaModo = LecturaModo(
        posicion = sesion.pelota().posicion(),
        velocidad = sesion.pelota().velocidad(),
        distancia = metricas.distanciaRecorrida(),
        altura = posicionPelota.alturaActual(),
        tiempo = metricas.tiempoVuelo(),
        angulo = entrada.anguloLanzamiento(),
        fuerza = entrada.fuerzaLanzamiento()
    )

    override fun previsualizarTrayectoria(): List<Vector2D> {
        if (!sesion.estado().permitePrevisualizar()) return emptyList()
        val condicion = sesion.modo().condicionDePrevia(entrada, sesion.pelota().posicion()) ?: return emptyList()
        return previsualizador.previsualizar(condicion, sesion.tipoPelota(), motor.obtenerParedes())
    }

    override fun cambiarA(nuevo: EstadoSimulacion) {
        sesion.cambiarEstado(nuevo)
    }

    override fun colocarPelotaParaLanzar(punto: Vector2D) = mecanica.colocarPelotaParaLanzar(punto)
    override fun iniciarApuntado(punto: Vector2D) = mecanica.iniciarApuntado(punto)
    override fun continuarApuntado(punto: Vector2D) = mecanica.continuarApuntado(punto)
    override fun lanzarPelota() = mecanica.lanzarPelota()

    override fun avanzarVuelo(deltaTime: Double) {
        if (mecanica.avanzarVuelo(deltaTime)) terminarVuelo()
    }

    private fun terminarVuelo() {
        edicion.desactivar()
        entrada.reiniciar()
        cambiarA(EstadoSimulacion.EnSuelo)
        observadores.notificar(resultadoFinal())
    }
}
