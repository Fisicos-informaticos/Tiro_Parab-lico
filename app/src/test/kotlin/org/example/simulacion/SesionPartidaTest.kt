package org.example.simulacion

import org.example.modelo.Pelota
import org.example.modelo.Proyectil
import org.example.modelo.TipoPelota
import org.example.modelo.Vector2D
import org.example.modelo.creadores.FabricaProyectiles
import org.example.modos.CatalogoModos
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class SesionPartidaTest {

    @Test
    fun delegaLaCreacionDelProyectilEnLaFabricaInyectada() {
        val solicitudes = mutableListOf<Pair<Vector2D, TipoPelota>>()
        val proyectiles = mutableListOf<Proyectil>()
        val fabrica = object : FabricaProyectiles {
            override fun crear(posicion: Vector2D, tipo: TipoPelota): Proyectil {
                solicitudes += posicion to tipo
                return Pelota(posicion, tipo).also(proyectiles::add)
            }
        }
        val sesion = SesionPartida(
            alto = 650.0,
            tipoPelotaInicial = TipoPelota.GOMA,
            modoInicial = CatalogoModos.porDefecto(),
            fabricaProyectiles = fabrica
        )

        assertSame(proyectiles[0], sesion.pelota())
        assertEquals(Vector2D(50.0, 590.0) to TipoPelota.GOMA, solicitudes[0])

        sesion.cambiarTipoPelota(TipoPelota.BOLICHE)
        assertSame(proyectiles[1], sesion.pelota())
        assertEquals(Vector2D(50.0, 590.0) to TipoPelota.BOLICHE, solicitudes[1])

        val nuevaPosicion = Vector2D(120.0, 300.0)
        sesion.colocarPelota(nuevaPosicion)
        assertSame(proyectiles[2], sesion.pelota())
        assertEquals(nuevaPosicion to TipoPelota.BOLICHE, solicitudes[2])
    }
}
