package org.example.simulacion

import org.example.fisica.Ambiente
import org.example.fisica.FabricaAmbiente
import org.example.fisica.ConfiguracionFisica
import org.example.fisica.Tierra
import org.example.modelo.TipoPelota
import org.example.modelo.Vector2D
import org.example.modos.FabricaModos

/** Dependencias de [ComandosPartida] agrupadas para evitar un constructor largo. */
internal class ContextoComandosPartida(
    val sesion: EstadoSesionJuego,
    val configuracion: ConfiguracionSesionJuego,
    val mecanica: Mecanica,
    val edicion: EditorEscenario,
    val metricas: Metricas,
    val efectos: Efectos,
    val superficies: SuperficiesApoyo,
    val controladorCamara: ControlCamara,
    val motor: ConfiguracionFisica,
    val fabricaModos: FabricaModos,
    val fabricaAmbiente: FabricaAmbiente,
    val ancho: Double,
    val alto: Double
) {
    fun crearAmbiente(): Ambiente {
        val gravedad = Vector2D(0.0, Tierra.GRAVEDAD_PIXELES * configuracion.factorGravedad())
        return fabricaAmbiente.crear(ancho, alto, gravedad)
    }
}

/** Comandos que cambian el escenario o reinician la partida. */
internal class ComandosPartida(contexto: ContextoComandosPartida) : ComandosJuego {
    private val datos = DatosComandosPartida(contexto)

    override fun seleccionarPelota(tipo: TipoPelota) = datos.seleccionarPelota(tipo)

    override fun aplicarGravedad(nombre: String?) = datos.aplicarGravedad(nombre)

    override fun seleccionarModo(nombre: String?) = datos.seleccionarModo(nombre)

    override fun nuevaPartida() = datos.nuevaPartida()

    override fun volverAlMenu() = datos.volverAlMenu()

    private data class DatosComandosPartida(
        private val contexto: ContextoComandosPartida
    ) {
        fun seleccionarPelota(tipo: TipoPelota) {
            if (!contexto.sesion.permiteEditar()) return
            contexto.mecanica.cambiarPelota(tipo)
        }

        fun aplicarGravedad(nombre: String?) {
            val factor = Gravedades.factorDe(nombre) ?: return
            if (factor == contexto.configuracion.factorGravedad()) return
            contexto.configuracion.cambiarGravedad(factor)
            contexto.motor.cambiarAmbiente(contexto.crearAmbiente())
            reiniciarSiNoVuela()
        }

        fun seleccionarModo(nombre: String?) {
            val nuevo = contexto.fabricaModos.crear(nombre)
            if (nuevo.nombre() == contexto.configuracion.modo().nombre()) return
            contexto.configuracion.cambiarModo(nuevo)
            reiniciarSiNoVuela()
        }

        fun nuevaPartida() {
            contexto.sesion.reanudar()
            contexto.edicion.desactivar()
            contexto.mecanica.reiniciar()
            contexto.controladorCamara.reiniciar()
            contexto.metricas.reiniciarPartida()
            contexto.efectos.limpiar()
            contexto.superficies.limpiar()
        }

        fun volverAlMenu() {
            nuevaPartida()
            contexto.sesion.cambiarEstado(EstadoSimulacion.PantallaInicio)
        }

        fun descripcion(): String =
            "comandosPartida(sesion=${contexto.sesion.javaClass.simpleName}, modo=${contexto.configuracion.modo().nombre()}, gravedad=${contexto.configuracion.factorGravedad()})"

        fun esValida(): Boolean =
            contexto.ancho > 0 && contexto.alto > 0 && contexto.sesion.javaClass !== contexto.mecanica.javaClass &&
                contexto.mecanica.javaClass !== contexto.edicion.javaClass && contexto.edicion.javaClass !== contexto.metricas.javaClass &&
                contexto.metricas.javaClass !== contexto.efectos.javaClass && contexto.efectos.javaClass !== contexto.superficies.javaClass &&
                contexto.superficies.javaClass !== contexto.controladorCamara.javaClass && contexto.controladorCamara.javaClass !== contexto.motor.javaClass &&
                contexto.motor.javaClass !== contexto.fabricaModos.javaClass && contexto.fabricaModos.javaClass !== contexto.fabricaAmbiente.javaClass

        private fun reiniciarSiNoVuela() {
            if (!contexto.sesion.estaEnVuelo()) contexto.mecanica.reiniciar()
        }
    }
}
