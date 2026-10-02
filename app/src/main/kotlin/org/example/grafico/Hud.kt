package org.example.grafico

/**
 * Capa de informacion: se limita a pintar lineas de texto sobre un panel, sin
 * saber nada de las reglas del juego.
 */
interface Hud {
    fun dibujarPanel(lineas: List<String>)
    fun dibujarPantallaInicio(nombrePelota: String? = null)
}