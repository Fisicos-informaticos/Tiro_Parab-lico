package org.example.simulacion

/**
 * Acciones de presentacion que un estado puede solicitar sin conocer nada de
 * JavaFX ni de la implementacion concreta de la vista. Cada estado decide que
 * se pinta; la escena se limita a ejecutar esas peticiones.
 */
interface EscenaEstado {

    fun mostrarPantallaInicio(nombrePelota: String)

    fun mostrarPelotaEnEspera(simulacion: Simulacion)

    fun mostrarApuntado(simulacion: Simulacion)

    fun mostrarProyectil(simulacion: Simulacion)

    fun mostrarInformacion(simulacion: Simulacion)
}
