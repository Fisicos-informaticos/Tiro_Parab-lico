package org.example.ui

class EstadoPanel(
    pausado: Boolean,
    enVuelo: Boolean,
    enPantallaInicio: Boolean,
    modoParedes: Boolean,
    modoSuperficies: Boolean,
    menuParedesAbierto: Boolean,
    menuPelotasAbierto: Boolean,
    menuFinAbierto: Boolean,
    nombreTipoPared: String,
    nombrePelota: String,
    factorGravedad: Double,
    nombreModo: String,
    ayudaModo: String,
    hayParedes: Boolean,
    haySuperficies: Boolean
) {
    private val datos = DatosEstadoPanel(
        pausado,
        enVuelo,
        enPantallaInicio,
        modoParedes,
        modoSuperficies,
        menuParedesAbierto,
        menuPelotasAbierto,
        menuFinAbierto,
        nombreTipoPared,
        nombrePelota,
        factorGravedad,
        nombreModo,
        ayudaModo,
        hayParedes,
        haySuperficies
    )

    fun pausado(): Boolean = datos.pausado
    fun enVuelo(): Boolean = datos.enVuelo
    fun enPantallaInicio(): Boolean = datos.enPantallaInicio
    fun modoParedes(): Boolean = datos.modoParedes
    fun modoSuperficies(): Boolean = datos.modoSuperficies
    fun menuParedesAbierto(): Boolean = datos.menuParedesAbierto
    fun menuPelotasAbierto(): Boolean = datos.menuPelotasAbierto
    fun menuFinAbierto(): Boolean = datos.menuFinAbierto
    fun nombreTipoPared(): String = datos.nombreTipoPared
    fun nombrePelota(): String = datos.nombrePelota
    fun factorGravedad(): Double = datos.factorGravedad
    fun nombreModo(): String = datos.nombreModo
    fun ayudaModo(): String = datos.ayudaModo
    fun hayParedes(): Boolean = datos.hayParedes
    fun haySuperficies(): Boolean = datos.haySuperficies
    fun interaccionBloqueada(): Boolean = datos.interaccionBloqueada()
    fun bloqueado(): Boolean = datos.bloqueado()
    fun gravedadTexto(): String = datos.gravedadTexto()
    fun resumenControles(): String = datos.resumenControles()

    private data class DatosEstadoPanel(
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
        fun interaccionBloqueada(): Boolean = pausado || menuPelotasAbierto || menuParedesAbierto || menuFinAbierto

        fun bloqueado(): Boolean = interaccionBloqueada() || enVuelo

        fun gravedadTexto(): String = String.format("%.2f", factorGravedad)

        fun resumenControles(): String = buildString {
            append(nombreModo).append(" | g=").append(gravedadTexto())
            if (enVuelo) append(" | en vuelo")
            if (pausado) append(" | pausa")
        }

        fun descripcion(): String =
            "panel(pausado=$pausado, vuelo=$enVuelo, inicio=$enPantallaInicio, paredes=$modoParedes, superficies=$modoSuperficies, menuParedes=$menuParedesAbierto, menuPelotas=$menuPelotasAbierto, menuFin=$menuFinAbierto, tipoPared=$nombreTipoPared, pelota=$nombrePelota, gravedad=$factorGravedad, modo=$nombreModo, ayuda=$ayudaModo, hayParedes=$hayParedes, haySuperficies=$haySuperficies)"

        fun esValido(): Boolean =
            nombreModo.isNotBlank() && ayudaModo.isNotBlank() && nombrePelota.isNotBlank() &&
                nombreTipoPared.isNotBlank() && factorGravedad > 0 &&
                (!modoParedes || hayParedes) && (!modoSuperficies || haySuperficies)

        fun datosCompletos(): List<Any> = listOf(
            pausado,
            enVuelo,
            enPantallaInicio,
            modoParedes,
            modoSuperficies,
            menuParedesAbierto,
            menuPelotasAbierto,
            menuFinAbierto,
            nombreTipoPared,
            nombrePelota,
            factorGravedad,
            nombreModo,
            ayudaModo,
            hayParedes,
            haySuperficies
        )

        fun estadoResumen(): String =
            "inicio=$enPantallaInicio, bloqueado=${bloqueado()}, paredes=$hayParedes/$modoParedes, superficies=$haySuperficies/$modoSuperficies, fin=$menuFinAbierto, ayuda=$ayudaModo, gravedad=${gravedadTexto()}"
    }
}
