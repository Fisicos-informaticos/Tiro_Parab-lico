package org.example.grafico

import org.example.modelo.Vector2D
import org.example.simulacion.EscenaEstado
import org.example.simulacion.Simulacion
import org.example.sprites.SpriteManager

/**
 * Traduce una [Simulacion] a pixeles. El estado activo decide que escena mostrar
 * mediante [EscenaEstado], de modo que añadir un estado no obliga a tocar este
 * presentador. Depende de [Renderable], no de JavaFX (DIP).
 */
class PresentadorJuego(
    private val renderer: Renderable,
    private val sprites: SpriteManager = SpriteManager()
) : EscenaEstado {

    fun sprites(): SpriteManager = sprites

    fun pintar(simulacion: Simulacion) {
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

        simulacion.estado().dibujar(this, simulacion)
    }

    override fun mostrarPantallaInicio(nombrePelota: String) {
        renderer.dibujarPantallaInicio(nombrePelota)
    }

    override fun mostrarPelotaEnEspera(simulacion: Simulacion) {
        if (simulacion.modoParedes()) return

        renderer.dibujarPuntoLanzamiento(
            simulacion.pelota().posicion() - simulacion.offsetCamara(),
            simulacion.pelota().radio()
        )
        renderer.dibujarProyectil(simulacion.pelota())
    }

    override fun mostrarApuntado(simulacion: Simulacion) {
        val modo = simulacion.modo()
        val origen = simulacion.pelota().posicion() - simulacion.offsetCamara()
        renderer.dibujarPuntoLanzamiento(origen, simulacion.pelota().radio())
        renderer.dibujarProyectil(simulacion.pelota())

        if (modo.dibujaVectorDeTiro()) dibujarVectorDeTiro(simulacion, origen)

        val etiquetas = modo.etiquetasDePreparacion(simulacion.lecturaModo())
        for (indice in etiquetas.indices) {
            renderer.dibujarTexto(
                etiquetas[indice],
                origen.x() + 15.0,
                origen.y() - 25.0 + indice * 17.0,
                13.0
            )
        }

        val puntos = simulacion.previsualizarTrayectoria()
        if (puntos.size < 2) return
        for (i in 0 until puntos.size - 1) {
            renderer.dibujarLinea(
                puntos[i] - simulacion.offsetCamara(),
                puntos[i + 1] - simulacion.offsetCamara(),
                "rgba(255, 255, 255, 0.55)",
                1.5
            )
        }
    }

    override fun mostrarProyectil(simulacion: Simulacion) {
        renderer.dibujarProyectil(simulacion.pelota())
    }

    override fun mostrarInformacion(simulacion: Simulacion) {
        dibujarVectoresFisica(simulacion)
        dibujarEfectos(simulacion)
        renderer.dibujarPanel(lineasPanel(simulacion))
    }

    /** Flechas de velocidad y de gravedad sobre la pelota en su posicion de pantalla. */
    private fun dibujarVectoresFisica(simulacion: Simulacion) {
        renderer.dibujarVectoresFisica(
            simulacion.pelota().posicion() - simulacion.offsetCamara(),
            simulacion.pelota().velocidad(),
            simulacion.gravedad()
        )
    }

    /** Linea de arrastre, flecha y etiquetas del vector de tiro. */
    private fun dibujarVectorDeTiro(simulacion: Simulacion, origen: Vector2D) {
        val entrada = simulacion.entrada()
        val inicio = entrada.posicionInicial() ?: return
        val actual = entrada.posicionActual() ?: return
        renderer.dibujarLinea(inicio, actual, "#ffffff", 2.0)

        val vector = entrada.vectorLanzamiento() ?: return
        val fuerza = entrada.fuerzaLanzamiento().coerceAtMost(Simulacion.MAX_FUERZA)
        renderer.dibujarLineaDireccion(origen, origen + vector.normalizado() * fuerza)
    }

    /** Estela blanca que se apaga en el sitio donde estaba la pared rota. */
    private fun dibujarEfectos(simulacion: Simulacion) {
        for (efecto in simulacion.efectos()) {
            val pared = efecto.pared()
            renderer.dibujarLinea(
                pared.inicio() - simulacion.offsetCamara(),
                pared.fin() - simulacion.offsetCamara(),
                "rgba(255, 255, 255, ${"%.2f".format(efecto.proporcion())})",
                pared.grosor() * efecto.proporcion()
            )
        }
    }

    private fun lineasPanel(simulacion: Simulacion): List<String> {
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
