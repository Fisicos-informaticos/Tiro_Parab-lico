package org.example

import org.example.grafico.GeneradorEntorno
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DecoradoTest {
    @Test
    fun elDecoradoEsIgualAunqueSeRepitaLaPeticion() {
        val primero = GeneradorEntorno.arboles(-400.0, 600.0)
        val segundo = GeneradorEntorno.arboles(-400.0, 600.0)
        assertEquals(primero, segundo)
        assertEquals(GeneradorEntorno.pasto(0.0, 500.0), GeneradorEntorno.pasto(0.0, 500.0))
    }

    @Test
    fun elDecoradoNoTieneHuecosEnNingunPuntoDelMapa() {
        var camara = -20000.0
        while (camara < 20000.0) {
            val desde = camara
            val hasta = camara + 1000.0

            val arboles = GeneradorEntorno.arboles(desde, hasta).count { it.x() in desde..hasta }
            val nubes = GeneradorEntorno.nubes(desde, hasta).count { it.x() in desde..hasta }
            val briznas = GeneradorEntorno.pasto(desde, hasta).count { it.x() in desde..hasta }

            assertTrue(arboles >= 4, "Solo $arboles arboles en la pantalla x=$desde")
            assertTrue(nubes >= 3, "Solo $nubes nubes en la pantalla x=$desde")
            assertTrue(briznas >= 70, "Solo $briznas briznas en la pantalla x=$desde")

            camara += 37.0
        }
    }

    @Test
    fun elCespedNoSeSeparaEnTiras() {
        val desde = -5000.0
        val hasta = 5000.0
        val briznas = GeneradorEntorno.pasto(desde, hasta)
            .map { it.x() + it.ancho() / 2.0 }
            .sorted()

        for (i in 0 until briznas.size - 1) {
            val hueco = briznas[i + 1] - briznas[i]
            assertTrue(hueco < 25.0, "Hueco de $hueco px entre briznas cerca de ${briznas[i]}")
        }
    }

    @Test
    fun losArbolesYLasNubesNoSePisan() {
        val desde = -5000.0
        val hasta = 5000.0
        val copas = GeneradorEntorno.arboles(desde, hasta)
            .map { it.x() to 58.0 * it.escala() }
            .sortedBy { it.first }
        for (i in 0 until copas.size - 1) {
            val separacion = copas[i + 1].first - copas[i].first
            val mitad = (copas[i].second + copas[i + 1].second) / 2.0
            assertTrue(separacion > mitad, "Arboles solapados en x=${copas[i].first}")
        }

        val anchos = GeneradorEntorno.nubes(desde, hasta)
            .map { it.x() to 92.0 * it.escala() }
            .sortedBy { it.first }
        for (i in 0 until anchos.size - 1) {
            val separacion = anchos[i + 1].first - anchos[i].first
            assertTrue(separacion > 100.0, "Nubes amontonadas en x=${anchos[i].first}")
        }
    }

    @Test
    fun elDecoradoNoSaleDeLaFranjaDelCielo() {
        for (nube in GeneradorEntorno.nubes(-3000.0, 3000.0)) {
            assertTrue(nube.y() >= 20.0, "Nube muy alta en y=${nube.y()}")
            assertTrue(nube.y() <= 160.0, "Nube muy baja en y=${nube.y()}")
        }
    }
}
