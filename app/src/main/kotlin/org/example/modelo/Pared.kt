package org.example.modelo

data class Pared(
    val inicio: Vector2D,
    val fin: Vector2D,
    val tipo: TipoPared = TipoPared.POR_DEFECTO
) {
    val grosor: Double get() = tipo.grosor
    val direccion: Vector2D get() = fin - inicio
    val longitud: Double get() = direccion.magnitud

    /** 0.0 intacta, 1.0 a punto de romperse. Lo va comiendo el impacto. */
    var danio: Double = 0.0
        private set

    /**
     * Suma el efecto del golpe y devuelve true si la pared queda rota. Las
     * pelotas incapaces de danar el material no cuentan, por dura que sea la
     * energia que lleven.
     */
    fun registrarImpacto(energia: Double, tipoPelota: TipoPelota): Boolean {
        if (!tipo.registraDanio(tipoPelota)) return false

        danio = (danio + energia / tipo.resistencia).coerceAtMost(1.0)
        return danio >= 1.0
    }

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
        private const val EPSILON = 1e-9
    }
}