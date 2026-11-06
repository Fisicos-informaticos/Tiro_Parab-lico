package org.example.modelo

import org.example.modelo.colision.FormaColision

/**
 * Pelota generica. Su comportamiento depende del [TipoPelota] elegido y de la
 * forma con la que colisiona. Toda su mecanica de movimiento vive aqui.
 */
class Pelota(
    private var posicion: Vector2D,
    private val tipo: TipoPelota,
    private val forma: FormaColision = tipo.crearForma(),
    private val masa: Double = tipo.masa(),
    private val coeficienteRestitucion: Double = tipo.coeficienteRestitucion()
) : Proyectil {
    private var velocidad = Vector2D(0.0, 0.0)
    private var angulo = 0.0
    private var velocidadAngular = 0.0

    override fun posicion(): Vector2D = posicion

    override fun velocidad(): Vector2D = velocidad

    override fun angulo(): Double = angulo

    override fun velocidadAngular(): Double = velocidadAngular

    override fun tipo(): TipoPelota = tipo

    override fun forma(): FormaColision = forma

    override fun masa(): Double = masa

    override fun coeficienteRestitucion(): Double = coeficienteRestitucion

    override fun fijarPosicion(nueva: Vector2D) {
        posicion = nueva
    }

    override fun fijarVelocidad(nueva: Vector2D) {
        velocidad = nueva
    }

    override fun fijarAngulo(nuevo: Double) {
        angulo = nuevo
    }

    override fun fijarVelocidadAngular(nueva: Double) {
        velocidadAngular = nueva
    }

    override fun aplicarFuerza(fuerza: Vector2D, deltaTime: Double) {
        val aceleracion = fuerza * (1.0 / masa)
        velocidad += aceleracion * deltaTime
        posicion += velocidad * deltaTime
    }

    override fun estaEnSuelo(limiteY: Double): Boolean = posicion.y() >= limiteY
}
