package org.example.grafico

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
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

    private val pantallaInicio = DibujantePantallaInicio(gc, ancho, alto)
    private val flechaDireccion = DibujanteFlechaDireccion(gc, colorLinea)
    private val panelInfo = DibujantePanelInfo(gc, colorTexto)
    private val objetos = DibujanteObjetos(gc, colorTexto)

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

    override fun dibujarEntorno() = entorno.dibujar(camara)

    override fun dibujarPared(pared: Pared) = paredes.dibujar(pared, camara)

    override fun dibujarSuperficie(pared: Pared) = paredes.dibujarSuperficie(pared, camara)

    override fun dibujarParedTemporal(pared: Pared) = paredes.dibujarTemporal(pared, camara)

    override fun dibujarPantallaInicio(nombrePelota: String?) = pantallaInicio.dibujar(nombrePelota)

    override fun dibujarProyectil(proyectil: Proyectil) = objetos.dibujarProyectil(proyectil, camara)

    override fun dibujarVectoresFisica(centro: Vector2D, velocidad: Vector2D, gravedad: Vector2D) {
        dibujoVectores.dibujar(centro, velocidad, gravedad)
    }

    override fun dibujarPuntoLanzamiento(posicion: Vector2D, radio: Double) =
        objetos.dibujarPuntoLanzamiento(posicion, radio)

    override fun dibujarLineaDireccion(inicio: Vector2D, fin: Vector2D) =
        flechaDireccion.dibujar(inicio, fin)

    override fun dibujarLinea(inicio: Vector2D, fin: Vector2D, color: String, grosor: Double) =
        objetos.dibujarLinea(inicio, fin, color, grosor)

    override fun dibujarTexto(texto: String, x: Double, y: Double, tamano: Double) =
        objetos.dibujarTexto(texto, x, y, tamano)

    override fun dibujarPanel(lineas: List<String>) = panelInfo.dibujar(lineas)

    override fun dibujarImagenMundo(imagen: javafx.scene.image.Image, x: Double, y: Double, ancho: Double, alto: Double) =
        objetos.dibujarImagen(imagen, x, y, ancho, alto, camara)
}
