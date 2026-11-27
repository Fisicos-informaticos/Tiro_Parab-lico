package org.example.grafico

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import org.example.modelo.Pared
import org.example.modelo.Proyectil
import org.example.modelo.Vector2D

class DibujantesCanvas(
    val dibujoVectores: DibujoVectoresFisica,
    val entorno: DibujanteEntorno,
    val paredes: DibujanteParedes,
    val pantallaInicio: DibujantePantallaInicio,
    val flechaDireccion: DibujanteFlechaDireccion,
    val panelInfo: DibujantePanelInfo,
    val objetos: DibujanteObjetos
) {
    fun componentes(): List<Any> = listOf(
        dibujoVectores, entorno, paredes, pantallaInicio, flechaDireccion, panelInfo, objetos
    )
}

class CanvasRenderer(
    gc: GraphicsContext,
    ancho: Double,
    alto: Double,
    dibujantes: DibujantesCanvas
) : Renderer {
    private val datos = DatosCanvasRenderer(gc, ancho, alto, dibujantes)

    override fun ancho(): Double = datos.ancho
    override fun alto(): Double = datos.alto
    override fun offsetCamara(): Vector2D = datos.offsetCamara()
    override fun fijarOffsetCamara(nuevo: Vector2D) = datos.fijarOffsetCamara(nuevo)
    override fun limpiar() = datos.limpiar()
    override fun dibujarEntorno() = datos.dibujarEntorno()
    override fun dibujarPared(pared: Pared) = datos.dibujarPared(pared)
    override fun dibujarSuperficie(pared: Pared) = datos.dibujarSuperficie(pared)
    override fun dibujarParedTemporal(pared: Pared) = datos.dibujarParedTemporal(pared)
    override fun dibujarPantallaInicio(nombrePelota: String?) = datos.dibujarPantallaInicio(nombrePelota)
    override fun dibujarProyectil(proyectil: Proyectil) = datos.dibujarProyectil(proyectil)
    override fun dibujarVectoresFisica(centro: Vector2D, velocidad: Vector2D, gravedad: Vector2D) =
        datos.dibujarVectoresFisica(centro, velocidad, gravedad)
    override fun dibujarPuntoLanzamiento(posicion: Vector2D, radio: Double) =
        datos.dibujarPuntoLanzamiento(posicion, radio)
    override fun dibujarLineaDireccion(inicio: Vector2D, fin: Vector2D) =
        datos.dibujarLineaDireccion(inicio, fin)
    override fun dibujarLinea(linea: LineaDibujo) = datos.dibujarLinea(linea)
    override fun dibujarTexto(texto: TextoDibujo) = datos.dibujarTexto(texto)
    override fun dibujarPanel(lineas: List<String>) = datos.dibujarPanel(lineas)
    override fun dibujarImagenMundo(imagen: ImagenEnMundo) = datos.dibujarImagenMundo(imagen)

    private data class DatosCanvasRenderer(
        val gc: GraphicsContext,
        val ancho: Double,
        val alto: Double,
        val dibujantes: DibujantesCanvas,
        val colorCielo: Color = Color.web("#87CEEB"),
        var camara: Vector2D = Vector2D(0.0, 0.0)
    ) {
        fun offsetCamara(): Vector2D = camara

        fun fijarOffsetCamara(nuevo: Vector2D) {
            camara = nuevo
        }

        fun limpiar() {
            gc.fill = colorCielo
            gc.fillRect(0.0, 0.0, ancho, alto)
        }

        fun dibujarEntorno() = dibujantes.entorno.dibujar(camara)

        fun dibujarPared(pared: Pared) = dibujantes.paredes.dibujar(pared, camara)

        fun dibujarSuperficie(pared: Pared) = dibujantes.paredes.dibujarSuperficie(pared, camara)

        fun dibujarParedTemporal(pared: Pared) = dibujantes.paredes.dibujarTemporal(pared, camara)

        fun dibujarPantallaInicio(nombrePelota: String?) = dibujantes.pantallaInicio.dibujar(nombrePelota)

        fun dibujarProyectil(proyectil: Proyectil) = dibujantes.objetos.dibujarProyectil(proyectil, camara)

        fun dibujarVectoresFisica(centro: Vector2D, velocidad: Vector2D, gravedad: Vector2D) =
            dibujantes.dibujoVectores.dibujar(centro, velocidad, gravedad)

        fun dibujarPuntoLanzamiento(posicion: Vector2D, radio: Double) =
            dibujantes.objetos.dibujarPuntoLanzamiento(posicion, radio)

        fun dibujarLineaDireccion(inicio: Vector2D, fin: Vector2D) =
            dibujantes.flechaDireccion.dibujar(inicio, fin)

        fun dibujarLinea(linea: LineaDibujo) = dibujantes.objetos.dibujarLinea(linea)

        fun dibujarTexto(texto: TextoDibujo) = dibujantes.objetos.dibujarTexto(texto)

        fun dibujarPanel(lineas: List<String>) = dibujantes.panelInfo.dibujar(lineas)

        fun dibujarImagenMundo(imagen: ImagenEnMundo) = dibujantes.objetos.dibujarImagen(imagen, camara)

        fun descripcion(): String =
            "renderer(${ancho}x${alto}, camara=$camara, cielo=$colorCielo, gc=${gc.javaClass.simpleName}, dibujantes=${dibujantes.componentes().size})"

        fun esValido(): Boolean =
            ancho > 0 && alto > 0 && gc.javaClass !== dibujantes.objetos.javaClass &&
                dibujantes.entorno.javaClass !== dibujantes.paredes.javaClass &&
                dibujantes.pantallaInicio.javaClass !== dibujantes.flechaDireccion.javaClass &&
                dibujantes.panelInfo.javaClass !== dibujantes.objetos.javaClass &&
                dibujantes.dibujoVectores.javaClass !== dibujantes.entorno.javaClass

        fun resumen(): String =
            "${ancho}x${alto} camara=${camara.x()},${camara.y()} cielo=$colorCielo ${dibujantes.componentes().size} dibujantes"

        fun componentes(): List<Any> = listOf(gc, ancho, alto, dibujantes, colorCielo, camara)
    }
}
