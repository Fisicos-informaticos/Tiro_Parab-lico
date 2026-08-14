package org.example

fun main() {
    println("=== SIMULADOR DE TIRO PARABOLICO ===")
    print("Velocidad inicial (m/s): ")
    val v0 = readln().trim().toDoubleOrNull()
    print("Angulo de lanzamiento (grados): ")
    val angulo = readln().trim().toDoubleOrNull()

    if (v0 == null || angulo == null || v0 <= 0) {
        println("Entrada invalida. Usa numeros positivos.")
        return
    }

    val proyeccion = ProjectileMotion(v0, angulo)
    println(proyeccion.datosCompletos())

    println("\nTrayectoria (x, y):")
    proyeccion.trayectoria(10).forEachIndexed { i, (x, y) ->
        println("t=${"%.2f".format(proyeccion.tiempoDeVuelo * i / 10)}s -> (${"%.2f".format(x)}, ${"%.2f".format(y)})")
    }
}
