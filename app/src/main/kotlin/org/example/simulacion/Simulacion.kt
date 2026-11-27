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

internal class ContextoSimulacion(
    val ancho: Double,
    val alto: Double,
    val entrada: InputHandler,
    val sesion: SesionJuego,
    val servicios: ServiciosCableados,
    val control: ComponentesControl,
    val partida: ComponentesPartida,
    val fisica: ComponentesFisica,
    val consultas: SimulacionConsultas,
    val ediciones: SimulacionEdicion,
    val acciones: SimulacionAcciones
)

class Simulacion internal constructor(
    ancho: Double,
    alto: Double,
    componentes: ComponentesSimulacion
) : InputListener, ContextoJuego {
    private val datos = DatosSimulacion(ancho, alto, componentes)

    override fun ancho(): Double = datos.ancho

    override fun alto(): Double = datos.alto

    override fun entrada(): InputHandler = datos.entrada

    override fun estado(): EstadoSimulacion = datos.estado()

    override fun pausado(): Boolean = datos.pausado()

    override fun modoParedes(): Boolean = datos.modoParedes()

    override fun modoSuperficies(): Boolean = datos.modoSuperficies()

    override fun tipoPelota(): TipoPelota = datos.tipoPelota()

    override fun tipoPared(): TipoPared = datos.tipoPared()

    override fun factorGravedad(): Double = datos.factorGravedad()

    override fun modo(): ModoSimulacion = datos.modo()

    override fun nombresModos(): List<String> = datos.nombresModos()

    override fun pelota(): Proyectil = datos.pelota()

    override fun offsetCamara(): Vector2D = datos.offsetCamara()

    override fun tiempoVuelo(): Double = datos.tiempoVuelo()

    override fun distanciaRecorrida(): Double = datos.distanciaRecorrida()

    override fun alturaMaxima(): Double = datos.alturaMaxima()

    override fun distanciaX(): Double = datos.distanciaX()

    override fun paredesDestruidas(): Int = datos.paredesDestruidas()

    override fun paredTemporal(): Pared? = datos.paredTemporal()

    override fun paredes(): List<Pared> = datos.paredes()

    override fun superficiesLanzamientoLista(): List<Pared> = datos.superficiesLanzamientoLista()

    override fun efectos(): List<EfectoRotura> = datos.efectos()

    override fun sueloY(): Double = datos.sueloY()

    override fun gravedad(): Vector2D = datos.gravedad()

    override fun pulsar(punto: Vector2D) = datos.pulsar(this, punto)

    override fun arrastrar(punto: Vector2D) = datos.arrastrar(this, punto)

    override fun soltar(punto: Vector2D) = datos.soltar(this, punto)

    override fun actualizar(deltaTime: Double) = datos.actualizar(this, deltaTime)

    override fun alternarPausa() = datos.alternarPausa()

    override fun alternarModoParedes() = datos.alternarModoParedes(this)

    override fun alternarModoSuperficies() = datos.alternarModoSuperficies(this)

    override fun agregarSuperficieLanzamiento(pared: Pared) = datos.agregarSuperficieLanzamiento(pared)

    override fun limpiarSuperficiesLanzamiento() = datos.limpiarSuperficiesLanzamiento()

    override fun seleccionarPelota(tipo: TipoPelota) = datos.seleccionarPelota(tipo)

    override fun seleccionarTipoPared(tipo: TipoPared) = datos.seleccionarTipoPared(tipo)

    override fun aplicarGravedad(nombre: String?) = datos.aplicarGravedad(nombre)

    override fun seleccionarModo(nombre: String?) = datos.seleccionarModo(nombre)

    override fun agregarPared(pared: Pared) = datos.agregarPared(pared)

    override fun limpiarParedes() = datos.limpiarParedes()

    override fun abrirEdicion() = datos.abrirEdicion(this)

    override fun reiniciar() = datos.reiniciar()

    override fun nuevaPartida() = datos.nuevaPartida()

    override fun volverAlMenu() = datos.volverAlMenu()

    override fun agregarObservadorFinSimulacion(observador: ObservadorFinSimulacion) =
        datos.agregarObservadorFinSimulacion(observador)

    override fun quitarObservadorFinSimulacion(observador: ObservadorFinSimulacion) =
        datos.quitarObservadorFinSimulacion(observador)

    override fun resultadoFinal(): ResultadoSimulacion = datos.resultadoFinal()

    override fun rompeParedSeleccionada(): Boolean = datos.rompeParedSeleccionada()

    override fun etiquetaColision(): String = datos.etiquetaColision()

    override fun lecturaModo(): LecturaModo = datos.lecturaModo()

    override fun previsualizarTrayectoria(): List<Vector2D> = datos.previsualizarTrayectoria()

    override fun cambiarA(nuevo: EstadoSimulacion) = datos.cambiarA(nuevo)

    override fun colocarPelotaParaLanzar(punto: Vector2D) = datos.colocarPelotaParaLanzar(punto)

    override fun iniciarApuntado(punto: Vector2D) = datos.iniciarApuntado(punto)

    override fun continuarApuntado(punto: Vector2D) = datos.continuarApuntado(punto)

    override fun lanzarPelota() = datos.lanzarPelota()

    override fun avanzarVuelo(deltaTime: Double) = datos.avanzarVuelo(this, deltaTime)

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

    private class DatosSimulacion(contexto: ContextoSimulacion) {
        val ancho = contexto.ancho
        val alto = contexto.alto
        val entrada = contexto.entrada
        private val sesion = contexto.sesion
        private val servicios = contexto.servicios
        private val control = contexto.control
        private val partida = contexto.partida
        private val fisica = contexto.fisica
        private val consultas = contexto.consultas
        private val ediciones = contexto.ediciones
        private val acciones = contexto.acciones

        fun estado(): EstadoSimulacion = consultas.estado()

        fun pausado(): Boolean = consultas.pausado()

        fun modoParedes(): Boolean = consultas.modoParedes()

        fun modoSuperficies(): Boolean = consultas.modoSuperficies()

        fun tipoPelota(): TipoPelota = consultas.tipoPelota()

        fun tipoPared(): TipoPared = consultas.tipoPared()

        fun factorGravedad(): Double = consultas.factorGravedad()

        fun modo(): ModoSimulacion = consultas.modo()

        fun nombresModos(): List<String> = consultas.nombresModos()

        fun pelota(): Proyectil = consultas.pelota()

        fun offsetCamara(): Vector2D = consultas.offsetCamara()

        fun tiempoVuelo(): Double = consultas.tiempoVuelo()

        fun distanciaRecorrida(): Double = consultas.distanciaRecorrida()

        fun alturaMaxima(): Double = consultas.alturaMaxima()

        fun distanciaX(): Double = consultas.distanciaX()

        fun paredesDestruidas(): Int = consultas.paredesDestruidas()

        fun paredTemporal(): Pared? = consultas.paredTemporal()

        fun paredes(): List<Pared> = consultas.paredes()

        fun superficiesLanzamientoLista(): List<Pared> = consultas.superficiesLanzamientoLista()

        fun efectos(): List<EfectoRotura> = consultas.efectos()

        fun sueloY(): Double = consultas.sueloY()

        fun gravedad(): Vector2D = consultas.gravedad()

        fun pulsar(simulacion: Simulacion, punto: Vector2D) {
            if (servicios.edicionActivaCon(sesion.estado())) servicios.iniciarEdicion(punto)
            else sesion.alPulsar(simulacion, punto)
        }

        fun arrastrar(simulacion: Simulacion, punto: Vector2D) {
            if (servicios.edicionActivaCon(sesion.estado())) servicios.actualizarEdicion(punto)
            else sesion.alArrastrar(simulacion, punto)
        }

        fun soltar(simulacion: Simulacion, punto: Vector2D) {
            if (servicios.edicionActivaCon(sesion.estado())) servicios.finalizarEdicion(punto)
            else sesion.alSoltar(simulacion, punto)
        }

        fun actualizar(simulacion: Simulacion, deltaTime: Double) {
            if (sesion.pausado()) return
            servicios.actualizarCamara(deltaTime, entrada, sesion.pelota())
            partida.efectos().avanzar(deltaTime)
            sesion.alActualizar(simulacion, deltaTime)
        }

        fun alternarPausa() = ediciones.alternarPausa()

        fun alternarModoParedes(simulacion: Simulacion) =
            ediciones.alternarModoParedes { sesion.alActivarHerramienta(simulacion) }

        fun alternarModoSuperficies(simulacion: Simulacion) =
            ediciones.alternarModoSuperficies { sesion.alActivarHerramienta(simulacion) }

        fun agregarSuperficieLanzamiento(pared: Pared) = ediciones.agregarSuperficieLanzamiento(pared)

        fun limpiarSuperficiesLanzamiento() {
            ediciones.limpiarSuperficiesLanzamiento()
            acciones.reiniciar()
        }

        fun seleccionarPelota(tipo: TipoPelota) = acciones.seleccionarPelota(tipo)

        fun seleccionarTipoPared(tipo: TipoPared) = ediciones.seleccionarTipoPared(tipo)

        fun aplicarGravedad(nombre: String?) = acciones.aplicarGravedad(nombre)

        fun seleccionarModo(nombre: String?) = acciones.seleccionarModo(nombre)

        fun agregarPared(pared: Pared) = ediciones.agregarPared(pared)

        fun limpiarParedes() = ediciones.limpiarParedes()

        fun abrirEdicion(simulacion: Simulacion) = ediciones.abrirEdicion { sesion.alAbrirEdicion(simulacion) }

        fun reiniciar() = acciones.reiniciar()

        fun nuevaPartida() = acciones.nuevaPartida()

        fun volverAlMenu() = acciones.volverAlMenu()

        fun agregarObservadorFinSimulacion(observador: ObservadorFinSimulacion) =
            servicios.observadoresAgregar(observador)

        fun quitarObservadorFinSimulacion(observador: ObservadorFinSimulacion) =
            servicios.observadoresQuitar(observador)

        fun resultadoFinal(): ResultadoSimulacion = consultas.resultadoFinal()

        fun rompeParedSeleccionada(): Boolean = consultas.rompeParedSeleccionada()

        fun etiquetaColision(): String = consultas.etiquetaColision()

        fun lecturaModo(): LecturaModo = consultas.lecturaModo()

        fun previsualizarTrayectoria(): List<Vector2D> = consultas.previsualizarTrayectoria()

        fun cambiarA(nuevo: EstadoSimulacion) = sesion.cambiarEstado(nuevo)

        fun colocarPelotaParaLanzar(punto: Vector2D) = servicios.colocarPelotaParaLanzar(punto)

        fun iniciarApuntado(punto: Vector2D) = servicios.iniciarApuntado(punto)

        fun continuarApuntado(punto: Vector2D) = servicios.continuarApuntado(punto)

        fun lanzarPelota() = servicios.lanzarPelota()

        fun avanzarVuelo(simulacion: Simulacion, deltaTime: Double) {
            ediciones.avanzarVuelo(deltaTime) {
                sesion.cambiarEstado(EstadoSimulacion.EnSuelo)
                servicios.notificarFin(consultas.resultadoFinal())
            }
        }

        fun descripcion(): String =
            "simulacion(${ancho}x${alto}, estado=${estado()}, modo=${modo()}, pelota=${tipoPelota()}, pared=${tipoPared()}, paredes=${paredes().size})"

        fun esValida(): Boolean =
            ancho > 0 && alto > 0 && entrada.javaClass !== sesion.javaClass && sesion.javaClass !== servicios.javaClass &&
                servicios.javaClass !== control.javaClass && control.javaClass !== partida.javaClass &&
                partida.javaClass !== fisica.javaClass && fisica.javaClass !== consultas.javaClass &&
                consultas.javaClass !== ediciones.javaClass && ediciones.javaClass !== acciones.javaClass

        fun resumen(): String =
            "${ancho}x${alto} ${estado()} ${tipoPelota()}/${tipoPared()} ${paredes().size} paredes ${efectos().size} efectos"

        companion object {
            operator fun invoke(
                ancho: Double,
                alto: Double,
                componentes: ComponentesSimulacion
            ): DatosSimulacion {
                val entrada = componentes.control().entrada()
                val sesion = componentes.partida().sesion()
                val servicios = componentes.control().servicios()
                val control = componentes.control()
                val partida = componentes.partida()
                val fisica = componentes.fisica()
                val consultas = SimulacionConsultas(
                    ancho,
                    alto,
                    ContextoConsultas(
                        entrada,
                        sesion,
                        servicios.edicion(),
                        fisica.creadorParedes(),
                        partida.fabricaModos(),
                        fisica.camara(),
                        partida.metricas(),
                        partida.efectos(),
                        partida.superficies(),
                        fisica.motor(),
                        servicios.posicionPelota(),
                        servicios.previsualizador()
                    )
                )
                val ediciones = SimulacionEdicion(
                    servicios.edicion(),
                    control.comandos().edicion(),
                    servicios.mecanica(),
                    entrada,
                    servicios.observadores(),
                    sesion
                )
                val acciones = SimulacionAcciones(control.comandos().partida(), servicios.mecanica())
                return DatosSimulacion(
                    ContextoSimulacion(
                        ancho,
                        alto,
                        entrada,
                        sesion,
                        servicios,
                        control,
                        partida,
                        fisica,
                        consultas,
                        ediciones,
                        acciones
                    )
                ).also { it.seleccionarTipoPared(TipoPared.porDefecto()) }
            }
        }
    }
}
