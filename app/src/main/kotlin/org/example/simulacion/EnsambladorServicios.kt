package org.example.simulacion

import org.example.entrada.InputHandler
import org.example.fisica.Ambiente
import org.example.fisica.PhysicsEngine
import org.example.fisica.Tierra
import org.example.modelo.creadores.CreadorParedes
import org.example.modelo.Vector2D
import org.example.modos.FabricaModos

internal class ContextoServicios(
    fisica: ComponentesFisica,
    partida: ComponentesPartida,
    ancho: Double,
    alto: Double,
    entrada: InputHandler
) {
    private val datos = DatosContextoServicios(fisica, partida, ancho, alto, entrada)

    fun fisica(): ComponentesFisica = datos.fisica
    fun partida(): ComponentesPartida = datos.partida
    fun ancho(): Double = datos.ancho
    fun alto(): Double = datos.alto
    fun entrada(): InputHandler = datos.entrada
    fun gravedadActual(): Vector2D = datos.gravedadActual()
    fun descripcion(): String = datos.descripcion()

    fun sesion(): SesionJuego = datos.partida.sesion()
    fun metricas(): Metricas = datos.partida.metricas()
    fun efectos(): Efectos = datos.partida.efectos()
    fun superficies(): SuperficiesApoyo = datos.partida.superficies()
    fun motor(): PhysicsEngine = datos.fisica.motor()
    fun crearAmbiente(): Ambiente = datos.fisica.fabricaAmbiente().crear(datos.ancho, datos.alto, gravedadActual())

    private data class DatosContextoServicios(
        val fisica: ComponentesFisica,
        val partida: ComponentesPartida,
        val ancho: Double,
        val alto: Double,
        val entrada: InputHandler
    ) {
        fun gravedadActual(): Vector2D =
            Vector2D(0.0, Tierra.GRAVEDAD_PIXELES * partida.sesion().factorGravedad())

        fun descripcion(): String =
            "servicios ${ancho}x${alto}(${fisica.describir()}, ${partida.describir()}, ${entrada.javaClass.simpleName})"

        fun esValido(): Boolean =
            ancho > 0 && alto > 0 && fisica.javaClass !== partida.javaClass && partida.javaClass !== entrada.javaClass

        fun resumen(): String =
            "${ancho}x${alto} g=${gravedadActual().y()} ${fisica.describir()} ${partida.describir()}"
    }
}

internal class ContextoComandos(
    fisica: ComponentesFisica,
    partida: ComponentesPartida,
    servicios: ServiciosCableados,
    ancho: Double,
    alto: Double,
    entrada: InputHandler
) {
    private val datos = DatosContextoComandos(fisica, partida, servicios, ancho, alto, entrada)

    fun fisica(): ComponentesFisica = datos.fisica
    fun partida(): ComponentesPartida = datos.partida
    fun servicios(): ServiciosCableados = datos.servicios
    fun ancho(): Double = datos.ancho
    fun alto(): Double = datos.alto
    fun entrada(): InputHandler = datos.entrada
    fun puedeEditar(): Boolean = datos.puedeEditar()
    fun resumen(): String = datos.resumen()

    fun sesion(): SesionJuego = datos.partida.sesion()
    fun mecanica(): Mecanica = datos.servicios.mecanica()
    fun edicion(): EditorEscenario = datos.servicios.edicion()
    fun controladorCamara(): ControlCamara = datos.servicios.controladorCamara()
    fun motor(): PhysicsEngine = datos.fisica.motor()
    fun creadorParedes(): CreadorParedes = datos.fisica.creadorParedes()
    fun superficies(): SuperficiesApoyo = datos.partida.superficies()
    fun metricas(): Metricas = datos.partida.metricas()
    fun efectos(): Efectos = datos.partida.efectos()
    fun fabricaModos(): FabricaModos = datos.partida.fabricaModos()
    fun fabricaAmbiente(): org.example.fisica.FabricaAmbiente = datos.fisica.fabricaAmbiente()

    private data class DatosContextoComandos(
        val fisica: ComponentesFisica,
        val partida: ComponentesPartida,
        val servicios: ServiciosCableados,
        val ancho: Double,
        val alto: Double,
        val entrada: InputHandler
    ) {
        fun puedeEditar(): Boolean = partida.sesion().estado().permiteEditar()

        fun resumen(): String =
            "comandos editar=${puedeEditar()} ${ancho}x${alto} ${servicios.describir()} ${entrada.javaClass.simpleName}"

        fun descripcion(): String =
            "comandos(${fisica.describir()}, ${partida.describir()}, ${servicios.describir()}, ${ancho}x${alto})"

        fun esValido(): Boolean =
            ancho > 0 && alto > 0 && fisica.javaClass !== partida.javaClass &&
                partida.javaClass !== servicios.javaClass && servicios.javaClass !== entrada.javaClass
    }
}

