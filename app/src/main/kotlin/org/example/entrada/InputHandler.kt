package org.example.entrada

import org.example.modelo.Vector2D

interface ControlEntradaArrastre {
    fun iniciarArrastre(posicion: Vector2D)
    fun actualizarArrastre(posicion: Vector2D)
    fun reiniciar()
}

interface ConsultaEntradaArrastre {
    fun estaArrastrando(): Boolean
    fun posicionInicial(): Vector2D?
    fun posicionActual(): Vector2D?
    fun vectorLanzamiento(): Vector2D?
    fun anguloLanzamiento(): Double
    fun fuerzaLanzamiento(): Double
}

interface InputHandler : ControlEntradaArrastre, ConsultaEntradaArrastre
