package org.example.simulacion

import org.example.entrada.InputHandler
import org.example.entrada.InputListener
import org.example.fisica.Tierra
import org.example.modelo.Pared
import org.example.modelo.Proyectil
import org.example.modelo.TipoPelota
import org.example.modelo.TipoPared
import org.example.modelo.Vector2D
import org.example.modos.LecturaModo
import org.example.modos.ModoSimulacion

/**
 * Nucleo del simulador: coordina las reglas del tiro, la fisica, la camara y el
 * estado de la partida sin conocer ninguna presentacion concreta. Todas sus
 * dependencias se reciben ya construidas a traves de puertos (DIP); la unica
 * clase que conoce implementaciones concretas es [FabricaSimulacion].
 */
class Simulacion internal constructor(
    private val ancho: Double,
    private val alto: Double,
    componentes: ComponentesSimulacion
) : InputListener, ContextoJuego {

    private val entrada = componentes.control.entrada
    private val camara = componentes.fisica.camara
    private val motor = componentes.fisica.motor
    private val creadorParedes = componentes.fisica.creadorParedes
    private val fabricaModos = componentes.partida.fabricaModos
    private val sesion = componentes.partida.sesion
    private val metricas = componentes.partida.metricas
    private val efectos = componentes.partida.efectos
    private val superficies = componentes.partida.superficies
    private val controladorCamara = componentes.control.servicios.controladorCamara
    private val edicion = componentes.control.servicios.edicion
    private val previsualizador = componentes.control.servicios.previsualizador
    private val observadores = componentes.control.servicios.observadores
    private val posicionPelota = componentes.control.servicios.posicionPelota
    private val mecanica = componentes.control.servicios.mecanica
    private val comandosEdicion = componentes.control.comandos.edicion
    private val comandosPartida = componentes.control.comandos.partida

    constructor(
        ancho: Double = 1000.0,
        alto: Double = 650.0,
        entrada: InputHandler? = null
    ) : this(ancho, alto, FabricaSimulacion.componentes(ancho, alto, entrada))

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
    override fun pelota(): Proyectil = sesion.pelota()
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
