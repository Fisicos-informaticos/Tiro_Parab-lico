package org.example.ui

import javafx.scene.layout.Region
import javafx.scene.layout.VBox

/**
 * Panel lateral de control (zona derecha).
 *
 * Compone las secciones de simulacion, pelotas y obstaculos. Cada seccion
 * encapsula sus propios controles y su actualizacion; este panel solo las
 * organiza y delega el refresco.
 */
class PanelControl(
    acciones: AccionesJuego,
    configuracionJuego: ConfiguracionJuego,
    edicion: EdicionEscenario,
    configuracion: ConfiguracionPanelSimulacion
) {

    private val simulacion = PanelSimulacion(acciones, configuracionJuego, configuracion)
    private val pelotas = PanelPelotas(acciones)
    private val obstaculos = PanelObstaculos(acciones, edicion)

    private val raiz: VBox = VBox(16.0).apply {
        style = EstilosUI.PANEL_CONTENEDOR
        prefWidth = ANCHO_PANEL
        maxWidth = Region.USE_PREF_SIZE
        maxHeight = Region.USE_PREF_SIZE
        isPickOnBounds = true
        children.addAll(simulacion.seccion(), pelotas.seccion(), obstaculos.seccion())
    }

    /** Contenedor estilizado que se inserta en la escena. */
    fun raiz(): VBox = raiz

    /** Vuelca un estado de la partida sobre los textos y bloqueos del panel. */
    fun refrescar(estado: EstadoPanel) {
        simulacion.refrescar(estado)
        pelotas.refrescar(estado)
        obstaculos.refrescar(estado)
    }

    companion object {
        /** Ancho fijo del panel para que ningun control se solape con el mundo. */
        const val ANCHO_PANEL = 272.0
    }
}
