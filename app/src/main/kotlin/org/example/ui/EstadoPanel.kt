package org.example.ui

/**
 * Instantanea de solo lectura del estado de la partida que el panel necesita
 * para decidir los textos y los bloqueos de sus controles. Evita que la vista
 * tenga que conocer la simulacion completa.
 */
data class EstadoPanel(
    val pausado: Boolean,
    val enVuelo: Boolean,
    val enPantallaInicio: Boolean,
    val modoParedes: Boolean,
    val modoSuperficies: Boolean,
    val menuParedesAbierto: Boolean,
    val menuPelotasAbierto: Boolean,
    val menuFinAbierto: Boolean,
    val nombreTipoPared: String,
    val nombrePelota: String,
    val factorGravedad: Double,
    val nombreModo: String,
    val ayudaModo: String,
    val hayParedes: Boolean,
    val haySuperficies: Boolean
) {

    /** Con un menu abierto o en pausa el mundo no responde al raton. */
    val interaccionBloqueada: Boolean
        get() = pausado || menuPelotasAbierto || menuParedesAbierto || menuFinAbierto

    /** Los controles de edicion quedan inertes tambien mientras la pelota vuela. */
    val bloqueado: Boolean
        get() = interaccionBloqueada || enVuelo
}