internal interface FabricasServicios {
    fun crearControladorCamara(contexto: ContextoServicios): ControlCamara
    fun crearEdicion(contexto: ContextoServicios): EditorEscenario
    fun crearPrevisualizador(contexto: ContextoServicios): Previsualizador
    fun crearObservadores(): ObservadoresFin
    fun crearPosicionPelota(contexto: ContextoServicios): UbicadorPelota
    fun crearMecanica(contexto: ContextoServicios, posicionPelota: UbicadorPelota): Mecanica
    fun crearComandosEdicion(contexto: ContextoComandos): ComandosEditor
    fun crearComandosPartida(contexto: ContextoComandos): ComandosJuego
}

internal class FabricasServiciosPredeterminada : FabricasServicios {
    override fun crearControladorCamara(contexto: ContextoServicios) =
        ControladorCamara(contexto.fisica().camara(), contexto.ancho(), contexto.alto())

    override fun crearEdicion(contexto: ContextoServicios) =
        GestorEdicion(contexto.fisica().herramienta(), contexto.motor(), contexto.superficies())

    override fun crearPrevisualizador(contexto: ContextoServicios) =
        PrevisualizadorTrayectoria(contexto.fisica().fabricaMotorFisico()) { contexto.crearAmbiente() }

    override fun crearObservadores() = RegistroObservadoresFin()

    override fun crearPosicionPelota(contexto: ContextoServicios) = PosicionPelota(
        contexto.sesion(),
        contexto.motor(),
        contexto.superficies(),
        contexto.fisica().transformador(),
        contexto.fisica().camara(),
        contexto.alto()
    )

    override fun crearMecanica(contexto: ContextoServicios, posicionPelota: UbicadorPelota) = MecanicaTiro(
        contexto.sesion(),
        contexto.entrada(),
        contexto.metricas(),
        IntegradorVuelo(contexto.motor(), contexto.metricas(), contexto.efectos()),
        posicionPelota
    )

    override fun crearComandosEdicion(contexto: ContextoComandos) = ComandosEdicion(
        contexto.sesion(),
        contexto.edicion(),
        contexto.entrada(),
        contexto.motor(),
        contexto.creadorParedes(),
        contexto.superficies()
    )

    override fun crearComandosPartida(contexto: ContextoComandos) = ComandosPartida(
        ContextoComandosPartida(
            contexto.sesion(),
            contexto.sesion(),
            contexto.mecanica(),
            contexto.edicion(),
            contexto.metricas(),
            contexto.efectos(),
            contexto.superficies(),
            contexto.controladorCamara(),
            contexto.motor(),
            contexto.fabricaModos(),
            contexto.fabricaAmbiente(),
            contexto.ancho(),
            contexto.alto()
        )
    )
}

internal class EnsambladorServicios(
    private val fabricas: FabricasServicios = FabricasServiciosPredeterminada()
) {
    fun crearServicios(contexto: ContextoServicios): ServiciosCableados {
        val posicionPelota = fabricas.crearPosicionPelota(contexto)
        return ServiciosCableados(
            controladorCamara = fabricas.crearControladorCamara(contexto),
            edicion = fabricas.crearEdicion(contexto),
            previsualizador = fabricas.crearPrevisualizador(contexto),
            observadores = fabricas.crearObservadores(),
            posicionPelota = posicionPelota,
            mecanica = fabricas.crearMecanica(contexto, posicionPelota)
        )
    }

    fun crearComandos(contexto: ContextoComandos) = ComandosCableados(
        edicion = fabricas.crearComandosEdicion(contexto),
        partida = fabricas.crearComandosPartida(contexto)
    )
}
