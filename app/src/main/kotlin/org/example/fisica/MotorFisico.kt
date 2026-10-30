package org.example.fisica

import org.example.modelo.Pared
import org.example.modelo.Proyectil
import org.example.modelo.Vector2D
import org.example.modelo.colision.Transformaciones
import kotlin.math.ceil
import kotlin.math.exp

class MotorFisico(
    private var ambiente: Ambiente,
    paredes: List<Pared> = emptyList(),
    private val detectorReposo: DetectorDeReposo = FabricaDetectoresReposo.crear(ambiente)
) : PhysicsEngine {
    private val listaParedes = paredes.toMutableList()
    private val resolutor = ResolutorColisiones(listaParedes, ambiente)

    override fun sueloY(): Double = ambiente.sueloY()

    override fun techoY(): Double = ambiente.techoY()

    override fun agregarPared(pared: Pared) {
        listaParedes.add(pared)
    }

    override fun removerPared(pared: Pared): Boolean = listaParedes.remove(pared)

    override fun quitarPared(pared: Pared) {
        listaParedes.remove(pared)
    }

    override fun limpiarParedes() {
        listaParedes.clear()
    }

    override fun obtenerParedes(): List<Pared> = listaParedes.toList()

    override fun cambiarAmbiente(ambiente: Ambiente) {
        this.ambiente = ambiente
        detectorReposo.cambiarAmbiente(ambiente)
        resolutor.cambiarAmbiente(ambiente)
    }

    /** Avanza el proyectil y devuelve las paredes que ha roto en este paso. */
    override fun simularPaso(proyectil: Proyectil, deltaTime: Double): List<Pared> {
        if (deltaTime <= 0.0) return emptyList()

        val fuerzaTotal = ambiente.gravedad() * proyectil.masa() + fuerzaArrastre(proyectil)
        val velocidadTrasFuerza = proyectil.velocidad() + fuerzaTotal * deltaTime
        val desplazamiento = velocidadTrasFuerza.magnitud() * deltaTime
        val cantidadSubpasos = ceil(desplazamiento / MAXIMA_DISTANCIA_POR_SUBPASO)
            .toInt()
            .coerceIn(1, MAXIMOS_SUBPASOS)
        val deltaSubpaso = deltaTime / cantidadSubpasos

        val paredesRotas = mutableListOf<Pared>()
        repeat(cantidadSubpasos) {
            proyectil.aplicarFuerza(fuerzaTotal, deltaSubpaso)
            avanzarOrientacion(proyectil, deltaSubpaso)
            paredesRotas += resolutor.resolver(proyectil, deltaSubpaso)
        }
        return paredesRotas
    }

    /** Freno del aire: proporcional al cuadrado de la rapidez y opuesto al movimiento. */
    private fun fuerzaArrastre(p: Proyectil): Vector2D {
        if (p.coeficienteArrastre() <= 0.0) return Vector2D(0.0, 0.0)
        val rapidez = p.velocidad().magnitud()
        if (rapidez < UMBRAL_ARRASTRE) return Vector2D(0.0, 0.0)
        return p.velocidad() * (-p.coeficienteArrastre() * rapidez * p.masa())
    }

    /** Giro propio y orientacion aerodinamica de las formas alargadas. */
    private fun avanzarOrientacion(p: Proyectil, deltaTime: Double) {
        p.fijarAngulo(p.angulo() + p.velocidadAngular() * deltaTime)
        if (p.giro() <= 0.0 || !p.forma().alineable()) return
        if (p.velocidad().magnitud() < UMBRAL_ARRASTRE) return

        val tasa = p.giro() * TASA_ORIENTACION * deltaTime
        p.fijarVelocidadAngular(p.velocidadAngular() * exp(-tasa))
        val objetivo = Transformaciones.anguloDe(p.velocidad()) -
            Transformaciones.anguloDe(p.forma().ejePrincipal())
        p.fijarAngulo(p.angulo() + Transformaciones.diferenciaAngular(objetivo, p.angulo()) * tasa)
    }

    /**
     * El tiro ha terminado: lo decide [DetectorReposo]. Se conserva aqui porque
     * es parte del contrato del motor con quien lo usa.
     */
    override fun estaEnReposo(proyectil: Proyectil): Boolean =
        detectorReposo.estaEnReposo(proyectil, listaParedes)

    private companion object {
        const val MAXIMA_DISTANCIA_POR_SUBPASO = 4.0
        const val MAXIMOS_SUBPASOS = 256
        const val UMBRAL_ARRASTRE = 1.0
        const val TASA_ORIENTACION = 4.0
    }
}
