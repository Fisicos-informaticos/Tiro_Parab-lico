package org.example.grafico

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.RadialGradient
import javafx.scene.paint.Stop
import org.example.modelo.TipoPelota
import org.example.modelo.Vector2D
import org.example.modelo.colision.FormaColision
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/**
 * Dibuja una pelota con la forma y los colores de su tipo. El GraphicsContext debe
 * llegar sin transformaciones: la funcion se encarga de situar y rotar el cuerpo para
 * que las decoraciones se pinten en su sistema de coordenadas local.
 */
object DibujoPelota {

    private interface DecoracionPelota {
        fun decorar(gc: GraphicsContext, tipo: TipoPelota, forma: FormaColision)
    }

    private object DecoracionTenis : DecoracionPelota {
        override fun decorar(gc: GraphicsContext, tipo: TipoPelota, forma: FormaColision) {
            val radio = forma.radio()
            gc.stroke = Color.color(1.0, 1.0, 1.0, 0.85)
            gc.lineWidth = radio * 0.16
            gc.strokeOval(-radio * 0.5, -radio, radio, radio * 2.0)
            gc.strokeOval(-radio, -radio * 0.5, radio * 2.0, radio)
        }
    }

    private object DecoracionRugby : DecoracionPelota {
        override fun decorar(gc: GraphicsContext, tipo: TipoPelota, forma: FormaColision) {
            val radio = forma.radio()
            val largo = forma.medioLargo()
            gc.fill = Color.color(1.0, 1.0, 1.0, 0.9)
            gc.fillRect(-largo - radio * 0.1, -radio, radio * 0.45, radio * 2.0)
            gc.fillRect(largo - radio * 0.35, -radio, radio * 0.45, radio * 2.0)
            gc.fill = Color.color(0.15, 0.10, 0.06, 0.85)
            for (i in -1..1) {
                gc.fillRect(
                    -radio * 0.55 + i * radio * 0.2,
                    -radio * 0.26,
                    radio * 0.12,
                    radio * 0.52
                )
            }
        }
    }

    private object DecoracionBeisbol : DecoracionPelota {
        override fun decorar(gc: GraphicsContext, tipo: TipoPelota, forma: FormaColision) {
            val radio = forma.radio()
            gc.fill = color(tipo.colorSecundario())
            gc.fillOval(-radio * 0.78, -radio * 0.34, radio * 0.3, radio * 0.68)
            gc.fillOval(radio * 0.48, -radio * 0.34, radio * 0.3, radio * 0.68)
        }
    }

    private object DecoracionFutbol : DecoracionPelota {
        override fun decorar(gc: GraphicsContext, tipo: TipoPelota, forma: FormaColision) {
            val radio = forma.radio()
            gc.stroke = Color.color(1.0, 1.0, 1.0, 0.55)
            gc.lineWidth = radio * 0.1
            gc.strokeOval(-radio * 0.78, -radio * 0.78, radio * 1.56, radio * 1.56)
            gc.fill = color(tipo.colorSecundario())
            trazar(gc, poligonoLocal(5, radio * 0.42, -PI / 2.0))
            gc.fill()
        }
    }

    private object DecoracionBoliche : DecoracionPelota {
        override fun decorar(gc: GraphicsContext, tipo: TipoPelota, forma: FormaColision) {
            val radio = forma.radio()
            gc.fill = tono(tipo.color(), -0.55)
            val hueco = radio * 0.13
            for (agujero in listOf(
                Vector2D(-0.3, -0.14), Vector2D(0.02, -0.3), Vector2D(0.08, 0.14)
            )) {
                gc.fillOval(
                    agujero.x() * radio - hueco,
                    agujero.y() * radio - hueco,
                    hueco * 2.0,
                    hueco * 2.0
                )
            }
        }
    }

    private object DecoracionBloque : DecoracionPelota {
        override fun decorar(gc: GraphicsContext, tipo: TipoPelota, forma: FormaColision) {
            val medioAlto = forma.medioAlto()
            val largo = forma.radioEnvoltura()
            gc.stroke = tono(tipo.color(), -0.4)
            gc.lineWidth = 1.0
            gc.strokeLine(-largo, -medioAlto * 0.34, largo, -medioAlto * 0.34)
            gc.strokeLine(-largo, medioAlto * 0.34, largo, medioAlto * 0.34)
        }
    }

