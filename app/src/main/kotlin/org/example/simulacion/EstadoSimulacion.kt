package org.example.simulacion

import org.example.modelo.Vector2D
import org.example.modos.LecturaModo
import org.example.modos.ModoSimulacion

/**
 * Estados posibles del tiro. Cada estado encapsula sus reglas, sus capacidades,
 * sus reacciones y su forma de presentarse. Añadir un estado nuevo solo requiere
 * una clase nueva; ni el nucleo ni el presentador tienen que conocer los tipos
 * concretos.
 */
sealed class EstadoSimulacion {

    /** Nombre para el panel de informacion. */
    abstract fun nombre(): String

    /** La simulacion esta avanzando y no admite cambios de escenario. */
    open fun estaEnVuelo(): Boolean = false

    /** El mundo aun muestra la pantalla de bienvenida. */
    open fun esPantallaInicio(): Boolean = false

    /** El tiro ha terminado y espera al siguiente clic. */
    open fun esEnSuelo(): Boolean = false

    /** Se puede pausar o reanudar desde este estado. */
    open fun permitePausa(): Boolean = true

    /** Se puede modificar el escenario o seleccionar material. */
    open fun permiteEditar(): Boolean = true

    /** Hay una trayectoria de apuntado que se puede previsualizar. */
    open fun permitePrevisualizar(): Boolean = false

    open fun alPulsar(simulacion: Simulacion, punto: Vector2D) = Unit

    open fun alArrastrar(simulacion: Simulacion, punto: Vector2D) = Unit

    open fun alSoltar(simulacion: Simulacion, punto: Vector2D) = Unit

    open fun alActualizar(simulacion: Simulacion, deltaTime: Double) = Unit

    /** Deja el estado listo para empezar a dibujar una pared o superficie. */
    open fun alActivarHerramienta(simulacion: Simulacion) = Unit

    /** Abandona el apuntado o el resultado al abrir un menu de edicion. */
    open fun alAbrirEdicion(simulacion: Simulacion) = Unit

    /** Pinta la escena propia del estado y, si procede, su informacion. */
    abstract fun dibujar(escena: EscenaEstado, simulacion: Simulacion)

    /** Lineas del panel que dependen del estado y del modo activo. */
    abstract fun lineasDeEstado(modo: ModoSimulacion, lectura: LecturaModo): List<String>

    /** Primer contacto: un clic saca la pantalla de bienvenida. */
    object PantallaInicio : EstadoSimulacion() {
        override fun nombre(): String = "PANTALLA_INICIO"

        override fun esPantallaInicio(): Boolean = true

        override fun permitePausa(): Boolean = false

        override fun alPulsar(simulacion: Simulacion, punto: Vector2D) {
            simulacion.cambiarA(Inactivo)
        }

        override fun alActivarHerramienta(simulacion: Simulacion) {
            simulacion.cambiarA(Inactivo)
        }

        override fun dibujar(escena: EscenaEstado, simulacion: Simulacion) {
            escena.mostrarPantallaInicio(simulacion.tipoPelota().nombre())
        }

        override fun lineasDeEstado(modo: ModoSimulacion, lectura: LecturaModo): List<String> =
            listOf("Haz click para comenzar")
    }

    /** La pelota espera en el suelo: otro clic la coloca para el tiro. */
    object Inactivo : EstadoSimulacion() {
        override fun nombre(): String = "INACTIVO"

        override fun alPulsar(simulacion: Simulacion, punto: Vector2D) {
            simulacion.colocarPelotaParaLanzar(punto)
        }

        override fun dibujar(escena: EscenaEstado, simulacion: Simulacion) {
            escena.mostrarPelotaEnEspera(simulacion)
            escena.mostrarInformacion(simulacion)
        }

        override fun lineasDeEstado(modo: ModoSimulacion, lectura: LecturaModo): List<String> =
            modo.lineasDeColocacion(lectura)
    }

    /** Arrastrando para apuntar: el vector dibujado es el vector de tiro. */
    object Arrojando : EstadoSimulacion() {
        override fun nombre(): String = "ARROJANDO"

        override fun permitePrevisualizar(): Boolean = true

        override fun alPulsar(simulacion: Simulacion, punto: Vector2D) {
            simulacion.iniciarApuntado(punto)
        }

        override fun alArrastrar(simulacion: Simulacion, punto: Vector2D) {
            simulacion.continuarApuntado(punto)
        }

        override fun alSoltar(simulacion: Simulacion, punto: Vector2D) {
            simulacion.lanzarPelota()
        }

        override fun alActivarHerramienta(simulacion: Simulacion) {
            simulacion.entrada().reiniciar()
            simulacion.cambiarA(Inactivo)
        }

        override fun alAbrirEdicion(simulacion: Simulacion) {
            alActivarHerramienta(simulacion)
        }

        override fun dibujar(escena: EscenaEstado, simulacion: Simulacion) {
            escena.mostrarApuntado(simulacion)
            escena.mostrarInformacion(simulacion)
        }

        override fun lineasDeEstado(modo: ModoSimulacion, lectura: LecturaModo): List<String> =
            modo.lineasDeApuntado(lectura)
    }

    /** La pelota vuela: la fisica manda y al detenerse se cierra el tiro. */
    object EnVuelo : EstadoSimulacion() {
        override fun nombre(): String = "EN_VUELO"

        override fun estaEnVuelo(): Boolean = true

        override fun permiteEditar(): Boolean = false

        override fun alActualizar(simulacion: Simulacion, deltaTime: Double) {
            simulacion.avanzarVuelo(deltaTime)
        }

        override fun dibujar(escena: EscenaEstado, simulacion: Simulacion) {
            escena.mostrarProyectil(simulacion)
            escena.mostrarInformacion(simulacion)
        }

        override fun lineasDeEstado(modo: ModoSimulacion, lectura: LecturaModo): List<String> =
            modo.lineasDeVuelo(lectura)
    }

    /** La pelota se ha parado: el siguiente clic reinicia. */
    object EnSuelo : EstadoSimulacion() {
        override fun nombre(): String = "EN_SUELO"

        override fun esEnSuelo(): Boolean = true

        override fun alPulsar(simulacion: Simulacion, punto: Vector2D) {
            simulacion.reiniciar()
        }

        override fun alActivarHerramienta(simulacion: Simulacion) {
            simulacion.reiniciar()
        }

        override fun alAbrirEdicion(simulacion: Simulacion) {
            simulacion.reiniciar()
        }

        override fun dibujar(escena: EscenaEstado, simulacion: Simulacion) {
            escena.mostrarProyectil(simulacion)
            escena.mostrarInformacion(simulacion)
        }

        override fun lineasDeEstado(modo: ModoSimulacion, lectura: LecturaModo): List<String> =
            modo.lineasDeReposo(lectura)
    }
}
