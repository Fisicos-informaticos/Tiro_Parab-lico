package org.example.grafico

/** Reparte las briznas de cesped por el mapa de forma determinista. */
internal object GeneradorPasto {
    fun generar(desde: Double, hasta: Double): List<Brizna> {
        val lista = mutableListOf<Brizna>()
        for (celda in RepartoEntorno.celdas(RepartoEntorno.ANCHO_CELDA_PASTO, desde, hasta)) {
            for (j in 0 until RepartoEntorno.BRIZNAS_POR_CELDA) {
                val semilla = celda * RepartoEntorno.BRIZNAS_POR_CELDA + j
                lista.add(
                    Brizna(
                        x = celda * RepartoEntorno.ANCHO_CELDA_PASTO +
                            j * RepartoEntorno.ANCHO_BRIZNA +
                            RepartoEntorno.dispersion(semilla) * 7.0,
                        alto = 9.0 + RepartoEntorno.dispersion(semilla + 17) * 10.0,
                        ancho = 6.0 + RepartoEntorno.dispersion(semilla + 53) * 5.0,
                        brillo = RepartoEntorno.dispersion(semilla + 101)
                    )
                )
            }
        }
        return lista
    }
}
