package org.example.ui

/**
 * Catalogo central de estilos JavaFX. Al vivir en un [object] no quedan cadenas
 * de estilo sueltas ni duplicadas entre las distintas vistas de la interfaz.
 */
object EstilosUI {

    /** Contenedor lateral: fondo semitransparente, esquinas redondeadas y sombra. */
    const val PANEL_CONTENEDOR =
        "-fx-padding: 16 16 16 16; " +
            "-fx-background-color: rgba(18, 22, 28, 0.88); " +
            "-fx-background-radius: 16; " +
            "-fx-border-radius: 16; " +
            "-fx-border-color: rgba(212, 225, 87, 0.30); " +
            "-fx-border-width: 1; " +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.50), 16, 0.15, 0, 6);"

    /** Titulo de cada bloque logico del panel (Simulacion, Pelotas, Obstaculos). */
    const val TITULO_SECCION =
        "-fx-text-fill: #d4e157; -fx-font-size: 12px; -fx-font-weight: bold;"

    /** Etiqueta pequena que acompana a un control. */
    const val CAPTION = "-fx-text-fill: #9aa4b0; -fx-font-size: 11px;"

    /** Linea divisoria entre el titulo de seccion y su contenido. */
    const val DIVISOR = "-fx-background-color: rgba(154, 164, 176, 0.28);"

    const val BOTON =
        "-fx-background-color: #2b3138; -fx-text-fill: white; " +
            "-fx-border-color: #4a515a; -fx-border-radius: 8; -fx-background-radius: 8; " +
            "-fx-padding: 6 8 6 8;"

    const val BOTON_ACTIVO =
        "-fx-background-color: #3d5a80; -fx-text-fill: white; " +
            "-fx-border-color: #d4e157; -fx-border-width: 2; -fx-border-radius: 8; " +
            "-fx-background-radius: 8; -fx-padding: 5 7 5 7;"

    const val MENU =
        "-fx-background-color: #20252d; -fx-background-radius: 12; " +
            "-fx-border-color: #6c757d; -fx-border-radius: 12;"

    const val MENU_FIN =
        "-fx-background-color: #20252d; -fx-background-radius: 12; " +
            "-fx-border-color: #d4e157; -fx-border-radius: 12;"

    const val TITULO =
        "-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;"

    const val NOMBRE = "-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;"

    const val DETALLE = "-fx-text-fill: #c8ced6; -fx-font-size: 11px;"

    const val SUBTITULO = "-fx-text-fill: #c8ced6; -fx-font-size: 12px;"

    const val AYUDA = "-fx-text-fill: #9aa4b0; -fx-font-size: 12px;"
}
