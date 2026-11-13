package org.example.grafico

import org.example.simulacion.ContextoJuego
import org.example.simulacion.EscenaEstado
import org.example.sprites.GestionSprites

/**
 * Traduce una [ContextoJuego] a pixeles. Delega el escenario, el apuntado y la
 * informacion en pintores especializados; el estado activo decide que escena
 * mostrar mediante [EscenaEstado].
 */
class PresentadorJuego(
    private val renderer: Renderable,
    private val sprites: GestionSprites
) : EscenaEstado {

    private val mundo = PintorMundo(renderer, sprites)
    private val apuntado = PintorApuntado(renderer)
    private val informacion = PintorInformacion(renderer)

    fun sprites(): GestionSprites = sprites

    fun pintar(simulacion: ContextoJuego) {
        mundo.pintar(simulacion)
        simulacion.estado().dibujar(this, simulacion)
    }

    override fun mostrarPantallaInicio(nombrePelota: String) {
        renderer.dibujarPantallaInicio(nombrePelota)
    }

    override fun mostrarPelotaEnEspera(simulacion: ContextoJuego) {
        apuntado.pelotaEnEspera(simulacion)
    }

    override fun mostrarApuntado(simulacion: ContextoJuego) {
        apuntado.apuntado(simulacion)
    }

    override fun mostrarProyectil(simulacion: ContextoJuego) {
        renderer.dibujarProyectil(simulacion.pelota())
    }

    override fun mostrarInformacion(simulacion: ContextoJuego) {
        informacion.mostrar(simulacion)
    }
}
