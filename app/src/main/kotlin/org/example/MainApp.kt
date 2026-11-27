package org.example

import javafx.application.Application
import javafx.stage.Stage
import org.example.modelo.TipoPelota
import org.example.modelo.TipoPared
import org.example.ui.ControlEventosUI

class MainApp : Application(), ControlEventosUI {
    companion object {
        const val ANCHO_VENTANA = 1000.0
        const val ALTO_VENTANA = 650.0

        @JvmStatic
        fun main(args: Array<String>) {
            Application.launch(MainApp::class.java, *args)
        }
    }

    private val arquitectura = ArquitecturaAplicacion(this, ANCHO_VENTANA, ALTO_VENTANA)

    override fun start(stage: Stage) = arquitectura.iniciar(stage)

    override fun alternarModoParedes() = arquitectura.alternarModoParedes()
    override fun alternarMenuParedes() = arquitectura.alternarMenuParedes()
    override fun limpiarParedes() = arquitectura.limpiarParedes()
    override fun alternarModoSuperficies() = arquitectura.alternarModoSuperficies()
    override fun limpiarSuperficiesLanzamiento() = arquitectura.limpiarSuperficiesLanzamiento()
    override fun alternarMenuPelotas() = arquitectura.alternarMenuPelotas()
    override fun alternarPausa() = arquitectura.alternarPausa()
    override fun reiniciar() = arquitectura.reiniciar()
    override fun volverAlMenu() = arquitectura.volverAlMenu()
    override fun seleccionarModo(nombre: String?) = arquitectura.seleccionarModo(nombre)
    override fun aplicarGravedad(nombre: String?) = arquitectura.aplicarGravedad(nombre)
    override fun seleccionarPelota(tipo: TipoPelota) = arquitectura.seleccionarPelota(tipo)
    override fun seleccionarTipoPared(tipo: TipoPared) = arquitectura.seleccionarTipoPared(tipo)

    override fun cerrarMenusDeSeleccion() = arquitectura.cerrarMenusDeSeleccion()

    override fun actualizarControles() = arquitectura.actualizarControles()
}
