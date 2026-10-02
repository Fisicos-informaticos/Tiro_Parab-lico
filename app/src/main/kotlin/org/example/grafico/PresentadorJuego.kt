package org.example.grafico

import org.example.modelo.Vector2D
import org.example.simulacion.EstadoSimulacion
import org.example.simulacion.Simulacion
import org.example.sprites.SpriteManager

/**
 * Traduce una [Simulacion] a pixeles. Es el unico sitio donde se decide que se ve
 * en cada estado, de modo que anadir un estado solo obliga a tocar este `when`,
 * y el compilador exige tratarlo.
 */
class PresentadorJuego(
    private val renderer: Renderer,
    val sprites: SpriteManager = SpriteManager()
) {

    fun pintar(simulacion: Simulacion) {
        renderer.offsetCamara = simulacion.offsetCamara

        renderer.limpiar()
        renderer.dibujarEntorno()
        sprites.dibujarTodos(renderer)

        for (pared in simulacion.paredes) {
            renderer.dibujarPared(pared)
        }
        simulacion.paredTemporal?.let { renderer.dibujarParedTemporal(it) }

        when (simulacion.estado) {
            EstadoSimulacion.PantallaInicio -> {
                renderer.dibujarPantallaInicio(simulacion.tipoPelota.nombre)
                return
            }
            EstadoSimulacion.Inactivo -> dibujarPelotaEnReposo(simulacion)
            EstadoSimulacion.Arrojando -> dibujarApuntado(simulacion)
            EstadoSimulacion.EnVuelo -> renderer.dibujarProyectil(simulacion.pelota)
            EstadoSimulacion.EnSuelo -> renderer.dibujarProyectil(simulacion.pelota)
        }

        dibujarEfectos(simulacion)
        renderer.dibujarPanel(lineasPanel(simulacion))
    }

    private fun dibujarPelotaEnReposo(simulacion: Simulacion) {
        if (simulacion.modoParedes) return

        renderer.dibujarPuntoLanzamiento(simulacion.pelota.posicion - simulacion.offsetCamara, simulacion.pelota.radio)
        renderer.dibujarProyectil(simulacion.pelota)
    }

    private fun dibujarApuntado(simulacion: Simulacion) {
        val entrada = simulacion.entrada
        val origen = simulacion.pelota.posicion - simulacion.offsetCamara
        renderer.dibujarPuntoLanzamiento(origen, simulacion.pelota.radio)
        renderer.dibujarProyectil(simulacion.pelota)

        val inicio = entrada.posicionInicial ?: return
        val actual = entrada.posicionActual ?: return
        renderer.dibujarLinea(inicio, actual, "#ffffff", 2.0)

        val vector = entrada.vectorLanzamiento ?: return
        val fuerza = entrada.fuerzaLanzamiento.coerceAtMost(Simulacion.MAX_FUERZA)
        val direccion = vector.normalizado
        renderer.dibujarLineaDireccion(origen, origen + direccion * fuerza)
        renderer.dibujarTexto(
            "Angulo: ${"%.1f".format(entrada.anguloLanzamiento)}°",
            origen.x + 15.0, origen.y - 25.0, 13.0
        )
        renderer.dibujarTexto(
            "Fuerza: ${"%.0f".format(fuerza)}",
            origen.x + 15.0, origen.y - 8.0, 13.0
        )

        val puntos = simulacion.previsualizarTrayectoria()
        if (puntos.size < 2) return
        for (i in 0 until puntos.size - 1) {
            renderer.dibujarLinea(
                puntos[i] - simulacion.offsetCamara,
                puntos[i + 1] - simulacion.offsetCamara,
                "rgba(255, 255, 255, 0.55)",
                1.5
            )
        }
    }

    /** Estela blanca que se apaga en el sitio donde estaba la pared rota. */
    private fun dibujarEfectos(simulacion: Simulacion) {
        for (efecto in simulacion.efectos) {
            val pared = efecto.pared
            renderer.dibujarLinea(
                pared.inicio - simulacion.offsetCamara,
                pared.fin - simulacion.offsetCamara,
                "rgba(255, 255, 255, ${"%.2f".format(efecto.proporcion)})",
                pared.grosor * efecto.proporcion
            )
        }
    }

    private fun lineasPanel(simulacion: Simulacion): List<String> {
        val lineas = mutableListOf("Estado: ${simulacion.estado.nombre}")
        lineas.add("Pelota: ${simulacion.tipoPelota.nombre}")
        lineas.add("Colision: ${simulacion.etiquetaColision()}")
        lineas.add("Pared: ${simulacion.tipoPared.nombre}")
        lineas.add("Tu pelota la rompe: ${if (simulacion.rompeParedSeleccionada()) "si" else "no"}")
        lineas.add("Paredes: ${simulacion.paredes.size}")
        if (simulacion.paredesDestruidas > 0) {
            lineas.add("Rompidas: ${simulacion.paredesDestruidas}")
        }
        if (simulacion.modoParedes) {
            lineas.add("Modo pared: ${simulacion.tipoPared.nombre}")
            lineas.add("Arrastra para dibujar")
        } else {
            lineas.add("Boton: crear pared")
        }
        lineas.addAll(lineasEstado(simulacion))
        return lineas
    }

    private fun lineasEstado(simulacion: Simulacion): List<String> {
        val entrada = simulacion.entrada
        val pelota = simulacion.pelota

        return when (simulacion.estado) {
            EstadoSimulacion.PantallaInicio -> listOf("Haz click para comenzar")
            EstadoSimulacion.Inactivo -> listOf("Click en el suelo para", "colocar la pelota")
            EstadoSimulacion.Arrojando -> listOf(
                "Arrastra para lanzar",
                "Angulo: ${"%.1f".format(entrada.anguloLanzamiento)}°",
                "Fuerza: ${"%.1f".format(entrada.fuerzaLanzamiento)}"
            )
            EstadoSimulacion.EnVuelo -> listOf(
                "Vel: ${"%.2f".format(pelota.velocidad.magnitud)} px/s",
                "Pos: ${posicion(pelota.posicion)}"
            )
            EstadoSimulacion.EnSuelo -> listOf(
                "Distancia: ${"%.2f".format(simulacion.distanciaRecorrida)} px",
                "Click para reiniciar"
            )
        }
    }

    private fun posicion(punto: Vector2D): String = "(${punto.x.toInt()}, ${punto.y.toInt()})"
}