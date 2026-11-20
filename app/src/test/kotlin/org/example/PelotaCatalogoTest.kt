package org.example

import org.example.fisica.Ambiente
import org.example.fisica.FabricaDetectoresReposo
import org.example.fisica.FabricaResolutorPredeterminada
import org.example.fisica.MotorFisico
import org.example.fisica.Tierra
import org.example.modelo.Pelota
import org.example.modelo.TipoPelota
import org.example.modelo.Vector2D
import org.example.modelo.colision.TipoColision
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PelotaCatalogoTest {
    private fun crearMotor(ambiente: Ambiente, paredes: List<org.example.modelo.Pared> = emptyList()): MotorFisico =
        MotorFisico(
            ambiente,
            paredes,
            FabricaDetectoresReposo.crear(ambiente),
            FabricaResolutorPredeterminada()
        )

    @Test
    fun elCatalogoDefineCuatroFormasDistintas() {
        val formas = TipoPelota.entries.map { it.crearForma().tipo() }.toSet()
        assertEquals(TipoColision.entries.toSet(), formas)
    }

    @Test
    fun lasCualidadesDelCatalogoSonValidas() {
        for (tipo in TipoPelota.entries) {
            assertTrue(tipo.masa() > 0.0, "${tipo.nombre()}: masa")
            assertTrue(tipo.coeficienteRestitucion() > 0.0, "${tipo.nombre()}: restitucion")
            assertTrue(tipo.coeficienteRestitucion() <= 1.0, "${tipo.nombre()}: restitucion")
            assertTrue(tipo.coeficienteFriccion() >= 0.0, "${tipo.nombre()}: friccion")
            assertTrue(tipo.coeficienteArrastre() >= 0.0, "${tipo.nombre()}: arrastre")
            assertTrue(tipo.tamano() > 0.0, "${tipo.nombre()}: tamano")
            assertNotNull(tipo.crearForma().contorno(), "${tipo.nombre()}: contorno")
        }
    }

    @Test
    fun elRadioDependeDelTipoDePelota() {
        val radios = TipoPelota.entries.map { it.crearForma().radioEnvoltura() }
        assertTrue(radios.toSet().size == TipoPelota.entries.size, "Cada tipo deberia tener su tamano")
        assertTrue(radios.all { it > 0.0 })
    }

    @Test
    fun ningunaPelotaAtraviesaElSuelo() {
        for (tipo in TipoPelota.entries) {
            val motor = crearMotor(Tierra(1000.0, 650.0, Vector2D(0.0, 1500.0)))
            val pelota = Pelota(Vector2D(100.0, 40.0), tipo)
            repeat(1500) { motor.simularPaso(pelota, 1.0 / 60.0) }

            assertTrue(
                pelota.posicion().y() <= motor.sueloY() + 0.5,
                "${tipo.nombre()} se hunde en el suelo"
            )
            assertTrue(
                pelota.posicion().y() >= motor.sueloY() - pelota.radio() - 0.5,
                "${tipo.nombre()} flota sobre el suelo"
            )
        }
    }

    @Test
    fun elArrastreFrenaLaPelotaDeTenis() {
        val motor = crearMotor(Tierra(gravedad = Vector2D(0.0, 0.0)))
        val pelota = Pelota(Vector2D(50.0, 300.0), TipoPelota.TENIS)
        pelota.fijarVelocidad(Vector2D(1200.0, 0.0))

        repeat(60) { motor.simularPaso(pelota, 1.0 / 60.0) }

        assertTrue(pelota.velocidad().x() < 1200.0, "El aire debe frenar la pelota de tenis")
        assertTrue(pelota.posicion().x() < 1200.0, "Sin arrastre recorreria 1200 px en un segundo")
    }

    @Test
    fun laPelotaPesadaRecorreMasQueLaLigera() {
        val distancia = { tipo: TipoPelota ->
            val motor = crearMotor(Tierra(1000.0, 650.0, Vector2D(0.0, 900.0)))
            val pelota = Pelota(Vector2D(50.0, 40.0), tipo)
            pelota.fijarVelocidad(Vector2D(1400.0, -200.0))
            var recorrida = 0.0
            repeat(120) {
                val antes = pelota.posicion()
                motor.simularPaso(pelota, 1.0 / 60.0)
                recorrida += pelota.posicion().distanciaA(antes)
            }
            recorrida
        }

        assertTrue(
            distancia(TipoPelota.BOLICHE) > distancia(TipoPelota.TENIS),
            "La bola de boliche deberia mantener la velocidad mucho mas que la de tenis"
        )
    }

    @Test
    fun laPelotaDeGomaRuedaMasLejosQueLaDeRugby() {
        val recorrido = { tipo: TipoPelota ->
            val motor = crearMotor(Tierra(1000.0, 650.0, Vector2D(0.0, 0.0)))
            val pelota = Pelota(Vector2D(50.0, motor.sueloY() - 20.0), tipo)
            pelota.fijarVelocidad(Vector2D(900.0, 0.0))
            repeat(60) { motor.simularPaso(pelota, 1.0 / 60.0) }
            pelota.posicion().x()
        }

        assertTrue(
            recorrido(TipoPelota.GOMA) > recorrido(TipoPelota.RUGBY),
            "La pelota de goma deberia deslizar mas que la de rugby"
        )
    }
}
