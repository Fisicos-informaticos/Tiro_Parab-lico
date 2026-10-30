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
import org.example.modos.Preparacion

/**
 * Nucleo del simulador: reglas del tiro, fisica, camara y estado de la partida.
 * No sabe nada de JavaFX ni de como se dibuja; cualquier presentacion (ventana o
 * consola) maneja la partida solo con lo que hay expuesto aqui. Todo su estado
 * es privado y se consulta mediante metodos.
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

    /**
     * Constructor de conveniencia que delega el cableado en el punto de
     * composicion. La clase sigue recibiendo solo abstracciones.
     */
    constructor(
        ancho: Double = 1000.0,
        alto: Double = 650.0,
        entrada: InputHandler? = null
    ) : this(
        ancho,
        alto,
        FabricaSimulacion.componentes(ancho, alto, entrada)
    )

    private constructor(
        ancho: Double,
        alto: Double,
        componentes: ComponentesSimulacion
    ) : this(
        ancho,
        alto,
        componentes.entrada,
        componentes.camara,
        componentes.motor,
        componentes.creadorParedes,
        componentes.transformador,
        componentes.herramienta,
        componentes.fabricaAmbiente,
        componentes.fabricaMotorFisico,
        componentes.fabricaModos
    )

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

    }

    private var estado: EstadoSimulacion = EstadoSimulacion.PantallaInicio
    private var pausado: Boolean = false
    private var tipoPelota: TipoPelota = TipoPelota.porDefecto()
    private var factorGravedad: Double = factorGravedadPorDefecto()

    /** Modo activo: el nucleo y el presentador trabajan contra esta abstraccion. */
    private var modo: ModoSimulacion = fabricaModos.porDefecto()

    private var pelota: Pelota = Pelota(posicionInicial(), tipoPelota)
    private var posicionOrigen: Vector2D = posicionInicial()

    private val metricas = MetricasTiro()
    private val efectos = RegistroEfectos()
    private val superficies = SuperficiesLanzamiento()
    private val controladorCamara = ControladorCamara(camara, ancho, alto)
    private val edicion = GestorEdicion(herramientaCreacionParedes, motor, superficies)
    private val previsualizador = PrevisualizadorTrayectoria(fabricaMotorFisico) { crearAmbiente() }
    private val observadores = RegistroObservadoresFin()
    private val integrador = IntegradorVuelo(motor, metricas, efectos)

    init {
        seleccionarTipoPared(TipoPared.porDefecto())
    }

    // ---- Consulta de solo lectura ---------------------------------------------------

    fun ancho(): Double = ancho

    fun alto(): Double = alto

    fun entrada(): InputHandler = entrada

    fun estado(): EstadoSimulacion = estado

    fun pausado(): Boolean = pausado

    fun modoParedes(): Boolean = edicion.modoParedes()

    fun modoSuperficies(): Boolean = edicion.modoSuperficies()

    fun tipoPelota(): TipoPelota = tipoPelota

    fun tipoPared(): TipoPared = creadorParedes.tipoParedActual()

    fun factorGravedad(): Double = factorGravedad

    fun modo(): ModoSimulacion = modo

    fun nombresModos(): List<String> = fabricaModos.nombres()

    fun pelota(): Pelota = pelota

    fun offsetCamara(): Vector2D = camara.offsetCamara()

    fun tiempoVuelo(): Double = metricas.tiempoVuelo()

    fun distanciaRecorrida(): Double = metricas.distanciaRecorrida()

    fun alturaMaxima(): Double = metricas.alturaMaxima()

    fun distanciaX(): Double = metricas.distanciaX()

    fun paredesDestruidas(): Int = metricas.paredesDestruidas()

    /** Pared que se esta dibujando ahora mismo, todavia no colocada. */
    fun paredTemporal(): Pared? = edicion.paredTemporal()

    fun paredes(): List<Pared> = motor.obtenerParedes()

    fun superficiesLanzamientoLista(): List<Pared> = superficies.lista()

    fun efectos(): List<EfectoRotura> = efectos.lista()

    fun sueloY(): Double = motor.sueloY()

    /** Gravedad activa en pixeles/segundo^2: la misma que empuja al proyectil. */
    fun gravedad(): Vector2D = Vector2D(0.0, Tierra.GRAVEDAD_PIXELES * factorGravedad)

    // ---- Entrada que reenvia la presentacion ---------------------------------------

    private fun aMundo(punto: Vector2D): Vector2D = transformador.pantallaAMundo(punto)

    override fun pulsar(punto: Vector2D) {
        if (edicion.activaCon(estado)) {
            edicion.iniciar(punto)
        } else {
            estado.alPulsar(this, punto)
        }
    }

    override fun arrastrar(punto: Vector2D) {
        if (edicion.activaCon(estado)) {
            edicion.actualizar(punto)
        } else {
            estado.alArrastrar(this, punto)
        }
    }

    override fun soltar(punto: Vector2D) {
        if (edicion.activaCon(estado)) {
            edicion.finalizar(punto)
        } else {
            estado.alSoltar(this, punto)
        }
    }

    // ---- Bucle de juego -------------------------------------------------------------

    fun actualizar(deltaTime: Double) {
        if (pausado) return

        controladorCamara.actualizar(deltaTime, entrada, pelota)
        efectos.avanzar(deltaTime)
        estado.alActualizar(this, deltaTime)
    }

    // ---- Comandos -------------------------------------------------------------------

    fun alternarPausa() {
        if (!estado.permitePausa()) return

        pausado = !pausado
        if (pausado) {
            edicion.desactivar()
            entrada.reiniciar()
        }
    }

    fun alternarModoParedes() {
        if (pausado || !estado.permiteEditar()) return
        if (edicion.alternarParedes()) estado.alActivarHerramienta(this)
    }

    fun alternarModoSuperficies() {
        if (pausado || !estado.permiteEditar()) return
        if (edicion.alternarSuperficies()) estado.alActivarHerramienta(this)
    }

    fun agregarSuperficieLanzamiento(pared: Pared) {
        if (!estado.permiteEditar()) return
        superficies.agregar(pared)
    }

    fun limpiarSuperficiesLanzamiento() {
        if (!estado.permiteEditar()) return
        superficies.limpiar()
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
        edicion.cancelarDibujo()
    }

    fun aplicarGravedad(nombre: String?) {
        val factor = GRAVEDADES[nombre] ?: return
        if (factor == factorGravedad) return

        factorGravedad = factor
        motor.cambiarAmbiente(crearAmbiente())
        if (!estado.estaEnVuelo()) reiniciar()
    }

    /** Cambia el modo activo y deja la pelota lista para preparar otro tiro. */
    fun seleccionarModo(nombre: String?) {
        val nuevo = fabricaModos.crear(nombre)
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
        edicion.desactivar()
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
        metricas.reiniciarTiro()
        cambiarA(EstadoSimulacion.Inactivo)
    }

    /** Vuelve al principio de la partida: camara al origen y sin roturas. */
    fun nuevaPartida() {
        pausado = false
        edicion.desactivar()
        reiniciar()
        controladorCamara.reiniciar()
        metricas.reiniciarPartida()
        efectos.limpiar()
        superficies.limpiar()
    }

    /** Regresa a la pantalla de bienvenida, como si se abriese el juego de nuevo. */
    fun volverAlMenu() {
        nuevaPartida()
        cambiarA(EstadoSimulacion.PantallaInicio)
    }

    // ---- Fin de tiro (patron Observer) ---------------------------------------------

    /** Registra a quien quiera enterarse de que el tiro ha terminado. */
    fun agregarObservadorFinSimulacion(observador: ObservadorFinSimulacion) {
        observadores.agregar(observador)
    }

    fun quitarObservadorFinSimulacion(observador: ObservadorFinSimulacion) {
        observadores.quitar(observador)
    }

    /** Instantanea con los resultados del tiro terminado. */
    fun resultadoFinal(): ResultadoSimulacion = metricas.resultado()

    // ---- Datos para el HUD ----------------------------------------------------------

    /** Si la pelota actual tiene material que romper en la pared seleccionada. */
    fun rompeParedSeleccionada(): Boolean = tipoPared().registraDanio(tipoPelota)

    fun etiquetaColision(): String = pelota.forma().tipo().etiqueta()

    /** Instantanea del mundo que el modo activo traduce a texto para el panel. */
    fun lecturaModo(): LecturaModo = LecturaModo(
        posicion = pelota.posicion(),
        velocidad = pelota.velocidad(),
        distancia = metricas.distanciaRecorrida(),
        altura = alturaActual(),
        tiempo = metricas.tiempoVuelo(),
        angulo = entrada.anguloLanzamiento(),
        fuerza = entrada.fuerzaLanzamiento()
    )

    /** Simula el tiro que se esta apuntando para dibujar su trayectoria. */
    fun previsualizarTrayectoria(): List<Vector2D> {
        if (!estado.permitePrevisualizar()) return emptyList()

        val condicion = modo.condicionDePrevia(entrada, pelota.posicion()) ?: return emptyList()
        return previsualizador.previsualizar(condicion, tipoPelota, motor.obtenerParedes())
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

        metricas.reiniciarTiro()
        metricas.registrarAltura(alturaActual())
        cambiarA(EstadoSimulacion.EnVuelo)
        entrada.reiniciar()
    }

    internal fun avanzarVuelo(deltaTime: Double) {
        val terminado = integrador.avanzar(pelota, deltaTime, posicionOrigen) { alturaActual() }
        if (terminado) terminarVuelo()
    }

    /** Cierra el vuelo: deja la pelota en reposo y avisa a los observadores. */
    private fun terminarVuelo() {
        edicion.desactivar()
        entrada.reiniciar()
        cambiarA(EstadoSimulacion.EnSuelo)
        observadores.notificar(resultadoFinal())
    }

    // ---- Utilidades -----------------------------------------------------------------

    /** Vista de solo lectura del mundo que el modo activo necesita para decidir. */
    private fun preparacionPara(puntoMundo: Vector2D): Preparacion {
        val radio = pelota.radio()
        val minY = maxOf(motor.techoY(), offsetCamara().y()) + radio
        val maxY = maxOf(minY, minOf(motor.sueloY(), offsetCamara().y() + alto) - radio)

        return Preparacion(
            punto = puntoMundo,
            origen = pelota.posicion(),
            apoyoY = superficies.yDeApoyo(puntoMundo.x(), radio, sueloY()),
            sueloY = sueloY(),
            minY = minY,
            maxY = maxY
        )
    }

    /** Altura sobre el suelo: cero cuando la pelota apoya. */
    private fun alturaActual(): Double =
        (sueloY() - pelota.radio() - SuperficiesLanzamiento.ALTURA_APOYO_PELOTA - pelota.posicion().y()).coerceAtLeast(0.0)

    private fun apoyarPelotaEnElSueloOSuperficie() {
        val y = superficies.yDeApoyo(posicionOrigen.x(), pelota.radio(), sueloY())
        posicionOrigen = Vector2D(posicionOrigen.x(), y)
        pelota.fijarPosicion(posicionOrigen)
    }

    private fun crearAmbiente(): Ambiente = fabricaAmbiente.crear(ancho, alto, gravedad())

    private fun posicionInicial() = Vector2D(50.0, alto - 60.0)
}
