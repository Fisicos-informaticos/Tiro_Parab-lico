package org.example

import org.example.entrada.DragInputHandler
import org.example.modos.CaidaLibre
import org.example.modos.CatalogoModos
import org.example.modos.LecturaModo
import org.example.modos.ModoCaidaLibre
import org.example.modos.ModoTiroParabolico
import org.example.modos.TiroParabolico
import org.example.modelo.TipoPelota
import org.example.modelo.Vector2D
import org.example.simulacion.EstadoSimulacion
import org.example.simulacion.Simulacion
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Pruebas del modo caida libre y del contrato que comparten los modos.
 * Tocan solo el nucleo, sin JavaFX.
 */
class CaidaLibreTest {

    @Test
    fun elTiroParabolicoSigueSiendoElModoPorDefecto() {
        assertEquals(CatalogoModos.TIRO_PARABOLICO, Simulacion().modo.nombre)
    }

    @Test
    fun elRegistroConoceLosDosModos() {
        assertEquals(listOf(CatalogoModos.TIRO_PARABOLICO, CatalogoModos.CAIDA_LIBRE), CatalogoModos.nombres)
        assertEquals(CatalogoModos.TIRO_PARABOLICO, CatalogoModos.crear("modo inventado").nombre)
    }

    @Test
    fun sePuedeCambiarDeModoSinPerderPelotaNiGravedad() {
        val simulacion = Simulacion()
        simulacion.seleccionarPelota(TipoPelota.BOLICHE)
        simulacion.aplicarGravedad("Luna (0.17 g)")

        simulacion.seleccionarModo(CatalogoModos.CAIDA_LIBRE)

        assertEquals(CatalogoModos.CAIDA_LIBRE, simulacion.modo.nombre)
        assertEquals(TipoPelota.BOLICHE, simulacion.tipoPelota)
        assertEquals(Simulacion.GRAVEDADES["Luna (0.17 g)"], simulacion.factorGravedad)
        assertEquals(EstadoSimulacion.Inactivo, simulacion.estado)
    }

    @Test
    fun laPelotaSueltaEnCaidaLibreNoTieneVelocidadHorizontal() {
        val simulacion = Simulacion()
        prepararCaida(simulacion, 300.0)

        simulacion.soltar(Vector2D(500.0, 300.0))

        assertEquals(EstadoSimulacion.EnVuelo, simulacion.estado)
        assertEquals(0.0, simulacion.pelota.velocidad.x, "Vx debe ser cero")
        assertEquals(0.0, simulacion.pelota.velocidad.magnitud, "La suelta es desde el reposo")

        repeat(30) { simulacion.actualizar(Simulacion.DT_FISICA) }

        assertEquals(500.0, simulacion.pelota.posicion.x, "La pelota no se desplaza en X")
        assertTrue(simulacion.pelota.velocidad.y > 0.0, "Vy crece al caer")
        assertTrue(simulacion.pelota.posicion.y > 300.0, "La pelota baja en el eje Y")
    }

    @Test
    fun elArrastreFijaLaAlturaInicialSinMoverLaColumna() {
        val simulacion = Simulacion()
        prepararCaida(simulacion, 320.0)
        val columna = simulacion.pelota.posicion.x

        simulacion.arrastrar(Vector2D(120.0, 180.0))

        assertEquals(columna, simulacion.pelota.posicion.x, "La x de la columna no cambia")
        assertEquals(180.0, simulacion.pelota.posicion.y, "La pelota sigue al puntero en Y")
        assertTrue(simulacion.lecturaModo().altura > 0.0)
    }

    @Test
    fun laCaidaLibreTerminaEnElSueloConSusMetricas() {
        val simulacion = Simulacion()
        prepararCaida(simulacion, 300.0)
        val xInicial = simulacion.pelota.posicion.x
        simulacion.soltar(Vector2D(500.0, 300.0))

        avanzarHasta(simulacion) { it is EstadoSimulacion.EnSuelo }

        assertTrue(simulacion.tiempoVuelo > 0.0, "El tiempo de caida avanza")
        assertTrue(simulacion.lecturaModo().altura <= 0.0, "Al apoyar la altura es cero")
        assertEquals(xInicial, simulacion.pelota.posicion.x, 0.001, "Sigue sin haber movimiento en X")
    }

