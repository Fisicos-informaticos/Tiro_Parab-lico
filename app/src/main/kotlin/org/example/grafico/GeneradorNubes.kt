package org.example.grafico

/** Reparte las nubes por el mapa de forma determinista. */
internal object GeneradorNubes {
    fun generar(desde: Double, hasta: Double): List<Nube> {
        val lista = mutableListOf<Nube>()
        for (celda in RepartoEntorno.celdas(RepartoEntorno.ANCHO_CELDA_NUBE, desde, hasta)) {
            lista.add(
                Nube(
                    x = celda * RepartoEntorno.ANCHO_CELDA_NUBE +
                        RepartoEntorno.dispersion(celda + 5) *
                        (RepartoEntorno.ANCHO_CELDA_NUBE * RepartoEntorno.MAXIMO_SALTOS),
                    y = RepartoEntorno.ALTURA_NUBE_MINIMA +
                        RepartoEntorno.dispersion(celda + 11) *
                        (RepartoEntorno.ALTURA_NUBE_MAXIMA - RepartoEntorno.ALTURA_NUBE_MINIMA),
                    escala = 0.5 + RepartoEntorno.dispersion(celda + 19) * 0.45,
                    opacidad = 0.6 + RepartoEntorno.dispersion(celda + 23) * 0.28
                )
            )
        }
        return lista
    }
}
