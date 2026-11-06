package org.example.simulacion

import org.example.modelo.Pelota
import org.example.modelo.TipoPelota
import org.example.modelo.Vector2D
import org.example.modos.ModoSimulacion

/**
 * Modelo de dominio de una partida. Es dueno de su estado: nadie de fuera puede
 * leer ni alterar los campos directamente, solo pedirle las transiciones que
 * tienen sentido. Asi la logica de colocar, mover o detener la pelota vive junto
 * a los datos que modifica.
 */
class SesionPartida(
    alto: Double,
    tipoPelotaInicial: TipoPelota,
    modoInicial: ModoSimulacion
) : SesionJuego {
    private val puntoInicial: Vector2D = Vector2D(50.0, alto - 60.0)

    private var estadoActual: EstadoSimulacion = EstadoSimulacion.PantallaInicio
    private var enPausa: Boolean = false
    private var tipoPelotaActual: TipoPelota = tipoPelotaInicial
    private var factorGravedadActual: Double = Gravedades.factorPorDefecto()
    private var modoActual: ModoSimulacion = modoInicial
    private var proyectil: Pelota = Pelota(puntoInicial, tipoPelotaInicial)
    private var origen: Vector2D = puntoInicial

    override fun estado(): EstadoSimulacion = estadoActual

    override fun cambiarEstado(nuevo: EstadoSimulacion) {
        estadoActual = nuevo
    }

    override fun pausado(): Boolean = enPausa

    override fun pausar() {
        enPausa = true
    }

    override fun reanudar() {
        enPausa = false
    }

    /** Cambia la pausa y devuelve el estado resultante. */
    override fun alternarPausa(): Boolean {
        enPausa = !enPausa
        return enPausa
    }

    override fun tipoPelota(): TipoPelota = tipoPelotaActual

    override fun factorGravedad(): Double = factorGravedadActual

    override fun modo(): ModoSimulacion = modoActual

    override fun pelota(): Pelota = proyectil

    override fun posicionInicial(): Vector2D = puntoInicial

    override fun posicionOrigen(): Vector2D = origen

    /** Cambia el material y deja una pelota nueva lista en el origen. */
    override fun cambiarTipoPelota(tipo: TipoPelota) {
        tipoPelotaActual = tipo
        proyectil = Pelota(origen, tipo)
    }

    override fun cambiarModo(nuevo: ModoSimulacion) {
        modoActual = nuevo
    }

    override fun cambiarGravedad(nuevoFactor: Double) {
        factorGravedadActual = nuevoFactor
    }

    /** Coloca una pelota nueva en el punto de salida elegido. */
    override fun colocarPelota(posicion: Vector2D) {
        origen = posicion
        proyectil = Pelota(posicion, tipoPelotaActual)
    }

    /** Desplaza la pelota y su origen conservando el cuerpo actual. */
    override fun reubicarPelota(posicion: Vector2D) {
        origen = posicion
        proyectil.fijarPosicion(posicion)
    }

    /** Deja la pelota quieta y orientada, lista para un nuevo tiro. */
    override fun detenerPelota() {
        proyectil.fijarVelocidad(Vector2D(0.0, 0.0))
        proyectil.fijarAngulo(0.0)
        proyectil.fijarVelocidadAngular(0.0)
    }
}
