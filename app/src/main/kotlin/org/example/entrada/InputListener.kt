package org.example.entrada

import org.example.modelo.Vector2D

/**
 * Eventos de entrada que una presentacion reenvia al nucleo. Es una interfaz
 * minima (ISP) para que ni el motor ni los modos dependan de JavaFX.
 */
interface InputListener {
    fun pulsar(punto: Vector2D)
    fun arrastrar(punto: Vector2D)
    fun soltar(punto: Vector2D)
}
