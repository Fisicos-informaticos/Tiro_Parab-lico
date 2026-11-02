package org.example.simulacion

import org.example.modelo.Pelota
import org.example.modelo.TipoPelota
import org.example.modelo.Vector2D
import org.example.modos.ModoSimulacion

/** Estado mutable de una partida, compartido por los colaboradores del nucleo. */
class SesionPartida(
    alto: Double,
    tipoPelotaInicial: TipoPelota,
    modoInicial: ModoSimulacion
) {
    val posicionInicial: Vector2D = Vector2D(50.0, alto - 60.0)

    var estado: EstadoSimulacion = EstadoSimulacion.PantallaInicio
    var pausado: Boolean = false
    var tipoPelota: TipoPelota = tipoPelotaInicial
    var factorGravedad: Double = Gravedades.factorPorDefecto()
    var modo: ModoSimulacion = modoInicial
    var pelota: Pelota = Pelota(posicionInicial, tipoPelota)
    var posicionOrigen: Vector2D = posicionInicial
}
