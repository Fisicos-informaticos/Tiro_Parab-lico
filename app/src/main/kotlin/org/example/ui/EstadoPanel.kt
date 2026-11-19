package org.example.ui

/**
 * Instantanea de solo lectura del estado de la partida que el panel necesita
 * para decidir los textos y los bloqueos de sus controles. Evita que la vista
 * tenga que conocer la simulacion completa y encapsula sus propias reglas.
 */
class EstadoPanel(
    private val pausado: Boolean,
    private val enVuelo: Boolean,
    private val enPantallaInicio: Boolean,
    private val modoParedes: Boolean,
    private val modoSuperficies: Boolean,
    private val menuParedesAbierto: Boolean,
    private val menuPelotasAbierto: Boolean,
    private val menuFinAbierto: Boolean,
    private val nombreTipoPared: String,
    private val nombrePelota: String,
    private val factorGravedad: Double,
    private val nombreModo: String,
    private val ayudaModo: String,
    private val hayParedes: Boolean,
    private val haySuperficies: Boolean
) {
    fun pausado(): Boolean = pausado

    fun enVuelo(): Boolean = enVuelo

    fun enPantallaInicio(): Boolean = enPantallaInicio

    fun modoParedes(): Boolean = modoParedes

    fun modoSuperficies(): Boolean = modoSuperficies

    fun menuParedesAbierto(): Boolean = menuParedesAbierto

    fun menuPelotasAbierto(): Boolean = menuPelotasAbierto

    fun menuFinAbierto(): Boolean = menuFinAbierto

    fun nombreTipoPared(): String = nombreTipoPared

    fun nombrePelota(): String = nombrePelota

    fun factorGravedad(): Double = factorGravedad

    fun nombreModo(): String = nombreModo

    fun ayudaModo(): String = ayudaModo

    fun hayParedes(): Boolean = hayParedes

    fun haySuperficies(): Boolean = haySuperficies

    /** Con un menu abierto o en pausa el mundo no responde al raton. */
    fun interaccionBloqueada(): Boolean =
        pausado || menuPelotasAbierto || menuParedesAbierto || menuFinAbierto

    /** Los controles de edicion quedan inertes tambien mientras la pelota vuela. */
    fun bloqueado(): Boolean = interaccionBloqueada() || enVuelo

    /** Texto con dos decimales para mostrar la gravedad activa. */
    fun gravedadTexto(): String = String.format("%.2f", factorGravedad)
}
