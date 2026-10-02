package org.example

import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToggleGroup
import javafx.scene.control.Tooltip
import javafx.scene.input.KeyCode
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import org.example.entrada.DragInputHandler
import org.example.fisica.MotorFisico
import org.example.fisica.Tierra
import org.example.grafico.CanvasRenderer
import org.example.grafico.DibujoPelota
import org.example.modelo.EstadoSimulacion
import org.example.modelo.Pared
import org.example.modelo.Pelota
import org.example.modelo.TipoPelota
import org.example.modelo.Vector2D
import org.example.sprites.SpriteManager

class MainApp : Application() {

    companion object {
        const val ANCHO_VENTANA = 1000.0
        const val ALTO_VENTANA = 650.0
        const val DT_FISICA = 1.0 / 60.0
        const val ESCALA_VISTA = 1.0
        const val MAX_FUERZA = 500.0
        const val GROSOR_PARED = Pared.GROSOR_POR_DEFECTO
        const val LONGITUD_MINIMA_PARED = 12.0

        private const val FILAS_MENU_PELOTAS = 2
        private const val ANCHO_BOTON_PELOTA = 300.0
        private const val ALTO_PREVIA_PELOTA = 44.0
        private const val ESTILO_BOTON_PELOTA =
            "-fx-background-color: #2b3138; -fx-text-fill: white; " +
                "-fx-border-color: #4a515a; -fx-border-radius: 8; -fx-background-radius: 8; " +
                "-fx-padding: 6 8 6 8;"
        private const val ESTILO_BOTON_PELOTA_ACTIVO =
            "-fx-background-color: #3d5a80; -fx-text-fill: white; " +
                "-fx-border-color: #d4e157; -fx-border-width: 2; -fx-border-radius: 8; " +
                "-fx-background-radius: 8; -fx-padding: 5 7 5 7;"

        const val GRAVEDAD_POR_DEFECTO = "Tierra (1.00 g)"

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
    }

    private lateinit var canvas: Canvas
    private lateinit var renderer: CanvasRenderer
    private lateinit var inputHandler: DragInputHandler
    private lateinit var motorFisico: MotorFisico
    private lateinit var spriteManager: SpriteManager
    private lateinit var pelota: Pelota
    private lateinit var botonPared: Button
    private lateinit var botonLimpiarParedes: Button
    private lateinit var botonPelotas: Button
    private lateinit var botonPausa: Button
    private lateinit var etiquetaPelota: Label
    private lateinit var etiquetaGravedad: Label
    private lateinit var comboGravedad: ComboBox<String>
    private lateinit var menuPausa: VBox
    private lateinit var menuPelotas: VBox
    private val grupoPelotas = ToggleGroup()
    private val botonesPelota = mutableMapOf<TipoPelota, ToggleButton>()

    private var tipoPelota = TipoPelota.POR_DEFECTO
    private var factorGravedad = GRAVEDADES[GRAVEDAD_POR_DEFECTO] ?: 1.0
    private var etiquetaColision = tipoPelota.crearForma().tipo.etiqueta
    private var estado = EstadoSimulacion.PANTALLA_INICIO
    private var pausado = false
    private var modoParedes = false
    private var menuPelotasAbierto = false
    private var creandoPared = false
    private var inicioPared: Vector2D? = null
    private var finPared: Vector2D? = null
    private var offsetCamara = Vector2D(0.0, 0.0)
    private var tiempoVuelo = 0.0
    private var distanciaRecorrida = 0.0
    private var posicionOrigen: Vector2D = Vector2D(50.0, ALTO_VENTANA - 60.0)

