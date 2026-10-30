package org.example.simulacion

/** Lista de interesados a los que se avisa cuando termina un tiro. */
class RegistroObservadoresFin {

    private val observadores = mutableListOf<ObservadorFinSimulacion>()

    fun agregar(observador: ObservadorFinSimulacion) {
        if (!observadores.contains(observador)) observadores.add(observador)
    }

    fun quitar(observador: ObservadorFinSimulacion) {
        observadores.remove(observador)
    }

    fun notificar(resultado: ResultadoSimulacion) {
        for (observador in observadores.toList()) {
            observador.alTerminarSimulacion(resultado)
        }
    }
}
