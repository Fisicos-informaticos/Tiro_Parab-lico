package org.example.simulacion

import org.example.modelo.Proyectil
import org.example.modelo.TipoPelota
import org.example.modelo.Vector2D
import org.example.modelo.creadores.FabricaProyectiles
import org.example.modos.ModoSimulacion

class SesionPartida(
    alto: Double,
    tipoPelotaInicial: TipoPelota,
    modoInicial: ModoSimulacion,
    private val fabricaProyectiles: FabricaProyectiles
) : SesionJuego {
    private val puntoInicial: Vector2D = Vector2D(50.0, alto - 60.0)
    private var estadoActual: EstadoSimulacion = EstadoSimulacion.PantallaInicio
    private var enPausa: Boolean = false
    private var tipoPelotaActual: TipoPelota = tipoPelotaInicial
    private var factorGravedadActual: Double = Gravedades.factorPorDefecto()
    private var modoActual: ModoSimulacion = modoInicial
    private var proyectil: Proyectil = fabricaProyectiles.crear(puntoInicial, tipoPelotaInicial)
    private var origen: Vector2D = puntoInicial

    override fun estado(): EstadoSimulacion = estadoActual
    override fun cambiarEstado(nuevo: EstadoSimulacion) { estadoActual = nuevo }
    override fun pausado(): Boolean = enPausa
    override fun pausar() { enPausa = true }
    override fun reanudar() { enPausa = false }
    override fun alternarPausa(): Boolean { enPausa = !enPausa; return enPausa }
    override fun tipoPelota(): TipoPelota = tipoPelotaActual
    override fun factorGravedad(): Double = factorGravedadActual
    override fun modo(): ModoSimulacion = modoActual
    override fun pelota(): Proyectil = proyectil
    override fun posicionInicial(): Vector2D = puntoInicial
    override fun posicionOrigen(): Vector2D = origen

    private fun recrearProyectilEnOrigen() {
        proyectil = fabricaProyectiles.crear(origen, tipoPelotaActual)
    }

    override fun cambiarTipoPelota(tipo: TipoPelota) {
        tipoPelotaActual = tipo
        recrearProyectilEnOrigen()
    }
    override fun cambiarModo(nuevo: ModoSimulacion) { modoActual = nuevo }
    override fun cambiarGravedad(nuevoFactor: Double) { factorGravedadActual = nuevoFactor }

    override fun colocarPelota(posicion: Vector2D) {
        origen = posicion
        recrearProyectilEnOrigen()
    }

    override fun reubicarPelota(posicion: Vector2D) {
        origen = posicion
        proyectil.fijarPosicion(posicion)
    }

    override fun detenerPelota() {
        proyectil.fijarVelocidad(Vector2D(0.0, 0.0))
        proyectil.fijarAngulo(0.0)
        proyectil.fijarVelocidadAngular(0.0)
    }
}
