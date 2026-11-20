package org.example.simulacion

import org.example.entrada.InputHandler
import org.example.entrada.InputListener
import org.example.modelo.Pared
import org.example.modelo.Proyectil
import org.example.modelo.TipoPelota
import org.example.modelo.TipoPared
import org.example.modelo.Vector2D
import org.example.modos.LecturaModo
import org.example.modos.ModoSimulacion

class Simulacion internal constructor(
    private val ancho: Double,
    private val alto: Double,
    componentes: ComponentesSimulacion
) : InputListener, ContextoJuego {

    private val entrada = componentes.control().entrada()
    private val sesion = componentes.partida().sesion()
    private val servicios = componentes.control().servicios()
    private val control = componentes.control()
    private val partida = componentes.partida()
    private val fisica = componentes.fisica()
    private val consultas: SimulacionConsultas
    private val ediciones: SimulacionEdicion
    private val acciones: SimulacionAcciones

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
        consultas = SimulacionConsultas(
            ancho, alto, entrada, sesion, servicios.edicion(),
            fisica.creadorParedes(), partida.fabricaModos(),
            fisica.camara(), partida.metricas(), partida.efectos(),
            partida.superficies(), fisica.motor(),
            servicios.posicionPelota(), servicios.previsualizador()
        )
        ediciones = SimulacionEdicion(
            servicios.edicion(), control.comandos().edicion(),
            servicios.mecanica(), entrada, servicios.observadores(),
            sesion
        )
        acciones = SimulacionAcciones(control.comandos().partida(), servicios.mecanica())
        seleccionarTipoPared(TipoPared.porDefecto())
    }

    override fun ancho(): Double = consultas.ancho()
    override fun alto(): Double = consultas.alto()
    override fun entrada(): InputHandler = consultas.entrada()
    override fun estado(): EstadoSimulacion = consultas.estado()
    override fun pausado(): Boolean = consultas.pausado()
    override fun modoParedes(): Boolean = consultas.modoParedes()
    override fun modoSuperficies(): Boolean = consultas.modoSuperficies()
    override fun tipoPelota(): TipoPelota = consultas.tipoPelota()
    override fun tipoPared(): TipoPared = consultas.tipoPared()
    override fun factorGravedad(): Double = consultas.factorGravedad()
    override fun modo(): ModoSimulacion = consultas.modo()
    override fun nombresModos(): List<String> = consultas.nombresModos()
    override fun pelota(): Proyectil = consultas.pelota()
    override fun offsetCamara(): Vector2D = consultas.offsetCamara()
    override fun tiempoVuelo(): Double = consultas.tiempoVuelo()
    override fun distanciaRecorrida(): Double = consultas.distanciaRecorrida()
    override fun alturaMaxima(): Double = consultas.alturaMaxima()
    override fun distanciaX(): Double = consultas.distanciaX()
    override fun paredesDestruidas(): Int = consultas.paredesDestruidas()
    override fun paredTemporal(): Pared? = consultas.paredTemporal()
    override fun paredes(): List<Pared> = consultas.paredes()
    override fun superficiesLanzamientoLista(): List<Pared> = consultas.superficiesLanzamientoLista()
    override fun efectos(): List<EfectoRotura> = consultas.efectos()
    override fun sueloY(): Double = consultas.sueloY()
    override fun gravedad(): Vector2D = consultas.gravedad()
    override fun pulsar(punto: Vector2D) {
        if (servicios.edicion().activaCon(sesion.estado())) servicios.edicion().iniciar(punto)
        else sesion.estado().alPulsar(this, punto)
    }
    override fun arrastrar(punto: Vector2D) {
        if (servicios.edicion().activaCon(sesion.estado())) servicios.edicion().actualizar(punto)
        else sesion.estado().alArrastrar(this, punto)
    }
    override fun soltar(punto: Vector2D) {
        if (servicios.edicion().activaCon(sesion.estado())) servicios.edicion().finalizar(punto)
        else sesion.estado().alSoltar(this, punto)
    }
    override fun actualizar(deltaTime: Double) {
        if (sesion.pausado()) return
        servicios.controladorCamara().actualizar(deltaTime, entrada, sesion.pelota())
        partida.efectos().avanzar(deltaTime)
        sesion.estado().alActualizar(this, deltaTime)
    }
    override fun alternarPausa() { ediciones.alternarPausa() }
    override fun alternarModoParedes() { ediciones.alternarModoParedes { sesion.estado().alActivarHerramienta(this) } }
    override fun alternarModoSuperficies() { ediciones.alternarModoSuperficies { sesion.estado().alActivarHerramienta(this) } }
    override fun agregarSuperficieLanzamiento(pared: Pared) = ediciones.agregarSuperficieLanzamiento(pared)
    override fun limpiarSuperficiesLanzamiento() { ediciones.limpiarSuperficiesLanzamiento(); acciones.reiniciar() }
    override fun seleccionarPelota(tipo: TipoPelota) = acciones.seleccionarPelota(tipo)
    override fun seleccionarTipoPared(tipo: TipoPared) = ediciones.seleccionarTipoPared(tipo)
    override fun aplicarGravedad(nombre: String?) = acciones.aplicarGravedad(nombre)
    override fun seleccionarModo(nombre: String?) = acciones.seleccionarModo(nombre)
    override fun agregarPared(pared: Pared) = ediciones.agregarPared(pared)
    override fun limpiarParedes() = ediciones.limpiarParedes()
    override fun abrirEdicion() = ediciones.abrirEdicion { sesion.estado().alAbrirEdicion(this) }
    override fun reiniciar() = acciones.reiniciar()
    override fun nuevaPartida() = acciones.nuevaPartida()
    override fun volverAlMenu() = acciones.volverAlMenu()
    override fun agregarObservadorFinSimulacion(observador: ObservadorFinSimulacion) = servicios.observadores().agregar(observador)
    override fun quitarObservadorFinSimulacion(observador: ObservadorFinSimulacion) = servicios.observadores().quitar(observador)
    override fun resultadoFinal(): ResultadoSimulacion = consultas.resultadoFinal()
    override fun rompeParedSeleccionada(): Boolean = consultas.rompeParedSeleccionada()
    override fun etiquetaColision(): String = consultas.etiquetaColision()
    override fun lecturaModo(): LecturaModo = consultas.lecturaModo()
    override fun previsualizarTrayectoria(): List<Vector2D> = consultas.previsualizarTrayectoria()
    override fun cambiarA(nuevo: EstadoSimulacion) { sesion.cambiarEstado(nuevo) }
    override fun colocarPelotaParaLanzar(punto: Vector2D) = servicios.mecanica().colocarPelotaParaLanzar(punto)
    override fun iniciarApuntado(punto: Vector2D) = servicios.mecanica().iniciarApuntado(punto)
    override fun continuarApuntado(punto: Vector2D) = servicios.mecanica().continuarApuntado(punto)
    override fun lanzarPelota() = servicios.mecanica().lanzarPelota()
    override fun avanzarVuelo(deltaTime: Double) { ediciones.avanzarVuelo(deltaTime) { sesion.cambiarEstado(EstadoSimulacion.EnSuelo); servicios.observadores().notificar(consultas.resultadoFinal()) } }
}