    private object DecoracionPoliedro : DecoracionPelota {
        override fun decorar(gc: GraphicsContext, tipo: TipoPelota, forma: FormaColision) {
            val vertices = forma.vertices()
            gc.fill = Color.color(1.0, 1.0, 1.0, 0.9)
            for (i in vertices.indices step 2) {
                val siguiente = vertices[(i + 1) % vertices.size]
                gc.beginPath()
                gc.moveTo(0.0, 0.0)
                gc.lineTo(vertices[i].x(), vertices[i].y())
                gc.lineTo(siguiente.x(), siguiente.y())
                gc.closePath()
                gc.fill()
            }
        }
    }

    private val DECORACIONES: Map<TipoPelota, DecoracionPelota> = mapOf(
        TipoPelota.TENIS to DecoracionTenis,
        TipoPelota.RUGBY to DecoracionRugby,
        TipoPelota.BEISBOL to DecoracionBeisbol,
        TipoPelota.FUTBOL to DecoracionFutbol,
        TipoPelota.BOLICHE to DecoracionBoliche,
        TipoPelota.BLOQUE to DecoracionBloque,
        TipoPelota.POLIEDRO to DecoracionPoliedro
    )

    fun dibujar(
        gc: GraphicsContext,
        tipo: TipoPelota,
        forma: FormaColision,
        angulo: Double,
        centro: Vector2D,
        escala: Double = 1.0
    ) {
        val radio = forma.radio()
        val contorno = forma.contorno()

        gc.save()
        gc.translate(centro.x(), centro.y())
        gc.scale(escala, escala)
        gc.rotate(Math.toDegrees(angulo))

        trazar(gc, contorno)
        gc.fill = cuerpoDe(tipo, radio)
        gc.fill()

        gc.save()
        trazar(gc, contorno)
        gc.clip()
        decorar(gc, tipo, forma)
        gc.restore()

        trazar(gc, contorno)
        gc.stroke = tono(tipo.color(), -0.45)
        gc.lineWidth = 1.2
        gc.stroke()

        gc.restore()
    }

    private fun cuerpoDe(tipo: TipoPelota, radio: Double): RadialGradient {
        return RadialGradient(
            -0.3 * radio, -0.35 * radio,
            -0.3 * radio, -0.35 * radio, radio * 1.8,
            false, CycleMethod.NO_CYCLE,
            Stop(0.0, tono(tipo.color(), 0.45)),
            Stop(0.55, color(tipo.color())),
            Stop(1.0, tono(tipo.color(), -0.35))
        )
    }

    private fun decorar(gc: GraphicsContext, tipo: TipoPelota, forma: FormaColision) {
        DECORACIONES[tipo]?.decorar(gc, tipo, forma)

        if (forma.dibujaBrillo()) {
            val radio = forma.radio()
            gc.fill = Color.color(1.0, 1.0, 1.0, 0.28)
            gc.fillOval(-radio * 0.55, -radio * 0.62, radio * 0.5, radio * 0.36)
        }
    }

    private fun trazar(gc: GraphicsContext, puntos: List<Vector2D>) {
        gc.beginPath()
        if (puntos.isNotEmpty()) {
            gc.moveTo(puntos[0].x(), puntos[0].y())
            for (i in 1 until puntos.size) {
                gc.lineTo(puntos[i].x(), puntos[i].y())
            }
            gc.closePath()
        }
    }

    private fun poligonoLocal(lados: Int, radio: Double, anguloInicial: Double): List<Vector2D> {
        return (0 until lados).map { i ->
            val a = anguloInicial + 2.0 * PI * i / lados
            Vector2D(cos(a) * radio, sin(a) * radio)
        }
    }

    fun color(valor: Int): Color =
        Color.rgb((valor shr 16) and 0xFF, (valor shr 8) and 0xFF, valor and 0xFF)

    /** Aclara (cantidad positiva) u oscurece (cantidad negativa) un color 0xRRGGBB. */
    fun tono(valor: Int, cantidad: Double): Color {
        val destino = if (cantidad >= 0.0) 255.0 else 0.0
        val mezcla = abs(cantidad).coerceIn(0.0, 1.0)
        val rojo = (valor shr 16) and 0xFF
        val verde = (valor shr 8) and 0xFF
        val azul = valor and 0xFF
        return Color.rgb(
            (rojo + (destino - rojo) * mezcla).toInt().coerceIn(0, 255),
            (verde + (destino - verde) * mezcla).toInt().coerceIn(0, 255),
            (azul + (destino - azul) * mezcla).toInt().coerceIn(0, 255)
        )
    }
}
