package org.example.modos

import org.example.entrada.InputHandler
import org.example.modelo.Pelota
import org.example.modelo.Vector2D
import java.util.Locale

/**
 * Contrato de un modo de simulacion. El nucleo ([org.example.simulacion.Simulacion])
 * y el presentador solo conocen esta abstraccion, asi que anadir un modo nuevo es
 * implementar esta interfaz sin tocar el motor ni el renderizado (OCP). Cualquier
 * implementacion puede sustituir a otra sin romper a quien la usa (LSP).
 */
interface ModoSimulacion {

    /** Nombre que aparece en el selector de la interfaz. */
    fun nombre(): String

    /** Texto de ayuda del panel lateral para este modo. */
    fun ayuda(): String

    /** Si la preparacion dibuja el vector de tiro (flecha, angulo y fuerza). */
    fun dibujaVectorDeTiro(): Boolean

    /** Posicion donde se coloca la pelota al pulsar; null si el punto no vale. */
    fun colocar(preparacion: Preparacion): Vector2D?

    /** Nueva posicion mientras se arrastra; null si el modo no mueve la pelota. */
    fun arrastrar(preparacion: Preparacion): Vector2D?

    /** Velocidad con la que se suelta la pelota; null si aun no procede lanzar. */
    fun velocidadDeSuelta(entrada: InputHandler): Vector2D?

    /** Condiciones de la trayectoria previsualizada; null si no hay nada que ver. */
    fun condicionDePrevia(entrada: InputHandler, posicion: Vector2D): CondicionPrevia?

    /** Textos que se pintan junto a la pelota mientras se prepara la suelta. */
    fun etiquetasDePreparacion(lectura: LecturaModo): List<String>

    /** Lineas del panel de informacion en cada momento de la partida. */
    fun lineasDeColocacion(lectura: LecturaModo): List<String>
    fun lineasDeApuntado(lectura: LecturaModo): List<String>
    fun lineasDeVuelo(lectura: LecturaModo): List<String>
    fun lineasDeReposo(lectura: LecturaModo): List<String>

    companion object {
        /** Formato estable con punto decimal, independiente del idioma del sistema. */
        fun formatear(valor: Double, cifras: Int): String =
            String.format(Locale.US, "%.${cifras}f", valor)
    }
}

/**
 * Datos de solo lectura del mundo que un modo necesita para ubicar la pelota.
 * Encapsula las comprobaciones de altura que antes hacian los modos.
 */
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

    /** Altura del puntero limitada a la franja visible del mundo. */
    fun alturaPermitida(): Double = punto.y().coerceIn(minY, maxY)

    /** Cierto si el punto esta lo bastante cerca del suelo para colocar la pelota. */
    fun sobreElSuelo(margen: Double): Boolean = punto.y() >= sueloY - margen
}

/**
 * Instantanea del mundo traducida a texto por el modo activo. Ademas de exponer
 * los valores, ofrece las magnitudes derivadas que necesitan los paneles.
 */
class LecturaModo(
    private val posicion: Vector2D,
    private val velocidad: Vector2D,
    private val distancia: Double,
    /** Altura sobre el suelo: cero cuando la pelota apoya. */
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

    fun velocidadYTexto(cifras: Int = 1): String =
        ModoSimulacion.formatear(velocidad.y(), cifras)

    fun tiempoTexto(cifras: Int = 2): String = ModoSimulacion.formatear(tiempo, cifras)
}

/** Punto de partida de la trayectoria dibujada mientras se prepara la suelta. */
class CondicionPrevia(
    private val posicion: Vector2D,
    private val velocidad: Vector2D
) {
    fun posicion(): Vector2D = posicion

    fun velocidad(): Vector2D = velocidad

    /** Coloca la pelota de prueba con esta condicion inicial. */
    fun aplicarA(pelota: Pelota) {
        pelota.fijarPosicion(posicion)
        pelota.fijarVelocidad(velocidad)
    }
}
