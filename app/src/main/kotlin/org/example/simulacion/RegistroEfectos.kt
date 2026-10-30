package org.example.simulacion

import org.example.modelo.Pared

/** Coleccion de estelas de rotura que se apagan con el tiempo. */
class RegistroEfectos {

    private val efectos = mutableListOf<EfectoRotura>()

    fun lista(): List<EfectoRotura> = efectos.toList()

    fun agregarRoturas(paredes: List<Pared>, duracion: Double) {
        for (pared in paredes) {
            efectos.add(EfectoRotura(pared, duracion))
        }
    }

    fun avanzar(deltaTime: Double) {
        val iterator = efectos.iterator()
        while (iterator.hasNext()) {
            val efecto = iterator.next()
            efecto.avanzar(deltaTime)
            if (efecto.agotado()) iterator.remove()
        }
    }

    fun limpiar() {
        efectos.clear()
    }
}