    override fun start(stage: Stage) {
        canvas = Canvas(ANCHO_VENTANA, ALTO_VENTANA)
        val gc = canvas.graphicsContext2D
        gc.isImageSmoothing = true

        renderer = CanvasRenderer(gc, ANCHO_VENTANA, ALTO_VENTANA)
        inputHandler = DragInputHandler()
        motorFisico = MotorFisico(crearTierra())
        spriteManager = SpriteManager()

        crearPelota()

        inicializarSprites()

        val controles = crearControles()
        controles.maxWidth = Region.USE_PREF_SIZE
        controles.maxHeight = Region.USE_PREF_SIZE
        controles.isPickOnBounds = false
        crearMenuPausa()
        crearMenuPelotas()
        val root = StackPane(canvas, controles, menuPausa, menuPelotas)
        StackPane.setAlignment(controles, Pos.TOP_RIGHT)
        StackPane.setMargin(controles, Insets(10.0))
        StackPane.setAlignment(menuPausa, Pos.CENTER)
        StackPane.setAlignment(menuPelotas, Pos.CENTER)
        val scene = Scene(root, ANCHO_VENTANA, ALTO_VENTANA)

        configurarEventos(scene)
        actualizarControles()
        configurarGameLoop()

        stage.title = "Simulador de Tiro Parabolico"
        stage.scene = scene
        stage.isResizable = false
        stage.show()
    }

    private fun inicializarSprites() {
        // Los sprites se cargan desde archivos si existen en resources/
        // Ejemplo: spriteManager.crearDecoracion("/sprites/arbol.png", Vector2D(200.0, 500.0))
    }

    private fun crearPelota() {
        pelota = Pelota(posicionOrigen, tipoPelota)
        etiquetaColision = pelota.forma.tipo.etiqueta
    }

    /** Reubica la pelota apoyada en el suelo, respetando el tamano del tipo elegido. */
    private fun apoyarPelotaEnElSuelo() {
        posicionOrigen = Vector2D(posicionOrigen.x, motorFisico.sueloY - pelota.radio - 2.0)
        pelota.posicion = posicionOrigen
    }

    private fun crearControles(): VBox {
        botonPared = Button("Anadir pared")
        botonLimpiarParedes = Button("Limpiar paredes")
        botonPelotas = Button("Pelotas")
        botonPausa = Button("Pausa")
        etiquetaPelota = Label(textoPelotaActual())
        etiquetaGravedad = Label(textoGravedadActual())
        comboGravedad = ComboBox<String>()
        comboGravedad.setItems(FXCollections.observableArrayList(GRAVEDADES.keys))
        comboGravedad.value = GRAVEDAD_POR_DEFECTO
        comboGravedad.tooltip = Tooltip("Elige la gravedad con la que se simula el tiro")
        val ayuda = Label("Arrastra en el mundo para dibujar")

        botonPared.setOnAction { alternarModoParedes() }
        botonLimpiarParedes.setOnAction {
            if (!pausado && estado != EstadoSimulacion.EN_VUELO) {
                motorFisico.limpiarParedes()
                actualizarControles()
            }
        }
        botonPelotas.setOnAction { alternarMenuPelotas() }
        botonPausa.setOnAction { alternarPausa() }
        comboGravedad.setOnAction { aplicarGravedad(comboGravedad.value) }

        return VBox(
            8.0,
            botonPared, botonLimpiarParedes, botonPelotas, botonPausa,
            etiquetaPelota, etiquetaGravedad, comboGravedad, ayuda
        )
    }

    private fun textoPelotaActual(): String = "Pelota: ${tipoPelota.nombre}"

    private fun textoGravedadActual(): String = "Gravedad: ${"%.2f".format(factorGravedad)} g"

    /** Crea el ambiente con la gravedad seleccionada, escalada a pixeles. */
    private fun crearTierra() =
        Tierra(ANCHO_VENTANA, ALTO_VENTANA, Vector2D(0.0, Tierra.GRAVEDAD_PIXELES * factorGravedad))

