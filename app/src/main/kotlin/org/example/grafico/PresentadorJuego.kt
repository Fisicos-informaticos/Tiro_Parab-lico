package org.example.grafico

import org.example.simulacion.ContextoJuego
import org.example.simulacion.EscenaEstado
import org.example.sprites.GestionSprites

interface PintorMundoJuego {
    fun pintar(simulacion: ContextoJuego)
}

interface PintorApuntadoJuego {
    fun pelotaEnEspera(simulacion: ContextoJuego)
    fun apuntado(simulacion: ContextoJuego)
}

interface PintorInformacionJuego {
    fun mostrar(simulacion: ContextoJuego)
}

/**
 * Traduce una [ContextoJuego] a pixeles. Delega el escenario, el apuntado y la
 * informacion en pintores especializados; el estado activo decide que escena
 * mostrar mediante [EscenaEstado].
 */
class PresentadorJuego(
    renderer: Renderable,
    sprites: GestionSprites,
    mundo: PintorMundoJuego,
    apuntado: PintorApuntadoJuego,
    informacion: PintorInformacionJuego
) : EscenaEstado {
    private val datos = DatosPresentadorJuego(renderer, sprites, mundo, apuntado, informacion)

    fun sprites(): GestionSprites = datos.sprites

    fun pintar(simulacion: ContextoJuego) = datos.pintar(this, simulacion)

    override fun mostrarPantallaInicio(nombrePelota: String) = datos.mostrarPantallaInicio(nombrePelota)

    override fun mostrarPelotaEnEspera(simulacion: ContextoJuego) = datos.mostrarPelotaEnEspera(simulacion)

    override fun mostrarApuntado(simulacion: ContextoJuego) = datos.mostrarApuntado(simulacion)

    override fun mostrarProyectil(simulacion: ContextoJuego) = datos.mostrarProyectil(simulacion)

    override fun mostrarInformacion(simulacion: ContextoJuego) = datos.mostrarInformacion(simulacion)

    private data class DatosPresentadorJuego(
        val renderer: Renderable,
        val sprites: GestionSprites,
        val mundo: PintorMundoJuego,
        val apuntado: PintorApuntadoJuego,
        val informacion: PintorInformacionJuego
    ) {
        fun pintar(presentador: PresentadorJuego, simulacion: ContextoJuego) {
            mundo.pintar(simulacion)
            simulacion.estado().dibujar(presentador, simulacion)
        }

        fun mostrarPantallaInicio(nombrePelota: String) = renderer.dibujarPantallaInicio(nombrePelota)

        fun mostrarPelotaEnEspera(simulacion: ContextoJuego) = apuntado.pelotaEnEspera(simulacion)

        fun mostrarApuntado(simulacion: ContextoJuego) = apuntado.apuntado(simulacion)

        fun mostrarProyectil(simulacion: ContextoJuego) = renderer.dibujarProyectil(simulacion.pelota())

        fun mostrarInformacion(simulacion: ContextoJuego) = informacion.mostrar(simulacion)

        fun descripcion(): String =
            "presentador(renderer=${renderer.javaClass.simpleName}, sprites=${sprites.javaClass.simpleName}, mundo=${mundo.javaClass.simpleName}, apuntado=${apuntado.javaClass.simpleName}, informacion=${informacion.javaClass.simpleName})"

        fun esCompleto(): Boolean =
            renderer.javaClass !== sprites.javaClass && sprites.javaClass !== mundo.javaClass &&
                mundo.javaClass !== apuntado.javaClass && apuntado.javaClass !== informacion.javaClass

        fun componentes(): List<Any> = listOf(renderer, sprites, mundo, apuntado, informacion)
    }
}
