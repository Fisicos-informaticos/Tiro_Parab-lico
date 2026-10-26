package org.example

import org.example.modelo.Vector2D
import org.example.simulacion.Simulacion
import kotlin.test.Test

class DbgTest {
    @Test
    fun dbg() {
        // Tiro parabolico clasico: ¿acaba por reposo o por el tiempo maximo?
        val s = Simulacion()
        s.pulsar(Vector2D(500.0, 600.0))
        s.pulsar(Vector2D(500.0, 600.0))
        s.arrastrar(Vector2D(350.0, 715.0))
        s.soltar(Vector2D(350.0, 715.0))
        println("estado=${s.estado().nombre()} v=${s.pelota().velocidad()}")
        var i = 0
        while (i < 6000 && s.estado().estaEnVuelo()) { s.actualizar(Simulacion.DT_FISICA); i++ }
        println("parabolico -> t=${s.tiempoVuelo()} pasos=$i v=${s.pelota().velocidad()} y=${s.pelota().posicion().y()} suelo=${s.sueloY()}")

        // Mismo caso pero mirando si la velocidad llega a bajar de 0.5 en el suelo
        val s2 = Simulacion()
        s2.pulsar(Vector2D(500.0, 600.0))
        s2.pulsar(Vector2D(500.0, 600.0))
        s2.arrastrar(Vector2D(450.0, 650.0))
        s2.soltar(Vector2D(450.0, 650.0))
        var j = 0
        var vistoReposo = false
        while (j < 3000 && s2.estado().estaEnVuelo()) {
            s2.actualizar(Simulacion.DT_FISICA); j++
            if (s2.pelota().velocidad().magnitud() < 0.5 && s2.pelota().posicion().y() >= s2.sueloY() - s2.pelota().radio() - 2.0) vistoReposo = true
        }
        println("parabolico suave -> estado=${s2.estado().nombre()} t=${s2.tiempoVuelo()} paso=$j vistoReposo=$vistoReposo v=${s2.pelota().velocidad()}")
    }
}
