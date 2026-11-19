package org.example.modelo.colision

import org.example.modelo.Vector2D
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
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
    private val medioLargo: Double,
    private val radio: Double
) : FormaColision {
    private val tipo = TipoColision.CAPSULA
    private val radioEnvoltura = medioLargo + radio
    private val ejePrincipal = Vector2D(1.0, 0.0)

    override fun medioLargo(): Double = medioLargo

    override fun tipo(): TipoColision = tipo

    override fun radioEnvoltura(): Double = radioEnvoltura

    override fun radio(): Double = radio

    override fun ejePrincipal(): Vector2D = ejePrincipal

    override fun alineable(): Boolean = true

    override fun soporte(direccionLocal: Vector2D): Double =
        radio + medioLargo * abs(direccionLocal.x())

    override fun contorno(pasos: Int): List<Vector2D> {
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
}

class FormaCaja(
    private val medioAncho: Double,
    private val medioAlto: Double
) : FormaColision {
    private val tipo = TipoColision.CAJA
    private val radio = kotlin.math.hypot(medioAncho, medioAlto)
    private val radioEnvoltura = radio
    private val ejePrincipal = Vector2D(1.0, 0.0)

    fun medioAncho(): Double = medioAncho

    override fun medioAlto(): Double = medioAlto

    override fun tipo(): TipoColision = tipo

    override fun radioEnvoltura(): Double = radioEnvoltura

    override fun radio(): Double = radio

    override fun ejePrincipal(): Vector2D = ejePrincipal

    override fun alineable(): Boolean = true

    override fun dibujaBrillo(): Boolean = false

    override fun soporte(direccionLocal: Vector2D): Double =
        medioAncho * abs(direccionLocal.x()) + medioAlto * abs(direccionLocal.y())

    override fun contorno(pasos: Int): List<Vector2D> = listOf(
        Vector2D(medioAncho, medioAlto),
        Vector2D(-medioAncho, medioAlto),
        Vector2D(-medioAncho, -medioAlto),
        Vector2D(medioAncho, -medioAlto)
    )
}

class FormaPoligono(
    private val lados: Int,
    private val radio: Double
) : FormaColision {
    private val tipo = TipoColision.POLIGONO
    private val radioEnvoltura = radio
    private val vertices: List<Vector2D> = List(lados) { i ->
        val a = 2.0 * PI * i / lados
        Vector2D(cos(a) * radio, sin(a) * radio)
    }

    /** Distancia del centro al centro de una cara. */
    private val apotema: Double = radio * cos(PI / lados)

    override fun vertices(): List<Vector2D> = vertices

    fun apotema(): Double = apotema

    override fun tipo(): TipoColision = tipo

    override fun radioEnvoltura(): Double = radioEnvoltura

    override fun radio(): Double = radio

    override fun alineable(): Boolean = true

    override fun soporte(direccionLocal: Vector2D): Double =
        vertices.maxOf { it.x() * direccionLocal.x() + it.y() * direccionLocal.y() }

    override fun contorno(pasos: Int): List<Vector2D> = vertices
}
