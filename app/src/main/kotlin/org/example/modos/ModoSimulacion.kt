package org.example.modos

import org.example.entrada.InputHandler
import org.example.modelo.Vector2D

/**
 * Contrato de un modo de simulacion. El nucleo ([org.example.simulacion.Simulacion])
 * y el presentador solo conocen esta abstraccion, asi que anadir un modo nuevo es
 * implementar esta interfaz sin tocar el motor ni el renderizado (OCP). Cualquier
 * implementacion puede sustituir a otra sin romper a quien la usa (LSP).
 */
interface ModoSimulacion {

    /** Nombre que aparece en el selector de la interfaz. */
    val nombre: String

    /** Texto de ayuda del panel lateral para este modo. */
    val ayuda: String

    /** Si la preparacion dibuja el vector de tiro (flecha, angulo y fuerza). */
    val dibujaVectorDeTiro: Boolean

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
}

/**
 * Datos de solo lectura del mundo que un modo necesita para ubicar la pelota.
 * La calcula el nucleo, de modo que el modo no depende de la simulacion concreta.
 */
data class Preparacion(
    /** Punto pulsado o arrastrado, ya convertido a coordenadas de mundo. */
    val punto: Vector2D,
    /** Posicion actual de la pelota. */
    val origen: Vector2D,
    /** y de reposo bajo el punto: suelo o superficie de lanzamiento. */
    val apoyoY: Double,
    /** y del suelo del mundo. */
    val sueloY: Double,
    /** y minima visible en la que la pelota puede estar. */
    val minY: Double,
    /** y maxima visible en la que la pelota puede estar. */
    val maxY: Double
)

/** Instantanea del mundo traducida a texto por el modo activo. */
data class LecturaModo(
    val posicion: Vector2D,
    val velocidad: Vector2D,
    val distancia: Double,
    /** Altura sobre el suelo: cero cuando la pelota apoya. */
    val altura: Double,
    val tiempo: Double,
    val angulo: Double,
    val fuerza: Double
)

/** Punto de partida de la trayectoria dibujada mientras se prepara la suelta. */
data class CondicionPrevia(
    val posicion: Vector2D,
    val velocidad: Vector2D
)
