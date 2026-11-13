package org.example.simulacion

import org.example.entrada.InputHandler
import org.example.fisica.Tierra
import org.example.modelo.Vector2D

internal data class ContextoServicios(
    val fisica: ComponentesFisica,
    val partida: ComponentesPartida,
    val ancho: Double,
    val alto: Double,
    val entrada: InputHandler
)

internal data class ContextoComandos(
    val fisica: ComponentesFisica,
    val partida: ComponentesPartida,
    val servicios: ServiciosCableados,
    val ancho: Double,
    val alto: Double,
    val entrada: InputHandler
)

internal class EnsambladorServicios {
    fun crearServicios(contexto: ContextoServicios): ServiciosCableados {
        val posicionPelota = crearPosicionPelota(contexto)
        return ServiciosCableados(
            controladorCamara = crearControladorCamara(contexto),
            edicion = crearEdicion(contexto),
            previsualizador = crearPrevisualizador(contexto),
            observadores = RegistroObservadoresFin(),
            posicionPelota = posicionPelota,
            mecanica = crearMecanica(contexto, posicionPelota)
        )
    }

    fun crearComandos(contexto: ContextoComandos) = ComandosCableados(
        edicion = crearComandosEdicion(contexto),
        partida = crearComandosPartida(contexto)
    )

    private fun crearControladorCamara(contexto: ContextoServicios) =
        ControladorCamara(contexto.fisica.camara, contexto.ancho, contexto.alto)

    private fun crearEdicion(contexto: ContextoServicios) =
        GestorEdicion(contexto.fisica.herramienta, contexto.fisica.motor, contexto.partida.superficies)

    private fun crearPrevisualizador(contexto: ContextoServicios) =
        PrevisualizadorTrayectoria(contexto.fisica.fabricaMotorFisico) {
            contexto.fisica.fabricaAmbiente.crear(
                contexto.ancho,
                contexto.alto,
                Vector2D(
                    0.0,
                    Tierra.GRAVEDAD_PIXELES * contexto.partida.sesion.factorGravedad()
                )
            )
        }

    private fun crearPosicionPelota(contexto: ContextoServicios) = PosicionPelota(
        contexto.partida.sesion,
        contexto.fisica.motor,
        contexto.partida.superficies,
        contexto.fisica.transformador,
        contexto.fisica.camara,
        contexto.alto
    )

    private fun crearMecanica(
        contexto: ContextoServicios,
        posicionPelota: UbicadorPelota
    ) = MecanicaTiro(
        contexto.partida.sesion,
        contexto.entrada,
        contexto.partida.metricas,
        IntegradorVuelo(
            contexto.fisica.motor,
            contexto.partida.metricas,
            contexto.partida.efectos
        ),
        posicionPelota
    )

    private fun crearComandosEdicion(contexto: ContextoComandos) = ComandosEdicion(
        contexto.partida.sesion,
        contexto.servicios.edicion,
        contexto.entrada,
        contexto.fisica.motor,
        contexto.fisica.creadorParedes,
        contexto.partida.superficies
    )

    private fun crearComandosPartida(contexto: ContextoComandos) = ComandosPartida(
        contexto.partida.sesion,
        contexto.servicios.mecanica,
        contexto.servicios.edicion,
        contexto.partida.metricas,
        contexto.partida.efectos,
        contexto.partida.superficies,
        contexto.servicios.controladorCamara,
        contexto.fisica.motor,
        contexto.partida.fabricaModos,
        contexto.fisica.fabricaAmbiente,
        contexto.ancho,
        contexto.alto
    )
}
