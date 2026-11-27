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
 * Punto de composicion del nucleo. Recibe fabricas pequenas por constructor para
 * que las piezas concretas se sustituyan sin tocar el ensamblado principal.
 */
internal class FabricaSimulacion(
    private val fisica: FabricaComponentesFisica,
    private val partida: FabricaComponentesPartida,
    private val ensambladorServicios: EnsambladorServicios = EnsambladorServicios()
) {
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
        val componentesFisica = fisica.crear(ancho, alto)
        val componentesPartida = partida.crear(alto)
        val contextoServicios = ContextoServicios(componentesFisica, componentesPartida, ancho, alto, entrada)
        val servicios = ensambladorServicios.crearServicios(contextoServicios)
        val comandos = ensambladorServicios.crearComandos(
            ContextoComandos(componentesFisica, componentesPartida, servicios, ancho, alto, entrada)
        )
        return ComponentesSimulacion(
            componentesFisica,
            componentesPartida,
            ComponentesControl(entrada, servicios, comandos)
        )
    }

    companion object {
        fun crear(ancho: Double = 1000.0, alto: Double = 650.0): Simulacion =
            predeterminada.crear(ancho, alto)

        fun componentes(
            ancho: Double,
            alto: Double,
            entrada: InputHandler? = null
        ): ComponentesSimulacion = predeterminada.componentes(ancho, alto, entrada)

        fun gravedadPorDefecto(): Vector2D = Vector2D(
            0.0,
            Tierra.GRAVEDAD_PIXELES * Simulacion.factorGravedadPorDefecto()
        )

        private val predeterminada = FabricaSimulacion(
            FabricaComponentesFisicaPredeterminada(),
            FabricaComponentesPartidaPredeterminada()
        )
    }
}

internal interface FabricaComponentesFisica {
    fun crear(ancho: Double, alto: Double): ComponentesFisica
}

internal interface FabricaComponentesPartida {
    fun crear(alto: Double): ComponentesPartida
}

internal class FabricaComponentesFisicaPredeterminada : FabricaComponentesFisica {
    override fun crear(ancho: Double, alto: Double): ComponentesFisica {
        val fabricaAmbiente = FabricaTierra()
        val ambiente = fabricaAmbiente.crear(ancho, alto, FabricaSimulacion.gravedadPorDefecto())
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
}

internal class FabricaComponentesPartidaPredeterminada : FabricaComponentesPartida {
    override fun crear(alto: Double): ComponentesPartida {
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
}
