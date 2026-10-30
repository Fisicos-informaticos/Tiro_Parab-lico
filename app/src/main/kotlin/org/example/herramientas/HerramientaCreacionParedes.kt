package org.example.herramientas

import org.example.coordenadas.TransformadorCoordenadas
import org.example.modelo.Pared
import org.example.modelo.Vector2D
import org.example.modelo.creadores.CreadorParedes

/**
 * Herramienta para crear paredes arrastrando el ratón.
 * Sigue SRP: solo se encarga de la interacción para dibujar paredes.
 * Sigue DIP: depende de abstracciones (TransformadorCoordenadas, CreadorParedes).
 * Puede extenderse (OCP) sin modificar código existente.
 */
class HerramientaCreacionParedes(
    private val transformador: TransformadorCoordenadas,
    private val creador: CreadorParedes,
    private val longitudMinima: Double = 12.0
) : HerramientaParedes {
    private var paredTemporal: Pared? = null
    private var estaActiva: Boolean = false

    override fun paredTemporal(): Pared? = paredTemporal

    override fun activar() {
        estaActiva = true
    }

    override fun desactivar() {
        estaActiva = false
        cancelar()
    }

    override fun iniciar(puntoPantalla: Vector2D) {
        if (!estaActiva) return
        val puntoMundo = transformador.pantallaAMundo(puntoPantalla)
        paredTemporal = Pared(puntoMundo, puntoMundo, creador.tipoParedActual())
    }

    override fun actualizar(puntoPantalla: Vector2D) {
        if (!estaActiva) return
        val puntoMundo = transformador.pantallaAMundo(puntoPantalla)
        paredTemporal = paredTemporal?.conFin(puntoMundo)
    }

    override fun finalizar(puntoPantalla: Vector2D?) {
        if (!estaActiva) return
        val temporal = paredTemporal ?: return
        val paredFinal = if (puntoPantalla != null) {
            temporal.conFin(transformador.pantallaAMundo(puntoPantalla))
        } else {
            temporal
        }
        cancelar()
        if (paredFinal.longitud() >= longitudMinima) {
            creador.agregarPared(paredFinal)
        }
    }

    override fun cancelar() {
        paredTemporal = null
    }
}
