package org.example.fisica

import org.example.modelo.Pared
import org.example.modelo.Proyectil
import org.example.modelo.Vector2D

/**
 * Decide cuando un proyectil ha terminado su recorrido. Vive aparte del
 * [MotorFisico] porque es una regla propia (SRP): el motor integra el
 * movimiento y este colaborador solo responde "¿se ha parado ya?".
 *
 * Se considera reposo cuando la rapidez es practicamente nula y ademas algo
 * sostiene al proyectil (el suelo, una pared o la ausencia de gravedad). En el
 * aire, rozando una pared vertical, el vuelo continua.
 */
class DetectorReposo(private var ambiente: Ambiente) : DetectorDeReposo {

    override fun estaEnReposo(proyectil: Proyectil, paredes: List<Pared>): Boolean =
        rapidezPracticamenteNula(proyectil.velocidad()) &&
            (tieneApoyo(proyectil, paredes) || !hayGravedad())

    override fun cambiarAmbiente(ambiente: Ambiente) {
        this.ambiente = ambiente
    }

    /** sqrt(Vx^2 + Vy^2) por debajo del umbral. */
    private fun rapidezPracticamenteNula(velocidad: Vector2D): Boolean =
        velocidad.magnitud() < UMBRAL_REPOSO

    private fun hayGravedad(): Boolean = ambiente.gravedad().magnitud() > EPSILON

    private fun tieneApoyo(proyectil: Proyectil, paredes: List<Pared>): Boolean {
        val apoyoSuelo = ambiente.sueloY() - proyectil.radio() - TOLERANCIA_APOYO
        if (proyectil.posicion().y() >= apoyoSuelo) return true

        return paredes.any { apoyaEnPared(proyectil, it) }
    }

    /**
     * Toca la pared y esta por encima de ella: solo entonces la pared empuja
     * hacia arriba y sostiene a la pelota. Rozar una pared vertical no vale.
     */
    private fun apoyaEnPared(proyectil: Proyectil, pared: Pared): Boolean {
        val delta = proyectil.posicion() - pared.puntoMasCercano(proyectil.posicion())
        if (delta.y() > -TOLERANCIA_APOYO) return false

        val alcance = pared.grosor() / 2.0 + proyectil.radio() + TOLERANCIA_APOYO
        return delta.magnitud() <= alcance
    }

    companion object {
        /** Rapidez (px/s) por debajo de la cual el proyectil se da por parado. */
        private const val UMBRAL_REPOSO = 1.0

        /** Holgura con la que se acepta que el suelo o una pared apoyan a la pelota. */
        private const val TOLERANCIA_APOYO = 2.0

        private const val EPSILON = 1e-9
    }
}
