package org.example.grafico

/**
 * Punto de acceso al decorado del mapa. Las dimensiones viven en
 * [RepartoEntorno] y cada elemento tiene su propio generador, de modo que el
 * reparto del pasto, los arboles, las nubes y las metas evoluciona por separado.
 */
object GeneradorEntorno {
    const val ANCHO_BRIZNA = RepartoEntorno.ANCHO_BRIZNA
    const val ANCHO_CELDA_PASTO = RepartoEntorno.ANCHO_CELDA_PASTO
    const val ANCHO_CELDA_ARBOL = RepartoEntorno.ANCHO_CELDA_ARBOL
    const val ANCHO_CELDA_NUBE = RepartoEntorno.ANCHO_CELDA_NUBE
    const val ANCHO_CELDA_META = RepartoEntorno.ANCHO_CELDA_META

    /** Hash entero de difusion: el mismo indice siempre da el mismo valor en [0, 1). */
    fun dispersion(indice: Int): Double = RepartoEntorno.dispersion(indice)

    /** Celdas que pueden colocar elementos dentro del tramo [desde, hasta]. */
    fun celdas(anchoCelda: Double, desde: Double, hasta: Double): IntRange =
        RepartoEntorno.celdas(anchoCelda, desde, hasta)

    fun pasto(desde: Double, hasta: Double): List<Brizna> =
        GeneradorPasto.generar(desde, hasta)

    fun arboles(desde: Double, hasta: Double): List<Arbol> =
        GeneradorArboles.generar(desde, hasta)

    fun nubes(desde: Double, hasta: Double): List<Nube> =
        GeneradorNubes.generar(desde, hasta)

    fun metas(desde: Double, hasta: Double): List<Meta> =
        GeneradorMetas.generar(desde, hasta)
}
