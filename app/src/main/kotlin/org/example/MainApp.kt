package org.example

import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.application.Platform
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.input.KeyCode
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import org.example.entrada.DragInputHandler
import org.example.fisica.MotorFisico
import org.example.fisica.Tierra
import org.example.grafico.CanvasRenderer
import org.example.modelo.EstadoSimulacion
import org.example.modelo.Pared
import org.example.modelo.PelotaGoma
import org.example.modelo.Vector2D
import org.example.sprites.SpriteManager

class MainApp : Application() {

    companion object {
        const val ANCHO_VENTANA = 1000.0
        const val ALTO_VENTANA = 650.0
        const val DT_FISICA = 1.0 / 60.0
        const val ESCALA_VISTA = 1.0
        const val MASA_POR_DEFECTO = 0.5
        const val COEFICIENTE_POR_DEFECTO = 0.7
        const val RADIO_POR_DEFECTO = 12.0
        const val MAX_FUERZA = 500.0
        const val GROSOR_PARED = Pared.GROSOR_POR_DEFECTO
        const val LONGITUD_MINIMA_PARED = 12.0
    }

    private lateinit var canvas: Canvas
    private lateinit var renderer: CanvasRenderer
    private lateinit var inputHandler: DragInputHandler
    private lateinit var motorFisico: MotorFisico
    private lateinit var spriteManager: SpriteManager
    private lateinit var pelota: PelotaGoma
    private lateinit var botonPared: Button
    private lateinit var botonLimpiarParedes: Button
    private lateinit var botonPausa: Button
    private lateinit var menuPausa: VBox

    private var estado = EstadoSimulacion.PANTALLA_INICIO
    private var pausado = false
    private var modoParedes = false
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
        motorFisico = MotorFisico(Tierra(ANCHO_VENTANA, ALTO_VENTANA, Vector2D(0.0, Tierra.GRAVEDAD_PIXELES)))
        spriteManager = SpriteManager()

        pelota = PelotaGoma(
            posicion = posicionOrigen,
            masa = MASA_POR_DEFECTO,
            coeficienteRestitucion = COEFICIENTE_POR_DEFECTO,
            radio = RADIO_POR_DEFECTO
        )

        inicializarSprites()

        val controles = crearControles()
        controles.maxWidth = Region.USE_PREF_SIZE
        controles.maxHeight = Region.USE_PREF_SIZE
        controles.isPickOnBounds = false
        crearMenuPausa()
        val root = StackPane(canvas, controles, menuPausa)
        StackPane.setAlignment(controles, Pos.TOP_RIGHT)
        StackPane.setMargin(controles, Insets(10.0))
        StackPane.setAlignment(menuPausa, Pos.CENTER)
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

    private fun crearControles(): VBox {
        botonPared = Button("Anadir pared")
        botonLimpiarParedes = Button("Limpiar paredes")
        botonPausa = Button("Pausa")
        val ayuda = Label("Arrastra en el mundo para dibujar")

        botonPared.setOnAction { alternarModoParedes() }
        botonLimpiarParedes.setOnAction {
            if (!pausado && estado != EstadoSimulacion.EN_VUELO) {
                motorFisico.limpiarParedes()
                actualizarControles()
            }
        }
        botonPausa.setOnAction { alternarPausa() }

        return VBox(8.0, botonPared, botonLimpiarParedes, botonPausa, ayuda)
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

    private fun configurarEventos(scene: Scene) {
        canvas.setOnMousePressed { e ->
            if (pausado) return@setOnMousePressed

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
                            posicionOrigen = Vector2D(
                                clic.x + offsetCamara.x,
                                motorFisico.sueloY - RADIO_POR_DEFECTO - 2.0
                            )
                            pelota.posicion = posicionOrigen
                            pelota.velocidad = Vector2D(0.0, 0.0)
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
            if (pausado) return@setOnMouseDragged

            if (modoParedes && creandoPared) {
                finPared = Vector2D(e.x + offsetCamara.x, e.y + offsetCamara.y)
            } else if (estado == EstadoSimulacion.ARROJANDO && inputHandler.estaArrastrando) {
                inputHandler.actualizarArrastre(Vector2D(e.x, e.y))
            }
        }

        canvas.setOnMouseReleased {
            if (pausado) return@setOnMouseReleased

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
                KeyCode.ESCAPE -> {
                    if (pausado) {
                        alternarPausa()
                    } else {
                        modoParedes = false
                        cancelarDibujoPared()
                        actualizarControles()
                    }
                }
                else -> { }
            }
        }
    }

    private fun alternarModoParedes() {
        if (pausado || estado == EstadoSimulacion.EN_VUELO) return

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
            modoParedes = false
            inputHandler.reiniciar()
            cancelarDibujoPared()
        }
        actualizarControles()
    }

    private fun reiniciarJuego() {
        pausado = false
        modoParedes = false
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
        botonPausa.text = if (pausado) "Continuar" else "Pausa"
        botonPausa.isDisable = estado == EstadoSimulacion.PANTALLA_INICIO
        menuPausa.isVisible = pausado
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
                renderer.dibujarPantallaInicio()
                renderer.presentar()
                return
            }
            EstadoSimulacion.INACTIVO -> {
                if (!modoParedes) {
                    renderer.dibujarPuntoLanzamiento(posicionOrigen - offsetCamara, RADIO_POR_DEFECTO)
                }
            }
            EstadoSimulacion.ARROJANDO -> {
                val origenPantalla = posicionOrigen - offsetCamara
                renderer.dibujarPuntoLanzamiento(origenPantalla, RADIO_POR_DEFECTO)

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
            modoParedes
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

        val pelotaSim = PelotaGoma(origen, MASA_POR_DEFECTO, COEFICIENTE_POR_DEFECTO, RADIO_POR_DEFECTO)
        pelotaSim.velocidad = velocidadSim

        val motorSim = MotorFisico(
            Tierra(ANCHO_VENTANA, ALTO_VENTANA, Vector2D(0.0, Tierra.GRAVEDAD_PIXELES)),
            motorFisico.obtenerParedes()
        )
        puntos.add(Vector2D(pelotaSim.posicion.x, pelotaSim.posicion.y))

        for (i in 0 until 150) {
            motorSim.simularPaso(pelotaSim, 1.0 / 60.0)
            puntos.add(Vector2D(pelotaSim.posicion.x, pelotaSim.posicion.y))
            if (pelotaSim.posicion.y >= motorSim.sueloY - RADIO_POR_DEFECTO) break
        }

        return puntos
    }
}

fun main() {
    Application.launch(MainApp::class.java)
}
