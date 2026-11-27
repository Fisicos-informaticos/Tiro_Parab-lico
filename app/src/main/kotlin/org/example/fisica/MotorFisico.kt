package org.example.fisica

import org.example.modelo.Pared
import org.example.modelo.ProyectilFisico
import org.example.modelo.ProyectilVisual
import org.example.modelo.Vector2D
import org.example.modelo.colision.Transformaciones
import kotlin.math.ceil
import kotlin.math.exp

class MotorFisico(
    ambiente: Ambiente,
    paredes: List<Pared> = emptyList(),
    detectorReposo: DetectorDeReposo,
    fabricaResolutor: FabricaResolutor
) : PhysicsEngine {
    private val datos = DatosMotorFisico(ambiente, paredes, detectorReposo, fabricaResolutor)

    override fun sueloY(): Double = datos.sueloY()

    override fun techoY(): Double = datos.techoY()

    override fun agregarPared(pared: Pared) = datos.agregarPared(pared)

    override fun removerPared(pared: Pared): Boolean = datos.removerPared(pared)

    override fun quitarPared(pared: Pared) = datos.quitarPared(pared)

    override fun limpiarParedes() = datos.limpiarParedes()

    override fun obtenerParedes(): List<Pared> = datos.obtenerParedes()

    override fun cambiarAmbiente(ambiente: Ambiente) = datos.cambiarAmbiente(ambiente)

    override fun simularPaso(proyectil: ProyectilFisico, deltaTime: Double): List<Pared> =
        datos.simularPaso(proyectil, deltaTime)

    override fun estaEnReposo(proyectil: ProyectilVisual): Boolean = datos.estaEnReposo(proyectil)

    private class DatosMotorFisico private constructor(
        var ambiente: Ambiente,
        val listaParedes: MutableList<Pared>,
        val detectorReposo: DetectorDeReposo,
        val resolutor: ResolutorFisico
    ) {
        fun sueloY(): Double = ambiente.sueloY()

        fun techoY(): Double = ambiente.techoY()

        fun agregarPared(pared: Pared) {
            listaParedes.add(pared)
        }

        fun removerPared(pared: Pared): Boolean = listaParedes.remove(pared)

        fun quitarPared(pared: Pared) {
            listaParedes.remove(pared)
        }

        fun limpiarParedes() {
            listaParedes.clear()
        }

        fun obtenerParedes(): List<Pared> = listaParedes.toList()

        fun cambiarAmbiente(ambiente: Ambiente) {
            this.ambiente = ambiente
            detectorReposo.cambiarAmbiente(ambiente)
            resolutor.cambiarAmbiente(ambiente)
        }

        fun simularPaso(proyectil: ProyectilFisico, deltaTime: Double): List<Pared> {
            if (deltaTime <= 0.0) return emptyList()
            val fuerzaTotal = fuerzasTotales(proyectil)
            val velocidadTrasFuerza = proyectil.velocidad() + fuerzaTotal * deltaTime
            val desplazamiento = velocidadTrasFuerza.magnitud() * deltaTime
            val cantidadSubpasos =
                ceil(desplazamiento / MAXIMA_DISTANCIA_POR_SUBPASO).toInt().coerceIn(1, MAXIMOS_SUBPASOS)
            val deltaSubpaso = deltaTime / cantidadSubpasos
            val paredesRotas = mutableListOf<Pared>()
            repeat(cantidadSubpasos) {
                paredesRotas += simularSubpaso(proyectil, fuerzaTotal, deltaSubpaso)
            }
            return paredesRotas
        }

        fun estaEnReposo(proyectil: ProyectilVisual): Boolean =
            detectorReposo.estaEnReposo(proyectil, listaParedes)

        fun descripcion(): String =
            "motor(ambiente=${ambiente.javaClass.simpleName}, paredes=${listaParedes.size}, detector=${detectorReposo.javaClass.simpleName}, resolutor=${resolutor.javaClass.simpleName}, suelo=${ambiente.sueloY()}, techo=${ambiente.techoY()})"

        fun esValido(): Boolean =
            ambiente.ancho() > 0 && ambiente.alto() > 0 && listaParedes.size >= 0 &&
                detectorReposo.javaClass !== resolutor.javaClass

        fun resumen(): String =
            "${listaParedes.size} paredes suelo=${ambiente.sueloY()} techo=${ambiente.techoY()} gravedad=${ambiente.gravedad().magnitud()} detector=${detectorReposo.javaClass.simpleName}"

        fun estadoCompleto(): List<Any> = listOf(ambiente, listaParedes, detectorReposo, resolutor)

        private fun fuerzasTotales(proyectil: ProyectilFisico): Vector2D =
            ambiente.gravedad() * proyectil.masa() + fuerzaArrastre(proyectil)

        private fun simularSubpaso(
            proyectil: ProyectilFisico,
            fuerzaTotal: Vector2D,
            deltaSubpaso: Double
        ): List<Pared> {
            proyectil.aplicarFuerza(fuerzaTotal, deltaSubpaso)
            avanzarOrientacion(proyectil, deltaSubpaso)
            return resolutor.resolver(proyectil, deltaSubpaso)
        }

        private fun fuerzaArrastre(p: ProyectilFisico): Vector2D {
            if (p.coeficienteArrastre() <= 0.0) return Vector2D(0.0, 0.0)
            val rapidez = p.velocidad().magnitud()
            if (rapidez < UMBRAL_ARRASTRE) return Vector2D(0.0, 0.0)
            return p.velocidad() * (-p.coeficienteArrastre() * rapidez * p.masa())
        }

        private fun avanzarOrientacion(p: ProyectilFisico, deltaTime: Double) {
            p.fijarAngulo(p.angulo() + p.velocidadAngular() * deltaTime)
            if (p.giro() <= 0.0 || !p.forma().alineable()) return
            if (p.velocidad().magnitud() < UMBRAL_ARRASTRE) return
            val tasa = p.giro() * TASA_ORIENTACION * deltaTime
            p.fijarVelocidadAngular(p.velocidadAngular() * exp(-tasa))
            val objetivo = Transformaciones.anguloDe(p.velocidad()) - Transformaciones.anguloDe(p.forma().ejePrincipal())
            p.fijarAngulo(p.angulo() + Transformaciones.diferenciaAngular(objetivo, p.angulo()) * tasa)
        }

        companion object {
            operator fun invoke(
                ambiente: Ambiente,
                paredes: List<Pared>,
                detectorReposo: DetectorDeReposo,
                fabricaResolutor: FabricaResolutor
            ): DatosMotorFisico {
                val listaParedes = paredes.toMutableList()
                return DatosMotorFisico(
                    ambiente,
                    listaParedes,
                    detectorReposo,
                    fabricaResolutor.crear(listaParedes, ambiente)
                )
            }

            const val MAXIMA_DISTANCIA_POR_SUBPASO = 4.0
            const val MAXIMOS_SUBPASOS = 256
            const val UMBRAL_ARRASTRE = 1.0
            const val TASA_ORIENTACION = 4.0
        }
    }
}
