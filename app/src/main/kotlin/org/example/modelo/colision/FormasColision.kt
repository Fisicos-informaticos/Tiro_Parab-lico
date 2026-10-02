package org.example.modelo.colision

import org.example.modelo.Vector2D
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class FormaCirculo(override val radio: Double) : FormaColision {
    override val tipo = TipoColision.CIRCULO
    override val radioEnvoltura = radio
    override fun soporte(direccionLocal: Vector2D) = radio
    override fun contorno(pasos: Int) = List(pasos) { i ->
        val a = 2.0 * PI * i / pasos
        Vector2D(cos(a) * radio, sin(a) * radio)
    }
}

class FormaCapsula(val medioLargo: Double, override val radio: Double) : FormaColision {
    override val tipo = TipoColision.CAPSULA
    override val radioEnvoltura = medioLargo + radio
    override val ejePrincipal = Vector2D(1.0, 0.0)
    override val alineable = true

    override fun soporte(direccionLocal: Vector2D): Double =
        radio + medioLargo * abs(direccionLocal.x)

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

class FormaCaja(val medioAncho: Double, val medioAlto: Double) : FormaColision {
    override val tipo = TipoColision.CAJA
    override val radio = kotlin.math.hypot(medioAncho, medioAlto)
    override val radioEnvoltura = radio
    override val ejePrincipal = Vector2D(1.0, 0.0)
    override val alineable = true

    override fun soporte(direccionLocal: Vector2D): Double =
        medioAncho * abs(direccionLocal.x) + medioAlto * abs(direccionLocal.y)

    override fun contorno(pasos: Int): List<Vector2D> = listOf(
        Vector2D(medioAncho, medioAlto),
        Vector2D(-medioAncho, medioAlto),
        Vector2D(-medioAncho, -medioAlto),
        Vector2D(medioAncho, -medioAlto)
    )
}

class FormaPoligono(val lados: Int, override val radio: Double) : FormaColision {
    override val tipo = TipoColision.POLIGONO
    override val radioEnvoltura = radio
    override val alineable = true

    val vertices: List<Vector2D> = List(lados) { i ->
        val a = 2.0 * PI * i / lados
        Vector2D(cos(a) * radio, sin(a) * radio)
    }

    /** Distancia del centro al centro de una cara. */
    val apotema: Double = radio * cos(PI / lados)

    private val normales: List<Vector2D> = List(lados) { i ->
        val siguiente = vertices[(i + 1) % lados]
        Vector2D(siguiente.y - vertices[i].y, vertices[i].x - siguiente.x).normalizado
    }

    override fun soporte(direccionLocal: Vector2D): Double =
        vertices.maxOf { it.x * direccionLocal.x + it.y * direccionLocal.y }

    override fun contorno(pasos: Int): List<Vector2D> = vertices
}
