package org.example.ui

class ConfiguracionPanelSimulacion(
    modos: List<String>,
    modoInicial: String,
    gravedades: List<String>,
    gravedadInicial: String
) {
    private val datos = DatosConfiguracionPanelSimulacion(modos, modoInicial, gravedades, gravedadInicial)

    fun modos(): List<String> = datos.modos

    fun gravedades(): List<String> = datos.gravedades

    fun modoInicialValido(): String = datos.modoInicialValido()

    fun gravedadInicialValida(): String = datos.gravedadInicialValida()

    fun describir(): String = datos.describir()

    private data class DatosConfiguracionPanelSimulacion(
        val modos: List<String>,
        val modoInicial: String,
        val gravedades: List<String>,
        val gravedadInicial: String
    ) {
        fun modoInicialValido(): String =
            if (modoInicial in modos) modoInicial else modos.firstOrNull().orEmpty()

        fun gravedadInicialValida(): String =
            if (gravedadInicial in gravedades) gravedadInicial else gravedades.firstOrNull().orEmpty()

        fun describir(): String = "modo=${modoInicialValido()} g=${gravedadInicialValida()}"

        fun descripcionDetallada(): String =
            "configuracion(modos=$modos, inicial=$modoInicial, gravedades=$gravedades, gravedadInicial=$gravedadInicial)"

        fun esValida(): Boolean =
            modos.isNotEmpty() && gravedades.isNotEmpty() && modoInicialValido().isNotBlank() && gravedadInicialValida().isNotBlank()

        fun resumen(): String =
            "${modoInicialValido()} de $modos | ${gravedadInicialValida()} de $gravedades"

        fun datosCompletos(): List<String> =
            modos + modoInicial + gravedades + gravedadInicial
    }
}
