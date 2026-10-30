package org.example.simulacion

import org.example.coordenadas.Camara
import org.example.entrada.InputHandler
import org.example.modelo.Pelota
import org.example.modelo.Vector2D
import kotlin.math.exp

/** Sigue al proyectil con suavizado y decide cuando la camara debe quedarse quieta. */
class ControladorCamara(
    private val camara: Camara,
    private val ancho: Double,
    private val alto: Double
) {

    fun reiniciar() {
        camara.fijarOffsetCamara(Vector2D(0.0, 0.0))
    }

    fun actualizar(deltaTime: Double, entrada: InputHandler, pelota: Pelota) {
        // Mientras se arrastra, el mapa quieto es lo que mantiene sincronizados
        // el puntero y la pelota (decisivo en caida libre, que la mueve).
        if (entrada.estaArrastrando()) return

        val objetivoX = pelota.posicion().x() - ancho * ANCHOR_CAMARA
        val objetivoY = pelota.posicion().y() - alto * ALTURA_CAMARA
        val factor = 1.0 - exp(-VELOCIDAD_CAMARA * deltaTime)
        val anterior = camara.offsetCamara()
        camara.fijarOffsetCamara(
            Vector2D(
                anterior.x() + (objetivoX - anterior.x()) * factor,
                anterior.y() + (objetivoY - anterior.y()) * factor
            )
        )
    }

    private companion object {
        const val VELOCIDAD_CAMARA = 6.0
        const val ANCHOR_CAMARA = 0.4
        const val ALTURA_CAMARA = 0.45
    }
}
