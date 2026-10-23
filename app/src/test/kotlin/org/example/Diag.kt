package org.example

import org.example.modos.CatalogoModos
import org.example.modelo.Vector2D
import org.example.simulacion.EstadoSimulacion
import org.example.simulacion.Simulacion
import kotlin.test.Test

class Diag {

    @Test
    fun diagnostico() {
        for (g in listOf("Tierra (1.00 g)", "Luna (0.17 g)")) {
            val s = Simulacion()
            s.seleccionarModo(CatalogoModos.CAIDA_LIBRE)
            s.aplicarGravedad(g)
            s.pulsar(Vector2D(500.0, 250.0))
            s.soltar(Vector2D(500.0, 250.0))
            var pasos = 0
            while (pasos < 6000 && s.estado !is EstadoSimulacion.EnSuelo) {
                s.actualizar(Simulacion.DT_FISICA)
                pasos++
            }
            println("$g -> estado=${s.estado.nombre} t=${s.tiempoVuelo} pasos=$pasos x=${s.pelota.posicion.x} y=${s.pelota.posicion.y} v=${s.pelota.velocidad.magnitud}")
        }
        println("sueloY=${Simulacion().sueloY}")
    }
}
