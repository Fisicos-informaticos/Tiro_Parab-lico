package org.example.ui

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import org.example.simulacion.ObservadorFinSimulacion
import org.example.simulacion.ResultadoSimulacion

/**
 * Menu que se abre cuando la pelota se ha parado. Resume el tiro con sus
 * metricas y ofrece repetir o volver al menu principal.
 *
 * Es a la vez la vista y un [ObservadorFinSimulacion]: el nucleo le avisa en el
 * instante exacto en que el vuelo termina y ella se abre sola. Asi la capa de
 * arranque no tiene que vigilar el estado en cada fotograma (SRP).
 */
class MenuFinJuego(private val escucha: UIEventListener) : ObservadorFinSimulacion {

    private val etiquetaTiempo = Label("Tiempo total").also { it.style = EstilosUI.DETALLE }
    private val etiquetaDistancia = Label("Distancia en X").also { it.style = EstilosUI.DETALLE }
    private val etiquetaAltura = Label("Altura máxima").also { it.style = EstilosUI.DETALLE }

    /** Raiz que se monta en la escena. */
    val raiz: VBox = construirRaiz()

    /** Cierto mientras el resumen esta en pantalla. */
    var abierto: Boolean = false
        private set

    override fun alTerminarSimulacion(resultado: ResultadoSimulacion) = mostrar(resultado)

    fun mostrar(resultado: ResultadoSimulacion) {
        etiquetaTiempo.text = "Tiempo total: ${decimales(resultado.tiempoTotal)} s"
        etiquetaDistancia.text = "Distancia en X: ${decimales(resultado.distanciaX)} px"
        etiquetaAltura.text = "Altura máxima: ${decimales(resultado.alturaMaxima)} px"
        abierto = true
        raiz.isVisible = true
    }

    fun ocultar() {
        abierto = false
        raiz.isVisible = false
    }

    private fun construirRaiz(): VBox {
        val titulo = Label("LA PELOTA HA CAÍDO").also { it.style = EstilosUI.TITULO }
        val resumen = VBox(4.0, etiquetaTiempo, etiquetaDistancia, etiquetaAltura).also {
            it.alignment = Pos.CENTER_LEFT
        }
        val botonReiniciar = Button("Reiniciar simulación").also { it.prefWidth = ANCHO_BOTON }
        val botonMenu = Button("Volver al menú").also { it.prefWidth = ANCHO_BOTON }
        botonReiniciar.setOnAction { escucha.reiniciar() }
        botonMenu.setOnAction { escucha.volverAlMenu() }
        for (boton in listOf(botonReiniciar, botonMenu)) {
            boton.maxWidth = Double.MAX_VALUE
            boton.style = EstilosUI.BOTON
        }

        val menu = VBox(14.0, titulo, resumen, botonReiniciar, botonMenu).also {
            it.alignment = Pos.CENTER
            it.padding = Insets(20.0)
            it.maxWidth = Region.USE_PREF_SIZE
            it.maxHeight = Region.USE_PREF_SIZE
            it.isPickOnBounds = true
            it.style = EstilosUI.MENU_FIN
            it.isVisible = false
        }
        return menu
    }

    /** Dos decimales fijos para las metricas del resumen. */
    private fun decimales(valor: Double): String = String.format("%.2f", valor)

    companion object {
        private const val ANCHO_BOTON = 220.0
    }
}
