package org.example.grafico

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

/**
 * Punto de composicion de los dibujantes JavaFX. [CanvasRenderer] recibe todas
 * sus piezas ya construidas y puede probarse con dobles sin crear controles.
 */
object FabricaRenderer {

    fun crear(gc: GraphicsContext, ancho: Double, alto: Double): CanvasRenderer {
        val colorLinea = Color.web("#ff6b35")
        return CanvasRenderer(
            gc = gc,
            ancho = ancho,
            alto = alto,
            dibujoVectores = VectorRenderer(gc),
            entorno = DibujanteEntorno(gc, ancho, alto),
            paredes = DibujanteParedes(gc),
            pantallaInicio = DibujantePantallaInicio(gc, ancho, alto),
            flechaDireccion = DibujanteFlechaDireccion(gc, colorLinea),
            panelInfo = DibujantePanelInfo(gc, Color.WHITE),
            objetos = DibujanteObjetos(gc, Color.WHITE)
        )
    }
}
