package org.example.simulacion

import org.example.modelo.Pared

/** Coleccion de estelas de rotura que se apagan con el tiempo. */
class RegistroEfectos : Efectos {

    private val efectos = mutableListOf<EfectoRotura>()

    override fun lista(): List<EfectoRotura> = efectos.toList()

    override fun agregarRoturas(paredes: List<Pared>, duracion: Double) {
        for (pared in paredes) {
            efectos.add(EfectoRotura(pared, duracion))
        }
    }

    override fun avanzar(deltaTime: Double) {
        val iterator = efectos.iterator()
        while (iterator.hasNext()) {
            val efecto = iterator.next()
            efecto.avanzar(deltaTime)
            if (efecto.agotado()) iterator.remove()
        }
    }

    override fun limpiar() {
        efectos.clear()
    }
}
