package org.example.grafico

/** Reparte los arboles por el mapa de forma determinista. */
internal object GeneradorArboles {
    fun generar(desde: Double, hasta: Double): List<Arbol> {
        val lista = mutableListOf<Arbol>()
        for (celda in RepartoEntorno.celdas(RepartoEntorno.ANCHO_CELDA_ARBOL, desde, hasta)) {
            val semilla = celda
            lista.add(
                Arbol(
                    x = celda * RepartoEntorno.ANCHO_CELDA_ARBOL +
                        RepartoEntorno.dispersion(semilla) *
                        (RepartoEntorno.ANCHO_CELDA_ARBOL * RepartoEntorno.MAXIMO_SALTOS),
                    escala = RepartoEntorno.ESCALA_ARBOL_MINIMA +
                        RepartoEntorno.dispersion(semilla + 7) *
                        (RepartoEntorno.ESCALA_ARBOL_MAXIMA - RepartoEntorno.ESCALA_ARBOL_MINIMA),
                    claro = RepartoEntorno.dispersion(semilla + 29) < 0.5
                )
            )
        }
        return lista
    }
}
