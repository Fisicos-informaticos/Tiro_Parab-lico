package org.example.ui

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

    fun interaccionBloqueada(): Boolean = pausado || menuPelotasAbierto || menuParedesAbierto || menuFinAbierto

    fun bloqueado(): Boolean = interaccionBloqueada() || enVuelo

    fun gravedadTexto(): String = String.format("%.2f", factorGravedad)

    fun resumenControles(): String = buildString {
        append(nombreModo).append(" | g=").append(gravedadTexto())
        if (enVuelo) append(" | en vuelo")
        if (pausado) append(" | pausa")
    }
}
