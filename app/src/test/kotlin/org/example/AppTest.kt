package org.example

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AppTest {
    @Test fun alcancePara45Grados() {
        val proyeccion = ProjectileMotion(20.0, 45.0)
        val esperado = 20.0 * 20.0 / 9.81
        assertEquals(esperado, proyeccion.alcanceMaximo, esperado * 0.001)
    }

    @Test fun alturaMaximaEn45Grados() {
        val proyeccion = ProjectileMotion(20.0, 45.0)
        val esperado = 20.0 * 20.0 * 0.5 / 9.81
        assertEquals(esperado, proyeccion.alturaMaxima, esperado * 0.001)
    }

    @Test fun alcanceHastaAlturaFueraDeAlcance() {
        val proyeccion = ProjectileMotion(10.0, 30.0)
        assertNull(proyeccion.alcanceHastaAltura(100.0))
    }
}
