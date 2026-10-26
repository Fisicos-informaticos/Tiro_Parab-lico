package org.example.modelo

/**
 * Muro del escenario. Encapsula su geometria, su material y el desgaste que le
 * producen los impactos; nadie puede modificar su estado desde fuera.
 */
class Pared(
    private val inicio: Vector2D,
    private val fin: Vector2D,
    private val tipo: TipoPared = TipoPared.porDefecto()
) : Obstaculo {

    /** 0.0 intacta, 1.0 a punto de romperse. Lo va comiendo el impacto. */
    private var danio: Double = 0.0

    override fun inicio(): Vector2D = inicio

    override fun fin(): Vector2D = fin

    override fun tipo(): TipoPared = tipo

    override fun grosor(): Double = tipo.grosor()

    override fun danio(): Double = danio

    fun direccion(): Vector2D = fin - inicio

    fun longitud(): Double = direccion().magnitud()

    /** Copia con otro extremo final, usada mientras se dibuja la pared. */
    fun conFin(nuevoFin: Vector2D): Pared = Pared(inicio, nuevoFin, tipo)

    /** Copia independiente para previsualizar sin gastar el danio real. */
    fun copia(): Pared {
        val copia = Pared(inicio, fin, tipo)
        copia.danio = danio
        return copia
    }

    /**
     * Suma el efecto del golpe y devuelve true si la pared queda rota. Las
     * pelotas incapaces de danar el material no cuentan, por dura que sea la
     * energia que lleven.
     */
    override fun registrarImpacto(energia: Double, tipoPelota: TipoPelota): Boolean {
        if (!tipo.registraDanio(tipoPelota)) return false

        danio = (danio + energia / tipo.resistencia()).coerceAtMost(1.0)
        return danio >= 1.0
    }

    override fun puntoMasCercano(posicion: Vector2D): Vector2D {
        val d = direccion()
        val longitudCuadrada = d.x() * d.x() + d.y() * d.y()
        if (longitudCuadrada <= EPSILON) return inicio

        val desdeInicio = posicion - inicio
        val t = ((desdeInicio.x() * d.x() + desdeInicio.y() * d.y()) / longitudCuadrada)
            .coerceIn(0.0, 1.0)
        return inicio + d * t
    }

    override fun equals(other: Any?): Boolean =
        other is Pared &&
            inicio == other.inicio &&
            fin == other.fin &&
            tipo == other.tipo &&
            danio == other.danio

    override fun hashCode(): Int {
        var result = inicio.hashCode()
        result = 31 * result + fin.hashCode()
        result = 31 * result + tipo.hashCode()
        result = 31 * result + danio.hashCode()
        return result
    }

    override fun toString(): String = "Pared(inicio=$inicio, fin=$fin, tipo=$tipo, danio=$danio)"

    companion object {
        private const val EPSILON = 1e-9
    }
}
