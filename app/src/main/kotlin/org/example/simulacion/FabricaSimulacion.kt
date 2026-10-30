package org.example.simulacion

import org.example.coordenadas.Camara
import org.example.coordenadas.CamaraSimulacion
import org.example.coordenadas.TransformadorCoordenadas
import org.example.coordenadas.TransformadorCoordenadasSimulacion
import org.example.entrada.DragInputHandler
import org.example.entrada.InputHandler
import org.example.fisica.FabricaAmbiente
import org.example.fisica.FabricaMotorFisico
import org.example.fisica.FabricaMotorFisicoPredeterminada
import org.example.fisica.FabricaTierra
import org.example.fisica.PhysicsEngine
import org.example.fisica.Tierra
import org.example.herramientas.HerramientaCreacionParedes
import org.example.herramientas.HerramientaParedes
import org.example.modelo.TipoPared
import org.example.modelo.Vector2D
import org.example.modelo.creadores.ConstructorParedes
import org.example.modelo.creadores.CreadorParedes
import org.example.modos.CatalogoModos
import org.example.modos.FabricaModos

/**
 * Punto de composicion del nucleo. Es el unico lugar que conoce las clases
 * concretas y se encarga de cablearlas para que [Simulacion] reciba todo a
 * traves de sus interfaces.
 */
object FabricaSimulacion {

    fun crear(ancho: Double = 1000.0, alto: Double = 650.0): Simulacion {
        val componentes = componentes(ancho, alto)
        return Simulacion(
            ancho,
            alto,
            componentes.entrada,
            componentes.camara,
            componentes.motor,
            componentes.creadorParedes,
            componentes.transformador,
            componentes.herramienta,
            componentes.fabricaAmbiente,
            componentes.fabricaMotorFisico,
            componentes.fabricaModos
        )
    }

    fun componentes(
        ancho: Double,
        alto: Double,
        entrada: InputHandler? = null
    ): ComponentesSimulacion {
        val camara = CamaraSimulacion(ancho, alto)
        val transformador = TransformadorCoordenadasSimulacion(camara)
        val fabricaAmbiente = FabricaTierra()
        val ambiente = fabricaAmbiente.crear(ancho, alto, gravedadPorDefecto())
        val fabricaMotorFisico = FabricaMotorFisicoPredeterminada()
        val motor = fabricaMotorFisico.crear(ambiente)
        val creadorParedes = ConstructorParedes(motor, TipoPared.porDefecto())
        val herramienta = HerramientaCreacionParedes(
            transformador,
            creadorParedes,
            Simulacion.LONGITUD_MINIMA_PARED
        )

        return ComponentesSimulacion(
            entrada = entrada ?: DragInputHandler(),
            camara = camara,
            motor = motor,
            creadorParedes = creadorParedes,
            transformador = transformador,
            herramienta = herramienta,
            fabricaAmbiente = fabricaAmbiente,
            fabricaMotorFisico = fabricaMotorFisico,
            fabricaModos = CatalogoModos
        )
    }

    private fun gravedadPorDefecto(): Vector2D = Vector2D(
        0.0,
        Tierra.GRAVEDAD_PIXELES * Simulacion.factorGravedadPorDefecto()
    )
}

/** Dependencias ya cableadas que comparten la construccion de conveniencia y la fabrica. */
class ComponentesSimulacion(
    val entrada: InputHandler,
    val camara: Camara,
    val motor: PhysicsEngine,
    val creadorParedes: CreadorParedes,
    val transformador: TransformadorCoordenadas,
    val herramienta: HerramientaParedes,
    val fabricaAmbiente: FabricaAmbiente,
    val fabricaMotorFisico: FabricaMotorFisico,
    val fabricaModos: FabricaModos
)
