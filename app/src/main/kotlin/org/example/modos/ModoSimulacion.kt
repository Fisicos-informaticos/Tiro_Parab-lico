package org.example.modos

import org.example.entrada.InputHandler
import org.example.modelo.Proyectil
import org.example.modelo.Vector2D
import java.util.Locale

interface ModoSimulacion {
    fun nombre(): String
    fun ayuda(): String
    fun dibujaVectorDeTiro(): Boolean = false
    fun colocar(preparacion: Preparacion): Vector2D?
    fun velocidadDeSuelta(entrada: InputHandler): Vector2D?
    fun posicionAlArrastrar(preparacion: Preparacion): Vector2D? = null
    fun condicionDePrevia(entrada: InputHandler, posicion: Vector2D): CondicionPrevia?
    fun etiquetasDePreparacion(lectura: LecturaModo): List<String>
    fun lineasDeColocacion(lectura: LecturaModo): List<String>
    fun lineasDeApuntado(lectura: LecturaModo): List<String>
    fun lineasDeVuelo(lectura: LecturaModo): List<String>
    fun lineasDeReposo(lectura: LecturaModo): List<String>
    companion object {
        fun formatear(valor: Double, cifras: Int): String = String.format(Locale.US, "%.${cifras}f", valor)
    }
}

interface ModoArrastrable {
    fun arrastrar(preparacion: Preparacion): Vector2D
}

class Preparacion(
    private val punto: Vector2D,
    private val origen: Vector2D,
    private val apoyoY: Double,
    private val sueloY: Double,
    private val minY: Double,
    private val maxY: Double
) {
    fun punto(): Vector2D = punto
    fun origen(): Vector2D = origen
    fun apoyoY(): Double = apoyoY
    fun sueloY(): Double = sueloY
    fun minY(): Double = minY
    fun maxY(): Double = maxY
    fun alturaPermitida(): Double = punto.y().coerceIn(minY, maxY)
    fun sobreElSuelo(margen: Double): Boolean = punto.y() >= sueloY - margen
    fun conAlturaPermitida(): Vector2D = Vector2D(punto.x(), alturaPermitida())
}

class LecturaModo(
    private val posicion: Vector2D,
    private val velocidad: Vector2D,
    private val distancia: Double,
    private val altura: Double,
    private val tiempo: Double,
    private val angulo: Double,
    private val fuerza: Double
) {
    fun posicion(): Vector2D = posicion
    fun velocidad(): Vector2D = velocidad
    fun distancia(): Double = distancia
    fun altura(): Double = altura
    fun tiempo(): Double = tiempo
    fun angulo(): Double = angulo
    fun fuerza(): Double = fuerza
    fun rapidez(): Double = velocidad.magnitud()
    fun alturaTexto(cifras: Int = 1): String = ModoSimulacion.formatear(altura, cifras)
    fun velocidadYTexto(cifras: Int = 1): String = ModoSimulacion.formatear(velocidad.y(), cifras)
    fun tiempoTexto(cifras: Int = 2): String = ModoSimulacion.formatear(tiempo, cifras)
    fun resumenLinea(): String = "t=${tiempoTexto()} s  h=${alturaTexto(1)} px  v=${ModoSimulacion.formatear(rapidez(), 1)}"
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