    /** Cambia la gravedad en caliente, conservando las paredes ya dibujadas. */
    private fun aplicarGravedad(nombre: String?) {
        val factor = GRAVEDADES[nombre] ?: return
        if (factor == factorGravedad) return

        factorGravedad = factor
        motorFisico = MotorFisico(crearTierra(), motorFisico.obtenerParedes())
        etiquetaGravedad.text = textoGravedadActual()
        if (estado != EstadoSimulacion.EN_VUELO) {
            reiniciarSimulacion()
        }
        actualizarControles()
    }

    private fun crearMenuPausa() {
        val titulo = Label("PAUSA")
        val continuar = Button("Continuar")
        val reiniciar = Button("Reiniciar")
        val salir = Button("Salir")

        titulo.alignment = Pos.CENTER
        titulo.style = "-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;"
        continuar.prefWidth = 180.0
        reiniciar.prefWidth = 180.0
        salir.prefWidth = 180.0

        continuar.setOnAction { alternarPausa() }
        reiniciar.setOnAction { reiniciarJuego() }
        salir.setOnAction { Platform.exit() }

        menuPausa = VBox(16.0, titulo, continuar, reiniciar, salir)
        menuPausa.alignment = Pos.CENTER
        menuPausa.padding = Insets(24.0)
        menuPausa.maxWidth = Region.USE_PREF_SIZE
        menuPausa.maxHeight = Region.USE_PREF_SIZE
        menuPausa.isPickOnBounds = true
        menuPausa.style = "-fx-background-color: #20252d; -fx-background-radius: 12; -fx-border-color: #6c757d; -fx-border-radius: 12;"
        menuPausa.isVisible = false
    }

    private fun crearMenuPelotas() {
        val titulo = Label("ELIGE TU PELOTA")
        val subtitulo = Label("Cada tipo tiene su masa, rebote, rozamiento y forma de colision")
        val rejilla = GridPane()
        rejilla.hgap = 10.0
        rejilla.vgap = 10.0

        var columna = 0
        var fila = 0
        for (tipo in TipoPelota.entries) {
            rejilla.add(crearBotonPelota(tipo), columna, fila)
            columna++
            if (columna >= FILAS_MENU_PELOTAS) {
                columna = 0
                fila++
            }
        }

        val ayuda = Label("Pulsa B o ESC para cerrar")
        titulo.alignment = Pos.CENTER
        titulo.style = "-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;"
        subtitulo.alignment = Pos.CENTER
        subtitulo.style = "-fx-text-fill: #c8ced6; -fx-font-size: 12px;"
        ayuda.alignment = Pos.CENTER
        ayuda.style = "-fx-text-fill: #9aa4b0; -fx-font-size: 12px;"

        menuPelotas = VBox(14.0, titulo, subtitulo, rejilla, ayuda)
        menuPelotas.alignment = Pos.CENTER
        menuPelotas.padding = Insets(20.0)
        menuPelotas.maxWidth = Region.USE_PREF_SIZE
        menuPelotas.maxHeight = Region.USE_PREF_SIZE
        menuPelotas.isPickOnBounds = true
        menuPelotas.style = "-fx-background-color: #20252d; -fx-background-radius: 12; -fx-border-color: #6c757d; -fx-border-radius: 12;"
        menuPelotas.isVisible = false

        grupoPelotas.selectedToggleProperty().addListener { _, _, _ ->
            for (boton in botonesPelota.values) {
                boton.style = if (boton.isSelected) ESTILO_BOTON_PELOTA_ACTIVO else ESTILO_BOTON_PELOTA
            }
        }
        grupoPelotas.selectToggle(botonesPelota[tipoPelota])
        for (boton in botonesPelota.values) {
            boton.style = if (boton.isSelected) ESTILO_BOTON_PELOTA_ACTIVO else ESTILO_BOTON_PELOTA
        }
    }

