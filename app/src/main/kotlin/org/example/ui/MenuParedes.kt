package org.example.ui

import javafx.scene.control.ToggleButton
import javafx.scene.control.ToggleGroup
import javafx.scene.control.Tooltip
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import org.example.modelo.TipoPared

/** Menu de seleccion del material de las paredes. */
internal class MenuParedes(
    private val escucha: ConfiguracionJuego,
    private val tipoParedActual: () -> TipoPared,
    private val alCerrarMenus: () -> Unit
) {
    private val grupo = ToggleGroup()
    private val botones = mutableMapOf<TipoPared, ToggleButton>()
    private val menu: VBox = crearMenu()

    init {
        grupo.selectedToggleProperty().addListener { _, _, _ -> marcarSeleccion() }
    }

    fun menu(): VBox = menu

    fun marcarActual() {
        grupo.selectToggle(botones[tipoParedActual()])
    }

    private fun crearMenu(): VBox {
        val menu = ConstructorMenu.construir(
            ConstructorMenu.EspecificacionMenu(
                titulo = "ELIGE EL TIPO DE PARED",
                subtitulo = "Cada material aguanta distinto y solo lo rompen ciertas pelotas",
                ayuda = "Pulsa W o ESC para cerrar",
                contenido = ConstructorMenu.rejillaTipos(TipoPared.entries.toList()) {
                    crearBoton(it)
                }
            )
        )
        grupo.selectToggle(botones[tipoParedActual()])
        marcarSeleccion()
        return menu
    }

    private fun crearBoton(tipo: TipoPared): ToggleButton {
        val previa = crearPrevia(tipo)
        val textos = crearTextos(tipo)

        val boton = ConstructorMenu.botonMenu(
            ConstructorMenu.EspecificacionBotonMenu(
                previa = previa,
                textos = textos,
                ancho = ConstructorMenu.medidas.anchoBotonPared,
                alCerrarMenus = alCerrarMenus,
                accion = { escucha.seleccionarTipoPared(tipo) }
            )
        )
        boton.tooltip = Tooltip(tipo.descripcion())
        botones[tipo] = boton
        return boton
    }

    private fun crearPrevia(tipo: TipoPared) = ConstructorMenu.previaCanvas { gc ->
        val yCentro = ConstructorMenu.medidas.altoPrevia / 2.0
        gc.stroke = ConstructorMenu.colorDe(tipo.colorBorde())
        gc.lineWidth = tipo.grosor() + 2.0
        gc.strokeLine(4.0, yCentro, 116.0, yCentro)
        gc.stroke = ConstructorMenu.colorDe(tipo.color())
        gc.lineWidth = tipo.grosor()
        gc.strokeLine(4.0, yCentro, 116.0, yCentro)
        if (tipo.indestructible()) {
            gc.stroke = Color.web("#f1c40f")
            gc.lineWidth = 2.0
            gc.strokeLine(60.0, 6.0, 60.0, ConstructorMenu.medidas.altoPrevia - 6.0)
        }
    }

    private fun crearTextos(tipo: TipoPared) = ConstructorMenu.textos(
        ConstructorMenu.EspecificacionTextos(
            nombre = tipo.nombre(),
            datos = "grosor ${tipo.grosor().toInt()} px · aguanta ${textoResistencia(tipo)}",
            extra = tipo.textoRompible(),
            anchoExtra = ConstructorMenu.medidas.anchoBotonPared
        )
    )

    private fun textoResistencia(tipo: TipoPared): String =
        if (tipo.indestructible()) "todo" else "${tipo.resistencia().toInt()} de energia"

    private fun marcarSeleccion() {
        for (boton in botones.values) {
            boton.style = if (boton.isSelected) EstilosUI.BOTON_ACTIVO else EstilosUI.BOTON
        }
    }
}
