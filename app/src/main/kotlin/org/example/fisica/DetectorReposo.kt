package org.example.fisica

import org.example.modelo.Pared
import org.example.modelo.Proyectil
import org.example.modelo.Vector2D

class DetectorReposo(private var ambiente: Ambiente) : DetectorDeReposo {
    override fun estaEnReposo(proyectil: Proyectil, paredes: List<Pared>): Boolean =
        rapidezPracticamenteNula(proyectil.velocidad()) && (tieneApoyo(proyectil, paredes) || !hayGravedad())

    override fun cambiarAmbiente(ambiente: Ambiente) { this.ambiente = ambiente }

    private fun rapidezPracticamenteNula(velocidad: Vector2D): Boolean = velocidad.magnitud() < UMBRAL_REPOSO
    private fun hayGravedad(): Boolean = ambiente.gravedad().magnitud() > EPSILON

    private fun tieneApoyo(proyectil: Proyectil, paredes: List<Pared>): Boolean {
        val apoyoSuelo = ambiente.sueloY() - proyectil.radio() - TOLERANCIA_APOYO
        if (proyectil.posicion().y() >= apoyoSuelo) return true
        return paredes.any { apoyaEnPared(proyectil, it) }
    }

    private fun apoyaEnPared(proyectil: Proyectil, pared: Pared): Boolean {
        val delta = proyectil.posicion() - pared.puntoMasCercano(proyectil.posicion())
        if (delta.y() > -TOLERANCIA_APOYO) return false
        val alcance = pared.grosor() / 2.0 + proyectil.radio() + TOLERANCIA_APOYO
        return delta.magnitud() <= alcance
    }

    companion object {
        private const val UMBRAL_REPOSO = 1.0
        private const val TOLERANCIA_APOYO = 2.0
        private const val EPSILON = 1e-9
    }
}
