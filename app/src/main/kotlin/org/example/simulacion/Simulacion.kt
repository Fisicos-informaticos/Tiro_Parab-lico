package org.example.simulacion

import org.example.entrada.DragInputHandler
import org.example.entrada.InputHandler
import org.example.entrada.InputListener
import org.example.fisica.MotorFisico
import org.example.fisica.PhysicsEngine
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
import org.example.modos.CatalogoModos
import org.example.modos.LecturaModo
import org.example.modos.ModoSimulacion
import org.example.modos.Preparacion
import kotlin.math.exp
import kotlin.math.min
import kotlin.math.max

/**
 * Nucleo del simulador: reglas del tiro, fisica, camara y estado de la partida.
 * No sabe nada de JavaFX ni de como se dibuja; cualquier presentacion (ventana o
 * consola) maneja la partida solo con lo que hay expuesto aqui. Todo su estado
 * es privado y se consulta mediante metodos.
 */
class Simulacion(
    private val ancho: Double = 1000.0,
    private val alto: Double = 650.0,
    private val entrada: InputHandler = DragInputHandler()
) : InputListener {

    companion object {
        const val DT_FISICA = 1.0 / 60.0
        const val MAX_FUERZA = 500.0
        const val LONGITUD_MINIMA_PARED = 12.0
        const val GRAVEDAD_POR_DEFECTO = "Tierra (1.00 g)"
        const val DURACION_ROTURA = 0.45

        /** Gravedades seleccionables, expresadas como multiplicador de la terrestre. */
        private val GRAVEDADES = linkedMapOf(
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

        /** Nombres de gravedad en el orden en el que los muestra el selector. */
        fun gravedades(): List<String> = GRAVEDADES.keys.toList()

        fun factorGravedadDe(nombre: String): Double? = GRAVEDADES[nombre]

        fun factorGravedadPorDefecto(): Double = GRAVEDADES[GRAVEDAD_POR_DEFECTO] ?: 1.0

        private const val TIEMPO_MAXIMO_VUELO = 30.0
        /** Resto de tiempo por debajo del cual ya no se da un paso mas. */
        private const val PASO_MINIMO = 1e-9
        private const val VELOCIDAD_CAMARA = 6.0
        private const val ANCHOR_CAMARA = 0.4
        private const val ALTURA_CAMARA = 0.45
        private const val PASOS_PREVISUALIZACION = 150
        private const val ALTURA_APOYO_PELOTA = 2.0
    }

    private var estado: EstadoSimulacion = EstadoSimulacion.PantallaInicio
    private var pausado: Boolean = false
    private var modoParedes: Boolean = false
    private var modoSuperficies: Boolean = false
    private var tipoPelota: TipoPelota = TipoPelota.porDefecto()
    private var factorGravedad: Double = factorGravedadPorDefecto()

    /** Modo activo: el nucleo y el presentador trabajan contra esta abstraccion. */
    private var modo: ModoSimulacion = CatalogoModos.crear(CatalogoModos.POR_DEFECTO)

    private var pelota: Pelota = Pelota(posicionInicial(), tipoPelota)
    private var offsetCamara: Vector2D = Vector2D(0.0, 0.0)
    private var tiempoVuelo: Double = 0.0
    private var distanciaRecorrida: Double = 0.0

    /** Altura maxima alcanzada sobre el suelo en el tiro actual, en px. */
    private var alturaMaxima: Double = 0.0

    /** Distancia horizontal maxima alcanzada desde el lanzamiento, en px. */
    private var distanciaX: Double = 0.0
    private var paredesDestruidas: Int = 0

    private val motor: PhysicsEngine = MotorFisico(crearTierra())
    private val creadorParedes: CreadorParedes = ConstructorParedes(motor, TipoPared.porDefecto())
    private val transformador: TransformadorCoordenadas = TransformadorCoordenadasSimulacion(this)
    private val herramientaCreacionParedes = HerramientaCreacionParedes(
        transformador,
        creadorParedes,
        LONGITUD_MINIMA_PARED
    )
    private var posicionOrigen: Vector2D = posicionInicial()
    private val superficiesLanzamiento = mutableListOf<Pared>()
    private val listaEfectos = mutableListOf<EfectoRotura>()

    /** Interesados en enterarse de que el tiro ha terminado (patron Observer). */
    private val observadoresFin = mutableListOf<ObservadorFinSimulacion>()

    init {
        seleccionarTipoPared(TipoPared.porDefecto())
    }

    // ---- Consulta de solo lectura ---------------------------------------------------

    fun ancho(): Double = ancho

    fun alto(): Double = alto

    fun entrada(): InputHandler = entrada

    fun estado(): EstadoSimulacion = estado

    fun pausado(): Boolean = pausado

    fun modoParedes(): Boolean = modoParedes

    fun modoSuperficies(): Boolean = modoSuperficies

    fun tipoPelota(): TipoPelota = tipoPelota

    fun tipoPared(): TipoPared = creadorParedes.tipoParedActual()

    fun factorGravedad(): Double = factorGravedad

    fun modo(): ModoSimulacion = modo

    fun pelota(): Pelota = pelota

    fun offsetCamara(): Vector2D = offsetCamara

    fun tiempoVuelo(): Double = tiempoVuelo

    fun distanciaRecorrida(): Double = distanciaRecorrida

    fun alturaMaxima(): Double = alturaMaxima

    fun distanciaX(): Double = distanciaX

    fun paredesDestruidas(): Int = paredesDestruidas

    /** Pared que se esta dibujando ahora mismo, todavia no colocada. */
    fun paredTemporal(): Pared? = herramientaCreacionParedes.paredTemporal()

    fun paredes(): List<Pared> = motor.obtenerParedes()

    fun superficiesLanzamientoLista(): List<Pared> = superficiesLanzamiento.toList()

    fun efectos(): List<EfectoRotura> = listaEfectos.toList()

    fun sueloY(): Double = motor.sueloY()

    /** Gravedad activa en pixeles/segundo^2: la misma que empuja al proyectil. */
    fun gravedad(): Vector2D = Vector2D(0.0, Tierra.GRAVEDAD_PIXELES * factorGravedad)

    // ---- Entrada que reenvia la presentacion ---------------------------------------

    private fun aMundo(punto: Vector2D): Vector2D = transformador.pantallaAMundo(punto)

    override fun pulsar(punto: Vector2D) {
        if (dibujandoPared()) {
            herramientaCreacionParedes.iniciar(punto)
        } else {
            estado.alPulsar(this, punto)
        }
    }

    override fun arrastrar(punto: Vector2D) {
        if (dibujandoPared()) {
            herramientaCreacionParedes.actualizar(punto)
        } else {
            estado.alArrastrar(this, punto)
        }
    }

    override fun soltar(punto: Vector2D) {
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
                herramientaCreacionParedes.desactivar()
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
        if (!estado.permitePausa()) return

        pausado = !pausado
        if (pausado) {
            modoParedes = false
            modoSuperficies = false
            herramientaCreacionParedes.desactivar()
            cancelarDibujoPared()
            entrada.reiniciar()
        }
    }

    fun alternarModoParedes() {
        if (pausado || !estado.permiteEditar()) return

        modoParedes = !modoParedes
        modoSuperficies = false
        if (modoParedes) herramientaCreacionParedes.activar() else herramientaCreacionParedes.desactivar()
        if (!modoParedes) {
            cancelarDibujoPared()
            return
        }
        estado.alActivarHerramienta(this)
    }

    fun alternarModoSuperficies() {
        if (pausado || !estado.permiteEditar()) return

        modoSuperficies = !modoSuperficies
        modoParedes = false
        if (modoSuperficies) herramientaCreacionParedes.activar() else herramientaCreacionParedes.desactivar()
        if (!modoSuperficies) {
            cancelarDibujoPared()
            return
        }
        estado.alActivarHerramienta(this)
    }

    fun agregarSuperficieLanzamiento(pared: Pared) {
        if (!estado.permiteEditar()) return
        superficiesLanzamiento.add(pared)
    }

    fun limpiarSuperficiesLanzamiento() {
        if (!estado.permiteEditar()) return
        superficiesLanzamiento.clear()
        reiniciar()
    }

    fun seleccionarPelota(tipo: TipoPelota) {
        if (!estado.permiteEditar()) return

        tipoPelota = tipo
        pelota = Pelota(posicionOrigen, tipoPelota)
        apoyarPelotaEnElSueloOSuperficie()
        reiniciar()
    }

    fun seleccionarTipoPared(tipo: TipoPared) {
        if (!estado.permiteEditar()) return

        creadorParedes.seleccionarTipoPared(tipo)
        cancelarDibujoPared()
    }

    fun aplicarGravedad(nombre: String?) {
        val factor = GRAVEDADES[nombre] ?: return
        if (factor == factorGravedad) return

        factorGravedad = factor
        motor.cambiarAmbiente(crearTierra())
        if (!estado.estaEnVuelo()) reiniciar()
    }

    /** Cambia el modo activo y deja la pelota lista para preparar otro tiro. */
    fun seleccionarModo(nombre: String?) {
        val nuevo = CatalogoModos.crear(nombre)
        if (nuevo.nombre() == modo.nombre()) return

        modo = nuevo
        if (!estado.estaEnVuelo()) reiniciar()
    }

    fun agregarPared(pared: Pared) {
        if (!estado.estaEnVuelo()) motor.agregarPared(pared)
    }

    fun limpiarParedes() {
        if (!estado.permiteEditar()) return
        motor.limpiarParedes()
    }

    /** Al abrir un menu de edicion se abandona el apuntado y el tiro ya cerrado. */
    fun abrirEdicion() {
        pausado = false
        modoParedes = false
        modoSuperficies = false
        herramientaCreacionParedes.desactivar()
        cancelarDibujoPared()
        entrada.reiniciar()
        estado.alAbrirEdicion(this)
    }

    /** Reinicia el tiro conservando el escenario: paredes, gravedad y tipo de pelota. */
    fun reiniciar() {
        apoyarPelotaEnElSueloOSuperficie()
        pelota.fijarPosicion(posicionOrigen)
        pelota.fijarVelocidad(Vector2D(0.0, 0.0))
        pelota.fijarAngulo(0.0)
        pelota.fijarVelocidadAngular(0.0)
        entrada.reiniciar()
        tiempoVuelo = 0.0
        distanciaRecorrida = 0.0
        alturaMaxima = 0.0
        distanciaX = 0.0
        cambiarA(EstadoSimulacion.Inactivo)
    }

    /** Vuelve al principio de la partida: camara al origen y sin roturas. */
    fun nuevaPartida() {
        pausado = false
        modoParedes = false
        modoSuperficies = false
        herramientaCreacionParedes.desactivar()
        cancelarDibujoPared()
        reiniciar()
        offsetCamara = Vector2D(0.0, 0.0)
        paredesDestruidas = 0
        listaEfectos.clear()
        superficiesLanzamiento.clear()
    }

    /** Regresa a la pantalla de bienvenida, como si se abriese el juego de nuevo. */
    fun volverAlMenu() {
        nuevaPartida()
        cambiarA(EstadoSimulacion.PantallaInicio)
    }

    // ---- Fin de tiro (patron Observer) ---------------------------------------------

    /** Registra a quien quiera enterarse de que el tiro ha terminado. */
    fun agregarObservadorFinSimulacion(observador: ObservadorFinSimulacion) {
        if (!observadoresFin.contains(observador)) observadoresFin.add(observador)
    }

    fun quitarObservadorFinSimulacion(observador: ObservadorFinSimulacion) {
        observadoresFin.remove(observador)
    }

    /** Instantanea con los resultados del tiro terminado. */
    fun resultadoFinal(): ResultadoSimulacion = ResultadoSimulacion(
        tiempoTotal = tiempoVuelo,
        distanciaX = distanciaX,
        alturaMaxima = alturaMaxima
    )

    private fun notificarFinSimulacion() {
        val resultado = resultadoFinal()
        for (observador in observadoresFin.toList()) {
            observador.alTerminarSimulacion(resultado)
        }
    }

    // ---- Datos para el HUD ----------------------------------------------------------

    /** Si la pelota actual tiene material que romper en la pared seleccionada. */
    fun rompeParedSeleccionada(): Boolean = tipoPared().registraDanio(tipoPelota)

    fun etiquetaColision(): String = pelota.forma().tipo().etiqueta()

    /** Instantanea del mundo que el modo activo traduce a texto para el panel. */
    fun lecturaModo(): LecturaModo = LecturaModo(
        posicion = pelota.posicion(),
        velocidad = pelota.velocidad(),
        distancia = distanciaRecorrida,
        altura = alturaActual(),
        tiempo = tiempoVuelo,
        angulo = entrada.anguloLanzamiento(),
        fuerza = entrada.fuerzaLanzamiento()
    )

    /** Simula el tiro que se esta apuntando para dibujar su trayectoria. */
    fun previsualizarTrayectoria(): List<Vector2D> {
        if (!estado.permitePrevisualizar()) return emptyList()

        val condicion = modo.condicionDePrevia(entrada, pelota.posicion()) ?: return emptyList()
        val simulada = Pelota(condicion.posicion(), tipoPelota)
        condicion.aplicarA(simulada)

        // Copias: previsualizar nunca debe gastar el danio de las paredes de verdad.
        val motorSim = MotorFisico(crearTierra(), motor.obtenerParedes().map { it.copia() })
        val puntos = mutableListOf(Vector2D(simulada.posicion().x(), simulada.posicion().y()))
        repeat(PASOS_PREVISUALIZACION) {
            motorSim.simularPaso(simulada, DT_FISICA)
            puntos.add(Vector2D(simulada.posicion().x(), simulada.posicion().y()))
            if (simulada.posicion().y() >= motorSim.sueloY() - simulada.radio()) return puntos
        }
        return puntos
    }

    // ---- Mecanica interna, llamada desde los estados --------------------------------

    internal fun cambiarA(nuevo: EstadoSimulacion) {
        estado = nuevo
    }

    internal fun colocarPelotaParaLanzar(punto: Vector2D) {
        val destino = modo.colocar(preparacionPara(aMundo(punto))) ?: return

        posicionOrigen = destino
        pelota = Pelota(destino, tipoPelota)
        entrada.iniciarArrastre(punto)
        cambiarA(EstadoSimulacion.Arrojando)
    }

    internal fun iniciarApuntado(punto: Vector2D) = entrada.iniciarArrastre(punto)

    internal fun continuarApuntado(punto: Vector2D) {
        if (!entrada.estaArrastrando()) return

        entrada.actualizarArrastre(punto)
        val nuevaPosicion = modo.arrastrar(preparacionPara(aMundo(punto))) ?: return
        posicionOrigen = nuevaPosicion
        pelota.fijarPosicion(nuevaPosicion)
    }

    internal fun lanzarPelota() {
        if (!entrada.estaArrastrando()) return

        val velocidad = modo.velocidadDeSuelta(entrada)
        if (velocidad == null) {
            entrada.reiniciar()
            return
        }

        pelota.fijarVelocidad(velocidad)
        pelota.fijarPosicion(posicionOrigen)

        tiempoVuelo = 0.0
        distanciaRecorrida = 0.0
        alturaMaxima = alturaActual()
        distanciaX = 0.0
        cambiarA(EstadoSimulacion.EnVuelo)
        entrada.reiniciar()
    }

    internal fun avanzarVuelo(deltaTime: Double) {
        var restante = deltaTime
        while (restante > PASO_MINIMO) {
            val paso = min(restante, DT_FISICA)
            val antes = pelota.posicion()
            registrarRoturas(motor.simularPaso(pelota, paso))
            distanciaRecorrida += pelota.posicion().distanciaA(antes)
            tiempoVuelo += paso
            registrarMetricas()
            restante -= paso

            // El reloj se detiene en el instante exacto en que la pelota para.
            if (motor.estaEnReposo(pelota)) {
                terminarVuelo()
                return
            }
        }
        if (tiempoVuelo >= TIEMPO_MAXIMO_VUELO) terminarVuelo()
    }

    /** Cierra el vuelo: deja la pelota en reposo y avisa a los observadores. */
    private fun terminarVuelo() {
        modoParedes = false
        herramientaCreacionParedes.desactivar()
        cancelarDibujoPared()
        entrada.reiniciar()
        cambiarA(EstadoSimulacion.EnSuelo)
        notificarFinSimulacion()
    }

    /** Guarda los maximos que resumen el tiro (altura y alcance horizontal). */
    private fun registrarMetricas() {
        alturaMaxima = maxOf(alturaMaxima, alturaActual())
        distanciaX = maxOf(distanciaX, pelota.posicion().x() - posicionOrigen.x())
    }

    // ---- Utilidades -----------------------------------------------------------------

    private fun dibujandoPared(): Boolean =
        (modoParedes || modoSuperficies) && estado.permiteEditar()

    private fun ySuperficieBajo(x: Double, radio: Double): Double? {
        var mejor: Double? = null
        for (s in superficiesLanzamiento) {
            val x1 = minOf(s.inicio().x(), s.fin().x())
            val x2 = maxOf(s.inicio().x(), s.fin().x())
            if (x < x1 - radio * 0.8) continue
            if (x > x2 + radio * 0.8) continue
            val ySup = minOf(s.inicio().y(), s.fin().y())
            mejor = when {
                mejor == null -> ySup
                ySup < mejor -> ySup
                else -> mejor
            }
        }
        return mejor
    }

    /** y donde la pelota apoya: suelo o superficie de lanzamiento bajo el punto. */
    private fun yDeApoyo(x: Double, radio: Double): Double =
        (ySuperficieBajo(x, radio) ?: sueloY()) - radio - ALTURA_APOYO_PELOTA

    /** Vista de solo lectura del mundo que el modo activo necesita para decidir. */
    private fun preparacionPara(puntoMundo: Vector2D): Preparacion {
        val radio = pelota.radio()
        val minY = maxOf(motor.techoY(), offsetCamara.y()) + radio
        val maxY = maxOf(minY, minOf(motor.sueloY(), offsetCamara.y() + alto) - radio)

        return Preparacion(
            punto = puntoMundo,
            origen = pelota.posicion(),
            apoyoY = yDeApoyo(puntoMundo.x(), radio),
            sueloY = sueloY(),
            minY = minY,
            maxY = maxY
        )
    }

    /** Altura sobre el suelo: cero cuando la pelota apoya. */
    private fun alturaActual(): Double =
        (sueloY() - pelota.radio() - ALTURA_APOYO_PELOTA - pelota.posicion().y()).coerceAtLeast(0.0)

    private fun apoyarPelotaEnElSueloOSuperficie() {
        posicionOrigen = Vector2D(posicionOrigen.x(), yDeApoyo(posicionOrigen.x(), pelota.radio()))
        pelota.fijarPosicion(posicionOrigen)
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
            efecto.avanzar(deltaTime)
            if (efecto.agotado()) iterator.remove()
        }
    }

    private fun actualizarCamara(deltaTime: Double) {
        // Mientras se arrastra, el mapa quieto es lo que mantiene sincronizados
        // el puntero y la pelota (decisivo en caida libre, que la mueve).
        if (entrada.estaArrastrando()) return

        val objetivoX = pelota.posicion().x() - ancho * ANCHOR_CAMARA
        val objetivoY = pelota.posicion().y() - alto * ALTURA_CAMARA
        val factor = 1.0 - exp(-VELOCIDAD_CAMARA * deltaTime)
        offsetCamara = Vector2D(
            offsetCamara.x() + (objetivoX - offsetCamara.x()) * factor,
            offsetCamara.y() + (objetivoY - offsetCamara.y()) * factor
        )
    }

    private fun crearTierra() = Tierra(ancho, alto, gravedad())

    private fun posicionInicial() = Vector2D(50.0, alto - 60.0)
}

/** Estela que deja una pared al romperse y que se apaga sola. */
class EfectoRotura(
    private val pared: Pared,
    vida: Double
) {
    private var vida: Double = vida

    fun pared(): Pared = pared

    fun proporcion(): Double =
        (vida / Simulacion.DURACION_ROTURA).coerceIn(0.0, 1.0)

    fun agotado(): Boolean = vida <= 0.0

    fun avanzar(deltaTime: Double) {
        vida = (vida - deltaTime).coerceAtLeast(0.0)
    }
}
