package org.example

import org.example.modelo.TipoPelota
import org.example.modelo.Vector2D
import org.example.simulacion.EstadoSimulacion
import org.example.simulacion.Simulacion
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SimulacionEstadoTest {
    @Test
    fun arrancaEnLaPantallaDeInicio() {
        assertEquals(EstadoSimulacion.PantallaInicio, Simulacion().estado())
    }

    @Test
    fun unClicSacaDeLaPantallaDeInicio() {
        val simulacion = Simulacion()
        simulacion.pulsar(Vector2D(500.0, 300.0))
        assertEquals(EstadoSimulacion.Inactivo, simulacion.estado())
    }

    @Test
    fun elArrastreLanzaLaPelotaYAlPararTerminaElTiro() {
        val simulacion = Simulacion()
        prepararTiro(simulacion)
        assertEquals(EstadoSimulacion.EnVuelo, simulacion.estado())
        assertTrue(simulacion.pelota().velocidad().magnitud() > 0.0)
        avanzarHasta(simulacion) { it.esEnSuelo() }
        assertEquals(EstadoSimulacion.EnSuelo, simulacion.estado())
        assertTrue(simulacion.distanciaRecorrida() > 0.0)
        assertTrue(simulacion.tiempoVuelo() > 0.0)
    }

    @Test
    fun enPantallaDeInicioNoSePuedePausar() {
        val simulacion = Simulacion()
        simulacion.alternarPausa()
        assertTrue(!simulacion.pausado())
    }

    @Test
    fun pausarDetieneElRelojDelVuelo() {
        val simulacion = Simulacion()
        prepararTiro(simulacion)
        simulacion.alternarPausa()
        val tiempo = simulacion.tiempoVuelo()
        repeat(30) { simulacion.actualizar(Simulacion.DT_FISICA) }
        assertEquals(tiempo, simulacion.tiempoVuelo())
        assertEquals(EstadoSimulacion.EnVuelo, simulacion.estado())
    }

    private fun prepararTiro(simulacion: Simulacion) {
        apuntarSinSoltar(simulacion)
        val origen = simulacion.pelota().posicion() - simulacion.offsetCamara()
        simulacion.soltar(origen + Vector2D(-150.0, 115.0))
    }

    private fun apuntarSinSoltar(simulacion: Simulacion) {
        val enElSuelo = simulacion.sueloY() - simulacion.offsetCamara().y() - 5.0
        simulacion.pulsar(Vector2D(500.0, enElSuelo))
        simulacion.pulsar(Vector2D(500.0, enElSuelo))
        assertEquals(EstadoSimulacion.Arrojando, simulacion.estado(), "El clic debe colocar la pelota")
        val origen = simulacion.pelota().posicion() - simulacion.offsetCamara()
        simulacion.arrastrar(origen + Vector2D(-150.0, 115.0))
    }

    private fun avanzarHasta(simulacion: Simulacion, condicion: (EstadoSimulacion) -> Boolean) {
        repeat(6000) {
            simulacion.actualizar(Simulacion.DT_FISICA)
            if (condicion(simulacion.estado())) return
        }
        throw AssertionError("La simulacion no llego al estado esperado")
    }
}
