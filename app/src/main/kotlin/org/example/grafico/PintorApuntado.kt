package org.example.grafico

import org.example.modelo.Vector2D
import org.example.simulacion.ContextoJuego
import org.example.simulacion.Simulacion

/** Pinta la pelota y las ayudas visuales de preparacion del tiro. */
internal class PintorApuntado(private val renderer: Renderable) : PintorApuntadoJuego {

    override fun pelotaEnEspera(simulacion: ContextoJuego) {
        if (simulacion.modoParedes()) return

        renderer.dibujarPuntoLanzamiento(
            simulacion.pelota().posicion() - simulacion.offsetCamara(),
            simulacion.pelota().radio()
        )
        renderer.dibujarProyectil(simulacion.pelota())
    }

    override fun apuntado(simulacion: ContextoJuego) {
        val modo = simulacion.modo()
        val origen = simulacion.pelota().posicion() - simulacion.offsetCamara()
        renderer.dibujarPuntoLanzamiento(origen, simulacion.pelota().radio())
        renderer.dibujarProyectil(simulacion.pelota())

        if (modo.dibujaVectorDeTiro()) dibujarVectorDeTiro(simulacion, origen)

        dibujarEtiquetasDePreparacion(simulacion, origen)
        dibujarTrayectoria(simulacion)
    }

    private fun dibujarVectorDeTiro(simulacion: ContextoJuego, origen: Vector2D) {
        val entrada = simulacion.entrada()
        val inicio = entrada.posicionInicial() ?: return
        val actual = entrada.posicionActual() ?: return
        renderer.dibujarLinea(LineaDibujo(inicio, actual, "#ffffff"))

        val vector = entrada.vectorLanzamiento() ?: return
        val fuerza = entrada.fuerzaLanzamiento().coerceAtMost(Simulacion.MAX_FUERZA)
        renderer.dibujarLineaDireccion(origen, origen + vector.normalizado() * fuerza)
    }

    private fun dibujarEtiquetasDePreparacion(simulacion: ContextoJuego, origen: Vector2D) {
        val etiquetas = simulacion.modo().etiquetasDePreparacion(simulacion.lecturaModo())
        for (indice in etiquetas.indices) {
            renderer.dibujarTexto(
                TextoDibujo(
                    etiquetas[indice],
                    Vector2D(
                        origen.x() + 15.0,
                        origen.y() - 25.0 + indice * 17.0
                    ),
                    13.0
                )
            )
        }
    }

    private fun dibujarTrayectoria(simulacion: ContextoJuego) {
        val puntos = simulacion.previsualizarTrayectoria()
        if (puntos.size < 2) return
        for (i in 0 until puntos.size - 1) {
            renderer.dibujarLinea(
                LineaDibujo(
                    inicio = puntos[i] - simulacion.offsetCamara(),
                    fin = puntos[i + 1] - simulacion.offsetCamara(),
                    color = "rgba(255, 255, 255, 0.55)",
                    grosor = 1.5
                )
            )
        }
    }
}
