package org.example.grafico

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.text.Font
import org.example.modelo.Pared
import org.example.modelo.Proyectil
import org.example.modelo.Vector2D
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.sin

class CanvasRenderer(
    private val gc: GraphicsContext,
    override val ancho: Double,
    override val alto: Double
) : Renderer {

    private val colorSuelo = Color.web("#4a7c3f")
    private val colorCielo = Color.web("#87CEEB")
    private val colorCesped = Color.web("#5cb85c")
    private val colorCespedOscuro = Color.web("#3f9142")
    private val colorCespedClaro = Color.web("#7fd07f")
    private val colorMeta = Color.web("#e74c3c")
    private val colorLinea = Color.web("#ff6b35")
    private val colorTexto = Color.WHITE
    private val sueloY: Double get() = alto - 50.0

    override var offsetCamara = Vector2D(0.0, 0.0)

    override fun limpiar() {
        gc.fill = colorCielo
        gc.fillRect(0.0, 0.0, ancho, alto)
    }

    override fun dibujarEntorno() {
        val ySuelo = sueloY - offsetCamara.y
        if (ySuelo < alto) {
            gc.fill = colorSuelo
            val desde = Math.max(ySuelo, 0.0)
            gc.fillRect(0.0, desde, ancho, alto - desde)
        }

        val margen = MARGEN_VISIBILIDAD
        val desde = -offsetCamara.x - margen
        val hasta = ancho - offsetCamara.x + margen

        dibujarPasto(desde, hasta, ySuelo)
        dibujarArboles(desde, hasta)
        dibujarNubes(desde, hasta)
        dibujarMetas(desde, hasta, ySuelo)
    }

    /** Briznas cada GeneradorEntorno.ANCHO_BRIZNA: el cesped no se interrumpe. */
    private fun dibujarPasto(desde: Double, hasta: Double, yBase: Double) {
        if (yBase > alto + 30.0) return

        for (brizna in GeneradorEntorno.pasto(desde, hasta)) {
            gc.fill = when {
                brizna.brillo < 0.4 -> colorCespedOscuro
                brizna.brillo > 0.78 -> colorCespedClaro
                else -> colorCesped
            }
            gc.fillOval(brizna.x, yBase - brizna.alto, brizna.ancho, brizna.alto + 3.0)
        }
    }

    private fun dibujarArboles(desde: Double, hasta: Double) {
        if (sueloY - offsetCamara.y > alto + 220.0) return

        for (arbol in GeneradorEntorno.arboles(desde, hasta)) {
            dibujarArbol(arbol.x, sueloY, arbol.escala, arbol.claro)
        }
    }

    private fun dibujarNubes(desde: Double, hasta: Double) {
        for (nube in GeneradorEntorno.nubes(desde, hasta)) {
            dibujarNube(nube.x, nube.y, nube.escala, nube.opacidad)
        }
    }

    /** La meta se repite cada celda y siempre apoya en el suelo. */
    private fun dibujarMetas(desde: Double, hasta: Double, ySuelo: Double) {
        if (ySuelo > alto + 110.0) return

        for (meta in GeneradorEntorno.metas(desde, hasta)) {
            val yPoste = ySuelo - 78.0
            gc.stroke = colorMeta
            gc.lineWidth = 4.0
            gc.setLineDashes()
            gc.strokeLine(meta.x, yPoste, meta.x, ySuelo)

            gc.fill = colorMeta
            gc.fillRect(meta.x + 2.0, yPoste - 4.0, 30.0, 17.0)
            gc.lineWidth = 2.0
            gc.strokeRect(meta.x + 2.0, yPoste - 4.0, 30.0, 17.0)
        }
    }

    private fun dibujarArbol(xMundo: Double, sueloMundo: Double, escala: Double, clara: Boolean) {
        val x = xMundo - offsetCamara.x
        val yBase = sueloMundo - offsetCamara.y
        val tronco = 45.0 * escala
        val copa = if (clara) 58.0 * escala else 48.0 * escala

        gc.fill = Color.web("#7B4A21")
        gc.fillRect(x - 4.5 * escala, yBase - tronco, 9.0 * escala, tronco + 2.0)

        gc.fill = Color.web("#1b5e20")
        gc.fillOval(x - copa / 2.0, yBase - tronco - copa * 0.52, copa, copa * 0.78)
        gc.fill = if (clara) Color.web("#43a047") else Color.web("#2e7d32")
        gc.fillOval(x - copa * 0.33, yBase - tronco - copa * 0.68, copa * 0.64, copa * 0.54)
        gc.fill = Color.web("#66bb6a")
        gc.fillOval(x - copa * 0.28, yBase - tronco - copa * 0.74, copa * 0.4, copa * 0.3)
    }

    private fun dibujarNube(xMundo: Double, yMundo: Double, escala: Double, opacidad: Double) {
        val x = xMundo - offsetCamara.x
        val y = yMundo - offsetCamara.y
        val ancho = 92.0 * escala
        val alto = 30.0 * escala

        gc.fill = Color.color(1.0, 1.0, 1.0, opacidad)
        gc.fillOval(x, y, ancho, alto)
        gc.fillOval(x + ancho * 0.28, y - alto * 0.5, ancho * 0.7, alto * 1.16)
        gc.fillOval(x - ancho * 0.14, y + alto * 0.14, ancho * 0.6, alto * 0.86)
    }

    override fun dibujarPared(pared: Pared) {
        val inicio = pared.inicio - offsetCamara
        val fin = pared.fin - offsetCamara

        gc.stroke = mezclar(colorDe(pared.tipo.colorBorde), COLOR_DANO, pared.danio * 0.6)
        gc.lineWidth = pared.grosor + 2.0
        gc.setLineDashes()
        gc.strokeLine(inicio.x, inicio.y, fin.x, fin.y)

        gc.stroke = mezclar(colorDe(pared.tipo.color), COLOR_DANO, pared.danio * 0.5)
        gc.lineWidth = pared.grosor
        gc.strokeLine(inicio.x, inicio.y, fin.x, fin.y)

        dibujarGrietas(pared, inicio, fin)
    }

    /** Grietas estables: la semilla sale de la pared, asi que no parpadean. */
    private fun dibujarGrietas(pared: Pared, inicio: Vector2D, fin: Vector2D) {
        if (pared.danio <= UMBRAL_GRIETAS) return

        val largo = pared.longitud
        if (largo <= 0.0) return

        val direccion = (fin - inicio) / largo
        val perpendicular = Vector2D(-direccion.y, direccion.x)
        val medio = pared.grosor / 2.0
        val semilla = semillaDe(pared)

        gc.stroke = COLOR_GRIETA
        gc.lineWidth = max(1.0, pared.grosor * 0.16)
        gc.setLineDashes()

        val numero = ceil(pared.danio * GRIETAS_MAXIMAS).toInt()
        for (i in 1..numero) {
            val t = 0.12 + 0.76 * fraccion(semilla + i * 0.41)
            val centro = inicio + direccion * (largo * t)
            val desvio = (fraccion(semilla + i * 0.77) - 0.5) * pared.grosor * 1.4
            gc.strokeLine(
                centro.x - perpendicular.x * medio,
                centro.y - perpendicular.y * medio,
                centro.x + perpendicular.x * medio + direccion.x * desvio,
                centro.y + perpendicular.y * medio + direccion.y * desvio
            )
        }
    }

    override fun dibujarParedTemporal(pared: Pared) {
        val inicioPantalla = pared.inicio - offsetCamara
        val finPantalla = pared.fin - offsetCamara

        gc.stroke = colorDe(pared.tipo.color)
        gc.lineWidth = pared.grosor
        gc.setLineDashes(10.0, 6.0)
        gc.strokeLine(inicioPantalla.x, inicioPantalla.y, finPantalla.x, finPantalla.y)
        gc.setLineDashes()
    }

    private fun colorDe(valor: Int): Color =
        Color.rgb((valor shr 16) and 0xFF, (valor shr 8) and 0xFF, valor and 0xFF)

    private fun mezclar(base: Color, destino: Color, cantidad: Double): Color = Color.color(
        base.red + (destino.red - base.red) * cantidad,
        base.green + (destino.green - base.green) * cantidad,
        base.blue + (destino.blue - base.blue) * cantidad
    )

    private fun fraccion(valor: Double): Double = valor - floor(valor)

    private fun semillaDe(pared: Pared): Double =
        fraccion(sin(pared.inicio.x * 12.9898 + pared.inicio.y * 78.233 + pared.fin.y * 37.719) * 43758.5453)

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
        gc.fillText("Arrastra con el mouse para apuntar y soltar para lanzar", ancho / 2.0 - 290.0, alto / 2.0 + 100.0)
        gc.fillText("Crea paredes (tipo con W) y haz rebotar la pelota", ancho / 2.0 - 250.0, alto / 2.0 + 125.0)
        gc.fillText("Pulsa B para cambiar de pelota", ancho / 2.0 - 180.0, alto / 2.0 + 150.0)
        if (nombrePelota != null) {
            gc.fill = Color.web("#d4e157")
            gc.fillText("Pelota actual: $nombrePelota", ancho / 2.0 - 175.0, alto / 2.0 + 180.0)
        }
    }

    override fun dibujarProyectil(proyectil: Proyectil) {
        DibujoPelota.dibujar(
            gc,
            proyectil.tipo,
            proyectil.forma,
            proyectil.angulo,
            proyectil.posicion - offsetCamara
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

    override fun dibujarImagenMundo(imagen: Any, x: Double, y: Double, ancho: Double, alto: Double) {
        if (imagen is javafx.scene.image.Image) {
            gc.drawImage(imagen, x - offsetCamara.x, y - offsetCamara.y, ancho, alto)
        }
    }

    companion object {
        /** Margen extra que se genera a los lados para no cortar el decorado. */
        private const val MARGEN_VISIBILIDAD = 120.0

        private val COLOR_DANO = Color.web("#4a4a4a")
        private val COLOR_GRIETA = Color.web("#2b2b2b")
        private const val UMBRAL_GRIETAS = 0.05
        private const val GRIETAS_MAXIMAS = 5.0
    }
}
