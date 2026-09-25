package org.example.modelo

interface Proyectil {
    var posicion: Vector2D
    var velocidad: Vector2D
    val masa: Double
    val coeficienteRestitucion: Double
    val radio: Double

    fun aplicarFuerza(fuerza: Vector2D, deltaTime: Double) {
        val aceleracion = fuerza * (1.0 / masa)
        velocidad += aceleracion * deltaTime
        posicion += velocidad * deltaTime
    }

    fun estaEnSuelo(limiteY: Double): Boolean = posicion.y >= limiteY
}
