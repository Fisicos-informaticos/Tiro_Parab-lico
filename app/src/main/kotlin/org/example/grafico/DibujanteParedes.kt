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

    fun dibujar(pared: Pared, camara: Vector2D) =
        dibujar(pared, camara, EstiloDibujoPared())

    fun dibujarSuperficie(pared: Pared, camara: Vector2D) =
        dibujar(pared, camara, EstiloDibujoPared(Color.DODGERBLUE, 3.0))

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
        estilo: EstiloDibujoPared
    ) {
        val inicio = pared.inicio() - camara
        val fin = pared.fin() - camara

        val colorBase = estilo.colorOverride ?: colorDe(pared.tipo().color())
        val colorBordeBase = estilo.colorOverride ?: colorDe(pared.tipo().colorBorde())

        gc.stroke = mezclar(colorBordeBase, COLOR_DANO, pared.danio() * 0.6)
        gc.lineWidth = pared.grosor() + (estilo.grosorExtra ?: 2.0)
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

        val geometria = geometriaGrietas(pared, inicio, fin) ?: return
        gc.stroke = COLOR_GRIETA
        gc.lineWidth = max(1.0, geometria.grosor * 0.16)
        gc.setLineDashes()

        repeat(geometria.cantidad) { indice ->
            dibujarGrieta(geometria, indice + 1)
        }
    }

    private fun geometriaGrietas(
        pared: Pared,
        inicio: Vector2D,
        fin: Vector2D
    ): GeometriaGrietas? {
        val largo = pared.longitud()
        if (largo <= 0.0) return null
        val direccion = (fin - inicio) / largo
        return GeometriaGrietas(
            inicio = inicio,
            direccion = direccion,
            perpendicular = Vector2D(-direccion.y(), direccion.x()),
            longitud = largo,
            medio = pared.grosor() / 2.0,
            grosor = pared.grosor(),
            semilla = semillaDe(pared),
            cantidad = ceil(pared.danio() * GRIETAS_MAXIMAS).toInt()
        )
    }

    private fun dibujarGrieta(geometria: GeometriaGrietas, indice: Int) {
        val t = 0.12 + 0.76 * fraccion(geometria.semilla + indice * 0.41)
        val centro = geometria.inicio + geometria.direccion * (geometria.longitud * t)
        val desvio =
            (fraccion(geometria.semilla + indice * 0.77) - 0.5) * geometria.grosor * 1.4
        gc.strokeLine(
            centro.x() - geometria.perpendicular.x() * geometria.medio,
            centro.y() - geometria.perpendicular.y() * geometria.medio,
            centro.x() + geometria.perpendicular.x() * geometria.medio +
                geometria.direccion.x() * desvio,
            centro.y() + geometria.perpendicular.y() * geometria.medio +
                geometria.direccion.y() * desvio
        )
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

    private data class GeometriaGrietas(
        val inicio: Vector2D,
        val direccion: Vector2D,
        val perpendicular: Vector2D,
        val longitud: Double,
        val medio: Double,
        val grosor: Double,
        val semilla: Double,
        val cantidad: Int
    )

    private data class EstiloDibujoPared(
        val colorOverride: Color? = null,
        val grosorExtra: Double? = null
    )
}
