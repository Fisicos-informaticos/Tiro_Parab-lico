package org.example.simulacion

import org.example.fisica.PhysicsEngine
import org.example.herramientas.HerramientaParedes
import org.example.modelo.Pared
import org.example.modelo.Vector2D

/**
 * Agrupa el estado y las operaciones de edicion del escenario: dibujar paredes,
 * crear superficies de lanzamiento y activar o desactivar la herramienta.
 */
class GestorEdicion(
    private val herramienta: HerramientaParedes,
    private val motor: PhysicsEngine,
    private val superficies: SuperficiesApoyo
) : EditorEscenario {

    private var modoParedes: Boolean = false
    private var modoSuperficies: Boolean = false

    override fun modoParedes(): Boolean = modoParedes

    override fun modoSuperficies(): Boolean = modoSuperficies

    override fun paredTemporal(): Pared? = herramienta.paredTemporal()

    /** Cierto si el estado actual permite dibujar con la herramienta activa. */
    override fun activaCon(estado: EstadoSimulacion): Boolean =
        (modoParedes || modoSuperficies) && estado.permiteEditar()

    /** Activa o desactiva el modo pared. Devuelve true si quedo activo. */
    override fun alternarParedes(): Boolean {
        modoParedes = !modoParedes
        modoSuperficies = false
        if (modoParedes) {
            herramienta.activar()
        } else {
            herramienta.desactivar()
            herramienta.cancelar()
        }
        return modoParedes
    }

    /** Activa o desactiva el modo superficie. Devuelve true si quedo activo. */
    override fun alternarSuperficies(): Boolean {
        modoSuperficies = !modoSuperficies
        modoParedes = false
        if (modoSuperficies) {
            herramienta.activar()
        } else {
            herramienta.desactivar()
            herramienta.cancelar()
        }
        return modoSuperficies
    }

    override fun iniciar(punto: Vector2D) = herramienta.iniciar(punto)

    override fun actualizar(punto: Vector2D) = herramienta.actualizar(punto)

    override fun finalizar(punto: Vector2D) {
        val paredesAntes = motor.obtenerParedes().size
        herramienta.finalizar(punto)
        if (!modoSuperficies) return

        moverParedNuevaASuperficies(paredesAntes)
        modoSuperficies = false
        herramienta.desactivar()
    }

    /** Abandona cualquier modo de edicion y cancela el trazo en curso. */
    override fun desactivar() {
        modoParedes = false
        modoSuperficies = false
        herramienta.desactivar()
        herramienta.cancelar()
    }

    override fun cancelarDibujo() {
        herramienta.cancelar()
    }

    /** Si la herramienta creo una pared, la saca del motor y la guarda como apoyo. */
    private fun moverParedNuevaASuperficies(paredesAntes: Int) {
        val paredes = motor.obtenerParedes()
        if (paredes.size != paredesAntes + 1) return

        val nueva = paredes.lastOrNull() ?: return
        motor.quitarPared(nueva)
        superficies.agregar(nueva)
    }
}
