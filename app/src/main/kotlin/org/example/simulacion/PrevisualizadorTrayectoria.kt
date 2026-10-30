package org.example.simulacion

import org.example.fisica.Ambiente
import org.example.fisica.FabricaMotorFisico
import org.example.modelo.Pared
import org.example.modelo.Pelota
import org.example.modelo.TipoPelota
import org.example.modelo.Vector2D
import org.example.modos.CondicionPrevia

/** Simula una copia del tiro para dibujar la trayectoria sin alterar el mundo real. */
class PrevisualizadorTrayectoria(
    private val fabricaMotor: FabricaMotorFisico,
    private val ambienteActual: () -> Ambiente
) {

    fun previsualizar(
        condicion: CondicionPrevia,
        tipoPelota: TipoPelota,
        paredes: List<Pared>
    ): List<Vector2D> {
        val simulada = Pelota(condicion.posicion(), tipoPelota)
        condicion.aplicarA(simulada)

        // Copias: previsualizar nunca debe gastar el danio de las paredes de verdad.
        val motorSimulado = fabricaMotor.crear(
            ambienteActual(),
            paredes.map { it.copia() }
        )
        val puntos = mutableListOf(Vector2D(simulada.posicion().x(), simulada.posicion().y()))
        repeat(PASOS_PREVISUALIZACION) {
            motorSimulado.simularPaso(simulada, Simulacion.DT_FISICA)
            puntos.add(Vector2D(simulada.posicion().x(), simulada.posicion().y()))
            if (simulada.posicion().y() >= motorSimulado.sueloY() - simulada.radio()) return puntos
        }
        return puntos
    }

    private companion object {
        const val PASOS_PREVISUALIZACION = 150
    }
}
