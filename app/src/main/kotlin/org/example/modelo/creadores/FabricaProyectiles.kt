package org.example.modelo.creadores

import org.example.modelo.Pelota
import org.example.modelo.Proyectil
import org.example.modelo.TipoPelota
import org.example.modelo.Vector2D

interface FabricaProyectiles {
    fun crear(posicion: Vector2D, tipo: TipoPelota): Proyectil
}

class FabricaProyectilesPredeterminada : FabricaProyectiles {
    override fun crear(posicion: Vector2D, tipo: TipoPelota): Proyectil =
        Pelota(posicion, tipo)
}
