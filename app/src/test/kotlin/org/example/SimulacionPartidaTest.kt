package org.example

import org.example.modelo.TipoPelota
import org.example.modelo.Vector2D
import org.example.simulacion.EstadoSimulacion
import org.example.simulacion.Simulacion
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SimulacionPartidaTest {
    @Test
    fun cambiarDePelotaVuelveAPrepararElTiro() {
        val simulacion = Simulacion()
        prepararTiro(simulacion)
        avanzarHasta(simulacion) { it.esEnSuelo() }

        simulacion.seleccionarPelota(TipoPelota.BOLICHE)

        assertEquals(EstadoSimulacion.Inactivo, simulacion.estado())
        assertEquals(TipoPelota.BOLICHE, simulacion.pelota().tipo())
        assertEquals(Vector2D(0.0, 0.0), simulacion.pelota().velocidad())
        assertTrue(simulacion.pelota().posicion().y() >= simulacion.sueloY() - simulacion.pelota().radio() - 5.0)
    }

    @Test
    fun cambiarLaGravedadConservaLasParedesYReiniciaElTiro() {
        val simulacion = Simulacion()
        agregarPared(simulacion, org.example.modelo.TipoPared.LADRILLO)
        prepararTiro(simulacion)
        avanzarHasta(simulacion) { it.esEnSuelo() }

        simulacion.aplicarGravedad("Luna (0.17 g)")

        assertEquals(Simulacion.factorGravedadDe("Luna (0.17 g)"), simulacion.factorGravedad())
        assertEquals(1, simulacion.paredes().size)
        assertEquals(EstadoSimulacion.Inactivo, simulacion.estado())
    }

    @Test
    fun nuevaPartidaDejaTodoComoAlEmpezar() {
        val simulacion = Simulacion()
        agregarPared(simulacion, org.example.modelo.TipoPared.CARTON)
        prepararTiro(simulacion)
        avanzarHasta(simulacion) { simulacion.paredesDestruidas() > 0 }

        simulacion.nuevaPartida()

        assertEquals(EstadoSimulacion.Inactivo, simulacion.estado())
        assertEquals(Vector2D(0.0, 0.0), simulacion.offsetCamara())
        assertEquals(0, simulacion.paredesDestruidas())
        assertTrue(simulacion.efectos().isEmpty())
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

    private fun agregarPared(simulacion: Simulacion, tipo: org.example.modelo.TipoPared) {
        simulacion.seleccionarTipoPared(tipo)
        simulacion.pulsar(Vector2D(100.0, 100.0))
        simulacion.alternarModoParedes()
        val suelo = simulacion.sueloY()
        simulacion.pulsar(Vector2D(700.0, 60.0))
        simulacion.arrastrar(Vector2D(700.0, suelo))
        simulacion.soltar(Vector2D(700.0, suelo))
        assertEquals(tipo, simulacion.paredes().single().tipo())
        simulacion.alternarModoParedes()
    }

    private fun avanzarHasta(simulacion: Simulacion, condicion: (EstadoSimulacion) -> Boolean) {
        repeat(6000) {
            simulacion.actualizar(Simulacion.DT_FISICA)
            if (condicion(simulacion.estado())) return
        }
        throw AssertionError("La simulacion no llego al estado esperado")
    }
}
