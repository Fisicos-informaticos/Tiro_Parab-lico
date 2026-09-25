package org.example

import org.example.fisica.MotorFisico
import org.example.fisica.Tierra
import org.example.modelo.PelotaGoma
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

    fun iniciar() {
        println("=== SIMULADOR DE TIRO PARABOLICO ===")
        println()

        do {
            println("--- Nueva simulacion ---")
            val v0 = leerDouble("Velocidad inicial (m/s)", 20.0)
            val angulo = leerDouble("Angulo de lanzamiento (grados)", 45.0)
            val posX = leerDouble("Posicion inicial X", 0.0)
            val posY = leerDouble("Posicion inicial Y", 599.0)
            val masa = leerDouble("Masa de la pelota (kg)", 0.5)
            val coeficiente = leerDouble("Coeficiente de restitucion", 0.8)

            val ambiente = Tierra()
            val motor = MotorFisico(ambiente)
            val pelota = PelotaGoma(Vector2D(posX, posY), masa, coeficiente)

            val rad = Math.toRadians(angulo)
            pelota.velocidad = Vector2D(cos(rad) * v0, -sin(rad) * v0)

            println("\n--- Resultados ---")
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
