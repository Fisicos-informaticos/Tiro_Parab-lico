package org.example.modelo

/**
 * Contrato comun de cualquier obstaculo del mundo. Permite que el motor y la
 * presentacion trabajen con la abstraccion y que otras implementaciones puedan
 * sustituir a [Pared] sin romper la simulacion (LSP).
 */
interface Obstaculo {
    fun inicio(): Vector2D
    fun fin(): Vector2D
    fun tipo(): TipoPared
    fun grosor(): Double
    fun danio(): Double

    fun registrarImpacto(energia: Double, tipoPelota: TipoPelota): Boolean
    fun puntoMasCercano(posicion: Vector2D): Vector2D
}
