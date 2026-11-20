package org.example

import org.example.fisica.Ambiente
import org.example.fisica.FabricaDetectoresReposo
import org.example.fisica.FabricaResolutorPredeterminada
import org.example.fisica.MotorFisico
import org.example.fisica.Tierra
import org.example.modelo.Pared
import org.example.modelo.Pelota
import org.example.modelo.TipoPelota
import org.example.modelo.TipoPared
import org.example.modelo.Vector2D
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MotorColisionesTest {
    private fun crearMotor(ambiente: Ambiente, paredes: List<Pared> = emptyList()): MotorFisico =
        MotorFisico(
            ambiente,
            paredes,
            FabricaDetectoresReposo.crear(ambiente),
            FabricaResolutorPredeterminada()
        )

    @Test
    fun colisionSueloRebota() {
        val sueloY = 600.0
        val pelota = Pelota(Vector2D(50.0, sueloY - 1.0), TipoPelota.GOMA)
        pelota.fijarVelocidad(Vector2D(10.0, 10.0))
        val ambiente = Tierra()
        val motor = crearMotor(ambiente)

        repeat(20) { motor.simularPaso(pelota, 0.05) }

        assertTrue(
            pelota.velocidad().y() < 0 || pelota.posicion().y() < sueloY,
            "La pelota debe rebotar hacia arriba o permanecer en el suelo"
        )
    }

    @Test
    fun colisionParedReflejaPelota() {
        val pared = Pared(Vector2D(100.0, 100.0), Vector2D(100.0, 300.0))
        val pelota = Pelota(Vector2D(80.0, 200.0), TipoPelota.GOMA, org.example.modelo.colision.FormaCirculo(10.0), 1.0, 1.0)
        pelota.fijarVelocidad(Vector2D(100.0, 0.0))
        val motor = crearMotor(Tierra(gravedad = Vector2D(0.0, 0.0)), listOf(pared))

        motor.simularPaso(pelota, 0.1)

        assertTrue(pelota.posicion().x() < 100.0)
        assertTrue(pelota.velocidad().x() < 0.0)
    }

    @Test
    fun colisionRapidaNoAtraviesaPared() {
        val pared = Pared(Vector2D(100.0, 100.0), Vector2D(100.0, 300.0), TipoPared.ACERO)
        val pelota = Pelota(Vector2D(0.0, 200.0), TipoPelota.GOMA, org.example.modelo.colision.FormaCirculo(10.0), 1.0, 0.8)
        pelota.fijarVelocidad(Vector2D(1000.0, 0.0))
        val motor = crearMotor(Tierra(gravedad = Vector2D(0.0, 0.0)), listOf(pared))

        motor.simularPaso(pelota, 0.1)

        assertTrue(pelota.posicion().x() < 100.0)
        assertTrue(pelota.velocidad().x() < 0.0)
    }

    @Test
    fun paredesSePuedenAgregarYEliminar() {
        val pared = Pared(Vector2D(0.0, 0.0), Vector2D(50.0, 50.0))
        val motor = crearMotor(Tierra(gravedad = Vector2D(0.0, 0.0)))

        motor.agregarPared(pared)
        assertEquals(listOf(pared), motor.obtenerParedes())
        assertTrue(motor.removerPared(pared))
        assertTrue(motor.obtenerParedes().isEmpty())
    }

    @Test
    fun ningunaPelotaAtraviesaLaPared() {
        val pared = Pared(Vector2D(300.0, -500.0), Vector2D(300.0, 2000.0), TipoPared.ACERO)
        for (tipo in TipoPelota.entries) {
            val motor = crearMotor(Tierra(1000.0, 650.0, Vector2D(0.0, 0.0)), listOf(pared))
            val pelota = Pelota(Vector2D(100.0, 300.0), tipo)
            pelota.fijarVelocidad(Vector2D(2000.0, 0.0))
            repeat(10) { motor.simularPaso(pelota, 1.0 / 60.0) }

            assertTrue(pelota.posicion().x() < 300.0, "${tipo.nombre()} atraviesa la pared")
            assertTrue(pelota.velocidad().x() < 0.0, "${tipo.nombre()} no rebota contra la pared")
        }
    }

    @Test
    fun unaPelotaQuietaSobreUnaParedTerminaElTiro() {
        val ambiente = Tierra()
        val pared = Pared(Vector2D(400.0, 500.0), Vector2D(600.0, 500.0), TipoPared.ACERO)
        val motor = crearMotor(ambiente, listOf(pared))
        val pelota = Pelota(Vector2D(500.0, 300.0), TipoPelota.BOLICHE)

        repeat(900) { motor.simularPaso(pelota, 1.0 / 60.0) }

        assertTrue(
            pelota.posicion().y() < ambiente.sueloY() - pelota.radio() - 5.0,
            "La pelota tiene que quedar apoyada sobre la pared, no en el suelo"
        )
        assertTrue(motor.estaEnReposo(pelota), "Quieta sobre una pared tambien termina el tiro")
    }

    @Test
    fun rozarUnaParedVerticalEnElAireNoCuentaComoApoyo() {
        val ambiente = Tierra()
        val pared = Pared(Vector2D(600.0, 600.0), Vector2D(600.0, 100.0), TipoPared.ACERO)
        val motor = crearMotor(ambiente, listOf(pared))
        val x = 600.0 - pared.grosor() / 2.0 - 10.0
        val pelota = Pelota(Vector2D(x, 400.0), TipoPelota.GOMA, org.example.modelo.colision.FormaCirculo(10.0))
        pelota.fijarVelocidad(Vector2D(0.0, -450.0))

        repeat(18) { motor.simularPaso(pelota, 1.0 / 60.0) }

        assertTrue(!motor.estaEnReposo(pelota), "En el aire, rozando la pared, el vuelo continua")
    }
}
