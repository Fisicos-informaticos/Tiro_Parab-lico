package org.example.ui

import javafx.collections.FXCollections
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.Tooltip
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.layout.VBox

/**
 * Panel lateral de control (zona derecha).
 *
 * Encapsula por completo la creacion, el estilo y la actualizacion de los
 * controles, repartidos en secciones (Simulacion, Pelotas y Obstaculos). La
 * unica via de comunicacion con el juego es [UIEventListener], de modo que la
 * vista no esta acoplada al motor fisico.
 */
class PanelControl(
    private val escucha: UIEventListener,
    modos: List<String>,
    modoInicial: String,
    gravedades: List<String>,
    gravedadInicial: String
) {

    private val comboModo = ComboBox<String>()
    private val comboGravedad = ComboBox<String>()

    private val botonPausa = Button()
    private val botonReiniciar = Button("Reiniciar")

    private val botonPelotas = Button()
    private val etiquetaPelota = Label()

    private val botonAnadirPared = Button()
    private val botonTipoPared = Button()
    private val botonLimpiarParedes = Button("Limpiar paredes")
    private val botonSuperficie = Button()
    private val botonLimpiarSuperficies = Button("Limpiar superficies")

    private val etiquetaGravedad = Label()
    private val etiquetaAyuda = Label()

    private val raiz: VBox = VBox(16.0)

    init {
        configurar(modos, modoInicial, gravedades, gravedadInicial)
    }

    /** Contenedor estilizado que se inserta en la escena. */
    fun raiz(): VBox = raiz

    /** Vuelca un estado de la partida sobre los textos y bloqueos del panel. */
    fun refrescar(estado: EstadoPanel) {
        botonAnadirPared.text = if (estado.modoParedes()) "Cancelar pared" else "Añadir pared"
        botonAnadirPared.isDisable = estado.bloqueado()

        botonTipoPared.text =
            if (estado.menuParedesAbierto()) "Cerrar paredes" else "Pared: ${estado.nombreTipoPared()}"
        botonTipoPared.isDisable = estado.bloqueado()

        botonLimpiarParedes.isDisable = estado.bloqueado() || !estado.hayParedes()

        botonSuperficie.text =
            if (estado.modoSuperficies()) "Cancelar superficie" else "Superficie de lanzamiento"
        botonSuperficie.isDisable = estado.bloqueado()

        botonLimpiarSuperficies.isDisable = estado.bloqueado() || !estado.haySuperficies()

        botonPelotas.text = if (estado.menuPelotasAbierto()) "Cerrar pelotas" else "Pelotas"
        botonPelotas.isDisable = estado.bloqueado()

        botonPausa.text = if (estado.pausado()) "Continuar" else "Pausa"
        botonPausa.isDisable = estado.enPantallaInicio()
        botonReiniciar.isDisable = estado.enVuelo() || estado.enPantallaInicio()

        etiquetaPelota.text = "Actual: ${estado.nombrePelota()}"
        etiquetaGravedad.text = "Gravedad actual: ${estado.gravedadTexto()} g"

        comboModo.isDisable = estado.bloqueado()
        if (comboModo.value != estado.nombreModo()) comboModo.value = estado.nombreModo()
        comboGravedad.isDisable = estado.interaccionBloqueada()
        etiquetaAyuda.text = estado.ayudaModo()
    }

    // ---- Construccion ---------------------------------------------------------------

    private fun configurar(
        modos: List<String>,
        modoInicial: String,
        gravedades: List<String>,
        gravedadInicial: String
    ) {
        comboModo.apply {
            items = FXCollections.observableArrayList(modos)
            value = modoInicial
            tooltip = Tooltip("Elige el modo de simulacion")
            maxWidth = Double.MAX_VALUE
            setOnAction { escucha.seleccionarModo(value) }
        }
        comboGravedad.apply {
            items = FXCollections.observableArrayList(gravedades)
            value = gravedadInicial
            tooltip = Tooltip("Elige la gravedad con la que se simula el tiro")
            maxWidth = Double.MAX_VALUE
            setOnAction { escucha.aplicarGravedad(value) }
        }

        prepararBoton(botonPausa) { escucha.alternarPausa() }
        prepararBoton(botonReiniciar) { escucha.reiniciar() }
        prepararBoton(botonPelotas) { escucha.alternarMenuPelotas() }
        prepararBoton(botonAnadirPared) { escucha.alternarModoParedes() }
        prepararBoton(botonTipoPared) { escucha.alternarMenuParedes() }
        prepararBoton(botonLimpiarParedes) { escucha.limpiarParedes() }
        prepararBoton(botonSuperficie) { escucha.alternarModoSuperficies() }
        prepararBoton(botonLimpiarSuperficies) { escucha.limpiarSuperficiesLanzamiento() }

        etiquetaPelota.style = EstilosUI.DETALLE
        etiquetaGravedad.style = EstilosUI.DETALLE
        etiquetaAyuda.apply {
            style = EstilosUI.AYUDA
            isWrapText = true
            maxWidth = ANCHO_PANEL
        }

        raiz.apply {
            style = EstilosUI.PANEL_CONTENEDOR
            prefWidth = ANCHO_PANEL
            maxWidth = Region.USE_PREF_SIZE
            maxHeight = Region.USE_PREF_SIZE
            isPickOnBounds = true
            children.addAll(
                seccion("SIMULACIÓN") {
                    listOf(
                        campo("Modo de simulación", comboModo),
                        campo("Gravedad", comboGravedad),
                        etiquetaGravedad,
                        fila(botonPausa, botonReiniciar),
                        etiquetaAyuda
                    )
                },
                seccion("PELOTAS") {
                    listOf(botonPelotas, etiquetaPelota)
                },
                seccion("OBSTÁCULOS") {
                    listOf(
                        botonAnadirPared,
                        botonTipoPared,
                        botonLimpiarParedes,
                        botonSuperficie,
                        botonLimpiarSuperficies
                    )
                }
            )
        }
    }

    private fun seccion(titulo: String, contenido: () -> List<Node>): VBox =
        SeccionControl(titulo, contenido()).raiz()

    private fun campo(titulo: String, control: Node): VBox =
        VBox(4.0).apply {
            val etiqueta = Label(titulo).apply { style = EstilosUI.CAPTION }
            children.addAll(etiqueta, control)
        }

    private fun fila(vararg nodos: Region): HBox =
        HBox(8.0).apply {
            for (nodo in nodos) {
                nodo.maxWidth = Double.MAX_VALUE
                HBox.setHgrow(nodo, Priority.ALWAYS)
                children.add(nodo)
            }
        }

    private fun prepararBoton(boton: Button, accion: () -> Unit) {
        boton.apply {
            style = EstilosUI.BOTON
            maxWidth = Double.MAX_VALUE
            setOnAction { accion() }
        }
    }

    companion object {
        /** Ancho fijo del panel para que ningun control se solape con el mundo. */
        const val ANCHO_PANEL = 272.0
    }
}
