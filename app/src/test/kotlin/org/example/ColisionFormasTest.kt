package org.example

import org.example.modelo.Vector2D
import org.example.modelo.colision.DatosContactoPlano
import org.example.modelo.colision.FormaCaja
import org.example.modelo.colision.FormaCapsula
import org.example.modelo.colision.FormaCirculo
import org.example.modelo.colision.FormaPoligono
import org.example.modelo.colision.Transformaciones
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ColisionFormasTest {
    private fun assertVectorIgual(esperado: Vector2D, real: Vector2D) {
        assertEquals(esperado.x(), real.x(), 1e-9, "Componente x")
        assertEquals(esperado.y(), real.y(), 1e-9, "Componente y")
    }

    @Test
    fun elCirculoColisionaComoUnCirculo() {
        val forma = FormaCirculo(10.0)
        val centro = Vector2D(100.0, 100.0)

        val contacto = forma.contactoPlano(
            DatosContactoPlano(Vector2D(108.0, 100.0), centro, 0.0, Vector2D(-1.0, 0.0))
        )
        assertNotNull(contacto)
        assertEquals(2.0, contacto.profundidad(), 1e-9)
        assertVectorIgual(Vector2D(-1.0, 0.0), contacto.normal())

        assertNull(
            forma.contactoPlano(
                DatosContactoPlano(Vector2D(115.0, 100.0), centro, 0.0, Vector2D(-1.0, 0.0))
            )
        )
    }

    @Test
    fun laCapsulaGolpeaConElExtremo() {
        val forma = FormaCapsula(20.0, 8.0)
        val centro = Vector2D(100.0, 100.0)
        val largo = forma.medioLargo() + forma.radio()

        val frontal = forma.contactoPlano(
            DatosContactoPlano(
                centro + Vector2D(largo - 2.0, 0.0), centro, 0.0, Vector2D(-1.0, 0.0)
            )
        )
        assertNotNull(frontal)
        assertEquals(2.0, frontal.profundidad(), 1e-9)
        assertVectorIgual(Vector2D(-1.0, 0.0), frontal.normal())

        assertNull(
            forma.contactoPlano(
                DatosContactoPlano(centro + Vector2D(largo, 0.0), centro, 0.0, Vector2D(-1.0, 0.0))
            )
        )

        val lateral = forma.contactoPlano(
            DatosContactoPlano(
                centro + Vector2D(0.0, 6.0), centro, 0.0, Vector2D(0.0, -1.0)
            )
        )
        assertNotNull(lateral)
        assertEquals(2.0, lateral.profundidad(), 1e-9)
        assertVectorIgual(Vector2D(0.0, -1.0), lateral.normal())
    }

    @Test
    fun laCajaColisionaPorEjes() {
        val forma = FormaCaja(15.0, 10.0)
        val centro = Vector2D(100.0, 100.0)

        val horizontal = forma.contactoPlano(
            DatosContactoPlano(Vector2D(112.0, 100.0), centro, 0.0, Vector2D(-1.0, 0.0))
        )
        assertNotNull(horizontal)
        assertEquals(3.0, horizontal.profundidad(), 1e-9)
        assertVectorIgual(Vector2D(-1.0, 0.0), horizontal.normal())

        val vertical = forma.contactoPlano(
            DatosContactoPlano(Vector2D(100.0, 108.0), centro, 0.0, Vector2D(0.0, -1.0))
        )
        assertNotNull(vertical)
        assertEquals(2.0, vertical.profundidad(), 1e-9)
    }

    @Test
    fun laCajaApoyadaEnUnaEsquinaTocaElSuelo() {
        val forma = FormaCaja(15.0, 10.0)
        val centro = Vector2D(100.0, 100.0)
        val angulo = Math.PI / 4.0

        val alcance = forma.soporte(Transformaciones.aLocal(Vector2D(0.0, 1.0), angulo))
        val contacto = forma.contactoPlano(
            DatosContactoPlano(
                Vector2D(100.0, 100.0 + alcance - 1.0), centro, angulo, Vector2D(0.0, -1.0)
            )
        )
        assertEquals((15.0 + 10.0) / Math.sqrt(2.0), alcance, 1e-9)
        assertNotNull(contacto)
        assertEquals(1.0, contacto.profundidad(), 1e-9)
    }

    @Test
    fun elPoligonoSoloTocaEnSusCaras() {
        val forma = FormaPoligono(6, 10.0)
        val centro = Vector2D(100.0, 100.0)

        assertNull(
            forma.contactoPlano(
                DatosContactoPlano(
                    Vector2D(100.0, 100.0 + forma.apotema() + 0.1),
                    centro,
                    0.0,
                    Vector2D(0.0, -1.0)
                )
            )
        )
        val contacto = forma.contactoPlano(
            DatosContactoPlano(
                Vector2D(100.0, 100.0 + forma.apotema() - 1.0),
                centro,
                0.0,
                Vector2D(0.0, -1.0)
            )
        )
        assertNotNull(contacto)
        assertEquals(1.0, contacto.profundidad(), 1e-9)
        assertVectorIgual(Vector2D(0.0, -1.0), contacto.normal())
    }
}
