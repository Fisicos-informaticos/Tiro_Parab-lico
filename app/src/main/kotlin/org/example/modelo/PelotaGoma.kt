package org.example.modelo

class PelotaGoma(
    override var posicion: Vector2D,
    override val masa: Double = 0.5,
    override val coeficienteRestitucion: Double = 0.8,
    override val radio: Double = 10.0,
    var spritePath: String? = null
) : Proyectil {
    override var velocidad = Vector2D(0.0, 0.0)
}
