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
class DibujanteParedes(gc: GraphicsContext) {
    private val datos = DatosDibujanteParedes(gc)

    fun dibujar(pared: Pared, camara: Vector2D) = datos.dibujar(pared, camara)

    fun dibujarSuperficie(pared: Pared, camara: Vector2D) = datos.dibujarSuperficie(pared, camara)

    fun dibujarTemporal(pared: Pared, camara: Vector2D) = datos.dibujarTemporal(pared, camara)

    private data class DatosDibujanteParedes(
        val gc: GraphicsContext
    ) {
        fun dibujar(pared: Pared, camara: Vector2D) = dibujar(pared, camara, EstiloDibujoPared())

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

        fun descripcion(): String =
            "dibujanteParedes(gc=${gc.javaClass.simpleName}, dano=$COLOR_DANO, grieta=$COLOR_GRIETA, umbral=$UMBRAL_GRIETAS, maximo=$GRIETAS_MAXIMAS)"

        fun esValido(): Boolean = gc.canvas.width > 0 && gc.canvas.height > 0 && UMBRAL_GRIETAS >= 0

        fun resumen(): String = "${gc.javaClass.simpleName} grietas>$UMBRAL_GRIETAS max=$GRIETAS_MAXIMAS"

        private fun dibujar(
            pared: Pared,
            camara: Vector2D,
            estilo: EstiloDibujoPared
        ) {
            val inicio = pared.inicio() - camara
            val fin = pared.fin() - camara

            val colorBase = estilo.colorOverride() ?: colorDe(pared.tipo().color())
            val colorBordeBase = estilo.colorOverride() ?: colorDe(pared.tipo().colorBorde())

            gc.stroke = mezclar(colorBordeBase, COLOR_DANO, pared.danio() * 0.6)
            gc.lineWidth = pared.grosor() + estilo.grosorBorde()
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
            gc.lineWidth = max(1.0, geometria.grosor() * 0.16)
            gc.setLineDashes()

            repeat(geometria.cantidad()) { indice ->
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
            val trazo = geometria.trazoDe(indice)
            gc.strokeLine(trazo.first.x(), trazo.first.y(), trazo.second.x(), trazo.second.y())
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

    private data class GeometriaGrietas(
        private val inicio: Vector2D,
        private val direccion: Vector2D,
        private val perpendicular: Vector2D,
        private val longitud: Double,
        private val medio: Double,
        private val grosor: Double,
        private val semilla: Double,
        private val cantidad: Int
    ) {
        fun grosor(): Double = grosor
        fun cantidad(): Int = cantidad

        fun trazoDe(indice: Int): Pair<Vector2D, Vector2D> {
            val t = 0.12 + 0.76 * fraccion(semilla + indice * 0.41)
            val centro = inicio + direccion * (longitud * t)
            val desvio = (fraccion(semilla + indice * 0.77) - 0.5) * grosor * 1.4
            val a = centro - perpendicular * medio
            val b = centro + perpendicular * medio + direccion * desvio
            return a to b
        }

        fun descripcion(): String =
            "grietas(inicio=$inicio, direccion=$direccion, longitud=$longitud, medio=$medio, grosor=$grosor, semilla=$semilla, cantidad=$cantidad)"

        fun esValida(): Boolean =
            longitud > 0 && grosor > 0 && cantidad > 0 && perpendicular.magnitud() > 0 && direccion.magnitud() > 0

        private fun fraccion(valor: Double): Double = valor - kotlin.math.floor(valor)
    }

    private data class EstiloDibujoPared(
        private val colorOverride: Color? = null,
        private val grosorExtra: Double? = null
    ) {
        fun colorOverride(): Color? = colorOverride
        fun grosorBorde(): Double = (grosorExtra ?: 2.0)
    }
}
