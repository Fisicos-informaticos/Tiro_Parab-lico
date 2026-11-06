package org.example.modelo

import org.example.modelo.colision.FormaColision

/**
 * Contrato de un cuerpo que se mueve por el mundo. El estado solo se consulta y
 * se modifica mediante metodos explicitos, nunca accediendo a campos.
 */
interface Proyectil {
    fun posicion(): Vector2D

    fun velocidad(): Vector2D

    /** Rotacion del cuerpo en radianes (sentido antihorario en el plano de la pantalla). */
    fun angulo(): Double

    fun velocidadAngular(): Double

    fun tipo(): TipoPelota

    fun forma(): FormaColision

    fun masa(): Double = tipo().masa()

    fun coeficienteRestitucion(): Double = tipo().coeficienteRestitucion()

    fun coeficienteFriccion(): Double = tipo().coeficienteFriccion()

    fun coeficienteArrastre(): Double = tipo().coeficienteArrastre()

    fun giro(): Double = tipo().giro()

    /** Radio del circulo que envuelve al proyectil. */
    fun radio(): Double = forma().radioEnvoltura()

    fun fijarPosicion(nueva: Vector2D)

    fun fijarVelocidad(nueva: Vector2D)

    fun fijarAngulo(nuevo: Double)

    fun fijarVelocidadAngular(nueva: Double)

    fun aplicarFuerza(fuerza: Vector2D, deltaTime: Double)

    fun estaEnSuelo(limiteY: Double): Boolean
}
