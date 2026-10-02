package org.example

import org.example.fisica.MotorFisico
import org.example.fisica.Tierra
import org.example.modelo.Pelota
import org.example.modelo.TipoPelota
import org.example.modelo.Vector2D
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class App {
    private fun leerDouble(mensaje: String, valorDefecto: Double): Double {
        while (true) {
            print("$mensaje (valor por defecto: $valorDefecto): ")
            val entrada = readlnOrNull()?.trim()
            if (entrada.isNullOrEmpty()) {
                println("  Usando valor por defecto: $valorDefecto")
                return valorDefecto
            }
            val valor = entrada.toDoubleOrNull()
            if (valor != null && valor > 0) return valor
            println("  Error: '$entrada' no es un numero valido. Intenta de nuevo.")
        }
    }

    private fun elegirPelota(): TipoPelota {
        println("Tipos de pelota:")
        TipoPelota.entries.forEachIndexed { i, tipo ->
            println("  ${i + 1}) ${tipo.nombre} - ${tipo.descripcion}")
        }
        print("Pelota (1-${TipoPelota.entries.size}, por defecto ${TipoPelota.POR_DEFECTO.nombre}): ")
        val entrada = readlnOrNull()?.trim()
        val indice = entrada?.toIntOrNull()
        return TipoPelota.entries.getOrNull((indice ?: 1) - 1) ?: TipoPelota.POR_DEFECTO
    }

    fun iniciar() {
        println("=== SIMULADOR DE TIRO PARABOLICO ===")
        println("Pelotas disponibles: ${TipoPelota.entries.joinToString { it.nombre }}")
        println()

        do {
            println("--- Nueva simulacion ---")
            val v0 = leerDouble("Velocidad inicial (m/s)", 20.0)
            val angulo = leerDouble("Angulo de lanzamiento (grados)", 45.0)
            val posX = leerDouble("Posicion inicial X", 0.0)
            val posY = leerDouble("Posicion inicial Y", 599.0)
            val tipo = elegirPelota()
            val masa = leerDouble("Masa de la pelota (kg)", tipo.masa)
            val coeficiente = leerDouble("Coeficiente de restitucion", tipo.coeficienteRestitucion)

            val ambiente = Tierra()
            val motor = MotorFisico(ambiente)
            val pelota = Pelota(Vector2D(posX, posY), tipo, tipo.crearForma(), masa, coeficiente)

            val rad = Math.toRadians(angulo)
            pelota.velocidad = Vector2D(cos(rad) * v0, -sin(rad) * v0)

            println("\n--- Resultados ---")
            println("Pelota: ${tipo.nombre} (colision ${tipo.crearForma().tipo.etiqueta})")
            println("V0: ${"%.2f".format(v0)} m/s | Angulo: ${"%.1f".format(angulo)}° | Masa: ${"%.2f".format(masa)} kg")
            println()
            var tiempo = 0.0
            repeat(30) {
                motor.simularPaso(pelota, 0.1)
                tiempo += 0.1
                println("  t=${"%.1f".format(tiempo)}s  x=${"%.2f".format(pelota.posicion.x)}  y=${"%.2f".format(pelota.posicion.y)}")
                if (pelota.posicion.y >= 599 && abs(pelota.velocidad.y) < 0.1) return@repeat
            }
            println()

            print("Ejecutar otra simulacion? (s/n): ")
        } while (readlnOrNull()?.trim()?.lowercase() == "s")

        println("\nFin del programa.")
    }
}

fun main() {
    App().iniciar()
}
