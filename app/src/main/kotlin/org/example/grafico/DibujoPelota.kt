package org.example.grafico

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.RadialGradient
import javafx.scene.paint.Stop
import org.example.modelo.TipoPelota
import org.example.modelo.Vector2D
import org.example.modelo.colision.FormaColision

data class VistaPelota(
    val tipo: TipoPelota,
    val forma: FormaColision,
    val angulo: Double,
    val centro: Vector2D,
    val escala: Double = 1.0
)

/**
 * Dibuja una pelota con la forma y los colores de su tipo. El GraphicsContext debe
 * llegar sin transformaciones: la funcion se encarga de situar y rotar el cuerpo para
 * que las decoraciones se pinten en su sistema de coordenadas local.
 */
object DibujoPelota {

    fun dibujar(gc: GraphicsContext, vista: VistaPelota) {
        val radio = vista.forma.radio()
        val contorno = vista.forma.contorno()

        gc.save()
        gc.translate(vista.centro.x(), vista.centro.y())
        gc.scale(vista.escala, vista.escala)
        gc.rotate(Math.toDegrees(vista.angulo))

        GeometriaPelota.trazar(gc, contorno)
        gc.fill = cuerpoDe(vista.tipo, radio)
        gc.fill()

        gc.save()
        GeometriaPelota.trazar(gc, contorno)
        gc.clip()
        decorar(gc, vista.tipo, vista.forma)
        gc.restore()

        GeometriaPelota.trazar(gc, contorno)
        gc.stroke = PaletaPelota.tono(vista.tipo.color(), -0.45)
        gc.lineWidth = 1.2
        gc.stroke()

        gc.restore()
    }

    private fun cuerpoDe(tipo: TipoPelota, radio: Double): RadialGradient {
        return RadialGradient(
            -0.3 * radio, -0.35 * radio,
            -0.3 * radio, -0.35 * radio, radio * 1.8,
            false, CycleMethod.NO_CYCLE,
            Stop(0.0, PaletaPelota.tono(tipo.color(), 0.45)),
            Stop(0.55, PaletaPelota.color(tipo.color())),
            Stop(1.0, PaletaPelota.tono(tipo.color(), -0.35))
        )
    }

    private fun decorar(gc: GraphicsContext, tipo: TipoPelota, forma: FormaColision) {
        DECORACIONES_PELOTA[tipo]?.decorar(gc, tipo, forma)

        if (forma.dibujaBrillo()) {
            val radio = forma.radio()
            gc.fill = Color.color(1.0, 1.0, 1.0, 0.28)
            gc.fillOval(-radio * 0.55, -radio * 0.62, radio * 0.5, radio * 0.36)
        }
    }

    fun color(valor: Int): Color = PaletaPelota.color(valor)

    fun tono(valor: Int, cantidad: Double): Color = PaletaPelota.tono(valor, cantidad)
}
