package org.example.grafico

/** Reparte los postes de meta por el mapa de forma determinista. */
internal object GeneradorMetas {
    fun generar(desde: Double, hasta: Double): List<Meta> {
        val lista = mutableListOf<Meta>()
        for (celda in RepartoEntorno.celdas(RepartoEntorno.ANCHO_CELDA_META, desde, hasta)) {
            lista.add(Meta(celda * RepartoEntorno.ANCHO_CELDA_META + 260.0))
        }
        return lista
    }
}
