package org.example.grafico

import kotlin.math.ceil
import kotlin.math.floor

/**
 * Reparto del decorado sobre el mapa. Todo se genera por celdas de tamaño fijo, de
 * forma que la cantidad de Pasto, Arboles y Nubes es siempre la misma en cualquier
 * zona y el mismo punto del mundo dibuja siempre lo mismo. El renderer solo pinta
 * lo que sale de aqui.
 */
object GeneradorEntorno {
    const val ANCHO_BRIZNA = 13.0
    const val ANCHO_CELDA_PASTO = 78.0
    const val ANCHO_CELDA_ARBOL = 190.0
    const val ANCHO_CELDA_NUBE = 210.0
    const val ANCHO_CELDA_META = 900.0

    private const val BRIZNAS_POR_CELDA = (ANCHO_CELDA_PASTO / ANCHO_BRIZNA).toInt()
    private const val MAXIMO_SALTOS = 0.4
    private const val ESCALA_ARBOL_MINIMA = 0.72
    private const val ESCALA_ARBOL_MAXIMA = 1.34
    private const val ALTURA_NUBE_MINIMA = 24.0
    private const val ALTURA_NUBE_MAXIMA = 144.0

    data class Brizna(val x: Double, val alto: Double, val ancho: Double, val brillo: Double)
    data class Arbol(val x: Double, val escala: Double, val claro: Boolean)
    data class Nube(val x: Double, val y: Double, val escala: Double, val opacidad: Double)
    data class Meta(val x: Double)

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

    fun pasto(desde: Double, hasta: Double): List<Brizna> {
        val lista = mutableListOf<Brizna>()
        for (celda in celdas(ANCHO_CELDA_PASTO, desde, hasta)) {
            for (j in 0 until BRIZNAS_POR_CELDA) {
                val semilla = celda * BRIZNAS_POR_CELDA + j
                lista.add(
                    Brizna(
                        x = celda * ANCHO_CELDA_PASTO + j * ANCHO_BRIZNA +
                            dispersion(semilla) * 7.0,
                        alto = 9.0 + dispersion(semilla + 17) * 10.0,
                        ancho = 6.0 + dispersion(semilla + 53) * 5.0,
                        brillo = dispersion(semilla + 101)
                    )
                )
            }
        }
        return lista
    }

    fun arboles(desde: Double, hasta: Double): List<Arbol> {
        val lista = mutableListOf<Arbol>()
        for (celda in celdas(ANCHO_CELDA_ARBOL, desde, hasta)) {
            val semilla = celda
            lista.add(
                Arbol(
                    x = celda * ANCHO_CELDA_ARBOL +
                        dispersion(semilla) * (ANCHO_CELDA_ARBOL * MAXIMO_SALTOS),
                    escala = ESCALA_ARBOL_MINIMA +
                        dispersion(semilla + 7) * (ESCALA_ARBOL_MAXIMA - ESCALA_ARBOL_MINIMA),
                    claro = dispersion(semilla + 29) < 0.5
                )
            )
        }
        return lista
    }

    fun nubes(desde: Double, hasta: Double): List<Nube> {
        val lista = mutableListOf<Nube>()
        for (celda in celdas(ANCHO_CELDA_NUBE, desde, hasta)) {
            lista.add(
                Nube(
                    x = celda * ANCHO_CELDA_NUBE +
                        dispersion(celda + 5) * (ANCHO_CELDA_NUBE * MAXIMO_SALTOS),
                    y = ALTURA_NUBE_MINIMA + dispersion(celda + 11) *
                        (ALTURA_NUBE_MAXIMA - ALTURA_NUBE_MINIMA),
                    escala = 0.5 + dispersion(celda + 19) * 0.45,
                    opacidad = 0.6 + dispersion(celda + 23) * 0.28
                )
            )
        }
        return lista
    }

    fun metas(desde: Double, hasta: Double): List<Meta> {
        val lista = mutableListOf<Meta>()
        for (celda in celdas(ANCHO_CELDA_META, desde, hasta)) {
            lista.add(Meta(celda * ANCHO_CELDA_META + 260.0))
        }
        return lista
    }
}
