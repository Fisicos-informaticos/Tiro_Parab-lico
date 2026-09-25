package org.example.grafico

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.text.Font
import org.example.modelo.Pared
import org.example.modelo.Proyectil
import org.example.modelo.EstadoSimulacion
import org.example.modelo.Vector2D

class CanvasRenderer(
    private val gc: GraphicsContext,
    override val ancho: Double,
    override val alto: Double
) : Renderer {

    private val colorSuelo = Color.web("#4a7c3f")
    private val colorCielo = Color.web("#87CEEB")
    private val colorCesped = Color.web("#5cb85c")
    private val colorMeta = Color.web("#e74c3c")
    private val colorLinea = Color.web("#ff6b35")
    private val colorPelota = Color.web("#e74c3c")
    private val colorPared = Color.web("#8e9aaf")
    private val colorParedBorde = Color.web("#34495e")
    private val colorParedTemporal = Color.web("#f1c40f")
    private val colorTexto = Color.WHITE
    private val sueloY: Double get() = alto - 50.0

    override var offsetCamara = Vector2D(0.0, 0.0)

    override fun limpiar() {
        gc.fill = colorCielo
        gc.fillRect(0.0, 0.0, ancho, alto)
    }

    private fun seudoAleatorio(semilla: Double): Double {
        val t = Math.sin(semilla * 12.9898 + 78.233) * 43758.5453
        return t - Math.floor(t)
    }

    override fun dibujarEntorno() {
        val ox = offsetCamara.x
        val oy = offsetCamara.y

        val ySuelo = sueloY - oy
        if (ySuelo < alto) {
            gc.fill = colorSuelo
            gc.fillRect(0.0, Math.max(ySuelo, 0.0), ancho, alto - Math.max(ySuelo, 0.0))
        }

        val yCesped = sueloY - 4.0 - oy
        val inicioCesped = ((Math.floor((0.0 - ox) / 30.0) - 1.0) * 30.0).toInt()
        val finCesped = ((Math.ceil((ancho - ox) / 30.0) + 1.0) * 30.0).toInt()
        gc.fill = colorCesped
        for (i in inicioCesped..finCesped step 30) {
            gc.fillOval(i.toDouble(), yCesped, 9.0, 13.0)
            gc.fillOval(i.toDouble() + 15.0, yCesped + 3.0, 7.0, 11.0)
        }

        val lapsoArbol = 170.0
        var idxArbol = Math.floor((0.0 - ox) / lapsoArbol).toInt() - 1
        while (idxArbol * lapsoArbol < (ancho - ox) + lapsoArbol) {
            val s = idxArbol.toDouble()
            val r = seudoAleatorio(s)
            if (r > 0.22) {
                val xArbol = s * lapsoArbol + r * 120.0
                val escala = 0.75 + seudoAleatorio(s + 7.0) * 0.65
                dibujarArbol(xArbol, sueloY, escala)
            }
            idxArbol++
        }

        val lapsoNube = 250.0
        var idxNube = Math.floor((0.0 - ox) / lapsoNube).toInt() - 1
        while (idxNube * lapsoNube < (ancho - ox) + lapsoNube) {
            val s = idxNube.toDouble()
            val r = seudoAleatorio(s + 3.0)
            val xNube = s * lapsoNube + r * 150.0
            val yNube = 30.0 + (((s.toLong() % 3) + 1) * 35) + seudoAleatorio(s + 9.0) * 25.0
            dibujarNube(xNube, yNube, 0.55 + seudoAleatorio(s + 11.0) * 0.3)
            idxNube++
        }

        val lapsoMeta = 900.0
        var idxMeta = Math.floor((0.0 - ox) / lapsoMeta).toInt() - 1
        while (idxMeta * lapsoMeta < (ancho - ox) + lapsoMeta) {
            val xMeta = idxMeta.toDouble() * lapsoMeta + 300.0 - ox
            val yMeta = sueloY - 80.0 - oy
            gc.fill = colorMeta
            gc.fillRect(xMeta, yMeta, 8.0, 35.0)
            gc.stroke = colorMeta
            gc.lineWidth = 3.0
            gc.strokeRect(xMeta + 5.0, yMeta - 15.0, 20.0, 15.0)
            idxMeta++
        }
    }

    private fun dibujarArbol(xMundo: Double, sueloMundo: Double, escala: Double) {
        val x = xMundo - offsetCamara.x
        val yBase = sueloMundo - offsetCamara.y
        val tronco = 45.0 * escala
        gc.fill = Color.web("#8B4513")
        gc.fillRect(x - 4.0 * escala, yBase - tronco, 8.0 * escala, tronco)
        gc.fill = Color.web("#2e7d32")
        gc.fillOval(x - 26.0 * escala, yBase - tronco - 26.0 * escala, 52.0 * escala, 38.0 * escala)
        gc.fill = Color.web("#388e3c")
        gc.fillOval(x - 14.0 * escala, yBase - tronco - 34.0 * escala, 34.0 * escala, 28.0 * escala)
    }

    private fun dibujarNube(xMundo: Double, yMundo: Double, opacidad: Double) {
        val x = xMundo - offsetCamara.x
        val y = yMundo - offsetCamara.y
        gc.fill = Color.color(1.0, 1.0, 1.0, opacidad)
        gc.fillOval(x, y, 90.0, 30.0)
        gc.fillOval(x + 25.0, y - 15.0, 65.0, 35.0)
        gc.fillOval(x - 12.0, y + 4.0, 55.0, 26.0)
    }

    override fun dibujarPared(pared: Pared) {
        val inicio = pared.inicio - offsetCamara
        val fin = pared.fin - offsetCamara

        gc.stroke = colorParedBorde
        gc.lineWidth = pared.grosor + 2.0
        gc.setLineDashes()
        gc.strokeLine(inicio.x, inicio.y, fin.x, fin.y)

        gc.stroke = colorPared
        gc.lineWidth = pared.grosor
        gc.strokeLine(inicio.x, inicio.y, fin.x, fin.y)
    }

    override fun dibujarParedTemporal(inicio: Vector2D, fin: Vector2D, grosor: Double) {
        val inicioPantalla = inicio - offsetCamara
        val finPantalla = fin - offsetCamara

        gc.stroke = colorParedTemporal
        gc.lineWidth = grosor
        gc.setLineDashes(10.0, 6.0)
        gc.strokeLine(inicioPantalla.x, inicioPantalla.y, finPantalla.x, finPantalla.y)
        gc.setLineDashes()
    }

    override fun dibujarPantallaInicio() {
        gc.fill = Color.color(0.0, 0.0, 0.0, 0.85)
        gc.fillRect(0.0, 0.0, ancho, alto)

        gc.fill = Color.web("#ff6b6b")
        gc.font = Font.font("Monospace", 52.0)
        gc.fillText("TIRO PARABOLICO", ancho / 2.0 - 300.0, alto / 2.0 - 90.0)

        gc.fill = Color.WHITE
        gc.font = Font.font("Monospace", 20.0)
        gc.fillText("Simulador de tiro parabolico", ancho / 2.0 - 240.0, alto / 2.0 - 40.0)

        gc.fill = Color.web("#ff6b35")
        gc.font = Font.font("Monospace", 22.0)
        gc.fillText(">> HAZ CLICK PARA COMENZAR <<", ancho / 2.0 - 245.0, alto / 2.0 + 40.0)

        gc.fill = Color.color(1.0, 1.0, 1.0, 0.8)
        gc.font = Font.font("Monospace", 15.0)
        gc.fillText("Arrastra con el mouse para apuntar y soltar para lanzar", ancho / 2.0 - 290.0, alto / 2.0 + 100.0)
        gc.fillText("Crea paredes y haz rebotar la pelota", ancho / 2.0 - 230.0, alto / 2.0 + 125.0)
    }

    override fun dibujarProyectil(proyectil: Proyectil) {
        val x = proyectil.posicion.x - offsetCamara.x
        val y = proyectil.posicion.y - offsetCamara.y
        val gradiente = javafx.scene.paint.RadialGradient(
            0.0, 0.0, x, y,
            proyectil.radio, true, javafx.scene.paint.CycleMethod.NO_CYCLE,
            javafx.scene.paint.Stop(0.0, Color.web("#ff6b6b")),
            javafx.scene.paint.Stop(0.5, colorPelota),
            javafx.scene.paint.Stop(1.0, Color.web("#c0392b"))
        )
        gc.fill = gradiente
        gc.fillOval(
            x - proyectil.radio,
            y - proyectil.radio,
            proyectil.radio * 2,
            proyectil.radio * 2
        )

        gc.fill = Color.color(1.0, 1.0, 1.0, 0.4)
        gc.fillOval(
            x - proyectil.radio * 0.4,
            y - proyectil.radio * 0.4,
            proyectil.radio * 0.6,
            proyectil.radio * 0.6
        )
    }

    override fun dibujarPuntoLanzamiento(posicion: Vector2D, radio: Double) {
        gc.fill = Color.web("#3498db")
        gc.fillOval(posicion.x - radio, posicion.y - radio, radio * 2, radio * 2)

        gc.stroke = Color.web("#2980b9")
        gc.lineWidth = 2.0
        gc.strokeOval(posicion.x - radio - 3.0, posicion.y - radio - 3.0, radio * 2 + 6.0, radio * 2 + 6.0)
    }

    override fun dibujarLineaDireccion(inicio: Vector2D, fin: Vector2D) {
        gc.stroke = colorLinea
        gc.lineWidth = 3.0
        gc.setLineDashes(8.0, 4.0)
        gc.strokeLine(inicio.x, inicio.y, fin.x, fin.y)
        gc.setLineDashes()

        val dx = fin.x - inicio.x
        val dy = fin.y - inicio.y
        val len = kotlin.math.sqrt(dx * dx + dy * dy)
        if (len < 1.0) return

        val nx = dx / len
        val ny = dy / len

        gc.fill = colorLinea
        gc.fillPolygon(
            doubleArrayOf(fin.x, fin.x - ny * 8.0 - nx * 12.0, fin.x + ny * 8.0 - nx * 12.0),
            doubleArrayOf(fin.y, fin.y + nx * 8.0 - ny * 12.0, fin.y - nx * 8.0 - ny * 12.0),
            3
        )
    }

    override fun dibujarLinea(inicio: Vector2D, fin: Vector2D, color: String, grosor: Double) {
        gc.stroke = Color.web(color)
        gc.lineWidth = grosor
        gc.setLineDashes()
        gc.strokeLine(inicio.x, inicio.y, fin.x, fin.y)
    }

    override fun dibujarTexto(texto: String, x: Double, y: Double, tamano: Double) {
        gc.fill = colorTexto
        gc.font = Font.font("Monospace", tamano)
        gc.fillText(texto, x, y)
    }

    override fun dibujarRectangulo(x: Double, y: Double, ancho: Double, alto: Double, color: String) {
        gc.fill = Color.web(color)
        gc.fillRect(x, y, ancho, alto)
    }

    override fun dibujarUI(
        estado: EstadoSimulacion,
        velocidad: Vector2D?,
        posicion: Vector2D?,
        angulo: Double?,
        distanciaRecorrida: Double?,
        cantidadParedes: Int,
        modoParedes: Boolean
    ) {
        gc.fill = Color.color(0.0, 0.0, 0.0, 0.6)
        gc.fillRoundRect(10.0, 10.0, 270.0, 145.0, 10.0, 10.0)

        gc.fill = colorTexto
        gc.font = Font.font("Monospace", 14.0)

        val lineas = mutableListOf<String>()
        lineas.add("Estado: ${estado.name}")
        lineas.add("Paredes: $cantidadParedes")
        if (modoParedes) {
            lineas.add("Modo pared: arrastra para dibujar")
        } else {
            lineas.add("Boton: crear pared")
        }

        when (estado) {
            EstadoSimulacion.ARROJANDO -> {
                if (velocidad != null) {
                    lineas.add("Arrastra para lanzar")
                    if (angulo != null) {
                        lineas.add("Angulo: ${"%.1f".format(angulo)}°")
                        lineas.add("Fuerza: ${"%.1f".format(velocidad.magnitud)}")
                    }
                }
            }
            EstadoSimulacion.EN_VUELO -> {
                if (velocidad != null && posicion != null) {
                    lineas.add("Vel: ${"%.2f".format(velocidad.magnitud)} px/s")
                    lineas.add("Pos: (${posicion.x.toInt()}, ${posicion.y.toInt()})")
                }
            }
            EstadoSimulacion.EN_SUELO -> {
                if (distanciaRecorrida != null) {
                    lineas.add("Distancia: ${"%.2f".format(distanciaRecorrida)} px")
                }
                lineas.add("Click para reiniciar")
            }
            EstadoSimulacion.INACTIVO -> {
                lineas.add("Click en el suelo para")
                lineas.add("colocar la pelota")
            }
            EstadoSimulacion.PANTALLA_INICIO -> {
                lineas.add("Haz click para comenzar")
            }
        }

        var yTexto = 30.0
        for (linea in lineas) {
            gc.fillText(linea, 20.0, yTexto)
            yTexto += 18.0
        }
    }

    override fun dibujarImagen(imagen: Any, x: Double, y: Double, ancho: Double, alto: Double) {
        if (imagen is javafx.scene.image.Image) {
            gc.drawImage(imagen, x, y, ancho, alto)
        }
    }

    override fun dibujarImagenMundo(imagen: Any, x: Double, y: Double, ancho: Double, alto: Double) {
        if (imagen is javafx.scene.image.Image) {
            gc.drawImage(imagen, x - offsetCamara.x, y - offsetCamara.y, ancho, alto)
        }
    }

    override fun presentar() {
        // JavaFX Canvas renderiza en vivo
    }
}