    private fun crearBotonPelota(tipo: TipoPelota): ToggleButton {
        val forma = tipo.crearForma()
        val escala = (ALTO_PREVIA_PELOTA * 0.42) / forma.radioEnvoltura
        val previa = Canvas(120.0, ALTO_PREVIA_PELOTA)
        previa.isMouseTransparent = true
        DibujoPelota.dibujar(
            previa.graphicsContext2D,
            tipo,
            forma,
            0.0,
            Vector2D(60.0, ALTO_PREVIA_PELOTA / 2.0),
            escala
        )

        val nombre = Label(tipo.nombre)
        nombre.style = "-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;"
        val datos = Label(
            "masa ${"%.2f".format(tipo.masa)} kg · rebote ${"%.2f".format(tipo.coeficienteRestitucion)}"
        )
        datos.style = "-fx-text-fill: #c8ced6; -fx-font-size: 11px;"
        val forma_ = Label("rozamiento ${"%.1f".format(tipo.coeficienteFriccion)} · ${forma.tipo.etiqueta}")
        forma_.style = "-fx-text-fill: #9aa4b0; -fx-font-size: 11px;"

        val textos = VBox(2.0, nombre, datos, forma_)
        textos.isMouseTransparent = true
        textos.alignment = Pos.CENTER_LEFT

        val boton = ToggleButton()
        boton.toggleGroup = grupoPelotas
        boton.graphic = HBox(10.0, previa, textos)
        boton.alignment = Pos.CENTER
        boton.prefWidth = ANCHO_BOTON_PELOTA
        boton.style = ESTILO_BOTON_PELOTA
        boton.setOnAction { seleccionarPelota(tipo) }
        boton.tooltip = Tooltip(tipo.descripcion)

        botonesPelota[tipo] = boton
        return boton
    }

    private fun configurarEventos(scene: Scene) {
        canvas.setOnMousePressed { e ->
            if (pausado || menuPelotasAbierto) return@setOnMousePressed

            if (modoParedes && estado != EstadoSimulacion.EN_VUELO) {
                creandoPared = true
                val punto = Vector2D(e.x + offsetCamara.x, e.y + offsetCamara.y)
                inicioPared = punto
                finPared = punto
            } else {
                when (estado) {
                    EstadoSimulacion.PANTALLA_INICIO -> {
                        estado = EstadoSimulacion.INACTIVO
                        inputHandler.reiniciar()
                    }
                    EstadoSimulacion.INACTIVO -> {
                        val clic = Vector2D(e.x, e.y)
                        val sueloPantalla = motorFisico.sueloY - offsetCamara.y
                        if (clic.y >= sueloPantalla - 30.0) {
                            posicionOrigen = Vector2D(clic.x + offsetCamara.x, 0.0)
        crearPelota()
        apoyarPelotaEnElSuelo()
                            apoyarPelotaEnElSuelo()
                            estado = EstadoSimulacion.ARROJANDO
                            inputHandler.reiniciar()
                        }
                    }
                    EstadoSimulacion.ARROJANDO -> {
                        inputHandler.iniciarArrastre(Vector2D(e.x, e.y))
                    }
                    EstadoSimulacion.EN_SUELO -> {
                        reiniciarSimulacion()
                    }
                    EstadoSimulacion.EN_VUELO -> { }
                }
            }
        }

        canvas.setOnMouseDragged { e ->
            if (pausado || menuPelotasAbierto) return@setOnMouseDragged

            if (modoParedes && creandoPared) {
                finPared = Vector2D(e.x + offsetCamara.x, e.y + offsetCamara.y)
            } else if (estado == EstadoSimulacion.ARROJANDO && inputHandler.estaArrastrando) {
                inputHandler.actualizarArrastre(Vector2D(e.x, e.y))
            }
        }

        canvas.setOnMouseReleased {
            if (pausado || menuPelotasAbierto) return@setOnMouseReleased

            if (modoParedes && creandoPared) {
                finalizarDibujoPared()
            } else if (estado == EstadoSimulacion.ARROJANDO && inputHandler.estaArrastrando) {
                inputHandler.finalizarArrastre()
                lanzarPelota()
            }
        }

        scene.setOnKeyPressed { e ->
            when (e.code) {
                KeyCode.P -> alternarModoParedes()
                KeyCode.B -> alternarMenuPelotas()
                KeyCode.ESCAPE -> {
                    when {
                        menuPelotasAbierto -> cerrarMenuPelotas()
                        pausado -> alternarPausa()
                        else -> {
                            modoParedes = false
                            cancelarDibujoPared()
                        }
                    }
                    actualizarControles()
                }
                else -> { }
            }
        }
    }

