package org.example.ui

class ConfiguracionPanelSimulacion(
    private val modos: List<String>,
    private val modoInicial: String,
    private val gravedades: List<String>,
    private val gravedadInicial: String
) {
    fun modos(): List<String> = modos
    fun gravedades(): List<String> = gravedades
    fun modoInicialValido(): String = if (modoInicial in modos) modoInicial else modos.firstOrNull().orEmpty()
    fun gravedadInicialValida(): String = if (gravedadInicial in gravedades) gravedadInicial else gravedades.firstOrNull().orEmpty()

    fun describir(): String = "modo=${modoInicialValido()} g=${gravedadInicialValida()}"
}
