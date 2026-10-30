package org.example.modelo.colision

import org.example.modelo.Vector2D
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

enum class TipoColision(private val etiqueta: String) {
    CIRCULO("Circulo"),
    CAPSULA("Capsula"),
    CAJA("Caja"),
    POLIGONO("Poligono");

    fun etiqueta(): String = etiqueta
}

/**
 * Resultado de una colision. Ademas de consultarse, sabe como separar al cuerpo
 * que la ha sufrido de la superficie con la que ha chocado.
 */
class Penetracion(
    private val profundidad: Double,
    private val normal: Vector2D
) {
    fun profundidad(): Double = profundidad

    fun normal(): Vector2D = normal

    /** Devuelve la posicion corregida del cuerpo para sacarlo de la superficie. */
    fun separarDesde(posicion: Vector2D, correccion: Double): Vector2D =
        posicion + normal * (profundidad + correccion)

    override fun equals(other: Any?): Boolean =
        other is Penetracion && profundidad == other.profundidad && normal == other.normal

    override fun hashCode(): Int = 31 * profundidad.hashCode() + normal.hashCode()

    override fun toString(): String = "Penetracion(profundidad=$profundidad, normal=$normal)"
}

/**
 * Forma de colision de un proyectil, expresada en su propio sistema de coordenadas
 * (origen en el centro del cuerpo). El mundo aplica la rotacion y la traslacion.
 */
interface FormaColision {
    fun tipo(): TipoColision

    /** Radio de la circunferencia que envuelve a la forma. */
    fun radioEnvoltura(): Double

    /** Grosor de la forma: radio del circulo o del tubo de la capsula. */
    fun radio(): Double

    /** Semilongitud de una capsula; cero en el resto de formas. */
    fun medioLargo(): Double = 0.0

    /** Semialtura de una caja; cero en el resto de formas. */
    fun medioAlto(): Double = 0.0

    /** Vertices propios de un poligono; vacio en el resto de formas. */
    fun vertices(): List<Vector2D> = emptyList()

    /** Si la forma admite el brillo decorativo de las pelotas redondeadas. */
    fun dibujaBrillo(): Boolean = true

    /** Eje mayor de la forma en coordenadas locales, usado para orientarse al volar. */
    fun ejePrincipal(): Vector2D = Vector2D(1.0, 0.0)

    /** Las formas no simetricas se orientan solas por efecto aerodinamico. */
    fun alineable(): Boolean = false

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
        val haciaPlano = Transformaciones.aLocal(-normalMundo, angulo).normalizado()
        val delta = centro - puntoMundo
        val separacion = delta.x() * normalMundo.x() + delta.y() * normalMundo.y()
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
        return Vector2D(punto.x() * c - punto.y() * s, punto.x() * s + punto.y() * c)
    }

    fun aMundo(punto: Vector2D, angulo: Double): Vector2D {
        val c = cos(angulo)
        val s = sin(angulo)
        return Vector2D(punto.x() * c - punto.y() * s, punto.x() * s + punto.y() * c)
    }

    /** Diferencia angular mas corta entre dos angulos, en el rango (-PI, PI]. */
    fun diferenciaAngular(objetivo: Double, actual: Double): Double {
        var d = (objetivo - actual) % (2.0 * PI)
        if (d > PI) d -= 2.0 * PI
        if (d < -PI) d += 2.0 * PI
        return d
    }

    fun anguloDe(direccion: Vector2D): Double = atan2(direccion.y(), direccion.x())
}
