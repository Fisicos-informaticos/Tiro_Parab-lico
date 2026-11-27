package org.example.ui

import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.input.KeyCode
import org.example.ControladorMenusRoles
import org.example.simulacion.AccionesEscenarioJuego
import org.example.simulacion.AccionesPartidaJuego
import org.example.simulacion.EntradaJuego
import org.example.simulacion.LecturaEstadoJuego
import org.example.simulacion.LecturaEscenarioJuego
import org.example.simulacion.ControlEntradaJuego
import org.example.modelo.Vector2D

interface ControlEventosUI : AccionesJuego, ConfiguracionJuego, EdicionEscenario {
    fun actualizarControles()
    fun cerrarMenusDeSeleccion() = Unit
}

class ControladorEventosUI(
    private val simulacion: ControlEntradaJuego,
    private val menus: ControladorMenusRoles,
    private val escucha: ControlEventosUI
) {
    fun enlazar(canvas: Canvas, escena: Scene) {
        enlazarRaton(canvas)
        enlazarTeclado(escena)
    }

    private fun enlazarRaton(canvas: Canvas) {
        canvas.setOnMousePressed { evento ->
            if (menus.interaccionBloqueada()) return@setOnMousePressed
            simulacion.pulsar(Vector2D(evento.x, evento.y))
            escucha.actualizarControles()
        }
        canvas.setOnMouseDragged { evento ->
            if (menus.interaccionBloqueada()) return@setOnMouseDragged
            simulacion.arrastrar(Vector2D(evento.x, evento.y))
        }
        canvas.setOnMouseReleased { evento ->
            if (menus.interaccionBloqueada()) return@setOnMouseReleased
            simulacion.soltar(Vector2D(evento.x, evento.y))
            escucha.actualizarControles()
        }
    }

    private fun enlazarTeclado(escena: Scene) {
        escena.setOnKeyPressed { evento ->
            when (evento.code) {
                KeyCode.P -> escucha.alternarModoParedes()
                KeyCode.B -> escucha.alternarMenuPelotas()
                KeyCode.W -> escucha.alternarMenuParedes()
                KeyCode.R -> if (menus.menuFinAbierto()) escucha.reiniciar()
                KeyCode.Q -> if (menus.menuFinAbierto()) Platform.exit()
                KeyCode.ESCAPE -> cerrarMenusConEscape()
                else -> Unit
            }
        }
    }

    private fun cerrarMenusConEscape() {
        if (!menus.cerrarConEscape()) {
            when {
                simulacion.pausado() -> simulacion.alternarPausa()
                simulacion.modoParedes() -> simulacion.alternarModoParedes()
            }
        }
        escucha.actualizarControles()
    }
}
