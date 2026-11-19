package org.example

class EntradaConsola {

    fun <T> elegir(que: String, opciones: List<T>, texto: (T) -> String): T {
        println("$que disponibles:")
        opciones.forEachIndexed { indice, opcion ->
            println("  ${indice + 1}) ${texto(opcion)}")
        }
        print("$que (1-${opciones.size}): ")
        val indice = readlnOrNull()?.trim()?.toIntOrNull() ?: 1
        return opciones.getOrNull(indice - 1) ?: opciones.first()
    }

    fun leerDouble(mensaje: String, valorDefecto: Double): Double {
        print("$mensaje (valor por defecto: $valorDefecto): ")
        val entrada = readlnOrNull()?.trim()
        return entrada?.toDoubleOrNull() ?: valorDefecto
    }

    fun preguntar(mensaje: String): Boolean {
        print(mensaje)
        return readlnOrNull()?.trim()?.lowercase() == "s"
    }
}
