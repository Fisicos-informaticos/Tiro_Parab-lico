package org.example.grafico

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.text.Font

class DibujantePantallaInicio(
    private val gc: GraphicsContext,
    private val ancho: Double,
    private val alto: Double
) {
    fun dibujar(nombrePelota: String?) {
        gc.fill = Color.color(0.0, 0.0, 0.0, 0.85)
        gc.fillRect(0.0, 0.0, ancho, alto)

        dibujarTitulo()
        dibujarSubtitulo()
        dibujarInstruccionPrincipal()
        dibujarInstruccionesSecundarias()
        dibujarNombrePelota(nombrePelota)
    }

    private fun dibujarTitulo() {
        gc.fill = Color.web("#ff6b6b")
        gc.font = Font.font("Monospace", 52.0)
        gc.fillText("TIRO PARABOLICO", ancho / 2.0 - 300.0, alto / 2.0 - 90.0)
    }

    private fun dibujarSubtitulo() {
        gc.fill = Color.WHITE
        gc.font = Font.font("Monospace", 20.0)
        gc.fillText("Simulador de tiro parabolico", ancho / 2.0 - 240.0, alto / 2.0 - 40.0)
    }

    private fun dibujarInstruccionPrincipal() {
        gc.fill = Color.web("#ff6b35")
        gc.font = Font.font("Monospace", 22.0)
        gc.fillText(">> HAZ CLICK PARA COMENZAR <<", ancho / 2.0 - 245.0, alto / 2.0 + 40.0)
    }

    private fun dibujarInstruccionesSecundarias() {
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
    }

    private fun dibujarNombrePelota(nombrePelota: String?) {
        if (nombrePelota == null) return
        gc.fill = Color.web("#d4e157")
        gc.font = Font.font("Monospace", 15.0)
        gc.fillText("Pelota actual: $nombrePelota", ancho / 2.0 - 175.0, alto / 2.0 + 180.0)
    }
}
