package org.example.simulacion

import org.example.entrada.DragInputHandler
import org.example.entrada.InputHandler
import org.example.fisica.MotorFisico
import org.example.fisica.Tierra
import org.example.coordenadas.TransformadorCoordenadas
import org.example.coordenadas.TransformadorCoordenadasSimulacion
import org.example.herramientas.HerramientaCreacionParedes
import org.example.modelo.Pared
import org.example.modelo.Pelota
import org.example.modelo.TipoPelota
import org.example.modelo.TipoPared
import org.example.modelo.Vector2D
import org.example.modelo.creadores.ConstructorParedes
import org.example.modelo.creadores.CreadorParedes
import kotlin.math.exp
import kotlin.math.min
import kotlin.math.max

/**
 * Nucleo del simulador: reglas del tiro, fisica, camara y estado de la partida.
 * No sabe nada de JavaFX ni de como se dibuja; cualquier presentacion (ventana o
 * consola) maneja la partida solo con lo que hay expuesto aqui.
 */
class Simulacion(
    val ancho: Double = 1000.0,
    val alto: Double = 650.0,
    val entrada: InputHandler = DragInputHandler()
) {

    companion object {
        const val DT_FISICA = 1.0 / 60.0
        const val ESCALA_VELOCIDAD = 3.0
        const val MAX_FUERZA = 500.0
        const val LONGITUD_MINIMA_PARED = 12.0
        const val GRAVEDAD_POR_DEFECTO = "Tierra (1.00 g)"
        const val DURACION_ROTURA = 0.45

        /** Gravedades seleccionables, expresadas como multiplicador de la terrestre. */
        val GRAVEDADES = linkedMapOf(
            "Sin gravedad" to 0.0,
            "Sol (28.0 g)" to 27.94,
            "Mercurio (0.38 g)" to 0.377,
            "Venus (0.90 g)" to 0.904,
            GRAVEDAD_POR_DEFECTO to 1.0,
            "Luna (0.17 g)" to 0.166,
            "Marte (0.38 g)" to 0.379,
            "Jupiter (2.53 g)" to 2.534,
            "Saturno (1.07 g)" to 1.064,
            "Urano (0.89 g)" to 0.886,
            "Neptuno (1.14 g)" to 1.137
        )

        private const val MARGEN_COLOCACION = 30.0
        private const val TIEMPO_MAXIMO_VUELO = 30.0
        private const val VELOCIDAD_CAMARA = 6.0
        private const val ANCHOR_CAMARA = 0.4
        private const val ALTURA_CAMARA = 0.45
        private const val PASOS_PREVISUALIZACION = 150
        private const val FUERZA_MINIMA_APUNTADO = 5.0
        private const val ALTURA_APOYO_PELOTA = 2.0
    }

    var estado: EstadoSimulacion = EstadoSimulacion.PantallaInicio
        private set
    var pausado: Boolean = false
        private set
    var modoParedes: Boolean = false
        private set
    var modoSuperficies: Boolean = false
        private set
    var tipoPelota: TipoPelota = TipoPelota.POR_DEFECTO
        private set
    var tipoPared: TipoPared
        get() = creadorParedes.tipoParedActual()
        private set(value) {
            creadorParedes.seleccionarTipoPared(value)
        }
    var factorGravedad: Double = GRAVEDADES[GRAVEDAD_POR_DEFECTO] ?: 1.0
        private set

    var pelota: Pelota = Pelota(posicionInicial(), tipoPelota)
        private set
    var offsetCamara: Vector2D = Vector2D(0.0, 0.0)
        private set
    var tiempoVuelo: Double = 0.0
        private set
    var distanciaRecorrida: Double = 0.0
        private set
    var paredesDestruidas: Int = 0
        private set

    /** Pared que se esta dibujando ahora mismo, todavia no colocada. */
    val paredTemporal: Pared? get() = herramientaCreacionParedes.paredTemporal

    val paredes: List<Pared> get() = motor.obtenerParedes()
    val superficiesLanzamientoLista: List<Pared> get() = superficiesLanzamiento.toList()
    val efectos: List<EfectoRotura> get() = listaEfectos.toList()
    val sueloY: Double get() = motor.sueloY

    private var motor: MotorFisico = MotorFisico(crearTierra())
    val creadorParedes: CreadorParedes = ConstructorParedes(motor, TipoPared.POR_DEFECTO)
    private val transformador: TransformadorCoordenadas = TransformadorCoordenadasSimulacion(this)
    private val herramientaCreacionParedes = HerramientaCreacionParedes(
        transformador,
        creadorParedes,
        LONGITUD_MINIMA_PARED
    )
    private var posicionOrigen: Vector2D = posicionInicial()
    private val superficiesLanzamiento = mutableListOf<Pared>()
    private val listaEfectos = mutableListOf<EfectoRotura>()

    init {
        tipoPared = TipoPared.POR_DEFECTO
    }

    // ---- Entrada que reenvia la presentacion ---------------------------------------

    private fun aMundo(punto: Vector2D): Vector2D = transformador.pantallaAMundo(punto)

    fun pulsar(punto: Vector2D) {
        if (dibujandoPared()) {
            herramientaCreacionParedes.iniciar(punto)
        } else {
            estado.alPulsar(this, punto)
        }
    }

    fun arrastrar(punto: Vector2D) {
        if (dibujandoPared()) {
            herramientaCreacionParedes.actualizar(punto)
        } else {
            estado.alArrastrar(this, punto)
        }
    }

    fun soltar(punto: Vector2D) {
        if (dibujandoPared()) {
            val antes = motor.obtenerParedes().size
            herramientaCreacionParedes.finalizar(punto)
            if (modoSuperficies) {
                val despues = motor.obtenerParedes().size
                if (despues == antes + 1) {
                    val ultima = motor.obtenerParedes().lastOrNull()
                    if (ultima != null) {
                        motor.quitarPared(ultima)
                        superficiesLanzamiento.add(ultima)
                    }
                }
                modoSuperficies = false
                herramientaCreacionParedes.estaActiva = false
            }
        } else {
            estado.alSoltar(this, punto)
        }
    }

    // ---- Bucle de juego -------------------------------------------------------------

    fun actualizar(deltaTime: Double) {
        if (pausado) return

        actualizarCamara(deltaTime)
        envejecerEfectos(deltaTime)
        estado.alActualizar(this, deltaTime)
    }

    // ---- Comandos -------------------------------------------------------------------

    fun alternarPausa() {
        if (estado is EstadoSimulacion.PantallaInicio) return

        pausado = !pausado
        if (pausado) {
            modoParedes = false
            modoSuperficies = false
            herramientaCreacionParedes.estaActiva = false
            cancelarDibujoPared()
            entrada.reiniciar()
        }
    }

    fun alternarModoParedes() {
        if (pausado || estado is EstadoSimulacion.EnVuelo) return

        modoParedes = !modoParedes
        modoSuperficies = false
        herramientaCreacionParedes.estaActiva = modoParedes
        if (!modoParedes) {
            cancelarDibujoPared()
            return
        }
        when (estado) {
            is EstadoSimulacion.PantallaInicio -> cambiarA(EstadoSimulacion.Inactivo)
            is EstadoSimulacion.Arrojando -> {
                entrada.reiniciar()
                cambiarA(EstadoSimulacion.Inactivo)
            }
            is EstadoSimulacion.EnSuelo -> reiniciar()
            else -> { }
        }
    }

    fun alternarModoSuperficies() {
        if (pausado || estado is EstadoSimulacion.EnVuelo) return

        modoSuperficies = !modoSuperficies
        modoParedes = false
        herramientaCreacionParedes.estaActiva = modoSuperficies
        if (!modoSuperficies) {
            cancelarDibujoPared()
            return
        }
        when (estado) {
            is EstadoSimulacion.PantallaInicio -> cambiarA(EstadoSimulacion.Inactivo)
            is EstadoSimulacion.Arrojando -> {
                entrada.reiniciar()
                cambiarA(EstadoSimulacion.Inactivo)
            }
            is EstadoSimulacion.EnSuelo -> reiniciar()
            else -> { }
        }
    }

    fun agregarSuperficieLanzamiento(pared: Pared) {
        if (estado is EstadoSimulacion.EnVuelo) return
        superficiesLanzamiento.add(pared)
    }

    fun limpiarSuperficiesLanzamiento() {
        if (estado is EstadoSimulacion.EnVuelo) return
        superficiesLanzamiento.clear()
        if (estado !is EstadoSimulacion.EnVuelo) reiniciar()
    }

    fun seleccionarPelota(tipo: TipoPelota) {
        if (estado is EstadoSimulacion.EnVuelo) return

        tipoPelota = tipo
        pelota = Pelota(posicionOrigen, tipoPelota)
        apoyarPelotaEnElSueloOSuperficie()
        reiniciar()
    }

    fun seleccionarTipoPared(tipo: TipoPared) {
        if (estado is EstadoSimulacion.EnVuelo) return

        creadorParedes.seleccionarTipoPared(tipo)
        cancelarDibujoPared()
    }

    fun aplicarGravedad(nombre: String?) {
        val factor = GRAVEDADES[nombre] ?: return
        if (factor == factorGravedad) return

        factorGravedad = factor
        motor = MotorFisico(crearTierra(), motor.obtenerParedes())
        if (estado !is EstadoSimulacion.EnVuelo) reiniciar()
    }

    fun agregarPared(pared: Pared) {
        if (estado !is EstadoSimulacion.EnVuelo) motor.agregarPared(pared)
    }

    fun limpiarParedes() {
        if (estado is EstadoSimulacion.EnVuelo) return
        motor.limpiarParedes()
    }

    /** Al abrir un menu de edicion se abandona el apuntado y el tiro ya cerrado. */
    fun abrirEdicion() {
        pausado = false
        modoParedes = false
        modoSuperficies = false
        herramientaCreacionParedes.estaActiva = false
        cancelarDibujoPared()
        entrada.reiniciar()
        when (estado) {
            is EstadoSimulacion.EnSuelo -> reiniciar()
            is EstadoSimulacion.Arrojando -> cambiarA(EstadoSimulacion.Inactivo)
            else -> { }
        }
    }

    /** Reinicia el tiro conservando el escenario: paredes, gravedad y tipo de pelota. */
    fun reiniciar() {
        apoyarPelotaEnElSueloOSuperficie()
        pelota.posicion = posicionOrigen
        pelota.velocidad = Vector2D(0.0, 0.0)
        pelota.angulo = 0.0
        pelota.velocidadAngular = 0.0
        entrada.reiniciar()
        tiempoVuelo = 0.0
        distanciaRecorrida = 0.0
        cambiarA(EstadoSimulacion.Inactivo)
    }

    /** Vuelve al principio de la partida: camara al origen y sin roturas. */
    fun nuevaPartida() {
        pausado = false
        modoParedes = false
        modoSuperficies = false
        herramientaCreacionParedes.estaActiva = false
        cancelarDibujoPared()
        reiniciar()
        offsetCamara = Vector2D(0.0, 0.0)
        paredesDestruidas = 0
        listaEfectos.clear()
        superficiesLanzamiento.clear()
    }

    // ---- Datos para el HUD ----------------------------------------------------------

    /** Si la pelota actual tiene material que romper en la pared seleccionada. */
    fun rompeParedSeleccionada(): Boolean = tipoPared.registraDanio(tipoPelota)

    fun etiquetaColision(): String = pelota.forma.tipo.etiqueta

    /** Simula el tiro que se esta apuntando para dibujar su trayectoria. */
    fun previsualizarTrayectoria(): List<Vector2D> {
        if (estado !is EstadoSimulacion.Arrojando) return emptyList()

        val vector = entrada.vectorLanzamiento ?: return emptyList()
        val simulada = Pelota(posicionOrigen, tipoPelota)
        simulada.velocidad = vector.normalizado *
            (entrada.fuerzaLanzamiento.coerceAtMost(MAX_FUERZA) * ESCALA_VELOCIDAD)

        // Copias: previsualizar nunca debe gastar el danio de las paredes de verdad.
        val motorSim = MotorFisico(crearTierra(), motor.obtenerParedes().map { it.copy() })
        val puntos = mutableListOf(Vector2D(simulada.posicion.x, simulada.posicion.y))
        repeat(PASOS_PREVISUALIZACION) {
            motorSim.simularPaso(simulada, DT_FISICA)
            puntos.add(Vector2D(simulada.posicion.x, simulada.posicion.y))
            if (simulada.posicion.y >= motorSim.sueloY - simulada.radio) return puntos
        }
        return puntos
    }

    // ---- Mecanica interna, llamada desde los estados --------------------------------

    internal fun cambiarA(nuevo: EstadoSimulacion) {
        estado = nuevo
        nuevo.alEntrar(this)
    }

    internal fun colocarPelotaParaLanzar(punto: Vector2D) {
        val puntoMundo = aMundo(punto)
        if (puntoMundo.y < sueloY - MARGEN_COLOCACION) return

        posicionOrigen = Vector2D(puntoMundo.x, 0.0)
        pelota = Pelota(posicionOrigen, tipoPelota)
        apoyarPelotaEnElSueloOSuperficie()
        entrada.iniciarArrastre(punto)
        cambiarA(EstadoSimulacion.Arrojando)
    }

    internal fun iniciarApuntado(punto: Vector2D) = entrada.iniciarArrastre(punto)

    internal fun continuarApuntado(punto: Vector2D) {
        if (entrada.estaArrastrando) entrada.actualizarArrastre(punto)
    }

    internal fun lanzarPelota() {
        if (!entrada.estaArrastrando) return

        val vector = entrada.vectorLanzamiento ?: return
        if (vector.magnitud < FUERZA_MINIMA_APUNTADO) {
            entrada.reiniciar()
            return
        }

        val fuerza = entrada.fuerzaLanzamiento.coerceAtMost(MAX_FUERZA)
        pelota.velocidad = vector.normalizado * (fuerza * ESCALA_VELOCIDAD)
        pelota.posicion = posicionOrigen

        tiempoVuelo = 0.0
        distanciaRecorrida = 0.0
        cambiarA(EstadoSimulacion.EnVuelo)
        entrada.reiniciar()
    }

    internal fun avanzarVuelo(deltaTime: Double) {
        val pasos = (deltaTime / DT_FISICA).toInt().coerceAtLeast(1)
        repeat(pasos) {
            val antes = pelota.posicion
            registrarRoturas(motor.simularPaso(pelota, DT_FISICA))
            distanciaRecorrida += pelota.posicion.distanciaA(antes)
        }
        tiempoVuelo += deltaTime

        if (motor.estaEnReposo(pelota) || tiempoVuelo > TIEMPO_MAXIMO_VUELO) {
            modoParedes = false
            herramientaCreacionParedes.estaActiva = false
            cancelarDibujoPared()
            entrada.reiniciar()
            cambiarA(EstadoSimulacion.EnSuelo)
        }
    }

    // ---- Utilidades -----------------------------------------------------------------

    private fun dibujandoPared(): Boolean =
        (modoParedes || modoSuperficies) && estado !is EstadoSimulacion.EnVuelo

    private fun ySuperficieBajo(x: Double): Double? {
        var mejor: Double? = null
        for (s in superficiesLanzamiento) {
            val x1 = minOf(s.inicio.x, s.fin.x)
            val x2 = maxOf(s.inicio.x, s.fin.x)
            if (x < x1 - pelota.radio * 0.8) continue
            if (x > x2 + pelota.radio * 0.8) continue
            val ySup = minOf(s.inicio.y, s.fin.y)
            mejor = when {
                mejor == null -> ySup
                ySup < mejor -> ySup
                else -> mejor
            }
        }
        return mejor
    }

    private fun apoyarPelotaEnElSueloOSuperficie() {
        val ySup = ySuperficieBajo(posicionOrigen.x)
        posicionOrigen = if (ySup != null) {
            Vector2D(posicionOrigen.x, ySup - pelota.radio - ALTURA_APOYO_PELOTA)
        } else {
            Vector2D(posicionOrigen.x, sueloY - pelota.radio - ALTURA_APOYO_PELOTA)
        }
        pelota.posicion = posicionOrigen
    }

    private fun apoyarPelotaEnElSuelo() {
        posicionOrigen = Vector2D(posicionOrigen.x, sueloY - pelota.radio - ALTURA_APOYO_PELOTA)
        pelota.posicion = posicionOrigen
    }

    private fun finalizarDibujoPared(enDibujo: Pared) {
        herramientaCreacionParedes.finalizar()
    }

    private fun cancelarDibujoPared() {
        herramientaCreacionParedes.cancelar()
    }

    private fun registrarRoturas(paredesRotas: List<Pared>) {
        for (pared in paredesRotas) {
            paredesDestruidas++
            listaEfectos.add(EfectoRotura(pared, DURACION_ROTURA))
        }
    }

    private fun envejecerEfectos(deltaTime: Double) {
        val iterator = listaEfectos.iterator()
        while (iterator.hasNext()) {
            val efecto = iterator.next()
            efecto.vida -= deltaTime
            if (efecto.vida <= 0.0) iterator.remove()
        }
    }

    private fun actualizarCamara(deltaTime: Double) {
        val objetivoX = pelota.posicion.x - ancho * ANCHOR_CAMARA
        val objetivoY = pelota.posicion.y - alto * ALTURA_CAMARA
        val factor = 1.0 - exp(-VELOCIDAD_CAMARA * deltaTime)
        offsetCamara = Vector2D(
            offsetCamara.x + (objetivoX - offsetCamara.x) * factor,
            offsetCamara.y + (objetivoY - offsetCamara.y) * factor
        )
    }

    private fun crearTierra() =
        Tierra(ancho, alto, Vector2D(0.0, Tierra.GRAVEDAD_PIXELES * factorGravedad))

    private fun posicionInicial() = Vector2D(50.0, alto - 60.0)
}

/** Estela que deja una pared al romperse y que se apaga sola. */
class EfectoRotura(
    val pared: Pared,
    var vida: Double
) {
    val proporcion: Double
        get() = (vida / Simulacion.DURACION_ROTURA).coerceIn(0.0, 1.0)
}