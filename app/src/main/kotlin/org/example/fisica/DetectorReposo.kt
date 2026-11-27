package org.example.fisica

import org.example.modelo.Pared
import org.example.modelo.ProyectilVisual
import org.example.modelo.Vector2D

class DetectorReposo(ambiente: Ambiente) : DetectorDeReposo {
    private val datos = DatosDetectorReposo(ambiente)

    override fun estaEnReposo(proyectil: ProyectilVisual, paredes: List<Pared>): Boolean =
        datos.estaEnReposo(proyectil, paredes)

    override fun cambiarAmbiente(ambiente: Ambiente) = datos.cambiarAmbiente(ambiente)

    private data class DatosDetectorReposo(
        var ambiente: Ambiente
    ) {
        fun estaEnReposo(proyectil: ProyectilVisual, paredes: List<Pared>): Boolean =
            rapidezPracticamenteNula(proyectil.velocidad()) && (tieneApoyo(proyectil, paredes) || !hayGravedad())

        fun cambiarAmbiente(ambiente: Ambiente) {
            this.ambiente = ambiente
        }

        fun descripcion(): String =
            "detector(ambiente=${ambiente.javaClass.simpleName}, suelo=${ambiente.sueloY()}, gravedad=${ambiente.gravedad()})"

        fun esValido(): Boolean = ambiente.ancho() > 0 && ambiente.alto() > 0

        fun resumen(): String =
            "${ambiente.javaClass.simpleName} suelo=${ambiente.sueloY()} gravedad=${ambiente.gravedad().magnitud()}"

        private fun rapidezPracticamenteNula(velocidad: Vector2D): Boolean = velocidad.magnitud() < UMBRAL_REPOSO

        private fun hayGravedad(): Boolean = ambiente.gravedad().magnitud() > EPSILON

        private fun tieneApoyo(proyectil: ProyectilVisual, paredes: List<Pared>): Boolean {
            val apoyoSuelo = ambiente.sueloY() - proyectil.radio() - TOLERANCIA_APOYO
            if (proyectil.posicion().y() >= apoyoSuelo) return true
            return paredes.any { apoyaEnPared(proyectil, it) }
        }

        private fun apoyaEnPared(proyectil: ProyectilVisual, pared: Pared): Boolean {
            val delta = proyectil.posicion() - pared.puntoMasCercano(proyectil.posicion())
            if (delta.y() > -TOLERANCIA_APOYO) return false
            val alcance = pared.grosor() / 2.0 + proyectil.radio() + TOLERANCIA_APOYO
            return delta.magnitud() <= alcance
        }

        private companion object {
            const val UMBRAL_REPOSO = 1.0
            const val TOLERANCIA_APOYO = 2.0
            const val EPSILON = 1e-9
        }
    }
}
