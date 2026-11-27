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
class MenuFinJuego(escucha: AccionesJuego) : ObservadorFinSimulacion {
    private val datos = DatosMenuFinJuego(escucha)

    /** Raiz que se monta en la escena. */
    fun raiz(): VBox = datos.raiz

    /** Cierto mientras el resumen esta en pantalla. */
    fun abierto(): Boolean = datos.abierto

    override fun alTerminarSimulacion(resultado: ResultadoSimulacion) = datos.mostrar(resultado)

    fun mostrar(resultado: ResultadoSimulacion) = datos.mostrar(resultado)

    fun ocultar() = datos.ocultar()

    private data class DatosMenuFinJuego(
        val escucha: AccionesJuego
    ) {
        val etiquetaTiempo: Label = Label("Tiempo total").also { it.style = EstilosUI.DETALLE }
        val etiquetaDistancia: Label = Label("Distancia en X").also { it.style = EstilosUI.DETALLE }
        val etiquetaAltura: Label = Label("Altura máxima").also { it.style = EstilosUI.DETALLE }
        val raiz: VBox = construirRaiz()
        var abierto: Boolean = false

        fun mostrar(resultado: ResultadoSimulacion) {
            etiquetaTiempo.text = "Tiempo total: ${resultado.tiempoTexto()} s"
            etiquetaDistancia.text = "Distancia en X: ${resultado.distanciaTexto()} px"
            etiquetaAltura.text = "Altura máxima: ${resultado.alturaTexto()} px"
            abierto = true
            raiz.isVisible = true
        }

        fun ocultar() {
            abierto = false
            raiz.isVisible = false
        }

        fun descripcion(): String =
            "menuFin(abierto=$abierto, raiz=${raiz.javaClass.simpleName}, tiempo=${etiquetaTiempo.text}, distancia=${etiquetaDistancia.text}, altura=${etiquetaAltura.text}, escucha=${escucha.javaClass.simpleName})"

        fun esValido(): Boolean =
            etiquetaTiempo.text.isNotBlank() && etiquetaDistancia.text.isNotBlank() && etiquetaAltura.text.isNotBlank()

        fun resumen(): String = "$abierto ${etiquetaTiempo.text} ${etiquetaDistancia.text} ${etiquetaAltura.text}"

        private fun construirRaiz(): VBox = contenedor(
            Label("LA PELOTA HA CAÍDO").also { it.style = EstilosUI.TITULO },
            bloqueResumen(),
            botonAccion("Reiniciar simulación") { escucha.reiniciar() },
            botonAccion("Volver al menú") { escucha.volverAlMenu() }
        )

        private fun bloqueResumen(): VBox = VBox(4.0, etiquetaTiempo, etiquetaDistancia, etiquetaAltura).also {
            it.alignment = Pos.CENTER_LEFT
        }

        private fun botonAccion(texto: String, accion: () -> Unit): Button =
            ConstructorMenu.botonAccion(texto, accion).also {
                it.prefWidth = ANCHO_BOTON
                it.maxWidth = Double.MAX_VALUE
                it.style = EstilosUI.BOTON
            }

        private companion object {
            const val ANCHO_BOTON = 220.0
        }


        private fun contenedor(vararg hijos: javafx.scene.Node): VBox = VBox(14.0, *hijos).also {
            it.alignment = Pos.CENTER
            it.padding = Insets(20.0)
            it.maxWidth = Region.USE_PREF_SIZE
            it.maxHeight = Region.USE_PREF_SIZE
            it.isPickOnBounds = true
            it.style = EstilosUI.MENU_FIN
            it.isVisible = false
        }
    }
}
