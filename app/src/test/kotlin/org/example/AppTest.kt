package org.example

import org.example.fisica.CalculadoraTrayectoria
import org.example.fisica.MotorFisico
import org.example.fisica.Tierra
import org.example.grafico.GeneradorEntorno
import org.example.modelo.Pared
import org.example.modelo.Pelota
import org.example.modelo.TipoPelota
import org.example.modelo.Vector2D
import org.example.modelo.colision.FormaCapsula
import org.example.modelo.colision.FormaCaja
import org.example.modelo.colision.FormaCirculo
import org.example.modelo.colision.FormaPoligono
import org.example.modelo.colision.Transformaciones
import org.example.modelo.colision.TipoColision
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
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
        val pelota = Pelota(Vector2D(50.0, sueloY - 1.0), TipoPelota.GOMA)
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
        val pelota = Pelota(Vector2D(80.0, 200.0), TipoPelota.GOMA, FormaCirculo(10.0), 1.0, 1.0)
        pelota.velocidad = Vector2D(100.0, 0.0)
        val motor = MotorFisico(Tierra(gravedad = Vector2D(0.0, 0.0)), listOf(pared))

        motor.simularPaso(pelota, 0.1)

        assertTrue(pelota.posicion.x < 100.0)
        assertTrue(pelota.velocidad.x < 0.0)
    }

    @Test
    fun colisionRapidaNoAtraviesaPared() {
        val pared = Pared(Vector2D(100.0, 100.0), Vector2D(100.0, 300.0))
        val pelota = Pelota(Vector2D(0.0, 200.0), TipoPelota.GOMA, FormaCirculo(10.0), 1.0, 0.8)
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

    @Test
    fun elCatalogoDefineCuatroFormasDistintas() {
        val formas = TipoPelota.entries.map { it.crearForma().tipo }.toSet()
        assertEquals(TipoColision.entries.toSet(), formas)
    }

    @Test
    fun lasCualidadesDelCatalogoSonValidas() {
        for (tipo in TipoPelota.entries) {
            assertTrue(tipo.masa > 0.0, "${tipo.nombre}: masa")
            assertTrue(tipo.coeficienteRestitucion > 0.0, "${tipo.nombre}: restitucion")
            assertTrue(tipo.coeficienteRestitucion <= 1.0, "${tipo.nombre}: restitucion")
            assertTrue(tipo.coeficienteFriccion >= 0.0, "${tipo.nombre}: friccion")
            assertTrue(tipo.coeficienteArrastre >= 0.0, "${tipo.nombre}: arrastre")
            assertTrue(tipo.tamano > 0.0, "${tipo.nombre}: tamano")
            assertNotNull(tipo.crearForma().contorno(), "${tipo.nombre}: contorno")
        }
    }

    @Test
    fun elRadioDependeDelTipoDePelota() {
        val radios = TipoPelota.entries.map { it.crearForma().radioEnvoltura }
        assertTrue(radios.toSet().size == TipoPelota.entries.size, "Cada tipo deberia tener su tamano")
        assertTrue(radios.all { it > 0.0 })
    }

    @Test
    fun elCirculoColisionaComoUnCirculo() {
        val forma = FormaCirculo(10.0)
        val centro = Vector2D(100.0, 100.0)

        val contacto = forma.contactoPlano(
            Vector2D(108.0, 100.0), centro, 0.0, Vector2D(-1.0, 0.0)
        )
        assertNotNull(contacto)
        assertEquals(2.0, contacto.profundidad, 1e-9)
        assertVectorIgual(Vector2D(-1.0, 0.0), contacto.normal)

        assertNull(forma.contactoPlano(Vector2D(115.0, 100.0), centro, 0.0, Vector2D(-1.0, 0.0)))
    }

    @Test
    fun laCapsulaGolpeaConElExtremo() {
        val forma = FormaCapsula(20.0, 8.0)
        val centro = Vector2D(100.0, 100.0)
        val largo = forma.medioLargo + forma.radio

        // Impacto de frente: solo la punta llega a la pared, 28 px del centro.
        val frontal = forma.contactoPlano(
            centro + Vector2D(largo - 2.0, 0.0), centro, 0.0, Vector2D(-1.0, 0.0)
        )
        assertNotNull(frontal)
        assertEquals(2.0, frontal.profundidad, 1e-9)
        assertVectorIgual(Vector2D(-1.0, 0.0), frontal.normal)

        // La punta justa rozando ya no produce contacto.
        assertNull(
            forma.contactoPlano(centro + Vector2D(largo, 0.0), centro, 0.0, Vector2D(-1.0, 0.0))
        )

        // Impacto de lado: solo cuenta el grosor del tubo.
        val lateral = forma.contactoPlano(
            centro + Vector2D(0.0, 6.0), centro, 0.0, Vector2D(0.0, -1.0)
        )
        assertNotNull(lateral)
        assertEquals(2.0, lateral.profundidad, 1e-9)
        assertVectorIgual(Vector2D(0.0, -1.0), lateral.normal)
    }

    @Test
    fun laCajaColisionaPorEjes() {
        val forma = FormaCaja(15.0, 10.0)
        val centro = Vector2D(100.0, 100.0)

        val horizontal = forma.contactoPlano(
            Vector2D(112.0, 100.0), centro, 0.0, Vector2D(-1.0, 0.0)
        )
        assertNotNull(horizontal)
        assertEquals(3.0, horizontal.profundidad, 1e-9)
        assertVectorIgual(Vector2D(-1.0, 0.0), horizontal.normal)

        val vertical = forma.contactoPlano(
            Vector2D(100.0, 108.0), centro, 0.0, Vector2D(0.0, -1.0)
        )
        assertNotNull(vertical)
        assertEquals(2.0, vertical.profundidad, 1e-9)
    }

    @Test
    fun laCajaApoyadaEnUnaEsquinaTocaElSuelo() {
        val forma = FormaCaja(15.0, 10.0)
        val centro = Vector2D(100.0, 100.0)
        val angulo = Math.PI / 4.0

        // Girada 45 grados: el suelo se alcanza en la esquina, no en el lado.
        val alcance = forma.soporte(Transformaciones.aLocal(Vector2D(0.0, 1.0), angulo))
        val contacto = forma.contactoPlano(
            Vector2D(100.0, 100.0 + alcance - 1.0), centro, angulo, Vector2D(0.0, -1.0)
        )
        assertEquals((15.0 + 10.0) / Math.sqrt(2.0), alcance, 1e-9)
        assertNotNull(contacto)
        assertEquals(1.0, contacto.profundidad, 1e-9)
    }

    @Test
    fun elPoligonoSoloTocaEnSusCaras() {
        val forma = FormaPoligono(6, 10.0)
        val centro = Vector2D(100.0, 100.0)

        // Justo fuera de una cara: no hay contacto.
        assertNull(
            forma.contactoPlano(
                Vector2D(100.0, 100.0 + forma.apotema + 0.1), centro, 0.0, Vector2D(0.0, -1.0)
            )
        )
        // Dentro de una cara: la profundidad la fija el apotema.
        val contacto = forma.contactoPlano(
            Vector2D(100.0, 100.0 + forma.apotema - 1.0), centro, 0.0, Vector2D(0.0, -1.0)
        )
        assertNotNull(contacto)
        assertEquals(1.0, contacto.profundidad, 1e-9)
        assertVectorIgual(Vector2D(0.0, -1.0), contacto.normal)
    }

    @Test
    fun ningunaPelotaAtraviesaElSuelo() {
        for (tipo in TipoPelota.entries) {
            val motor = MotorFisico(Tierra(1000.0, 650.0, Vector2D(0.0, 1500.0)))
            val pelota = Pelota(Vector2D(100.0, 40.0), tipo)
            repeat(1500) { motor.simularPaso(pelota, 1.0 / 60.0) }

            assertTrue(
                pelota.posicion.y <= motor.sueloY + 0.5,
                "${tipo.nombre} se hunde en el suelo"
            )
            assertTrue(
                pelota.posicion.y >= motor.sueloY - pelota.radio - 0.5,
                "${tipo.nombre} flota sobre el suelo"
            )
        }
    }

    @Test
    fun ningunaPelotaAtraviesaLaPared() {
        val pared = Pared(Vector2D(300.0, -500.0), Vector2D(300.0, 2000.0))
        for (tipo in TipoPelota.entries) {
            val motor = MotorFisico(
                Tierra(1000.0, 650.0, Vector2D(0.0, 0.0)),
                listOf(pared)
            )
            val pelota = Pelota(Vector2D(100.0, 300.0), tipo)
            pelota.velocidad = Vector2D(2000.0, 0.0)
            repeat(10) { motor.simularPaso(pelota, 1.0 / 60.0) }

            assertTrue(pelota.posicion.x < 300.0, "${tipo.nombre} atraviesa la pared")
            assertTrue(pelota.velocidad.x < 0.0, "${tipo.nombre} no rebota contra la pared")
        }
    }

    @Test
    fun elArrastreFrenaLaPelotaDeTenis() {
        val motor = MotorFisico(Tierra(gravedad = Vector2D(0.0, 0.0)))
        val pelota = Pelota(Vector2D(50.0, 300.0), TipoPelota.TENIS)
        pelota.velocidad = Vector2D(1200.0, 0.0)

        repeat(60) { motor.simularPaso(pelota, 1.0 / 60.0) }

        assertTrue(pelota.velocidad.x < 1200.0, "El aire debe frenar la pelota de tenis")
        assertTrue(pelota.posicion.x < 1200.0, "Sin arrastre recorreria 1200 px en un segundo")
    }

    @Test
    fun laPelotaPesadaRecorreMasQueLaLigera() {
        val distancia = { tipo: TipoPelota ->
            val motor = MotorFisico(Tierra(1000.0, 650.0, Vector2D(0.0, 900.0)))
            val pelota = Pelota(Vector2D(50.0, 40.0), tipo)
            pelota.velocidad = Vector2D(1400.0, -200.0)
            var recorrida = 0.0
            repeat(120) {
                val antes = pelota.posicion
                motor.simularPaso(pelota, 1.0 / 60.0)
                recorrida += pelota.posicion.distanciaA(antes)
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
            val motor = MotorFisico(Tierra(1000.0, 650.0, Vector2D(0.0, 0.0)))
            val pelota = Pelota(Vector2D(50.0, motor.sueloY - 20.0), tipo)
            pelota.velocidad = Vector2D(900.0, 0.0)
            repeat(60) { motor.simularPaso(pelota, 1.0 / 60.0) }
            pelota.posicion.x
        }

        assertTrue(
            recorrido(TipoPelota.GOMA) > recorrido(TipoPelota.RUGBY),
            "La pelota de goma deberia deslizar mas que la de rugby"
        )
    }

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

            val arboles = GeneradorEntorno.arboles(desde, hasta).count { it.x in desde..hasta }
            val nubes = GeneradorEntorno.nubes(desde, hasta).count { it.x in desde..hasta }
            val briznas = GeneradorEntorno.pasto(desde, hasta).count { it.x in desde..hasta }

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
            .map { it.x + it.ancho / 2.0 }
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
            .map { it.x to 58.0 * it.escala }
            .sortedBy { it.first }
        for (i in 0 until copas.size - 1) {
            val separacion = copas[i + 1].first - copas[i].first
            val mitad = (copas[i].second + copas[i + 1].second) / 2.0
            assertTrue(separacion > mitad, "Arboles solapados en x=${copas[i].first}")
        }

        val anchos = GeneradorEntorno.nubes(desde, hasta)
            .map { it.x to 92.0 * it.escala }
            .sortedBy { it.first }
        for (i in 0 until anchos.size - 1) {
            val separacion = anchos[i + 1].first - anchos[i].first
            assertTrue(separacion > 100.0, "Nubes amontonadas en x=${anchos[i].first}")
        }
    }

    @Test
    fun elDecoradoNoSaleDeLaFranjaDelCielo() {
        for (nube in GeneradorEntorno.nubes(-3000.0, 3000.0)) {
            assertTrue(nube.y >= 20.0, "Nube muy alta en y=${nube.y}")
            assertTrue(nube.y <= 160.0, "Nube muy baja en y=${nube.y}")
        }
    }

    private fun assertVectorIgual(esperado: Vector2D, real: Vector2D) {
        assertEquals(esperado.x, real.x, 1e-9, "Componente x")
        assertEquals(esperado.y, real.y, 1e-9, "Componente y")
    }
}
