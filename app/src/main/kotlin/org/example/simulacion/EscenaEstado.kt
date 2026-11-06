package org.example.simulacion

/**
 * Acciones de presentacion que un estado puede solicitar sin conocer nada de
 * JavaFX ni de la implementacion concreta de la vista. Cada estado decide que
 * se pinta; la escena se limita a ejecutar esas peticiones.
 */
interface EscenaEstado {

    fun mostrarPantallaInicio(nombrePelota: String)

    fun mostrarPelotaEnEspera(simulacion: ContextoJuego)

    fun mostrarApuntado(simulacion: ContextoJuego)

    fun mostrarProyectil(simulacion: ContextoJuego)

    fun mostrarInformacion(simulacion: ContextoJuego)
}
