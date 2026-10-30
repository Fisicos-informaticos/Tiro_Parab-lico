package org.example.simulacion

import org.example.modelo.Pared
import kotlin.math.max
import kotlin.math.min

/** Superficies extra sobre las que se puede apoyar la pelota antes de lanzar. */
class SuperficiesLanzamiento {

    private val superficies = mutableListOf<Pared>()

    fun agregar(pared: Pared) {
        superficies.add(pared)
    }

    fun limpiar() {
        superficies.clear()
    }

    fun lista(): List<Pared> = superficies.toList()

    /** y donde la pelota apoya: suelo o superficie de lanzamiento bajo el punto. */
    fun yDeApoyo(x: Double, radio: Double, sueloY: Double): Double =
        (ySuperficieBajo(x, radio) ?: sueloY) - radio - ALTURA_APOYO_PELOTA

    private fun ySuperficieBajo(x: Double, radio: Double): Double? {
        var mejor: Double? = null
        for (superficie in superficies) {
            val x1 = minOf(superficie.inicio().x(), superficie.fin().x())
            val x2 = maxOf(superficie.inicio().x(), superficie.fin().x())
            if (x < x1 - radio * 0.8) continue
            if (x > x2 + radio * 0.8) continue

            val ySuperficie = minOf(superficie.inicio().y(), superficie.fin().y())
            mejor = when {
                mejor == null -> ySuperficie
                ySuperficie < mejor -> ySuperficie
                else -> mejor
            }
        }
        return mejor
    }

    companion object {
        const val ALTURA_APOYO_PELOTA = 2.0
    }
}
