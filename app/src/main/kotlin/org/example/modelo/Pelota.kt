package org.example.modelo

import org.example.modelo.colision.FormaColision

/**
 * Pelota generica. Su comportamiento depende del [TipoPelota] elegido y de la
 * forma con la que colisiona.
 */
class Pelota(
    override var posicion: Vector2D,
    override val tipo: TipoPelota,
    override val forma: FormaColision = tipo.crearForma(),
    override val masa: Double = tipo.masa,
    override val coeficienteRestitucion: Double = tipo.coeficienteRestitucion
) : Proyectil {
    override var velocidad = Vector2D(0.0, 0.0)
    override var angulo = 0.0
    override var velocidadAngular = 0.0
}
