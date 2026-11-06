package org.example.entrada

import org.example.modelo.Vector2D
import kotlin.math.atan2

class DragInputHandler : InputHandler {

    private var posicionInicial: Vector2D? = null
    private var posicionActual: Vector2D? = null

    override fun posicionInicial(): Vector2D? = posicionInicial

    override fun posicionActual(): Vector2D? = posicionActual

    override fun estaArrastrando(): Boolean =
        posicionInicial != null && posicionActual != null

    override fun vectorLanzamiento(): Vector2D? {
        val inicio = posicionInicial ?: return null
        val actual = posicionActual ?: return null
        return inicio - actual
    }

    override fun anguloLanzamiento(): Double {
        val v = vectorLanzamiento() ?: return 0.0
        return Math.toDegrees(atan2(-v.y(), v.x()))
    }

    override fun fuerzaLanzamiento(): Double = vectorLanzamiento()?.magnitud() ?: 0.0

    override fun iniciarArrastre(posicion: Vector2D) {
        posicionInicial = posicion
        posicionActual = posicion
    }

    override fun actualizarArrastre(posicion: Vector2D) {
        if (estaArrastrando()) {
            posicionActual = posicion
        }
    }

    override fun finalizarArrastre() {
        // El vector se conserva para usar en el lanzamiento
    }

    override fun reiniciar() {
        posicionInicial = null
        posicionActual = null
    }
}
