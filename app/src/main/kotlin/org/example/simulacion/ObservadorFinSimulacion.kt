package org.example.simulacion

/**
 * Observador del final de un tiro (patron Observer). El nucleo avisa por esta
 * via en cuanto la pelota se detiene, sin conocer a nadie de la interfaz; la
 * presentacion que quiera actuar (abrir un menu de fin, registrar metricas...)
 * solo tiene que implementar esta interfaz y registrarse.
 */
interface ObservadorFinSimulacion {

    /** Se llama una sola vez por tiro, en el instante exacto en que termina. */
    fun alTerminarSimulacion(resultado: ResultadoSimulacion)
}
