package org.example.modos

/**
 * Registro de los modos disponibles. La interfaz pide solo nombres de texto
 * (combo box), de modo que aqui es donde se traduce cada nombre a su modo.
 * Anadir un modo es declarar su fabrica aqui, sin tocar el resto del registro.
 */
object CatalogoModos : FabricaModos {

    const val TIRO_PARABOLICO = "Tiro parabólico"
    const val CAIDA_LIBRE = "Caída libre"
    const val POR_DEFECTO = TIRO_PARABOLICO

    private val fabricas = linkedMapOf<String, () -> ModoSimulacion>(
        TIRO_PARABOLICO to { ModoTiroParabolico() },
        CAIDA_LIBRE to { ModoCaidaLibre() }
    )

    /** Nombres en el orden en el que los muestra el selector. */
    override fun nombres(): List<String> = fabricas.keys.toList()

    override fun crear(nombre: String?): ModoSimulacion =
        fabricas[nombre]?.invoke() ?: porDefecto()

    override fun porDefecto(): ModoSimulacion = fabricas.getValue(POR_DEFECTO)()
}
