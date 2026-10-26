package org.example.modos

import org.example.entrada.InputHandler
import org.example.modelo.Vector2D

/**
 * Caida libre: la velocidad inicial en X es cero, asi que la pelota solo se
 * mueve en el eje Y bajo la gravedad seleccionada. El arrastre no apunta, sino
 * que fija la altura inicial (h0) desde la que se suelta.
 */
open class ModoCaidaLibre : ModoSimulacion {

    override fun nombre(): String = CatalogoModos.CAIDA_LIBRE

    override fun ayuda(): String = "Coloca la pelota y arrastra en vertical"

    override fun dibujaVectorDeTiro(): Boolean = false

    override fun colocar(preparacion: Preparacion): Vector2D =
        Vector2D(preparacion.punto().x(), preparacion.alturaPermitida())

    /** La pelota mantiene su columna (x) y solo sube o baja con el puntero. */
    override fun arrastrar(preparacion: Preparacion): Vector2D =
        Vector2D(preparacion.origen().x(), preparacion.alturaPermitida())

    /** Suelta desde el reposo: no hay velocidad horizontal. */
    override fun velocidadDeSuelta(entrada: InputHandler): Vector2D = Vector2D(0.0, 0.0)

    override fun condicionDePrevia(entrada: InputHandler, posicion: Vector2D) =
        CondicionPrevia(posicion, Vector2D(0.0, 0.0))

    override fun etiquetasDePreparacion(lectura: LecturaModo) = listOf(
        "h0: ${lectura.alturaTexto(1)} px"
    )

    override fun lineasDeColocacion(lectura: LecturaModo) = listOf(
        "Click para colocar la pelota",
        "a la altura que quieras"
    )

    override fun lineasDeApuntado(lectura: LecturaModo) = listOf(
        "Arrastra en vertical",
        "h0: ${lectura.alturaTexto(1)} px",
        "Suelta para dejar caer"
    )

    override fun lineasDeVuelo(lectura: LecturaModo) = listOf(
        "h: ${lectura.alturaTexto(1)} px",
        "Vy: ${lectura.velocidadYTexto(1)} px/s",
        "t: ${lectura.tiempoTexto(2)} s"
    )

    override fun lineasDeReposo(lectura: LecturaModo) = listOf(
        "h: ${lectura.alturaTexto(1)} px",
        "Vy: ${lectura.velocidadYTexto(1)} px/s",
        "t: ${lectura.tiempoTexto(2)} s",
        "Click para reiniciar"
    )
}

/** Nombre historico del modo, conservado para no romper a sus consumidores. */
class CaidaLibre : ModoCaidaLibre()
