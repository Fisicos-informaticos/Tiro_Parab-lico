package org.example.grafico

import org.example.modelo.Vector2D

/**
 * Contrato para pintar los vectores fisicos sobre el mundo. El renderer
 * principal depende de esta abstraccion en lugar de crear un [VectorRenderer].
 */
interface DibujoVectoresFisica {

    fun dibujar(centro: Vector2D, velocidad: Vector2D, gravedad: Vector2D)
}