    private fun alternarModoParedes() {
        if (pausado || menuPelotasAbierto || estado == EstadoSimulacion.EN_VUELO) return

        modoParedes = !modoParedes
        if (modoParedes) {
            when (estado) {
                EstadoSimulacion.PANTALLA_INICIO -> estado = EstadoSimulacion.INACTIVO
                EstadoSimulacion.ARROJANDO -> {
                    inputHandler.reiniciar()
                    estado = EstadoSimulacion.INACTIVO
                }
                EstadoSimulacion.EN_SUELO -> reiniciarSimulacion()
                EstadoSimulacion.INACTIVO -> { }
                EstadoSimulacion.EN_VUELO -> { }
            }
        } else {
            cancelarDibujoPared()
        }
        actualizarControles()
    }

    private fun alternarPausa() {
        if (estado == EstadoSimulacion.PANTALLA_INICIO) return

        pausado = !pausado
        if (pausado) {
            cerrarMenuPelotas()
            modoParedes = false
            inputHandler.reiniciar()
            cancelarDibujoPared()
        }
        actualizarControles()
    }

    private fun alternarMenuPelotas() {
        if (estado == EstadoSimulacion.EN_VUELO) return

        if (menuPelotasAbierto) {
            cerrarMenuPelotas()
        } else {
            abrirMenuPelotas()
        }
        actualizarControles()
    }

    private fun abrirMenuPelotas() {
        menuPelotasAbierto = true
        pausado = false
        modoParedes = false
        cancelarDibujoPared()
        inputHandler.reiniciar()
        when (estado) {
            EstadoSimulacion.ARROJANDO -> estado = EstadoSimulacion.INACTIVO
            EstadoSimulacion.EN_SUELO -> reiniciarSimulacion()
            else -> { }
        }
        grupoPelotas.selectToggle(botonesPelota[tipoPelota])
        menuPelotas.isVisible = true
    }

    private fun cerrarMenuPelotas() {
        menuPelotasAbierto = false
        menuPelotas.isVisible = false
    }

    private fun seleccionarPelota(tipo: TipoPelota) {
        tipoPelota = tipo
        crearPelota()
        apoyarPelotaEnElSuelo()
        reiniciarSimulacion()
        cerrarMenuPelotas()
        actualizarControles()
    }

    private fun reiniciarJuego() {
        pausado = false
        modoParedes = false
        cerrarMenuPelotas()
        cancelarDibujoPared()
        reiniciarSimulacion()
        offsetCamara = Vector2D(0.0, 0.0)
        actualizarControles()
    }

    private fun finalizarDibujoPared() {
        val inicio = inicioPared
        val fin = finPared
        cancelarDibujoPared()

        if (inicio != null && fin != null && inicio.distanciaA(fin) >= LONGITUD_MINIMA_PARED) {
            motorFisico.agregarPared(Pared(inicio, fin, GROSOR_PARED))
        }
        actualizarControles()
    }

    private fun cancelarDibujoPared() {
        creandoPared = false
        inicioPared = null
        finPared = null
    }

