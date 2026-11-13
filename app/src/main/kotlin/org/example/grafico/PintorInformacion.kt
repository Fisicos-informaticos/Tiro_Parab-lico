package org.example.grafico

import org.example.simulacion.ContextoJuego

/** Pinta vectores fisicos, efectos y el panel textual de la partida. */
internal class PintorInformacion(private val renderer: Renderable) {

    fun mostrar(simulacion: ContextoJuego) {
        dibujarVectoresFisica(simulacion)
        dibujarEfectos(simulacion)
        renderer.dibujarPanel(lineasPanel(simulacion))
    }

    private fun dibujarVectoresFisica(simulacion: ContextoJuego) {
        renderer.dibujarVectoresFisica(
            simulacion.pelota().posicion() - simulacion.offsetCamara(),
            simulacion.pelota().velocidad(),
            simulacion.gravedad()
        )
    }

    private fun dibujarEfectos(simulacion: ContextoJuego) {
        for (efecto in simulacion.efectos()) {
            val pared = efecto.pared()
            renderer.dibujarLinea(
                LineaDibujo(
                    inicio = pared.inicio() - simulacion.offsetCamara(),
                    fin = pared.fin() - simulacion.offsetCamara(),
                    color = "rgba(255, 255, 255, ${"%.2f".format(efecto.proporcion())})",
                    grosor = pared.grosor() * efecto.proporcion()
                )
            )
        }
    }

    private fun lineasPanel(simulacion: ContextoJuego): List<String> {
        val lineas = mutableListOf("Estado: ${simulacion.estado().nombre()}")
        lineas.add("Modo: ${simulacion.modo().nombre()}")
        lineas.add("Pelota: ${simulacion.tipoPelota().nombre()}")
        lineas.add("Colision: ${simulacion.etiquetaColision()}")
        lineas.add("Pared: ${simulacion.tipoPared().nombre()}")
        lineas.add("Tu pelota la rompe: ${if (simulacion.rompeParedSeleccionada()) "si" else "no"}")
        lineas.add("Paredes: ${simulacion.paredes().size}")
        if (simulacion.paredesDestruidas() > 0) {
            lineas.add("Rompidas: ${simulacion.paredesDestruidas()}")
        }
        if (simulacion.modoParedes()) {
            lineas.add("Modo pared: ${simulacion.tipoPared().nombre()}")
            lineas.add("Arrastra para dibujar")
        } else {
            lineas.add("Boton: crear pared")
        }
        lineas.addAll(
            simulacion.estado().lineasDeEstado(simulacion.modo(), simulacion.lecturaModo())
        )
        return lineas
    }
}
