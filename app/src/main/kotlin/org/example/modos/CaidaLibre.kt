package org.example.modos

import org.example.entrada.InputHandler
import org.example.modelo.Vector2D

/**
 * Caida libre: la velocidad inicial en X es cero, asi que la pelota solo se
 * mueve en el eje Y bajo la gravedad seleccionada. El arrastre no apunta, sino
 * que fija la altura inicial (h0) desde la que se suelta.
 */
class CaidaLibre : ModoSimulacion {

    override val nombre = CatalogoModos.CAIDA_LIBRE
    override val ayuda = "Coloca la pelota y arrastra en vertical"
    override val dibujaVectorDeTiro = false

    override fun colocar(preparacion: Preparacion): Vector2D =
        Vector2D(preparacion.punto.x, alturaValida(preparacion))

    /** La pelota mantiene su columna (x) y solo sube o baja con el puntero. */
    override fun arrastrar(preparacion: Preparacion): Vector2D =
        Vector2D(preparacion.origen.x, alturaValida(preparacion))

    /** Suelta desde el reposo: no hay velocidad horizontal. */
    override fun velocidadDeSuelta(entrada: InputHandler): Vector2D = Vector2D(0.0, 0.0)

    override fun condicionDePrevia(entrada: InputHandler, posicion: Vector2D) =
        CondicionPrevia(posicion, Vector2D(0.0, 0.0))

    override fun etiquetasDePreparacion(lectura: LecturaModo) = listOf(
        "h0: ${"%.1f".format(lectura.altura)} px"
    )

    override fun lineasDeColocacion(lectura: LecturaModo) = listOf(
        "Click para colocar la pelota",
        "a la altura que quieras"
    )

    override fun lineasDeApuntado(lectura: LecturaModo) = listOf(
        "Arrastra en vertical",
        "h0: ${"%.1f".format(lectura.altura)} px",
        "Suelta para dejar caer"
    )

    override fun lineasDeVuelo(lectura: LecturaModo) = listOf(
        "h: ${"%.1f".format(lectura.altura)} px",
        "Vy: ${"%.1f".format(lectura.velocidad.y)} px/s",
        "t: ${"%.2f".format(lectura.tiempo)} s"
    )

    override fun lineasDeReposo(lectura: LecturaModo) = listOf(
        "h: ${"%.1f".format(lectura.altura)} px",
        "Vy: ${"%.1f".format(lectura.velocidad.y)} px/s",
        "t: ${"%.2f".format(lectura.tiempo)} s",
        "Click para reiniciar"
    )

    private fun alturaValida(preparacion: Preparacion): Double =
        preparacion.punto.y.coerceIn(preparacion.minY, preparacion.maxY)
}