    private fun actualizarControles() {
        botonPared.text = if (modoParedes) "Cancelar pared" else "Anadir pared"
        botonPared.isDisable = pausado || estado == EstadoSimulacion.EN_VUELO
        botonLimpiarParedes.isDisable = pausado || estado == EstadoSimulacion.EN_VUELO ||
            motorFisico.obtenerParedes().isEmpty()
        botonPelotas.text = if (menuPelotasAbierto) "Cerrar pelotas" else "Pelotas"
        botonPelotas.isDisable = estado == EstadoSimulacion.EN_VUELO
        botonPausa.text = if (pausado) "Continuar" else "Pausa"
        botonPausa.isDisable = estado == EstadoSimulacion.PANTALLA_INICIO
        etiquetaPelota.text = textoPelotaActual()
        etiquetaGravedad.text = textoGravedadActual()
        comboGravedad.isDisable = pausado
        menuPausa.isVisible = pausado
        menuPelotas.isVisible = menuPelotasAbierto
    }

    private fun lanzarPelota() {
        val vector = inputHandler.vectorLanzamiento ?: return
        val fuerza = inputHandler.fuerzaLanzamiento.coerceAtMost(MAX_FUERZA)
        val escalaVelocidad = fuerza * 3.0

        if (vector.magnitud < 5.0) {
            inputHandler.reiniciar()
            return
        }

        val direccion = vector.normalizado
        pelota.velocidad = direccion * escalaVelocidad
        pelota.posicion = posicionOrigen

        estado = EstadoSimulacion.EN_VUELO
        tiempoVuelo = 0.0
        distanciaRecorrida = 0.0
        inputHandler.reiniciar()
    }

    private fun reiniciarSimulacion() {
        pelota.posicion = posicionOrigen
        pelota.velocidad = Vector2D(0.0, 0.0)
        pelota.angulo = 0.0
        pelota.velocidadAngular = 0.0
        estado = EstadoSimulacion.INACTIVO
        inputHandler.reiniciar()
        tiempoVuelo = 0.0
        distanciaRecorrida = 0.0
    }

    private fun configurarGameLoop() {
        object : AnimationTimer() {
            private var ultimoTiempo = 0L

            override fun handle(now: Long) {
                if (ultimoTiempo == 0L) {
                    ultimoTiempo = now
                    return
                }

                val deltaSegundos = (now - ultimoTiempo) / 1_000_000_000.0
                ultimoTiempo = now

                actualizar(deltaSegundos.coerceAtMost(0.05))
                renderizar()
            }
        }.start()
    }

    private fun actualizar(dt: Double) {
        if (pausado) return

        actualizarCamara(dt)

        when (estado) {
            EstadoSimulacion.EN_VUELO -> {
                val pasos = (dt / DT_FISICA).toInt().coerceAtLeast(1)
                for (i in 0 until pasos) {
                    val posAntes = pelota.posicion
                    motorFisico.simularPaso(pelota, DT_FISICA)
                    distanciaRecorrida += pelota.posicion.distanciaA(posAntes)
                }
                tiempoVuelo += dt

                if (motorFisico.estaEnReposo(pelota) || tiempoVuelo > 30.0) {
                    estado = EstadoSimulacion.EN_SUELO
                }
            }
            else -> { /* nada que actualizar */ }
        }
    }

    private fun actualizarCamara(dt: Double) {
        val objetivoX = pelota.posicion.x - ANCHO_VENTANA * 0.4
        val objetivoY = pelota.posicion.y - ALTO_VENTANA * 0.45
        val factor = 1.0 - Math.exp(-6.0 * dt)
        offsetCamara = Vector2D(
            offsetCamara.x + (objetivoX - offsetCamara.x) * factor,
            offsetCamara.y + (objetivoY - offsetCamara.y) * factor
        )
    }

