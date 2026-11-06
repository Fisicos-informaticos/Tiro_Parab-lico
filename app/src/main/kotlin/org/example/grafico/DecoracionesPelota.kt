package org.example.grafico

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import org.example.modelo.TipoPelota
import org.example.modelo.Vector2D
import org.example.modelo.colision.FormaColision
import kotlin.math.PI

internal interface DecoracionPelota {
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
        gc.fill = PaletaPelota.color(tipo.colorSecundario())
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
        gc.fill = PaletaPelota.color(tipo.colorSecundario())
        GeometriaPelota.trazar(gc, GeometriaPelota.poligonoLocal(5, radio * 0.42, -PI / 2.0))
        gc.fill()
    }
}

private object DecoracionBoliche : DecoracionPelota {
    override fun decorar(gc: GraphicsContext, tipo: TipoPelota, forma: FormaColision) {
        val radio = forma.radio()
        gc.fill = PaletaPelota.tono(tipo.color(), -0.55)
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
        gc.stroke = PaletaPelota.tono(tipo.color(), -0.4)
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

internal val DECORACIONES_PELOTA: Map<TipoPelota, DecoracionPelota> = mapOf(
    TipoPelota.TENIS to DecoracionTenis,
    TipoPelota.RUGBY to DecoracionRugby,
    TipoPelota.BEISBOL to DecoracionBeisbol,
    TipoPelota.FUTBOL to DecoracionFutbol,
    TipoPelota.BOLICHE to DecoracionBoliche,
    TipoPelota.BLOQUE to DecoracionBloque,
    TipoPelota.POLIEDRO to DecoracionPoliedro
)
