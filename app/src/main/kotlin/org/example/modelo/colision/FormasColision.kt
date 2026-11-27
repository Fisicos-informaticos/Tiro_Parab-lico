package org.example.modelo.colision

import org.example.modelo.Vector2D
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

class FormaCirculo(private val radio: Double) : FormaColision {
    private val tipo = TipoColision.CIRCULO
    private val radioEnvoltura = radio

    override fun tipo(): TipoColision = tipo

    override fun radioEnvoltura(): Double = radioEnvoltura

    override fun radio(): Double = radio

    override fun soporte(direccionLocal: Vector2D): Double = radio

    override fun contorno(pasos: Int): List<Vector2D> = List(pasos) { i ->
        val a = 2.0 * PI * i / pasos
        Vector2D(cos(a) * radio, sin(a) * radio)
    }
}

class FormaCapsula(
    medioLargo: Double,
    radio: Double
) : FormaColision {
    private val datos = DatosFormaCapsula(medioLargo, radio)

    override fun medioLargo(): Double = datos.medioLargo

    override fun tipo(): TipoColision = datos.tipo

    override fun radioEnvoltura(): Double = datos.radioEnvoltura

    override fun radio(): Double = datos.radio

    override fun ejePrincipal(): Vector2D = datos.ejePrincipal

    override fun alineable(): Boolean = true

    override fun soporte(direccionLocal: Vector2D): Double = datos.soporte(direccionLocal)

    override fun contorno(pasos: Int): List<Vector2D> = datos.contorno(pasos)

    private data class DatosFormaCapsula(
        val medioLargo: Double,
        val radio: Double,
        val tipo: TipoColision = TipoColision.CAPSULA,
        val radioEnvoltura: Double = medioLargo + radio,
        val ejePrincipal: Vector2D = Vector2D(1.0, 0.0)
    ) {
        fun soporte(direccionLocal: Vector2D): Double =
            radio + medioLargo * abs(direccionLocal.x())

        fun contorno(pasos: Int): List<Vector2D> {
            val lista = mutableListOf<Vector2D>()
            val pasosCuerpo = (pasos / 2).coerceAtLeast(4)
            for (i in 0..pasosCuerpo) {
                val a = -PI / 2.0 + PI * i / pasosCuerpo
                lista.add(Vector2D(medioLargo + cos(a) * radio, sin(a) * radio))
            }
            for (i in 0..pasosCuerpo) {
                val a = PI / 2.0 + PI * i / pasosCuerpo
                lista.add(Vector2D(-medioLargo + cos(a) * radio, sin(a) * radio))
            }
            return lista
        }

        fun descripcion(): String =
            "capsula(medioLargo=$medioLargo, radio=$radio, envoltura=$radioEnvoltura, eje=$ejePrincipal, tipo=$tipo)"

        fun esValida(): Boolean =
            medioLargo >= 0 && radio > 0 && radioEnvoltura >= radio && ejePrincipal.magnitud() > 0

        fun resumen(): String = "$tipo ${radioEnvoltura}x${radio} eje=$ejePrincipal"
    }
}

class FormaCaja(
    medioAncho: Double,
    medioAlto: Double
) : FormaColision {
    private val datos = DatosFormaCaja(medioAncho, medioAlto)

    fun medioAncho(): Double = datos.medioAncho

    override fun medioAlto(): Double = datos.medioAlto

    override fun tipo(): TipoColision = datos.tipo

    override fun radioEnvoltura(): Double = datos.radioEnvoltura

    override fun radio(): Double = datos.radio

    override fun ejePrincipal(): Vector2D = datos.ejePrincipal

    override fun alineable(): Boolean = true

    override fun dibujaBrillo(): Boolean = false

    override fun soporte(direccionLocal: Vector2D): Double = datos.soporte(direccionLocal)

    override fun contorno(pasos: Int): List<Vector2D> = datos.contorno()

    private data class DatosFormaCaja(
        val medioAncho: Double,
        val medioAlto: Double,
        val tipo: TipoColision = TipoColision.CAJA,
        val radio: Double = hypot(medioAncho, medioAlto),
        val radioEnvoltura: Double = radio,
        val ejePrincipal: Vector2D = Vector2D(1.0, 0.0)
    ) {
        fun soporte(direccionLocal: Vector2D): Double =
            medioAncho * abs(direccionLocal.x()) + medioAlto * abs(direccionLocal.y())

        fun contorno(): List<Vector2D> = listOf(
            Vector2D(medioAncho, medioAlto),
            Vector2D(-medioAncho, medioAlto),
            Vector2D(-medioAncho, -medioAlto),
            Vector2D(medioAncho, -medioAlto)
        )

        fun descripcion(): String =
            "caja(ancho=$medioAncho, alto=$medioAlto, radio=$radio, envoltura=$radioEnvoltura, eje=$ejePrincipal, tipo=$tipo)"

        fun esValida(): Boolean =
            medioAncho > 0 && medioAlto > 0 && radio >= medioAncho && radioEnvoltura == radio && ejePrincipal.magnitud() > 0

        fun resumen(): String = "$tipo ${medioAncho}x${medioAlto} radio=$radio"
    }
}

class FormaPoligono(
    lados: Int,
    radio: Double
) : FormaColision {
    private val datos = DatosFormaPoligono(lados, radio)

    override fun vertices(): List<Vector2D> = datos.vertices

    fun apotema(): Double = datos.apotema

    override fun tipo(): TipoColision = datos.tipo

    override fun radioEnvoltura(): Double = datos.radioEnvoltura

    override fun radio(): Double = datos.radio

    override fun alineable(): Boolean = true

    override fun soporte(direccionLocal: Vector2D): Double = datos.soporte(direccionLocal)

    override fun contorno(pasos: Int): List<Vector2D> = datos.contorno()

    private data class DatosFormaPoligono(
        val lados: Int,
        val radio: Double,
        val tipo: TipoColision = TipoColision.POLIGONO,
        val radioEnvoltura: Double = radio,
        val vertices: List<Vector2D> = List(lados) { i ->
            val a = 2.0 * PI * i / lados
            Vector2D(cos(a) * radio, sin(a) * radio)
        },
        val apotema: Double = radio * cos(PI / lados)
    ) {
        fun soporte(direccionLocal: Vector2D): Double =
            vertices.maxOf { it.x() * direccionLocal.x() + it.y() * direccionLocal.y() }

        fun contorno(): List<Vector2D> = vertices

        fun descripcion(): String =
            "poligono(lados=$lados, radio=$radio, envoltura=$radioEnvoltura, apotema=$apotema, vertices=${vertices.size}, tipo=$tipo)"

        fun esValida(): Boolean =
            lados >= 3 && radio > 0 && vertices.size == lados && apotema <= radio && radioEnvoltura == radio

        fun resumen(): String = "$tipo $lados lados radio=$radio apotema=$apotema"
    }
}
