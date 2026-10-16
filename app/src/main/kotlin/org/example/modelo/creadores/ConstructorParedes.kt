package org.example.modelo.creadores

import org.example.fisica.MotorFisico
import org.example.modelo.Pared
import org.example.modelo.TipoPared

/**
 * Constructor de paredes que delega en el motor físico.
 * Implementa SRP al separar la construcción/gestión de paredes de la UI.
 */
class ConstructorParedes(
    private val motor: MotorFisico,
    private var tipoActual: TipoPared = TipoPared.POR_DEFECTO
) : CreadorParedes {
    override val paredes: List<Pared> get() = motor.obtenerParedes()

    override fun agregarPared(pared: Pared) {
        motor.agregarPared(pared)
    }

    override fun limpiarParedes() {
        motor.limpiarParedes()
    }

    override fun seleccionarTipoPared(tipo: TipoPared) {
        tipoActual = tipo
    }

    override fun tipoParedActual(): TipoPared = tipoActual
}
