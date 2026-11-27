package org.example.modelo

import org.example.modelo.colision.FormaColision

class Pelota(
    posicion: Vector2D,
    tipo: TipoPelota,
    forma: FormaColision = tipo.crearForma(),
    masa: Double = tipo.masa(),
    coeficienteRestitucion: Double = tipo.coeficienteRestitucion()
) : Proyectil {
    private val datos = DatosPelota(posicion, tipo, forma, masa, coeficienteRestitucion)

    override fun posicion(): Vector2D = datos.posicion()
    override fun velocidad(): Vector2D = datos.velocidad()
    override fun angulo(): Double = datos.angulo()
    override fun velocidadAngular(): Double = datos.velocidadAngular()
    override fun tipo(): TipoPelota = datos.tipo
    override fun forma(): FormaColision = datos.forma
    override fun masa(): Double = datos.masa
    override fun coeficienteRestitucion(): Double = datos.coeficienteRestitucion

    override fun fijarPosicion(nueva: Vector2D) = datos.fijarPosicion(nueva)

    override fun fijarVelocidad(nueva: Vector2D) = datos.fijarVelocidad(nueva)

    override fun fijarAngulo(nuevo: Double) = datos.fijarAngulo(nuevo)

    override fun fijarVelocidadAngular(nueva: Double) = datos.fijarVelocidadAngular(nueva)

    override fun aplicarFuerza(fuerza: Vector2D, deltaTime: Double) =
        datos.aplicarFuerza(fuerza, deltaTime)

    override fun estaEnSuelo(limiteY: Double): Boolean = datos.estaEnSuelo(limiteY)

    fun reiniciarEstadoEn(nuevaPosicion: Vector2D) = datos.reiniciarEstadoEn(nuevaPosicion)

    private data class DatosPelota(
        var posicion: Vector2D,
        val tipo: TipoPelota,
        val forma: FormaColision,
        val masa: Double,
        val coeficienteRestitucion: Double,
        var velocidad: Vector2D = Vector2D(0.0, 0.0),
        var angulo: Double = 0.0,
        var velocidadAngular: Double = 0.0
    ) {
        fun posicion(): Vector2D = posicion

        fun velocidad(): Vector2D = velocidad

        fun angulo(): Double = angulo

        fun velocidadAngular(): Double = velocidadAngular

        fun fijarPosicion(nueva: Vector2D) {
            posicion = nueva
        }

        fun fijarVelocidad(nueva: Vector2D) {
            velocidad = nueva
        }

        fun fijarAngulo(nuevo: Double) {
            angulo = nuevo
        }

        fun fijarVelocidadAngular(nueva: Double) {
            velocidadAngular = nueva
        }

        fun aplicarFuerza(fuerza: Vector2D, deltaTime: Double) {
            val aceleracion = fuerza * (1.0 / masa)
            velocidad += aceleracion * deltaTime
            posicion += velocidad * deltaTime
        }

        fun estaEnSuelo(limiteY: Double): Boolean = posicion.y() >= limiteY

        fun reiniciarEstadoEn(nuevaPosicion: Vector2D) {
            posicion = nuevaPosicion
            velocidad = Vector2D(0.0, 0.0)
            angulo = 0.0
            velocidadAngular = 0.0
        }

        fun descripcion(): String =
            "pelota(tipo=$tipo, forma=${forma.javaClass.simpleName}, masa=$masa, restitucion=$coeficienteRestitucion, posicion=$posicion, velocidad=$velocidad, angulo=$angulo, giro=$velocidadAngular)"

        fun esValida(): Boolean =
            masa > 0 && coeficienteRestitucion >= 0 && forma.radioEnvoltura() > 0 && velocidad.magnitud() >= 0

        fun resumen(): String =
            "$tipo ${posicion.x()},${posicion.y()} v=${velocidad.magnitud()} ang=$angulo giro=$velocidadAngular"

        fun estadoCompleto(): List<Any> =
            listOf(posicion, tipo, forma, masa, coeficienteRestitucion, velocidad, angulo, velocidadAngular)
    }
}
