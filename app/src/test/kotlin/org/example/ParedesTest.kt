package org.example

import org.example.modelo.Pared
import org.example.modelo.TipoPelota
import org.example.modelo.TipoPared
import org.example.modelo.Vector2D
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ParedesTest {
    @Test
    fun elCatalogoDeParedesEsCoherente() {
        for (tipo in TipoPared.entries) {
            assertTrue(tipo.grosor() > 0.0, "${tipo.nombre()}: grosor")
            assertTrue(tipo.resistencia() > 0.0, "${tipo.nombre()}: resistencia")
            assertTrue(tipo.pelotasQueLaRompen().size <= TipoPelota.entries.size)
            assertEquals(
                tipo.pelotasQueLaRompen().isEmpty(),
                tipo.indestructible(),
                "${tipo.nombre()}: coherencia entre indestructible y sus pelotas"
            )
        }
    }

    @Test
    fun laBolicheRompeMaderaYElAceroNoSeRompeNunca() {
        val madera = Pared(Vector2D(0.0, 0.0), Vector2D(0.0, 200.0), TipoPared.MADERA)
        val energiaBoliche = 0.5 * TipoPelota.BOLICHE.masa() * 600.0 * 600.0
        assertTrue(madera.registrarImpacto(energiaBoliche, TipoPelota.BOLICHE))

        val acero = Pared(Vector2D(0.0, 0.0), Vector2D(0.0, 200.0), TipoPared.ACERO)
        repeat(50) {
            assertTrue(!acero.registrarImpacto(energiaBoliche * 100, TipoPelota.BOLICHE))
        }
    }

    @Test
    fun unaPelotaQueNoRompeElMaterialNoLeHaceDano() {
        val ladrillo = Pared(Vector2D(0.0, 0.0), Vector2D(0.0, 200.0), TipoPared.LADRILLO)
        val energiaBoliche = 0.5 * TipoPelota.BOLICHE.masa() * 600.0 * 600.0

        assertTrue(!ladrillo.registrarImpacto(energiaBoliche, TipoPelota.TENIS))
        assertEquals(0.0, ladrillo.danio())
        assertTrue(!TipoPared.LADRILLO.registraDanio(TipoPelota.TENIS))
        assertTrue(TipoPared.LADRILLO.registraDanio(TipoPelota.BOLICHE))
    }

    @Test
    fun losGolpesSuavesVanGastandoLaParedHastaRomperla() {
        val pared = Pared(Vector2D(0.0, 0.0), Vector2D(0.0, 200.0), TipoPared.MADERA)
        val energia = TipoPared.MADERA.resistencia() / 4.0

        assertTrue(!pared.registrarImpacto(energia, TipoPelota.GOMA))
        assertEquals(0.25, pared.danio(), 1e-9)
        assertTrue(!pared.registrarImpacto(energia, TipoPelota.GOMA))
        assertTrue(!pared.registrarImpacto(energia, TipoPelota.GOMA))
        assertTrue(pared.registrarImpacto(energia, TipoPelota.GOMA))
        assertEquals(1.0, pared.danio(), 1e-9)
    }

    @Test
    fun unMaterialIndestructibleNoAdmiteNingunaPelotaQueLoRompa() {
        for (tipoPared in TipoPared.entries) {
            if (!tipoPared.indestructible()) continue
            for (tipoPelota in TipoPelota.entries) {
                assertTrue(
                    !tipoPared.registraDanio(tipoPelota),
                    "${tipoPelota.nombre()} no deberia romper ${tipoPared.nombre()}"
                )
            }
        }
    }
}
