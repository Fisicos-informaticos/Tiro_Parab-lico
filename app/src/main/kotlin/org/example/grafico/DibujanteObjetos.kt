package org.example.grafico

import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image
import javafx.scene.paint.Color
import javafx.scene.text.Font
import org.example.modelo.Proyectil
import org.example.modelo.Vector2D

/** Primitivas de dibujo de objetos sueltos: proyectil, punto, lineas y texto. */
class DibujanteObjetos(
    private val gc: GraphicsContext,
    private val colorTexto: Color
) {
    fun dibujarProyectil(proyectil: Proyectil, camara: Vector2D) {
        DibujoPelota.dibujar(
            gc,
            proyectil.tipo(),
            proyectil.forma(),
            proyectil.angulo(),
            proyectil.posicion() - camara
        )
    }

    fun dibujarPuntoLanzamiento(posicion: Vector2D, radio: Double) {
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

    fun dibujarLinea(inicio: Vector2D, fin: Vector2D, color: String, grosor: Double) {
        gc.stroke = Color.web(color)
        gc.lineWidth = grosor
        gc.setLineDashes()
        gc.strokeLine(inicio.x(), inicio.y(), fin.x(), fin.y())
    }

    fun dibujarTexto(texto: String, x: Double, y: Double, tamano: Double) {
        gc.fill = colorTexto
        gc.font = Font.font("Monospace", tamano)
        gc.fillText(texto, x, y)
    }

    fun dibujarImagen(imagen: Image, x: Double, y: Double, ancho: Double, alto: Double, camara: Vector2D) {
        gc.drawImage(imagen, x - camara.x(), y - camara.y(), ancho, alto)
    }
}
