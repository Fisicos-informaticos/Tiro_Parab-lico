package org.example.modelo.creadores

import org.example.modelo.Pared
import org.example.modelo.TipoPared

/**
 * Interfaz para crear y gestionar paredes en el mundo.
 * Sigue DIP: las clases de UI/controladores dependen de esta abstracción.
 */
interface CreadorParedes {
    fun paredes(): List<Pared>
    fun agregarPared(pared: Pared)
    fun limpiarParedes()
    fun seleccionarTipoPared(tipo: TipoPared)
    fun tipoParedActual(): TipoPared
}
