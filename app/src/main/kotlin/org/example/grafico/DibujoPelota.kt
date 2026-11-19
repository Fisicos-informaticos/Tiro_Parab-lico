package org.example.grafico

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.RadialGradient
import javafx.scene.paint.Stop
import org.example.modelo.Proyectil
import org.example.modelo.TipoPelota
import org.example.modelo.Vector2D
import org.example.modelo.colision.FormaColision

class VistaPelota(
    private val tipo: TipoPelota,
    private val forma: FormaColision,
    private val angulo: Double,
    private val centro: Vector2D,
    private val escala: Double = 1.0
) {
    fun tipo(): TipoPelota = tipo

    fun forma(): FormaColision = forma

    fun angulo(): Double = angulo

    fun centro(): Vector2D = centro

    fun escala(): Double = escala

    fun dibujar(gc: GraphicsContext) = DibujoPelota.dibujar(gc, this)

    companion object {
        fun desde(proyectil: Proyectil, camara: Vector2D): VistaPelota = VistaPelota(
            tipo = proyectil.tipo(),
            forma = proyectil.forma(),
            angulo = proyectil.angulo(),
            centro = proyectil.posicion() - camara
        )
    }
}

object DibujoPelota {

    fun dibujar(gc: GraphicsContext, vista: VistaPelota) {
        val contorno = vista.forma().contorno()

        gc.save()
        transformar(gc, vista)
        dibujarCuerpo(gc, vista, contorno)
        dibujarDecoracion(gc, vista, contorno)
        dibujarContorno(gc, vista, contorno)
        gc.restore()
    }

    private fun transformar(gc: GraphicsContext, vista: VistaPelota) {
        gc.translate(vista.centro().x(), vista.centro().y())
        gc.scale(vista.escala(), vista.escala())
        gc.rotate(Math.toDegrees(vista.angulo()))
    }

    private fun dibujarCuerpo(gc: GraphicsContext, vista: VistaPelota, contorno: List<Vector2D>) {
        GeometriaPelota.trazar(gc, contorno)
        gc.fill = cuerpoDe(vista.tipo(), vista.forma().radio())
        gc.fill()
    }

    private fun dibujarDecoracion(gc: GraphicsContext, vista: VistaPelota, contorno: List<Vector2D>) {
        gc.save()
        GeometriaPelota.trazar(gc, contorno)
        gc.clip()
        decorar(gc, vista.tipo(), vista.forma())
        gc.restore()
    }

    private fun dibujarContorno(gc: GraphicsContext, vista: VistaPelota, contorno: List<Vector2D>) {
        GeometriaPelota.trazar(gc, contorno)
        gc.stroke = PaletaPelota.tono(vista.tipo().color(), -0.45)
        gc.lineWidth = 1.2
        gc.stroke()
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
