package org.example.grafico

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.text.Font
import org.example.modelo.Pared
import org.example.modelo.Proyectil
import org.example.modelo.Vector2D

class CanvasRenderer(
    private val gc: GraphicsContext,
    private val ancho: Double,
    private val alto: Double,
    private val dibujoVectores: DibujoVectoresFisica,
    private val entorno: DibujanteEntorno = DibujanteEntorno(gc, ancho, alto),
    private val paredes: DibujanteParedes = DibujanteParedes(gc)
) : Renderer {

    private val colorCielo = Color.web("#87CEEB")
    private val colorLinea = Color.web("#ff6b35")
    private val colorTexto = Color.WHITE

    private var camara = Vector2D(0.0, 0.0)

    override fun ancho(): Double = ancho

    override fun alto(): Double = alto

    override fun offsetCamara(): Vector2D = camara

    override fun fijarOffsetCamara(nuevo: Vector2D) {
        camara = nuevo
    }

    override fun limpiar() {
        gc.fill = colorCielo
        gc.fillRect(0.0, 0.0, ancho, alto)
    }

    override fun dibujarEntorno() {
        entorno.dibujar(camara)
    }

    override fun dibujarPared(pared: Pared) = paredes.dibujar(pared, camara)

    override fun dibujarSuperficie(pared: Pared) = paredes.dibujarSuperficie(pared, camara)

    override fun dibujarParedTemporal(pared: Pared) = paredes.dibujarTemporal(pared, camara)

    override fun dibujarPantallaInicio(nombrePelota: String?) {
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
        gc.fillText(
            "Arrastra con el mouse para apuntar y soltar para lanzar",
            ancho / 2.0 - 290.0,
            alto / 2.0 + 100.0
        )
        gc.fillText(
            "Crea paredes (tipo con W) y haz rebotar la pelota",
            ancho / 2.0 - 250.0,
            alto / 2.0 + 125.0
        )
        gc.fillText("Pulsa B para cambiar de pelota", ancho / 2.0 - 180.0, alto / 2.0 + 150.0)
        if (nombrePelota != null) {
            gc.fill = Color.web("#d4e157")
            gc.fillText("Pelota actual: $nombrePelota", ancho / 2.0 - 175.0, alto / 2.0 + 180.0)
        }
    }

    override fun dibujarProyectil(proyectil: Proyectil) {
        DibujoPelota.dibujar(
            gc,
            proyectil.tipo(),
            proyectil.forma(),
            proyectil.angulo(),
            proyectil.posicion() - camara
        )
    }

    override fun dibujarVectoresFisica(centro: Vector2D, velocidad: Vector2D, gravedad: Vector2D) {
        dibujoVectores.dibujar(centro, velocidad, gravedad)
    }

    override fun dibujarPuntoLanzamiento(posicion: Vector2D, radio: Double) {
        gc.fill = Color.web("#3498db")
        gc.fillOval(posicion.x() - radio, posicion.y() - radio, radio * 2, radio * 2)

        gc.stroke = Color.web("#2980b9")
        gc.lineWidth = 2.0
        gc.strokeOval(
            posicion.x() - radio - 3.0,
            posicion.y() - radio - 3.0,
            radio * 2 + 6.0,
            radio * 2 + 6.0
        )
    }

    override fun dibujarLineaDireccion(inicio: Vector2D, fin: Vector2D) {
        gc.stroke = colorLinea
        gc.lineWidth = 3.0
        gc.setLineDashes(8.0, 4.0)
        gc.strokeLine(inicio.x(), inicio.y(), fin.x(), fin.y())
        gc.setLineDashes()

        val dx = fin.x() - inicio.x()
        val dy = fin.y() - inicio.y()
        val len = kotlin.math.sqrt(dx * dx + dy * dy)
        if (len < 1.0) return

        val nx = dx / len
        val ny = dy / len

        gc.fill = colorLinea
        gc.fillPolygon(
            doubleArrayOf(fin.x(), fin.x() - ny * 8.0 - nx * 12.0, fin.x() + ny * 8.0 - nx * 12.0),
            doubleArrayOf(fin.y(), fin.y() + nx * 8.0 - ny * 12.0, fin.y() - nx * 8.0 - ny * 12.0),
            3
        )
    }

    override fun dibujarLinea(inicio: Vector2D, fin: Vector2D, color: String, grosor: Double) {
        gc.stroke = Color.web(color)
        gc.lineWidth = grosor
        gc.setLineDashes()
        gc.strokeLine(inicio.x(), inicio.y(), fin.x(), fin.y())
    }

    override fun dibujarTexto(texto: String, x: Double, y: Double, tamano: Double) {
        gc.fill = colorTexto
        gc.font = Font.font("Monospace", tamano)
        gc.fillText(texto, x, y)
    }

    override fun dibujarPanel(lineas: List<String>) {
        gc.fill = Color.color(0.0, 0.0, 0.0, 0.6)
        gc.fillRoundRect(10.0, 10.0, 290.0, 24.0 + lineas.size * 18.0, 10.0, 10.0)

        gc.fill = colorTexto
        gc.font = Font.font("Monospace", 14.0)

        var yTexto = 30.0
        for (linea in lineas) {
            gc.fillText(linea, 20.0, yTexto)
            yTexto += 18.0
        }
    }

    override fun dibujarImagenMundo(imagen: javafx.scene.image.Image, x: Double, y: Double, ancho: Double, alto: Double) {
        gc.drawImage(imagen, x - camara.x(), y - camara.y(), ancho, alto)
    }
}
