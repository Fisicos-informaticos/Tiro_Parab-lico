package org.example.modos

import org.example.entrada.InputHandler
import org.example.modelo.Proyectil
import org.example.modelo.Vector2D
import java.util.Locale

interface IdentidadModo {
    fun nombre(): String
    fun ayuda(): String
    fun dibujaVectorDeTiro(): Boolean = false
}

interface PreparacionModo {
    fun colocar(preparacion: Preparacion): Vector2D?
    fun velocidadDeSuelta(entrada: InputHandler): Vector2D?
    fun posicionAlArrastrar(preparacion: Preparacion): Vector2D? = null
    fun condicionDePrevia(entrada: InputHandler, posicion: Vector2D): CondicionPrevia?
}

interface PresentacionModo {
    fun etiquetasDePreparacion(lectura: LecturaModo): List<String>
    fun lineasDeColocacion(lectura: LecturaModo): List<String>
    fun lineasDeApuntado(lectura: LecturaModo): List<String>
    fun lineasDeVuelo(lectura: LecturaModo): List<String>
    fun lineasDeReposo(lectura: LecturaModo): List<String>
}

interface ModoSimulacion : IdentidadModo, PreparacionModo, PresentacionModo {
    companion object {
        fun formatear(valor: Double, cifras: Int): String = String.format(Locale.US, "%.${cifras}f", valor)
    }
}

interface ModoArrastrable {
    fun arrastrar(preparacion: Preparacion): Vector2D
}

class Preparacion(
    punto: Vector2D,
    origen: Vector2D,
    apoyoY: Double,
    sueloY: Double,
    minY: Double,
    maxY: Double
) {
    private val datos = DatosPreparacion(punto, origen, apoyoY, sueloY, minY, maxY)

    fun punto(): Vector2D = datos.punto
    fun origen(): Vector2D = datos.origen
    fun apoyoY(): Double = datos.apoyoY
    fun sueloY(): Double = datos.sueloY
    fun minY(): Double = datos.minY
    fun maxY(): Double = datos.maxY
    fun alturaPermitida(): Double = datos.alturaPermitida()
    fun sobreElSuelo(margen: Double): Boolean = datos.sobreElSuelo(margen)
    fun conAlturaPermitida(): Vector2D = datos.conAlturaPermitida()

    private data class DatosPreparacion(
        val punto: Vector2D,
        val origen: Vector2D,
        val apoyoY: Double,
        val sueloY: Double,
        val minY: Double,
        val maxY: Double
    ) {
        fun alturaPermitida(): Double = punto.y().coerceIn(minY, maxY)

        fun sobreElSuelo(margen: Double): Boolean = punto.y() >= sueloY - margen

        fun conAlturaPermitida(): Vector2D = Vector2D(punto.x(), alturaPermitida())

        fun descripcion(): String =
            "preparacion(punto=$punto, origen=$origen, apoyo=$apoyoY, suelo=$sueloY, min=$minY, max=$maxY)"

        fun esValida(): Boolean =
            minY <= maxY && apoyoY >= minY && sueloY >= minY && punto !== origen

        fun resumen(): String =
            "$punto desde $origen (apoyo=$apoyoY, suelo=$sueloY, rango=$minY..$maxY)"
    }
}

class LecturaModo(
    posicion: Vector2D,
    velocidad: Vector2D,
    distancia: Double,
    altura: Double,
    tiempo: Double,
    angulo: Double,
    fuerza: Double
) {
    private val datos = DatosLecturaModo(posicion, velocidad, distancia, altura, tiempo, angulo, fuerza)

    fun posicion(): Vector2D = datos.posicion
    fun velocidad(): Vector2D = datos.velocidad
    fun distancia(): Double = datos.distancia
    fun altura(): Double = datos.altura
    fun tiempo(): Double = datos.tiempo
    fun angulo(): Double = datos.angulo
    fun fuerza(): Double = datos.fuerza
    fun rapidez(): Double = datos.rapidez()
    fun alturaTexto(cifras: Int = 1): String = datos.alturaTexto(cifras)
    fun velocidadYTexto(cifras: Int = 1): String = datos.velocidadYTexto(cifras)
    fun tiempoTexto(cifras: Int = 2): String = datos.tiempoTexto(cifras)
    fun resumenLinea(): String = datos.resumenLinea()

    private data class DatosLecturaModo(
        val posicion: Vector2D,
        val velocidad: Vector2D,
        val distancia: Double,
        val altura: Double,
        val tiempo: Double,
        val angulo: Double,
        val fuerza: Double
    ) {
        fun rapidez(): Double = velocidad.magnitud()

        fun alturaTexto(cifras: Int = 1): String = ModoSimulacion.formatear(altura, cifras)

        fun velocidadYTexto(cifras: Int = 1): String = ModoSimulacion.formatear(velocidad.y(), cifras)

        fun tiempoTexto(cifras: Int = 2): String = ModoSimulacion.formatear(tiempo, cifras)

        fun resumenLinea(): String =
            "t=${tiempoTexto()} s  h=${alturaTexto(1)} px  v=${ModoSimulacion.formatear(rapidez(), 1)}"

        fun datosCompletos(): List<Any> = listOf(posicion, velocidad, distancia, altura, tiempo, angulo, fuerza)

        fun descripcion(): String =
            "lectura(posicion=$posicion, velocidad=$velocidad, distancia=$distancia, altura=$altura, tiempo=$tiempo, angulo=$angulo, fuerza=$fuerza)"

        fun esValida(): Boolean =
            distancia >= 0 && altura >= 0 && tiempo >= 0 && fuerza >= 0 && posicion !== velocidad
    }
}

class CondicionPrevia(
    private val posicion: Vector2D,
    private val velocidad: Vector2D
) {
    fun posicion(): Vector2D = posicion
    fun velocidad(): Vector2D = velocidad
    fun aplicarA(pelota: Proyectil) {
        pelota.fijarPosicion(posicion)
        pelota.fijarVelocidad(velocidad)
    }
    fun esLanzable(): Boolean = velocidad.magnitud() > 1e-6
}
