package org.example.simulacion

/** Catalogo de gravedades seleccionables, expresadas como multiplicador de la terrestre. */
object Gravedades {

    const val POR_DEFECTO = "Tierra (1.00 g)"

    private val FACTORES = linkedMapOf(
        "Sin gravedad" to 0.0,
        "Sol (28.0 g)" to 27.94,
        "Mercurio (0.38 g)" to 0.377,
        "Venus (0.90 g)" to 0.904,
        POR_DEFECTO to 1.0,
        "Luna (0.17 g)" to 0.166,
        "Marte (0.38 g)" to 0.379,
        "Jupiter (2.53 g)" to 2.534,
        "Saturno (1.07 g)" to 1.064,
        "Urano (0.89 g)" to 0.886,
        "Neptuno (1.14 g)" to 1.137
    )

    fun nombres(): List<String> = FACTORES.keys.toList()

    fun factorDe(nombre: String?): Double? = FACTORES[nombre]

    fun factorPorDefecto(): Double = FACTORES[POR_DEFECTO] ?: 1.0
}
