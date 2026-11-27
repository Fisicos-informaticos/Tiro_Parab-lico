package org.example.fisica

import org.example.modelo.Vector2D

class Tierra(
    ancho: Double = 1000.0,
    alto: Double = 650.0,
    gravedad: Vector2D = Vector2D(0.0, 9.81)
) : Ambiente {
    private val datos = DatosTierra(ancho, alto, gravedad)

    override fun ancho(): Double = datos.ancho
    override fun alto(): Double = datos.alto
    override fun gravedad(): Vector2D = datos.gravedad
    override fun densidadAire(): Double = datos.densidadAire
    override fun sueloY(): Double = datos.sueloY
    override fun techoY(): Double = datos.techoY

    fun estaSobreSuelo(y: Double, margen: Double = 0.0): Boolean = datos.estaSobreSuelo(y, margen)
    fun gravedadEscalada(factor: Double): Vector2D = datos.gravedadEscalada(factor)

    private data class DatosTierra(
        val ancho: Double,
        val alto: Double,
        val gravedad: Vector2D,
        val densidadAire: Double = 1.225,
        val sueloY: Double = alto - 50.0,
        val techoY: Double = 0.0
    ) {
        fun estaSobreSuelo(y: Double, margen: Double = 0.0): Boolean = y >= sueloY - margen

        fun gravedadEscalada(factor: Double): Vector2D =
            Vector2D(gravedad.x(), Tierra.GRAVEDAD_PIXELES * factor)

        fun descripcion(): String =
            "tierra(ancho=$ancho, alto=$alto, gravedad=$gravedad, densidad=$densidadAire, suelo=$sueloY, techo=$techoY)"

        fun esValida(): Boolean =
            ancho > 0 && alto > 0 && gravedad.magnitud() > 0 && densidadAire > 0 && sueloY > techoY

        fun resumen(): String =
            "${ancho}x${alto} g=$gravedad suelo=$sueloY techo=$techoY densidad=$densidadAire"
    }

    companion object {
        const val GRAVEDAD_PIXELES = 1500.0
    }
}
