package org.example.modelo

data class Pared(
    val inicio: Vector2D,
    val fin: Vector2D,
    val grosor: Double = GROSOR_POR_DEFECTO
) {
    init {
        require(grosor > 0.0) { "El grosor de la pared debe ser positivo" }
    }

    val direccion: Vector2D get() = fin - inicio
    val longitud: Double get() = direccion.magnitud

    fun puntoMasCercano(posicion: Vector2D): Vector2D {
        val d = direccion
        val longitudCuadrada = d.x * d.x + d.y * d.y
        if (longitudCuadrada <= EPSILON) return inicio

        val desdeInicio = posicion - inicio
        val t = ((desdeInicio.x * d.x + desdeInicio.y * d.y) / longitudCuadrada)
            .coerceIn(0.0, 1.0)
        return inicio + d * t
    }

    companion object {
        const val GROSOR_POR_DEFECTO = 10.0
        private const val EPSILON = 1e-9
    }
}
