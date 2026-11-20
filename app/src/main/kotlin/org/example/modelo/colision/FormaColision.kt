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

data class DatosContactoPlano(
    private val puntoMundo: Vector2D,
    private val centro: Vector2D,
    private val angulo: Double,
    private val normalMundo: Vector2D
) {
    fun puntoMundo(): Vector2D = puntoMundo
    fun centro(): Vector2D = centro
    fun angulo(): Double = angulo
    fun normalMundo(): Vector2D = normalMundo
    fun haciaPlano(): Vector2D = Transformaciones.aLocal(-normalMundo(), angulo()).normalizado()
    fun separacion(): Double = (centro() - puntoMundo()).x() * normalMundo().x() + (centro() - puntoMundo()).y() * normalMundo().y()

    fun resolverCon(forma: FormaColision): Penetracion? = forma.contactoPlano(this)
}

data class Penetracion(
    private val profundidad: Double,
    private val normal: Vector2D
) {
    fun profundidad(): Double = profundidad
    fun normal(): Vector2D = normal
    fun separarDesde(posicion: Vector2D, correccion: Double): Vector2D = posicion + normal * (profundidad + correccion)
}

interface FormaColision {
    fun tipo(): TipoColision
    fun radioEnvoltura(): Double
    fun radio(): Double
    fun medioLargo(): Double = 0.0
    fun medioAlto(): Double = 0.0
    fun vertices(): List<Vector2D> = emptyList()
    fun dibujaBrillo(): Boolean = true
    fun ejePrincipal(): Vector2D = Vector2D(1.0, 0.0)
    fun alineable(): Boolean = false
    fun soporte(direccionLocal: Vector2D): Double
    fun contactoPlano(datos: DatosContactoPlano): Penetracion? {
        val profundidad = soporte(datos.haciaPlano()) - datos.separacion()
        return if (profundidad > 0.0) Penetracion(profundidad, datos.normalMundo()) else null
    }
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
    fun diferenciaAngular(objetivo: Double, actual: Double): Double {
        var d = (objetivo - actual) % (2.0 * PI)
        if (d > PI) d -= 2.0 * PI
        if (d < -PI) d += 2.0 * PI
        return d
    }
    fun anguloDe(direccion: Vector2D): Double = atan2(direccion.y(), direccion.x())
}
