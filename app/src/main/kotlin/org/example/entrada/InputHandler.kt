package org.example.entrada

import org.example.modelo.Vector2D

interface InputHandler {
    fun iniciarArrastre(posicion: Vector2D)
    fun actualizarArrastre(posicion: Vector2D)
    fun finalizarArrastre()
    fun reiniciar()

    val estaArrastrando: Boolean
    val vectorLanzamiento: Vector2D?
    val anguloLanzamiento: Double
    val fuerzaLanzamiento: Double
}
