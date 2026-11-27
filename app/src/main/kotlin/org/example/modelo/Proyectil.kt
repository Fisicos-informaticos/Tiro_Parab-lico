package org.example.modelo

import org.example.modelo.colision.FormaColision

/**
 * Contrato de un cuerpo que se mueve por el mundo. El estado solo se consulta y
 * se modifica mediante metodos explicitos, nunca accediendo a campos.
 */
interface CuerpoCinematico {
    fun posicion(): Vector2D
    fun velocidad(): Vector2D
    fun angulo(): Double
    fun velocidadAngular(): Double
}

interface EstadoProyectil {
    fun tipo(): TipoPelota
    fun forma(): FormaColision
    fun masa(): Double = tipo().masa()
    fun coeficienteRestitucion(): Double = tipo().coeficienteRestitucion()
    fun coeficienteFriccion(): Double = tipo().coeficienteFriccion()
    fun coeficienteArrastre(): Double = tipo().coeficienteArrastre()
    fun giro(): Double = tipo().giro()
    fun radio(): Double = forma().radioEnvoltura()
    fun estaEnSuelo(limiteY: Double): Boolean
}

interface ProyectilControlable {
    fun fijarPosicion(nueva: Vector2D)
    fun fijarVelocidad(nueva: Vector2D)
    fun fijarAngulo(nuevo: Double)
    fun fijarVelocidadAngular(nueva: Double)
    fun aplicarFuerza(fuerza: Vector2D, deltaTime: Double)
}

interface ProyectilVisual :
    CuerpoCinematico,
    EstadoProyectil

interface ProyectilFisico :
    ProyectilVisual,
    ProyectilControlable

interface Proyectil :
    ProyectilFisico
