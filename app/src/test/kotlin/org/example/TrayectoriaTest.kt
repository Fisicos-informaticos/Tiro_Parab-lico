package org.example

import org.example.fisica.CalculadoraTrayectoria
import kotlin.test.Test
import kotlin.test.assertEquals

class TrayectoriaTest {
    @Test
    fun alcancePara45Grados() {
        val v0 = 20.0
        val gravedad = 9.81
        val esperado = v0 * v0 / gravedad
        val resultado = CalculadoraTrayectoria().calcularAlcanceMaximo(v0, gravedad)
        assertEquals(esperado, resultado, esperado * 0.001)
    }

    @Test
    fun alturaMaximaEn45Grados() {
        val v0 = 20.0
        val angulo = 45.0
        val gravedad = 9.81
        val esperado = v0 * v0 * 0.5 / (2.0 * gravedad)
        val resultado = CalculadoraTrayectoria().calcularAlturaMaxima(v0, angulo, gravedad)
        assertEquals(esperado, resultado, esperado * 0.001)
    }

    @Test
    fun velocidadInicialCorrecta() {
        val vel = CalculadoraTrayectoria().calcularVelocidadInicial(20.0, 45.0)
        assertEquals(20.0, vel.magnitud(), 0.01)
    }

    @Test
    fun tiempoVueloCalculado() {
        val v0 = 20.0
        val angulo = 90.0
        val gravedad = 9.81
        val esperado = 2.0 * v0 / gravedad
        val resultado = CalculadoraTrayectoria().calcularTiempoVuelo(v0, angulo, gravedad)
        assertEquals(esperado, resultado, 0.01)
    }
}
