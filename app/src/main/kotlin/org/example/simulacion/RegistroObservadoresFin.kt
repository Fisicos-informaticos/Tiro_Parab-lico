package org.example.simulacion

/** Lista de interesados a los que se avisa cuando termina un tiro. */
class RegistroObservadoresFin : ObservadoresFin {

    private val observadores = mutableListOf<ObservadorFinSimulacion>()

    override fun agregar(observador: ObservadorFinSimulacion) {
        if (!observadores.contains(observador)) observadores.add(observador)
    }

    override fun quitar(observador: ObservadorFinSimulacion) {
        observadores.remove(observador)
    }

    override fun notificar(resultado: ResultadoSimulacion) {
        for (observador in observadores.toList()) {
            observador.alTerminarSimulacion(resultado)
        }
    }
}
