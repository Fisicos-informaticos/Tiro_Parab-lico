package org.example.simulacion

import org.example.modelo.Vector2D

/**
 * Estados posibles del tiro. Es una jerarquia sellada a proposito: cada estado
 * sabe como reaccionar a los eventos y al tiempo, asi que anadir un estado nuevo
 * es anadir una clase y no tocar los demás (OCP). El compilador avisa de los
 * `when` que se queden sin cubrir.
 */
sealed class EstadoSimulacion {

    /** Nombre para el panel de informacion. */
    abstract val nombre: String

    /** Si el mundo esta listo para recibir el arrastre de lanzamiento. */
    open val dibujaPuntoDeLanzamiento: Boolean get() = false

    open fun alEntrar(simulacion: Simulacion) = Unit

    open fun alPulsar(simulacion: Simulacion, punto: Vector2D) = Unit

    open fun alArrastrar(simulacion: Simulacion, punto: Vector2D) = Unit

    open fun alSoltar(simulacion: Simulacion, punto: Vector2D) = Unit

    open fun alActualizar(simulacion: Simulacion, deltaTime: Double) = Unit

    /** Primer contacto: un clic saca la pantalla de bienvenida. */
    object PantallaInicio : EstadoSimulacion() {
        override val nombre = "PANTALLA_INICIO"

        override fun alPulsar(simulacion: Simulacion, punto: Vector2D) {
            simulacion.cambiarA(Inactivo)
        }
    }

    /** La pelota espera en el suelo: otro clic la coloca para el tiro. */
    object Inactivo : EstadoSimulacion() {
        override val nombre = "INACTIVO"
        override val dibujaPuntoDeLanzamiento = true

        override fun alPulsar(simulacion: Simulacion, punto: Vector2D) {
            simulacion.colocarPelotaParaLanzar(punto)
        }
    }

    /** Arrastrando para apuntar: el vector dibujado es el vector de tiro. */
    object Arrojando : EstadoSimulacion() {
        override val nombre = "ARROJANDO"
        override val dibujaPuntoDeLanzamiento = true

        override fun alPulsar(simulacion: Simulacion, punto: Vector2D) {
            simulacion.iniciarApuntado(punto)
        }

        override fun alArrastrar(simulacion: Simulacion, punto: Vector2D) {
            simulacion.continuarApuntado(punto)
        }

        override fun alSoltar(simulacion: Simulacion, punto: Vector2D) {
            simulacion.lanzarPelota()
        }
    }

    /** La pelota vuela: la fisica manda y al detenerse se cierra el tiro. */
    object EnVuelo : EstadoSimulacion() {
        override val nombre = "EN_VUELO"

        override fun alActualizar(simulacion: Simulacion, deltaTime: Double) {
            simulacion.avanzarVuelo(deltaTime)
        }
    }

    /** La pelota se ha parado: el siguiente clic reinicia. */
    object EnSuelo : EstadoSimulacion() {
        override val nombre = "EN_SUELO"

        override fun alPulsar(simulacion: Simulacion, punto: Vector2D) {
            simulacion.reiniciar()
        }
    }
}