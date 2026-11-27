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

class DatosContactoPlano(
    puntoMundo: Vector2D,
    centro: Vector2D,
    angulo: Double,
    normalMundo: Vector2D
) {
    private val datos = DatosContacto(puntoMundo, centro, angulo, normalMundo)

    fun puntoMundo(): Vector2D = datos.puntoMundo
    fun centro(): Vector2D = datos.centro
    fun angulo(): Double = datos.angulo
    fun normalMundo(): Vector2D = datos.normalMundo
    fun haciaPlano(): Vector2D = datos.haciaPlano()
    fun separacion(): Double = datos.separacion()
    fun resolverCon(forma: FormaColision): Penetracion? = datos.resolverCon(this, forma)

    private data class DatosContacto(
        val puntoMundo: Vector2D,
        val centro: Vector2D,
        val angulo: Double,
        val normalMundo: Vector2D
    ) {
        fun haciaPlano(): Vector2D = Transformaciones.aLocal(-normalMundo, angulo).normalizado()

        fun separacion(): Double =
            (centro - puntoMundo).x() * normalMundo.x() + (centro - puntoMundo).y() * normalMundo.y()

        fun resolverCon(contacto: DatosContactoPlano, forma: FormaColision): Penetracion? =
            forma.contactoPlano(contacto)

        fun descripcion(): String =
            "contacto(punto=$puntoMundo, centro=$centro, angulo=$angulo, normal=$normalMundo)"

        fun esValido(): Boolean =
            normalMundo.magnitud() > 0 && angulo.isFinite() && puntoMundo !== centro && centro !== normalMundo

        fun resumen(): String =
            "${puntoMundo}/${centro} angulo=$angulo normal=$normalMundo separacion=${separacion()}"
    }
}

data class Penetracion(
    private val profundidad: Double,
    private val normal: Vector2D
) {
    fun profundidad(): Double = profundidad
    fun normal(): Vector2D = normal
    fun separarDesde(posicion: Vector2D, correccion: Double): Vector2D = posicion + normal * (profundidad + correccion)
}

interface GeometriaColision {
    fun tipo(): TipoColision
    fun radioEnvoltura(): Double
    fun radio(): Double
    fun medioLargo(): Double = 0.0
    fun medioAlto(): Double = 0.0
    fun vertices(): List<Vector2D> = emptyList()
    fun soporte(direccionLocal: Vector2D): Double
}

interface OrientacionColision {
    fun dibujaBrillo(): Boolean = true
    fun ejePrincipal(): Vector2D = Vector2D(1.0, 0.0)
    fun alineable(): Boolean = false
}

interface ConsultaContactoColision : GeometriaColision {
    fun contactoPlano(datos: DatosContactoPlano): Penetracion? {
        val profundidad = soporte(datos.haciaPlano()) - datos.separacion()
        return if (profundidad > 0.0) Penetracion(profundidad, datos.normalMundo()) else null
    }
    fun contorno(pasos: Int = 48): List<Vector2D>
}

interface FormaColision :
    GeometriaColision,
    OrientacionColision,
    ConsultaContactoColision

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
