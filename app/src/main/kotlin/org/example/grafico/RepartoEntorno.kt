package org.example.grafico

import kotlin.math.ceil
import kotlin.math.floor

/** Dimensiones y reparto por celdas compartidos por los generadores del decorado. */
internal object RepartoEntorno {
    const val ANCHO_BRIZNA = 13.0
    const val ANCHO_CELDA_PASTO = 78.0
    const val ANCHO_CELDA_ARBOL = 190.0
    const val ANCHO_CELDA_NUBE = 210.0
    const val ANCHO_CELDA_META = 900.0

    const val BRIZNAS_POR_CELDA = (ANCHO_CELDA_PASTO / ANCHO_BRIZNA).toInt()
    const val MAXIMO_SALTOS = 0.4
    const val ESCALA_ARBOL_MINIMA = 0.72
    const val ESCALA_ARBOL_MAXIMA = 1.34
    const val ALTURA_NUBE_MINIMA = 24.0
    const val ALTURA_NUBE_MAXIMA = 144.0

    /** Hash entero de difusion: el mismo indice siempre da el mismo valor en [0, 1). */
    fun dispersion(indice: Int): Double {
        var h = indice * -1640531527
        h = h xor (h ushr 16)
        h *= -2048144789
        h = h xor (h ushr 13)
        h *= -1028477387
        h = h xor (h ushr 16)
        return (h ushr 8).toDouble() / 16777216.0
    }

    /** Celdas que pueden colocar elementos dentro del tramo [desde, hasta]. */
    fun celdas(anchoCelda: Double, desde: Double, hasta: Double): IntRange {
        val primera = floor((desde - anchoCelda * MAXIMO_SALTOS) / anchoCelda).toInt() - 1
        val ultima = ceil(hasta / anchoCelda).toInt() + 1
        return primera..ultima
    }
}
