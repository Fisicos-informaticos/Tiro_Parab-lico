package org.example

import org.example.modelo.TipoPared
import org.example.modelo.Vector2D
import org.example.simulacion.EstadoSimulacion
import org.example.simulacion.Simulacion
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class SimulacionParedesTest {
    @Test
    fun unaParedDeLadrilloLaRompeLaBolicheYSeCuenta() {
        val simulacion = Simulacion()
        simulacion.seleccionarPelota(org.example.modelo.TipoPelota.BOLICHE)
        agregarPared(simulacion, TipoPared.LADRILLO)
        prepararTiro(simulacion)
        avanzarHasta(simulacion) { simulacion.paredes().isEmpty() }
        assertTrue(simulacion.paredes().isEmpty(), "La pared deberia haberse roto")
        assertEquals(1, simulacion.paredesDestruidas())
        assertTrue(simulacion.efectos().isNotEmpty(), "Deberia quedar la estela de la rotura")
        assertTrue(simulacion.rompeParedSeleccionada())
    }

    @Test
    fun elAceroAguantaYPelotaRebota() {
        val simulacion = Simulacion()
        simulacion.seleccionarPelota(org.example.modelo.TipoPelota.BOLICHE)
        simulacion.seleccionarTipoPared(TipoPared.ACERO)
        agregarPared(simulacion, TipoPared.ACERO)
        prepararTiro(simulacion)
        avanzarHasta(simulacion) { it.esEnSuelo() }
        assertEquals(1, simulacion.paredes().size)
        assertEquals(0, simulacion.paredesDestruidas())
        assertTrue(!simulacion.rompeParedSeleccionada())
    }

    @Test
    fun limpiarParedesNoBorraLasColocadas() {
        val simulacion = Simulacion()
        agregarPared(simulacion, TipoPared.ACERO)
        simulacion.limpiarParedes()
        assertTrue(simulacion.paredes().isEmpty())
        agregarPared(simulacion, TipoPared.MADERA)
        assertEquals(1, simulacion.paredes().size)
    }

    @Test
    fun laPrevisualizacionSoloExisteMientrasSeApunta() {
        val simulacion = Simulacion()
        assertTrue(simulacion.previsualizarTrayectoria().isEmpty())
        apuntarSinSoltar(simulacion)
        assertTrue(simulacion.previsualizarTrayectoria().size > 2)
        simulacion.soltar(simulacion.pelota().posicion())
        assertTrue(simulacion.previsualizarTrayectoria().isEmpty())
    }

    @Test
    fun abrirEdicionAbandonaElApuntadoYLaPausa() {
        val simulacion = Simulacion()
        apuntarSinSoltar(simulacion)
        simulacion.alternarPausa()
        simulacion.abrirEdicion()
        assertTrue(!simulacion.pausado())
        assertEquals(EstadoSimulacion.Inactivo, simulacion.estado())
        assertNull(simulacion.entrada().posicionInicial())
    }

    @Test
    fun elModoParedDibujaYColocaLaParedElegida() {
        val simulacion = Simulacion()
        simulacion.seleccionarTipoPared(TipoPared.HORMIGON)
        simulacion.pulsar(Vector2D(100.0, 100.0))
        simulacion.alternarModoParedes()
        simulacion.pulsar(Vector2D(100.0, 100.0))
        simulacion.arrastrar(Vector2D(100.0, 300.0))
        assertNotNull(simulacion.paredTemporal())
        assertEquals(TipoPared.HORMIGON, simulacion.paredTemporal()?.tipo())
        simulacion.soltar(Vector2D(100.0, 300.0))
        assertNull(simulacion.paredTemporal())
        assertEquals(1, simulacion.paredes().size)
        assertEquals(TipoPared.HORMIGON, simulacion.paredes().first().tipo())
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

    private fun agregarPared(simulacion: Simulacion, tipo: TipoPared) {
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
