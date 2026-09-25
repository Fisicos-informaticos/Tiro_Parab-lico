package org.example

import org.example.fisica.CalculadoraTrayectoria
import org.example.fisica.MotorFisico
import org.example.fisica.Tierra
import org.example.modelo.Pared
import org.example.modelo.PelotaGoma
import org.example.modelo.Vector2D
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AppTest {
    @Test
    fun alcancePara45Grados() {
        val v0 = 20.0
        val gravedad = 9.81
        val esperado = v0 * v0 / gravedad
        val resultado = CalculadoraTrayectoria.calcularAlcanceMaximo(v0, gravedad)
        assertEquals(esperado, resultado, esperado * 0.001)
    }

    @Test
    fun alturaMaximaEn45Grados() {
        val v0 = 20.0
        val angulo = 45.0
        val gravedad = 9.81
        val esperado = v0 * v0 * 0.5 / (2.0 * gravedad)
        val resultado = CalculadoraTrayectoria.calcularAlturaMaxima(v0, angulo, gravedad)
        assertEquals(esperado, resultado, esperado * 0.001)
    }

    @Test
    fun colisionSueloRebota() {
        val sueloY = 600.0
        val pelota = PelotaGoma(Vector2D(50.0, sueloY - 1.0), 1.0, 0.8)
        pelota.velocidad = Vector2D(10.0, 10.0)
        val ambiente = Tierra()
        val motor = MotorFisico(ambiente)

        repeat(20) { motor.simularPaso(pelota, 0.05) }

        assertTrue(pelota.velocidad.y < 0 || pelota.posicion.y < sueloY,
            "La pelota debe rebotar hacia arriba o permanecer en el suelo")
    }

    @Test
    fun velocidadInicialCorrecta() {
        val vel = CalculadoraTrayectoria.calcularVelocidadInicial(20.0, 45.0)
        assertEquals(20.0, vel.magnitud, 0.01)
    }

    @Test
    fun tiempoVueloCalculado() {
        val v0 = 20.0
        val angulo = 90.0
        val gravedad = 9.81
        val esperado = 2.0 * v0 / gravedad
        val resultado = CalculadoraTrayectoria.calcularTiempoVuelo(v0, angulo, gravedad)
        assertEquals(esperado, resultado, 0.01)
    }

    @Test
    fun colisionParedReflejaPelota() {
        val pared = Pared(Vector2D(100.0, 100.0), Vector2D(100.0, 300.0))
        val pelota = PelotaGoma(Vector2D(80.0, 200.0), 1.0, 1.0, 10.0)
        pelota.velocidad = Vector2D(100.0, 0.0)
        val motor = MotorFisico(Tierra(gravedad = Vector2D(0.0, 0.0)), listOf(pared))

        motor.simularPaso(pelota, 0.1)

        assertTrue(pelota.posicion.x < 100.0)
        assertTrue(pelota.velocidad.x < 0.0)
    }

    @Test
    fun colisionRapidaNoAtraviesaPared() {
        val pared = Pared(Vector2D(100.0, 100.0), Vector2D(100.0, 300.0))
        val pelota = PelotaGoma(Vector2D(0.0, 200.0), 1.0, 0.8, 10.0)
        pelota.velocidad = Vector2D(1000.0, 0.0)
        val motor = MotorFisico(Tierra(gravedad = Vector2D(0.0, 0.0)), listOf(pared))

        motor.simularPaso(pelota, 0.1)

        assertTrue(pelota.posicion.x < 100.0)
        assertTrue(pelota.velocidad.x < 0.0)
    }

    @Test
    fun paredesSePuedenAgregarYEliminar() {
        val pared = Pared(Vector2D(0.0, 0.0), Vector2D(50.0, 50.0))
        val motor = MotorFisico(Tierra(gravedad = Vector2D(0.0, 0.0)))

        motor.agregarPared(pared)
        assertEquals(listOf(pared), motor.obtenerParedes())
        assertTrue(motor.removerPared(pared))
        assertTrue(motor.obtenerParedes().isEmpty())
    }
}
