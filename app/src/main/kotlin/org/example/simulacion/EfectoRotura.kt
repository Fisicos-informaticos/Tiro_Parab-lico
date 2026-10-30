package org.example.simulacion

import org.example.modelo.Pared

/** Estela que deja una pared al romperse y que se apaga sola. */
class EfectoRotura(
    private val pared: Pared,
    vida: Double
) {
    private var vida: Double = vida

    fun pared(): Pared = pared

    fun proporcion(): Double =
        (vida / Simulacion.DURACION_ROTURA).coerceIn(0.0, 1.0)

    fun agotado(): Boolean = vida <= 0.0

    fun avanzar(deltaTime: Double) {
        vida = (vida - deltaTime).coerceAtLeast(0.0)
    }
}
