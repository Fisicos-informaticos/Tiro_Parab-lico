package org.example

import javafx.animation.AnimationTimer

/** AnimationTimer que convierte nanosegundos en pasos acotados y delega el fotograma. */
class BucleJuego(
    private val alFotograma: (Double) -> Unit
) {
    private var ultimoTiempo = 0L

    fun iniciar() {
        object : AnimationTimer() {
            override fun handle(now: Long) {
                if (ultimoTiempo == 0L) {
                    ultimoTiempo = now
                    return
                }
                val deltaSegundos = (now - ultimoTiempo) / 1_000_000_000.0
                ultimoTiempo = now
                alFotograma(deltaSegundos.coerceAtMost(0.05))
            }
        }.start()
    }
}
