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
    private val detectorReposo: DetectorDeReposo,
    fabricaResolutor: FabricaResolutor
) : PhysicsEngine {
    private val listaParedes = paredes.toMutableList()
    private val resolutor: ResolutorFisico = fabricaResolutor.crear(listaParedes, ambiente)

    override fun sueloY(): Double = ambiente.sueloY()
    override fun techoY(): Double = ambiente.techoY()
    override fun agregarPared(pared: Pared) { listaParedes.add(pared) }
    override fun removerPared(pared: Pared): Boolean = listaParedes.remove(pared)
    override fun quitarPared(pared: Pared) { listaParedes.remove(pared) }
    override fun limpiarParedes() { listaParedes.clear() }
    override fun obtenerParedes(): List<Pared> = listaParedes.toList()

    override fun cambiarAmbiente(ambiente: Ambiente) {
        this.ambiente = ambiente
        detectorReposo.cambiarAmbiente(ambiente)
        resolutor.cambiarAmbiente(ambiente)
    }

    override fun simularPaso(proyectil: Proyectil, deltaTime: Double): List<Pared> {
        if (deltaTime <= 0.0) return emptyList()
        val fuerzaTotal = fuerzasTotales(proyectil)
        val velocidadTrasFuerza = proyectil.velocidad() + fuerzaTotal * deltaTime
        val desplazamiento = velocidadTrasFuerza.magnitud() * deltaTime
        val cantidadSubpasos = ceil(desplazamiento / MAXIMA_DISTANCIA_POR_SUBPASO).toInt().coerceIn(1, MAXIMOS_SUBPASOS)
        val deltaSubpaso = deltaTime / cantidadSubpasos
        val paredesRotas = mutableListOf<Pared>()
        repeat(cantidadSubpasos) {
            paredesRotas += simularSubpaso(proyectil, fuerzaTotal, deltaSubpaso)
        }
        return paredesRotas
    }

    private fun fuerzasTotales(proyectil: Proyectil): Vector2D = ambiente.gravedad() * proyectil.masa() + fuerzaArrastre(proyectil)

    private fun simularSubpaso(proyectil: Proyectil, fuerzaTotal: Vector2D, deltaSubpaso: Double): List<Pared> {
        proyectil.aplicarFuerza(fuerzaTotal, deltaSubpaso)
        avanzarOrientacion(proyectil, deltaSubpaso)
        return resolutor.resolver(proyectil, deltaSubpaso)
    }

    private fun fuerzaArrastre(p: Proyectil): Vector2D {
        if (p.coeficienteArrastre() <= 0.0) return Vector2D(0.0, 0.0)
        val rapidez = p.velocidad().magnitud()
        if (rapidez < UMBRAL_ARRASTRE) return Vector2D(0.0, 0.0)
        return p.velocidad() * (-p.coeficienteArrastre() * rapidez * p.masa())
    }

    private fun avanzarOrientacion(p: Proyectil, deltaTime: Double) {
        p.fijarAngulo(p.angulo() + p.velocidadAngular() * deltaTime)
        if (p.giro() <= 0.0 || !p.forma().alineable()) return
        if (p.velocidad().magnitud() < UMBRAL_ARRASTRE) return
        val tasa = p.giro() * TASA_ORIENTACION * deltaTime
        p.fijarVelocidadAngular(p.velocidadAngular() * exp(-tasa))
        val objetivo = Transformaciones.anguloDe(p.velocidad()) - Transformaciones.anguloDe(p.forma().ejePrincipal())
        p.fijarAngulo(p.angulo() + Transformaciones.diferenciaAngular(objetivo, p.angulo()) * tasa)
    }

    override fun estaEnReposo(proyectil: Proyectil): Boolean = detectorReposo.estaEnReposo(proyectil, listaParedes)

    private companion object {
        const val MAXIMA_DISTANCIA_POR_SUBPASO = 4.0
        const val MAXIMOS_SUBPASOS = 256
        const val UMBRAL_ARRASTRE = 1.0
        const val TASA_ORIENTACION = 4.0
    }
}
