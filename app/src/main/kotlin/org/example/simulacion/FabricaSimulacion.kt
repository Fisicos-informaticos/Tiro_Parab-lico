package org.example.simulacion

import org.example.coordenadas.CamaraSimulacion
import org.example.coordenadas.TransformadorCoordenadasSimulacion
import org.example.entrada.DragInputHandler
import org.example.entrada.InputHandler
import org.example.fisica.FabricaMotorFisicoPredeterminada
import org.example.fisica.FabricaTierra
import org.example.fisica.Tierra
import org.example.herramientas.HerramientaCreacionParedes
import org.example.modelo.TipoPelota
import org.example.modelo.TipoPared
import org.example.modelo.Vector2D
import org.example.modelo.creadores.ConstructorParedes
import org.example.modelo.creadores.FabricaProyectilesPredeterminada
import org.example.modos.CatalogoModos

/**
 * Punto de composicion del nucleo. Es el unico lugar que conoce las clases
 * concretas y se encarga de construirlas para que [Simulacion] reciba todo a
 * traves de sus interfaces.
 */
object FabricaSimulacion {
    private val ensambladorServicios = EnsambladorServicios()

    fun crear(ancho: Double = 1000.0, alto: Double = 650.0): Simulacion =
        componentes(ancho, alto).crearSimulacion(ancho, alto)

    fun componentes(
        ancho: Double,
        alto: Double,
        entrada: InputHandler? = null
    ): ComponentesSimulacion = crearComponentes(ancho, alto, entrada ?: DragInputHandler())

    private fun crearComponentes(
        ancho: Double,
        alto: Double,
        entrada: InputHandler
    ): ComponentesSimulacion {
        val fisica = crearFisica(ancho, alto)
        val partida = crearPartida(alto)
        val contextoServicios = ContextoServicios(fisica, partida, ancho, alto, entrada)
        val servicios = ensambladorServicios.crearServicios(contextoServicios)
        val comandos = ensambladorServicios.crearComandos(
            ContextoComandos(fisica, partida, servicios, ancho, alto, entrada)
        )
        return ComponentesSimulacion(
            fisica,
            partida,
            ComponentesControl(entrada, servicios, comandos)
        )
    }

    private fun crearFisica(ancho: Double, alto: Double): ComponentesFisica {
        val fabricaAmbiente = FabricaTierra()
        val ambiente = fabricaAmbiente.crear(ancho, alto, gravedadPorDefecto())
        val fabricaMotorFisico = FabricaMotorFisicoPredeterminada()
        val motor = fabricaMotorFisico.crear(ambiente)
        val base = BaseFisica(
            camara = CamaraSimulacion(ancho, alto),
            fabricaAmbiente = fabricaAmbiente,
            ambiente = ambiente,
            fabricaMotorFisico = fabricaMotorFisico,
            motor = motor,
            creadorParedes = ConstructorParedes(motor, TipoPared.porDefecto())
        )
        return base.ensamblar()
    }

    private data class BaseFisica(
        private val camara: CamaraSimulacion,
        private val fabricaAmbiente: FabricaTierra,
        private val ambiente: org.example.fisica.Ambiente,
        private val fabricaMotorFisico: FabricaMotorFisicoPredeterminada,
        private val motor: org.example.fisica.PhysicsEngine,
        private val creadorParedes: ConstructorParedes
    ) {
        fun ensamblar(): ComponentesFisica {
            val transformador = TransformadorCoordenadasSimulacion(camara)
            val herramienta = HerramientaCreacionParedes(
                transformador,
                creadorParedes,
                Simulacion.LONGITUD_MINIMA_PARED
            )
            return ComponentesFisica(
                camara, transformador, ambiente, fabricaAmbiente,
                fabricaMotorFisico, motor, creadorParedes, herramienta
            )
        }
    }

    private fun crearPartida(alto: Double): ComponentesPartida {
        val fabricaModos = CatalogoModos
        return ComponentesPartida(
            sesion = SesionPartida(
                alto,
                TipoPelota.porDefecto(),
                fabricaModos.porDefecto(),
                FabricaProyectilesPredeterminada()
            ),
            metricas = MetricasTiro(),
            efectos = RegistroEfectos(),
            superficies = SuperficiesLanzamiento(),
            fabricaModos = fabricaModos
        )
    }

    private fun gravedadPorDefecto(): Vector2D = Vector2D(
        0.0,
        Tierra.GRAVEDAD_PIXELES * Simulacion.factorGravedadPorDefecto()
    )
}
