package org.example.entrada

import org.example.modelo.Vector2D
import kotlin.math.atan2

class DragInputHandler : InputHandler {

    private var _posicionInicial: Vector2D? = null
    private var _posicionActual: Vector2D? = null

    val posicionInicial: Vector2D? get() = _posicionInicial
    val posicionActual: Vector2D? get() = _posicionActual

    override val estaArrastrando: Boolean
        get() = _posicionInicial != null && _posicionActual != null

    override val vectorLanzamiento: Vector2D?
        get() {
            val inicio = _posicionInicial ?: return null
            val actual = _posicionActual ?: return null
            return inicio - actual
        }

    override val anguloLanzamiento: Double
        get() {
            val v = vectorLanzamiento ?: return 0.0
            return Math.toDegrees(atan2(-v.y, v.x))
        }

    override val fuerzaLanzamiento: Double
        get() = vectorLanzamiento?.magnitud ?: 0.0

    override fun iniciarArrastre(posicion: Vector2D) {
        _posicionInicial = posicion
        _posicionActual = posicion
    }

    override fun actualizarArrastre(posicion: Vector2D) {
        if (estaArrastrando) {
            _posicionActual = posicion
        }
    }

    override fun finalizarArrastre() {
        // El vector se conserva para usar en el lanzamiento
    }

    override fun reiniciar() {
        _posicionInicial = null
        _posicionActual = null
    }
}
