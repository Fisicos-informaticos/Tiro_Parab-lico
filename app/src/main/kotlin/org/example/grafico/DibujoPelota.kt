package org.example.grafico

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.RadialGradient
import javafx.scene.paint.Stop
import org.example.modelo.ProyectilVisual
import org.example.modelo.TipoPelota
import org.example.modelo.Vector2D
import org.example.modelo.colision.FormaColision

class VistaPelota(
    tipo: TipoPelota,
    forma: FormaColision,
    angulo: Double,
    centro: Vector2D,
    escala: Double = 1.0
) {
    private val datos = DatosVistaPelota(tipo, forma, angulo, centro, escala)

    fun tipo(): TipoPelota = datos.tipo
    fun forma(): FormaColision = datos.forma
    fun angulo(): Double = datos.angulo
    fun centro(): Vector2D = datos.centro
    fun escala(): Double = datos.escala
    fun dibujar(gc: GraphicsContext) = DibujoPelota.dibujar(gc, this)
    fun esVisibleEn(ancho: Double, alto: Double): Boolean = datos.esVisibleEn(ancho, alto)

    private data class DatosVistaPelota(
        val tipo: TipoPelota,
        val forma: FormaColision,
        val angulo: Double,
        val centro: Vector2D,
        val escala: Double
    ) {
        fun esVisibleEn(ancho: Double, alto: Double): Boolean =
            centro.x() >= -100 && centro.x() <= ancho + 100 && centro.y() >= -100 && centro.y() <= alto + 100

        fun descripcion(): String =
            "vista(tipo=$tipo, forma=${forma.javaClass.simpleName}, angulo=$angulo, centro=$centro, escala=$escala)"

        fun esValida(): Boolean =
            escala > 0 && forma.radioEnvoltura() > 0 && angulo.isFinite() && centro.x().isFinite()

        fun resumen(): String =
            "$tipo en ${centro.x()},${centro.y()} angulo=$angulo escala=$escala forma=${forma.javaClass.simpleName}"
    }

    companion object {
        fun desde(proyectil: ProyectilVisual, camara: Vector2D): VistaPelota = VistaPelota(
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
        return RadialGradient(-0.3 * radio, -0.35 * radio, -0.3 * radio, -0.35 * radio, radio * 1.8, false, CycleMethod.NO_CYCLE,
            Stop(0.0, PaletaPelota.tono(tipo.color(), 0.45)),
            Stop(0.55, PaletaPelota.color(tipo.color())),
            Stop(1.0, PaletaPelota.tono(tipo.color(), -0.35)))
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