    @Test
    fun elPanelDeCaidaLibreMuestraAlturaVelocidadYTiempo() {
        val lineas = ModoCaidaLibre().lineasDeVuelo(lecturaDeEjemplo())

        assertEquals(3, lineas.size)
        assertTrue(lineas[0].startsWith("h:"), lineas[0])
        assertTrue(lineas[1].startsWith("Vy:"), lineas[1])
        assertTrue(lineas[2].startsWith("t:"), lineas[2])
        assertTrue(lineas[0].contains("200"), lineas[0])
        assertTrue(lineas[1].contains("250"), lineas[1])
        assertTrue(lineas[2].contains("0.75"), lineas[2])
    }

    @Test
    fun elTiempoDeCaidaDependeDeLaGravedadSeleccionada() {
        assertTrue(
            tiempoDeCaida("Luna (0.17 g)") > tiempoDeCaida("Tierra (1.00 g)"),
            "Con menos gravedad la caida debe durar mas"
        )
    }

    @Test
    fun todosLosModosCumplenElMismoContrato() {
        val lectura = lecturaDeEjemplo()

        for (modo in listOf(ModoTiroParabolico(), ModoCaidaLibre())) {
            assertTrue(modo.nombre.isNotBlank())
            assertTrue(modo.ayuda.isNotBlank())
            assertTrue(modo.lineasDeColocacion(lectura).isNotEmpty(), modo.nombre)
            assertTrue(modo.lineasDeApuntado(lectura).isNotEmpty(), modo.nombre)
            assertTrue(modo.lineasDeVuelo(lectura).isNotEmpty(), modo.nombre)
            assertTrue(modo.lineasDeReposo(lectura).isNotEmpty(), modo.nombre)
            assertTrue(modo.etiquetasDePreparacion(lectura).isNotEmpty(), modo.nombre)
        }
    }

    @Test
    fun soloLaCaidaLibreSueltaSiempreDesdeElReposo() {
        val sinApuntar = DragInputHandler()

        assertEquals(null, ModoTiroParabolico().velocidadDeSuelta(sinApuntar), "Sin arrastre no hay tiro")
        assertEquals(Vector2D(0.0, 0.0), ModoCaidaLibre().velocidadDeSuelta(sinApuntar), "Vx = 0 y parte del reposo")
    }

    private fun prepararCaida(simulacion: Simulacion, altura: Double) {
        simulacion.seleccionarModo(CatalogoModos.CAIDA_LIBRE)
        simulacion.pulsar(Vector2D(500.0, altura))
        assertEquals(EstadoSimulacion.Arrojando, simulacion.estado, "El clic debe colocar la pelota")
    }

    private fun tiempoDeCaida(nombreGravedad: String): Double {
        val simulacion = Simulacion()
        simulacion.seleccionarModo(CatalogoModos.CAIDA_LIBRE)
        simulacion.aplicarGravedad(nombreGravedad)
        simulacion.pulsar(Vector2D(500.0, 250.0))
        simulacion.soltar(Vector2D(500.0, 250.0))

        avanzarHasta(simulacion) { it is EstadoSimulacion.EnSuelo }
        return simulacion.tiempoVuelo
    }

    private fun avanzarHasta(simulacion: Simulacion, condicion: (EstadoSimulacion) -> Boolean) {
        repeat(6000) {
            simulacion.actualizar(Simulacion.DT_FISICA)
            if (condicion(simulacion.estado)) return
        }
        throw AssertionError("La simulacion no llego al estado esperado")
    }

    private fun lecturaDeEjemplo() = LecturaModo(
        posicion = Vector2D(500.0, 300.0),
        velocidad = Vector2D(0.0, 250.0),
        distancia = 120.0,
        altura = 200.0,
        tiempo = 0.75,
        angulo = 45.0,
        fuerza = 220.0
    )
}
