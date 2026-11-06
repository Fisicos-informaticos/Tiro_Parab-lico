package org.example.grafico

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import org.example.modelo.Pared
import org.example.modelo.Vector2D
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.sin

/** Dibuja muros, superficies de lanzamiento, su dano y la previsualizacion. */
class DibujanteParedes(private val gc: GraphicsContext) {

    fun dibujar(pared: Pared, camara: Vector2D) = dibujar(pared, camara, null, null)

    fun dibujarSuperficie(pared: Pared, camara: Vector2D) =
        dibujar(pared, camara, Color.DODGERBLUE, 3.0)

    fun dibujarTemporal(pared: Pared, camara: Vector2D) {
        val inicio = pared.inicio() - camara
        val fin = pared.fin() - camara

        gc.stroke = colorDe(pared.tipo().color())
        gc.lineWidth = pared.grosor()
        gc.setLineDashes(10.0, 6.0)
        gc.strokeLine(inicio.x(), inicio.y(), fin.x(), fin.y())
        gc.setLineDashes()
    }

    private fun dibujar(
        pared: Pared,
        camara: Vector2D,
        colorOverride: Color?,
        grosorExtra: Double?
    ) {
        val inicio = pared.inicio() - camara
        val fin = pared.fin() - camara

        val colorBase = colorOverride ?: colorDe(pared.tipo().color())
        val colorBordeBase = colorOverride ?: colorDe(pared.tipo().colorBorde())

        gc.stroke = mezclar(colorBordeBase, COLOR_DANO, pared.danio() * 0.6)
        gc.lineWidth = pared.grosor() + (grosorExtra ?: 2.0)
        gc.setLineDashes()
        gc.strokeLine(inicio.x(), inicio.y(), fin.x(), fin.y())

        gc.stroke = mezclar(colorBase, COLOR_DANO, pared.danio() * 0.5)
        gc.lineWidth = pared.grosor()
        gc.strokeLine(inicio.x(), inicio.y(), fin.x(), fin.y())

        dibujarGrietas(pared, inicio, fin)
    }

    /** Grietas estables: la semilla sale de la pared, asi que no parpadean. */
    private fun dibujarGrietas(pared: Pared, inicio: Vector2D, fin: Vector2D) {
        if (pared.danio() <= UMBRAL_GRIETAS) return

        val largo = pared.longitud()
        if (largo <= 0.0) return

        val direccion = (fin - inicio) / largo
        val perpendicular = Vector2D(-direccion.y(), direccion.x())
        val medio = pared.grosor() / 2.0
        val semilla = semillaDe(pared)

        gc.stroke = COLOR_GRIETA
        gc.lineWidth = max(1.0, pared.grosor() * 0.16)
        gc.setLineDashes()

        val numero = ceil(pared.danio() * GRIETAS_MAXIMAS).toInt()
        for (i in 1..numero) {
            val t = 0.12 + 0.76 * fraccion(semilla + i * 0.41)
            val centro = inicio + direccion * (largo * t)
            val desvio = (fraccion(semilla + i * 0.77) - 0.5) * pared.grosor() * 1.4
            gc.strokeLine(
                centro.x() - perpendicular.x() * medio,
                centro.y() - perpendicular.y() * medio,
                centro.x() + perpendicular.x() * medio + direccion.x() * desvio,
                centro.y() + perpendicular.y() * medio + direccion.y() * desvio
            )
        }
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
        fraccion(
            sin(
                pared.inicio().x() * 12.9898 +
                    pared.inicio().y() * 78.233 +
                    pared.fin().y() * 37.719
            ) * 43758.5453
        )

    private companion object {
        val COLOR_DANO = Color.web("#4a4a4a")
        val COLOR_GRIETA = Color.web("#2b2b2b")
        const val UMBRAL_GRIETAS = 0.05
        const val GRIETAS_MAXIMAS = 5.0
    }
}