    private fun renderizar() {
        renderer.offsetCamara = offsetCamara

        renderer.limpiar()
        renderer.dibujarEntorno()

        spriteManager.dibujarTodos(renderer)

        for (pared in motorFisico.obtenerParedes()) {
            renderer.dibujarPared(pared)
        }

        if (creandoPared) {
            val inicio = inicioPared
            val fin = finPared
            if (inicio != null && fin != null) {
                renderer.dibujarParedTemporal(inicio, fin, GROSOR_PARED)
            }
        }

        when (estado) {
            EstadoSimulacion.PANTALLA_INICIO -> {
                renderer.dibujarPantallaInicio(tipoPelota.nombre)
                renderer.presentar()
                return
            }
            EstadoSimulacion.INACTIVO -> {
                if (!modoParedes) {
                    renderer.dibujarPuntoLanzamiento(posicionOrigen - offsetCamara, pelota.radio)
                    renderer.dibujarProyectil(pelota)
                }
            }
            EstadoSimulacion.ARROJANDO -> {
                val origenPantalla = posicionOrigen - offsetCamara
                renderer.dibujarPuntoLanzamiento(origenPantalla, pelota.radio)
                renderer.dibujarProyectil(pelota)

                val inicio = inputHandler.posicionInicial
                val actual = inputHandler.posicionActual
                if (inicio != null && actual != null) {
                    renderer.dibujarLinea(inicio, actual, "#ffffff", 2.0)

                    val vector = inputHandler.vectorLanzamiento
                    if (vector != null) {
                        val fuerza = inputHandler.fuerzaLanzamiento.coerceAtMost(MAX_FUERZA)
                        val angulo = inputHandler.anguloLanzamiento

                        val direccion = vector.normalizado
                        val puntaFlecha = origenPantalla + direccion * fuerza
                        renderer.dibujarLineaDireccion(origenPantalla, puntaFlecha)

                        renderer.dibujarTexto(
                            "Angulo: ${"%.1f".format(angulo)}°",
                            origenPantalla.x + 15.0, origenPantalla.y - 25.0, 13.0
                        )
                        renderer.dibujarTexto(
                            "Fuerza: ${"%.0f".format(fuerza)}",
                            origenPantalla.x + 15.0, origenPantalla.y - 8.0, 13.0
                        )

                        val puntos = calcularPuntosTrayectoria(pelota.posicion, vector, fuerza)
                        if (puntos.size >= 2) {
                            for (i in 0 until puntos.size - 1) {
                                renderer.dibujarLineaDireccion(
                                    puntos[i] - offsetCamara,
                                    puntos[i + 1] - offsetCamara
                                )
                            }
                        }
                    }
                }
            }
            EstadoSimulacion.EN_VUELO -> {
                renderer.dibujarProyectil(pelota)
            }
            EstadoSimulacion.EN_SUELO -> {
                renderer.dibujarProyectil(pelota)
            }
        }

        actualizarControles()
        renderer.dibujarUI(estado, pelota.velocidad, pelota.posicion,
            if (estado == EstadoSimulacion.ARROJANDO) inputHandler.anguloLanzamiento else null,
            if (estado == EstadoSimulacion.EN_SUELO) distanciaRecorrida else null,
            motorFisico.obtenerParedes().size,
            modoParedes,
            tipoPelota.nombre,
            etiquetaColision
        )

        renderer.presentar()
    }

    private fun calcularPuntosTrayectoria(
        origen: Vector2D,
        vector: Vector2D,
        fuerza: Double
    ): List<Vector2D> {
        val puntos = mutableListOf<Vector2D>()
        val direccion = vector.normalizado
        val velocidadSim = direccion * (fuerza.coerceAtMost(MAX_FUERZA) * 3.0)

        val pelotaSim = Pelota(origen, tipoPelota)
        pelotaSim.velocidad = velocidadSim

        val motorSim = MotorFisico(
            crearTierra(),
            motorFisico.obtenerParedes()
        )
        puntos.add(Vector2D(pelotaSim.posicion.x, pelotaSim.posicion.y))

        for (i in 0 until 150) {
            motorSim.simularPaso(pelotaSim, 1.0 / 60.0)
            puntos.add(Vector2D(pelotaSim.posicion.x, pelotaSim.posicion.y))
            if (pelotaSim.posicion.y >= motorSim.sueloY - pelotaSim.radio) break
        }

        return puntos
    }
}

fun main() {
    Application.launch(MainApp::class.java)
}
