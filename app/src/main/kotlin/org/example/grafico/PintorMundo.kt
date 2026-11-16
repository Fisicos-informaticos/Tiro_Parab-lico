package org.example.grafico

import org.example.simulacion.ContextoJuego
import org.example.sprites.GestionSprites

/** Pinta el escenario estable: fondo, decorado, sprites, paredes y superficies. */
internal class PintorMundo(
    private val renderer: Renderable,
    private val sprites: GestionSprites
) : PintorMundoJuego {
    override fun pintar(simulacion: ContextoJuego) {
        renderer.fijarOffsetCamara(simulacion.offsetCamara())

        renderer.limpiar()
        renderer.dibujarEntorno()
        sprites.dibujarTodos(renderer)

        for (pared in simulacion.paredes()) {
            renderer.dibujarPared(pared)
        }
        for (pared in simulacion.superficiesLanzamientoLista()) {
            renderer.dibujarSuperficie(pared)
        }
        simulacion.paredTemporal()?.let { renderer.dibujarParedTemporal(it) }
    }
}
