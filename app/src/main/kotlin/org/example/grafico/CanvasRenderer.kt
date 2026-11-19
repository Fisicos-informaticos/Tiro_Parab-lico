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
    private val entorno: DibujanteEntorno,
    private val paredes: DibujanteParedes,
    private val pantallaInicio: DibujantePantallaInicio,
    private val flechaDireccion: DibujanteFlechaDireccion,
    private val panelInfo: DibujantePanelInfo,
    private val objetos: DibujanteObjetos
) : Renderer {

    private val colorCielo = Color.web("#87CEEB")

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

    override fun dibujarLinea(linea: LineaDibujo) = objetos.dibujarLinea(linea)

    override fun dibujarTexto(texto: TextoDibujo) = objetos.dibujarTexto(texto)

    override fun dibujarPanel(lineas: List<String>) = panelInfo.dibujar(lineas)

    override fun dibujarImagenMundo(imagen: ImagenEnMundo) = objetos.dibujarImagen(imagen, camara)
}
