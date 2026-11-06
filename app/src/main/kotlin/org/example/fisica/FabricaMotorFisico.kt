package org.example.fisica

import org.example.modelo.Pared

/**
 * Fabrica de motores fisicos. La simulacion la usa tanto para el motor real
 * como para el motor desechable de la previsualizacion, sin conocer [MotorFisico].
 */
interface FabricaMotorFisico {

    fun crear(ambiente: Ambiente, paredes: List<Pared> = emptyList()): PhysicsEngine
}

/** Implementacion por defecto del motor fisico. */
class FabricaMotorFisicoPredeterminada : FabricaMotorFisico {

    override fun crear(ambiente: Ambiente, paredes: List<Pared>): PhysicsEngine =
        MotorFisico(ambiente, paredes)
}
