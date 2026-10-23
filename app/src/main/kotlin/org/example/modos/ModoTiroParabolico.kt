package org.example.modos

import org.example.entrada.InputHandler
import org.example.modelo.Vector2D
import org.example.simulacion.Simulacion

/**
 * Tiro parabolico: la pelota se coloca en el suelo y sale con el vector
 * contrario al arrastre. Toda la fisica de lanzamiento vive aqui, no en el
 * nucleo (SRP).
 */
open class ModoTiroParabolico : ModoSimulacion {

    override val nombre = CatalogoModos.TIRO_PARABOLICO
    override val ayuda = "Arrastra en el mundo para dibujar"
    override val dibujaVectorDeTiro = true

    override fun colocar(preparacion: Preparacion): Vector2D? {
        if (preparacion.punto.y < preparacion.sueloY - MARGEN_COLOCACION) return null
        return Vector2D(preparacion.punto.x, preparacion.apoyoY)
    }

    override fun arrastrar(preparacion: Preparacion): Vector2D? = null

    override fun velocidadDeSuelta(entrada: InputHandler): Vector2D? {
        val vector = entrada.vectorLanzamiento ?: return null
        if (vector.magnitud < FUERZA_MINIMA_APUNTADO) return null
        return vector.normalizado * (entrada.fuerzaLanzamiento.coerceAtMost(Simulacion.MAX_FUERZA) * ESCALA_VELOCIDAD)
    }

    override fun condicionDePrevia(entrada: InputHandler, posicion: Vector2D): CondicionPrevia? {
        val vector = entrada.vectorLanzamiento ?: return null
        val velocidad = vector.normalizado *
            (entrada.fuerzaLanzamiento.coerceAtMost(Simulacion.MAX_FUERZA) * ESCALA_VELOCIDAD)
        return CondicionPrevia(posicion, velocidad)
    }

    override fun etiquetasDePreparacion(lectura: LecturaModo) = listOf(
        "Angulo: ${ModoSimulacion.formatear(lectura.angulo, 1)}°",
        "Fuerza: ${ModoSimulacion.formatear(lectura.fuerza, 0)}"
    )

    override fun lineasDeColocacion(lectura: LecturaModo) = listOf(
        "Click en el suelo para",
        "colocar la pelota"
    )

    override fun lineasDeApuntado(lectura: LecturaModo) = listOf(
        "Arrastra para lanzar",
        "Angulo: ${ModoSimulacion.formatear(lectura.angulo, 1)}°",
        "Fuerza: ${ModoSimulacion.formatear(lectura.fuerza, 1)}"
    )

    override fun lineasDeVuelo(lectura: LecturaModo) = listOf(
        "Vel: ${ModoSimulacion.formatear(lectura.velocidad.magnitud, 2)} px/s",
        "Pos: (${lectura.posicion.x.toInt()}, ${lectura.posicion.y.toInt()})"
    )

    override fun lineasDeReposo(lectura: LecturaModo) = listOf(
        "Distancia: ${ModoSimulacion.formatear(lectura.distancia, 2)} px",
        "Click para reiniciar"
    )

    companion object {
        /** Rapidez por unidad de fuerza del arrastre. */
        private const val ESCALA_VELOCIDAD = 3.0

        /** Arrastre mas corto que esto no llega a lanzar. */
        private const val FUERZA_MINIMA_APUNTADO = 5.0

        /** Solo se coloca la pelota pegada al suelo. */
        private const val MARGEN_COLOCACION = 30.0
    }
}

/** Nombre historico del modo, conservado para no romper a sus consumidores. */
class TiroParabolico : ModoTiroParabolico()
