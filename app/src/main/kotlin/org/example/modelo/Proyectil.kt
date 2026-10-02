package org.example.modelo

import org.example.modelo.colision.FormaColision

interface Proyectil {
    var posicion: Vector2D
    var velocidad: Vector2D

    /** Rotacion del cuerpo en radianes (sentido antihorario en el plano de la pantalla). */
    var angulo: Double
    var velocidadAngular: Double

    val tipo: TipoPelota
    val forma: FormaColision

    val masa: Double get() = tipo.masa
    val coeficienteRestitucion: Double get() = tipo.coeficienteRestitucion
    val coeficienteFriccion: Double get() = tipo.coeficienteFriccion
    val coeficienteArrastre: Double get() = tipo.coeficienteArrastre
    val giro: Double get() = tipo.giro

    /** Radio del circulo que envuelve al proyectil. */
    val radio: Double get() = forma.radioEnvoltura

    fun aplicarFuerza(fuerza: Vector2D, deltaTime: Double) {
        val aceleracion = fuerza * (1.0 / masa)
        velocidad += aceleracion * deltaTime
        posicion += velocidad * deltaTime
    }

    fun estaEnSuelo(limiteY: Double): Boolean = posicion.y >= limiteY
}
