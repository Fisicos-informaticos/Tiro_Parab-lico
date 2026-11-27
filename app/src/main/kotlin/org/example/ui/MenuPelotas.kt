package org.example.ui

import javafx.scene.control.ToggleButton
import javafx.scene.control.ToggleGroup
import javafx.scene.control.Tooltip
import javafx.scene.layout.VBox
import org.example.grafico.DibujoPelota
import org.example.grafico.VistaPelota
import org.example.modelo.TipoPelota
import org.example.modelo.Vector2D
import org.example.modelo.colision.FormaColision

internal interface VistaMenuPelotas {
    fun menu(): VBox
    fun marcarActual()
}

internal class VistaMenuPelotasJavaFx(
    private val escucha: ConfiguracionJuego,
    private val tipoPelotaActual: () -> TipoPelota,
    private val alCerrarMenus: () -> Unit
) : VistaMenuPelotas {
    private val grupo = ToggleGroup()
    private val botones = mutableMapOf<TipoPelota, ToggleButton>()
    private val menu: VBox = crearMenu()

    init {
        grupo.selectedToggleProperty().addListener { _, _, _ -> marcarSeleccion() }
    }

    override fun menu(): VBox = menu

    override fun marcarActual() {
        grupo.selectToggle(botones[tipoPelotaActual()])
    }

    private fun crearMenu(): VBox {
        val menu = ConstructorMenu.construir(
            ConstructorMenu.EspecificacionMenu(
                titulo = "ELIGE TU PELOTA",
                subtitulo = "Cada tipo tiene su masa, rebote, rozamiento y forma de colision",
                ayuda = "Pulsa B o ESC para cerrar",
                contenido = ConstructorMenu.rejillaTipos(TipoPelota.entries.toList()) {
                    crearBoton(it)
                }
            )
        )
        grupo.selectToggle(botones[tipoPelotaActual()])
        marcarSeleccion()
        return menu
    }

    private fun crearBoton(tipo: TipoPelota): ToggleButton {
        val forma = tipo.crearForma()
        val previa = crearPrevia(tipo, forma)
        val textos = crearTextos(tipo, forma)

        val boton = ConstructorMenu.botonMenu(
            ConstructorMenu.EspecificacionBotonMenu(
                previa = previa,
                textos = textos,
                ancho = ConstructorMenu.medidas.anchoBotonMenu,
                alCerrarMenus = alCerrarMenus,
                accion = { escucha.seleccionarPelota(tipo) }
            )
        )
        boton.tooltip = Tooltip(tipo.descripcion())
        botones[tipo] = boton
        return boton
    }

    private fun crearPrevia(tipo: TipoPelota, forma: FormaColision) =
        ConstructorMenu.previaCanvas {
            val escala = (ConstructorMenu.medidas.altoPrevia * 0.42) / forma.radioEnvoltura()
            DibujoPelota.dibujar(
                it,
                VistaPelota(
                    tipo = tipo,
                    forma = forma,
                    angulo = 0.0,
                    centro = Vector2D(60.0, ConstructorMenu.medidas.altoPrevia / 2.0),
                    escala = escala
                )
            )
        }

    private fun crearTextos(tipo: TipoPelota, forma: FormaColision) =
        ConstructorMenu.textos(
            ConstructorMenu.EspecificacionTextos(
                nombre = tipo.nombre(),
                datos = "masa ${"%.2f".format(tipo.masa())} kg · rebote " +
                    "${"%.2f".format(tipo.coeficienteRestitucion())}",
                extra = "rozamiento ${"%.1f".format(tipo.coeficienteFriccion())} · " +
                    forma.tipo().etiqueta()
            )
        )

    private fun marcarSeleccion() {
        for (boton in botones.values) {
            boton.style = if (boton.isSelected) EstilosUI.BOTON_ACTIVO else EstilosUI.BOTON
        }
    }
}

internal class MenuPelotas(
    escucha: ConfiguracionJuego,
    tipoPelotaActual: () -> TipoPelota,
    alCerrarMenus: () -> Unit,
    private val vista: VistaMenuPelotas = VistaMenuPelotasJavaFx(escucha, tipoPelotaActual, alCerrarMenus)
) {
    fun menu(): VBox = vista.menu()

    fun marcarActual() = vista.marcarActual()
}
