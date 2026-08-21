package org.example

import kotlin.math.* // Los imports SIEMPRE van arriba

// 1. Modelos de datos
data class Vector2D(val x: Double, val y: Double) {
    operator fun plus(other: Vector2D) = Vector2D(x + other.x, y + other.y)
    operator fun times(scalar: Double) = Vector2D(x * scalar, y * scalar)
}

// 2. Abstracciones
interface Proyectil {
    var posicion: Vector2D
    var velocidad: Vector2D
    val masa: Double
    val coeficienteRestitucion: Double
    
    fun aplicarFuerza(fuerza: Vector2D, deltaTime: Double) {
        val aceleracion = fuerza * (1.0 / masa)
        velocidad += aceleracion * deltaTime
        posicion += velocidad * deltaTime
    }
}

class PelotaGoma(override var posicion: Vector2D) : Proyectil {
    override var velocidad = Vector2D(0.0, 0.0)
    override val masa = 0.5
    override val coeficienteRestitucion = 0.8
}

// 3. Ambiente
interface Ambiente {
    val gravedad: Vector2D
    val densidadAire: Double
}

class Tierra : Ambiente {
    override val gravedad = Vector2D(0.0, 9.81)
    override val densidadAire = 1.225
}

// 4. Motor Físico
class MotorFisico(private val ambiente: Ambiente) {
    fun simularPaso(proyectil: Proyectil, deltaTime: Double) {
        val fuerzaGravedad = ambiente.gravedad * proyectil.masa
        proyectil.aplicarFuerza(fuerzaGravedad, deltaTime)
        
        // Simular el suelo a Y = 600
        if (proyectil.posicion.y >= 600) {
            resolverColisionSuelo(proyectil)
        }
    }

    private fun resolverColisionSuelo(p: Proyectil) {
        p.velocidad = Vector2D(p.velocidad.x, -p.velocidad.y * p.coeficienteRestitucion)
        p.posicion = Vector2D(p.posicion.x, 599.0)
    }
}

// Extensiones para facilitar acceso
val Proyectil.x get() = posicion.x
val Proyectil.y get() = posicion.y

// 5. Clase principal (App)
class App {
    fun iniciar() {
        println("=== SIMULADOR DE TIRO PARABÓLICO ===")
        
        print("Velocidad inicial (m/s): ")
        val v0 = readlnOrNull()?.toDoubleOrNull() ?: 20.0
        print("Ángulo de lanzamiento (grados): ")
        val angulo = readlnOrNull()?.toDoubleOrNull() ?: 45.0

        val ambiente = Tierra()
        val motor = MotorFisico(ambiente)
        val pelota = PelotaGoma(Vector2D(0.0, 599.0))

        val rad = Math.toRadians(angulo)
        pelota.velocidad = Vector2D(cos(rad) * v0, -sin(rad) * v0)

        println("\nIniciando simulación...")
        var tiempo = 0.0
        repeat(30) {
            motor.simularPaso(pelota, 0.1)
            tiempo += 0.1
            println("Tiempo: ${"%.1f".format(tiempo)}s | Pos: (${"%.2f".format(pelota.x)}, ${"%.2f".format(pelota.y)})")
            
            if (pelota.y >= 599 && abs(pelota.velocidad.y) < 0.1) return@repeat
        }
    }
}

// El punto de entrada debe llamar a la clase o ejecutar el código
fun main() {
    App().iniciar()
}