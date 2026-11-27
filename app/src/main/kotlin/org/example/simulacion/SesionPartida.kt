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
    fabricaProyectiles: FabricaProyectiles
) : SesionJuego {
    private val datos = DatosSesionPartida(alto, tipoPelotaInicial, modoInicial, fabricaProyectiles)

    override fun estado(): EstadoSimulacion = datos.estado()

    override fun cambiarEstado(nuevo: EstadoSimulacion) = datos.cambiarEstado(nuevo)

    override fun pausado(): Boolean = datos.pausado()

    override fun pausar() = datos.pausar()

    override fun reanudar() = datos.reanudar()

    override fun alternarPausa(): Boolean = datos.alternarPausa()

    override fun tipoPelota(): TipoPelota = datos.tipoPelota()

    override fun factorGravedad(): Double = datos.factorGravedad()

    override fun modo(): ModoSimulacion = datos.modo()

    override fun pelota(): Proyectil = datos.pelota()

    override fun posicionInicial(): Vector2D = datos.posicionInicial()

    override fun posicionOrigen(): Vector2D = datos.posicionOrigen()

    override fun cambiarTipoPelota(tipo: TipoPelota) = datos.cambiarTipoPelota(tipo)

    override fun cambiarModo(nuevo: ModoSimulacion) = datos.cambiarModo(nuevo)

    override fun cambiarGravedad(nuevoFactor: Double) = datos.cambiarGravedad(nuevoFactor)

    override fun colocarPelota(posicion: Vector2D) = datos.colocarPelota(posicion)

    override fun reubicarPelota(posicion: Vector2D) = datos.reubicarPelota(posicion)

    override fun detenerPelota() = datos.detenerPelota()

    private data class DatosSesionPartida(
        val fabricaProyectiles: FabricaProyectiles,
        val puntoInicial: Vector2D,
        var estadoActual: EstadoSimulacion,
        var enPausa: Boolean,
        var tipoPelotaActual: TipoPelota,
        var factorGravedadActual: Double,
        var modoActual: ModoSimulacion,
        var proyectil: Proyectil,
        var origen: Vector2D
    ) {
        constructor(
            alto: Double,
            tipoPelotaInicial: TipoPelota,
            modoInicial: ModoSimulacion,
            fabricaProyectiles: FabricaProyectiles
        ) : this(
            fabricaProyectiles,
            Vector2D(50.0, alto - 60.0),
            EstadoSimulacion.PantallaInicio,
            false,
            tipoPelotaInicial,
            Gravedades.factorPorDefecto(),
            modoInicial,
            fabricaProyectiles.crear(Vector2D(50.0, alto - 60.0), tipoPelotaInicial),
            Vector2D(50.0, alto - 60.0)
        )

        fun estado(): EstadoSimulacion = estadoActual

        fun cambiarEstado(nuevo: EstadoSimulacion) {
            estadoActual = nuevo
        }

        fun pausado(): Boolean = enPausa

        fun pausar() {
            enPausa = true
        }

        fun reanudar() {
            enPausa = false
        }

        fun alternarPausa(): Boolean {
            enPausa = !enPausa
            return enPausa
        }

        fun tipoPelota(): TipoPelota = tipoPelotaActual

        fun factorGravedad(): Double = factorGravedadActual

        fun modo(): ModoSimulacion = modoActual

        fun pelota(): Proyectil = proyectil

        fun posicionInicial(): Vector2D = puntoInicial

        fun posicionOrigen(): Vector2D = origen

        fun cambiarTipoPelota(tipo: TipoPelota) {
            tipoPelotaActual = tipo
            recrearProyectilEnOrigen()
        }

        fun cambiarModo(nuevo: ModoSimulacion) {
            modoActual = nuevo
        }

        fun cambiarGravedad(nuevoFactor: Double) {
            factorGravedadActual = nuevoFactor
        }

        fun colocarPelota(posicion: Vector2D) {
            origen = posicion
            recrearProyectilEnOrigen()
        }

        fun reubicarPelota(posicion: Vector2D) {
            origen = posicion
            proyectil.fijarPosicion(posicion)
        }

        fun detenerPelota() {
            proyectil.fijarVelocidad(Vector2D(0.0, 0.0))
            proyectil.fijarAngulo(0.0)
            proyectil.fijarVelocidadAngular(0.0)
        }

        fun descripcion(): String =
            "sesion(estado=$estadoActual, pausa=$enPausa, pelota=$tipoPelotaActual, gravedad=$factorGravedadActual, modo=$modoActual, origen=$origen, inicial=$puntoInicial, proyectil=${proyectil.javaClass.simpleName})"

        fun esValida(): Boolean =
            factorGravedadActual > 0 && origen !== puntoInicial && tipoPelotaActual.javaClass !== modoActual.javaClass

        fun resumen(): String =
            "$estadoActual | $tipoPelotaActual | $modoActual | g=$factorGravedadActual | pausa=$enPausa | origen=$origen"

        fun estadoCompleto(): List<Any> = listOf(
            estadoActual,
            enPausa,
            tipoPelotaActual,
            factorGravedadActual,
            modoActual,
            proyectil,
            origen,
            puntoInicial
        )

        private fun recrearProyectilEnOrigen() {
            proyectil = fabricaProyectiles.crear(origen, tipoPelotaActual)
        }
    }
}
