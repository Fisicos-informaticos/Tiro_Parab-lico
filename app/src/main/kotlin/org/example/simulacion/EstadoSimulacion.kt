package org.example.simulacion

import org.example.modelo.Vector2D
import org.example.modos.LecturaModo
import org.example.modos.ModoSimulacion

sealed class EstadoSimulacion {

    abstract fun nombre(): String

    open fun estaEnVuelo(): Boolean = false

    open fun esPantallaInicio(): Boolean = false

    open fun esEnSuelo(): Boolean = false

    open fun permitePausa(): Boolean = true

    open fun permiteEditar(): Boolean = true

    open fun permitePrevisualizar(): Boolean = false

    open fun alPulsar(simulacion: ContextoJuego, punto: Vector2D) = Unit

    open fun alArrastrar(simulacion: ContextoJuego, punto: Vector2D) = Unit

    open fun alSoltar(simulacion: ContextoJuego, punto: Vector2D) = Unit

    open fun alActualizar(simulacion: ContextoJuego, deltaTime: Double) = Unit

    open fun alActivarHerramienta(simulacion: ContextoJuego) = Unit

    open fun alAbrirEdicion(simulacion: ContextoJuego) = Unit

    abstract fun dibujar(escena: EscenaEstado, simulacion: ContextoJuego)

    abstract fun lineasDeEstado(modo: ModoSimulacion, lectura: LecturaModo): List<String>

    object PantallaInicio : EstadoSimulacion() {
        override fun nombre(): String = "PANTALLA_INICIO"

        override fun esPantallaInicio(): Boolean = true

        override fun permitePausa(): Boolean = false

        override fun alPulsar(simulacion: ContextoJuego, punto: Vector2D) {
            simulacion.cambiarA(Inactivo)
        }

        override fun alActivarHerramienta(simulacion: ContextoJuego) {
            simulacion.cambiarA(Inactivo)
        }

        override fun dibujar(escena: EscenaEstado, simulacion: ContextoJuego) {
            escena.mostrarPantallaInicio(simulacion.tipoPelota().nombre())
        }

        override fun lineasDeEstado(modo: ModoSimulacion, lectura: LecturaModo): List<String> =
            listOf("Haz click para comenzar")
    }

    object Inactivo : EstadoSimulacion() {
        override fun nombre(): String = "INACTIVO"

        override fun alPulsar(simulacion: ContextoJuego, punto: Vector2D) {
            simulacion.colocarPelotaParaLanzar(punto)
        }

        override fun dibujar(escena: EscenaEstado, simulacion: ContextoJuego) {
            escena.mostrarPelotaEnEspera(simulacion)
            escena.mostrarInformacion(simulacion)
        }

        override fun lineasDeEstado(modo: ModoSimulacion, lectura: LecturaModo): List<String> =
            modo.lineasDeColocacion(lectura)
    }

    object Arrojando : EstadoSimulacion() {
        override fun nombre(): String = "ARROJANDO"

        override fun permitePrevisualizar(): Boolean = true

        override fun alPulsar(simulacion: ContextoJuego, punto: Vector2D) {
            simulacion.iniciarApuntado(punto)
        }

        override fun alArrastrar(simulacion: ContextoJuego, punto: Vector2D) {
            simulacion.continuarApuntado(punto)
        }

        override fun alSoltar(simulacion: ContextoJuego, punto: Vector2D) {
            simulacion.lanzarPelota()
        }

        override fun alActivarHerramienta(simulacion: ContextoJuego) {
            simulacion.entrada().reiniciar()
            simulacion.cambiarA(Inactivo)
        }

        override fun alAbrirEdicion(simulacion: ContextoJuego) {
            alActivarHerramienta(simulacion)
        }

        override fun dibujar(escena: EscenaEstado, simulacion: ContextoJuego) {
            escena.mostrarApuntado(simulacion)
            escena.mostrarInformacion(simulacion)
        }

        override fun lineasDeEstado(modo: ModoSimulacion, lectura: LecturaModo): List<String> =
            modo.lineasDeApuntado(lectura)
    }

    object EnVuelo : EstadoSimulacion() {
        override fun nombre(): String = "EN_VUELO"

        override fun estaEnVuelo(): Boolean = true

        override fun permiteEditar(): Boolean = false

        override fun alActualizar(simulacion: ContextoJuego, deltaTime: Double) {
            simulacion.avanzarVuelo(deltaTime)
        }

        override fun dibujar(escena: EscenaEstado, simulacion: ContextoJuego) {
            escena.mostrarProyectil(simulacion)
            escena.mostrarInformacion(simulacion)
        }

        override fun lineasDeEstado(modo: ModoSimulacion, lectura: LecturaModo): List<String> =
            modo.lineasDeVuelo(lectura)
    }

    object EnSuelo : EstadoSimulacion() {
        override fun nombre(): String = "EN_SUELO"

        override fun esEnSuelo(): Boolean = true

        override fun alPulsar(simulacion: ContextoJuego, punto: Vector2D) {
            simulacion.reiniciar()
        }

        override fun alActivarHerramienta(simulacion: ContextoJuego) {
            simulacion.reiniciar()
        }

        override fun alAbrirEdicion(simulacion: ContextoJuego) {
            simulacion.reiniciar()
        }

        override fun dibujar(escena: EscenaEstado, simulacion: ContextoJuego) {
            escena.mostrarProyectil(simulacion)
            escena.mostrarInformacion(simulacion)
        }

        override fun lineasDeEstado(modo: ModoSimulacion, lectura: LecturaModo): List<String> =
            modo.lineasDeReposo(lectura)
    }
}
