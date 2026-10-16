package org.example.modelo.colision

import org.example.modelo.Vector2D
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

enum class TipoColision(val etiqueta: String) {
    CIRCULO("Circulo"),
    CAPSULA("Capsula"),
    CAJA("Caja"),
    POLIGONO("Poligono")
}

/**
 * Resultado de una colision.
 *
 * @param profundidad cuanto hay que separar el cuerpo.
 * @param normal direccion (en coordenadas del mundo) hacia la que hay que mover el
 * centro del cuerpo para salir del obstaculo.
 */
data class Penetracion(val profundidad: Double, val normal: Vector2D)

/**
 * Forma de colision de un proyectil, expresada en su propio sistema de coordenadas
 * (origen en el centro del cuerpo). El mundo aplica la rotacion y la traslacion.
 */
interface FormaColision {
    val tipo: TipoColision

    /** Radio de la circunferencia que envuelve a la forma. */
    val radioEnvoltura: Double

    /** Grosor de la forma: radio del circulo o del tubo de la capsula. */
    val radio: Double

    /** Eje mayor de la forma en coordenadas locales, usado para orientarse al volar. */
    val ejePrincipal: Vector2D get() = Vector2D(1.0, 0.0)

    /** Las formas no simetricas se orientan solas por efecto aerodinamico. */
    val alineable: Boolean get() = false

    /**
     * Distancia maxima, medida desde el centro, hasta la que la forma llega en la
     * direccion indicada (soporte del cuerpo convexo).
     */
    fun soporte(direccionLocal: Vector2D): Double

    /**
     * Consulta de colision contra un semiplano (suelo, techo o cara de una pared).
     * [puntoMundo] es un punto del plano y [normalMundo] su normal hacia el lado
     * libre. Devuelve null si el cuerpo no llega a tocarlo.
     */
    fun contactoPlano(
        puntoMundo: Vector2D,
        centro: Vector2D,
        angulo: Double,
        normalMundo: Vector2D
    ): Penetracion? {
        val haciaPlano = Transformaciones.aLocal(-normalMundo, angulo).normalizado
        val delta = centro - puntoMundo
        val separacion = delta.x * normalMundo.x + delta.y * normalMundo.y
        val profundidad = soporte(haciaPlano) - separacion
        return if (profundidad > 0.0) Penetracion(profundidad, normalMundo) else null
    }

    /** Polilinea del contorno en coordenadas locales, usada para dibujar la forma. */
    fun contorno(pasos: Int = 48): List<Vector2D>
}

object Transformaciones {
    fun aLocal(punto: Vector2D, angulo: Double): Vector2D {
        val c = cos(-angulo)
        val s = sin(-angulo)
        return Vector2D(punto.x * c - punto.y * s, punto.x * s + punto.y * c)
    }

    fun aMundo(punto: Vector2D, angulo: Double): Vector2D {
        val c = cos(angulo)
        val s = sin(angulo)
        return Vector2D(punto.x * c - punto.y * s, punto.x * s + punto.y * c)
    }

    /** Diferencia angular mas corta entre dos angulos, en el rango (-PI, PI]. */
    fun diferenciaAngular(objetivo: Double, actual: Double): Double {
        var d = (objetivo - actual) % (2.0 * PI)
        if (d > PI) d -= 2.0 * PI
        if (d < -PI) d += 2.0 * PI
        return d
    }

    fun anguloDe(direccion: Vector2D): Double = atan2(direccion.y, direccion.x)
}
