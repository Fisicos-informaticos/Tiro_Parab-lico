package org.example.simulacion

import org.example.entrada.InputHandler
import org.example.modelo.Pelota
import org.example.modelo.TipoPelota
import org.example.modelo.Vector2D

/** Reglas de colocacion, apuntado, lanzamiento y vuelo de la pelota. */
class MecanicaTiro(
    private val sesion: SesionPartida,
    private val entrada: InputHandler,
    private val metricas: MetricasTiro,
    private val integrador: IntegradorVuelo,
    private val posicion: PosicionPelota,
    private val alTerminarVuelo: () -> Unit
) {
    fun iniciarApuntado(punto: Vector2D) = entrada.iniciarArrastre(punto)

    fun colocarPelotaParaLanzar(punto: Vector2D) {
        val destino = sesion.modo.colocar(posicion.preparacionPara(posicion.aMundo(punto))) ?: return
        sesion.posicionOrigen = destino
        sesion.pelota = Pelota(destino, sesion.tipoPelota)
        entrada.iniciarArrastre(punto)
        sesion.estado = EstadoSimulacion.Arrojando
    }

    fun continuarApuntado(punto: Vector2D) {
        if (!entrada.estaArrastrando()) return
        entrada.actualizarArrastre(punto)
        val nuevaPosicion = sesion.modo.arrastrar(posicion.preparacionPara(posicion.aMundo(punto))) ?: return
        sesion.posicionOrigen = nuevaPosicion
        sesion.pelota.fijarPosicion(nuevaPosicion)
    }

    fun lanzarPelota() {
        if (!entrada.estaArrastrando()) return
        val velocidad = sesion.modo.velocidadDeSuelta(entrada)
        if (velocidad == null) {
            entrada.reiniciar()
            return
        }
        sesion.pelota.fijarVelocidad(velocidad)
        sesion.pelota.fijarPosicion(sesion.posicionOrigen)
        metricas.reiniciarTiro()
        metricas.registrarAltura(posicion.alturaActual())
        sesion.estado = EstadoSimulacion.EnVuelo
        entrada.reiniciar()
    }

    fun avanzarVuelo(deltaTime: Double) {
        val terminado = integrador.avanzar(
            sesion.pelota, deltaTime, sesion.posicionOrigen
        ) { posicion.alturaActual() }
        if (terminado) alTerminarVuelo()
    }

    fun reiniciar() {
        posicion.apoyarEnElSueloOSuperficie()
        sesion.pelota.fijarPosicion(sesion.posicionOrigen)
        sesion.pelota.fijarVelocidad(Vector2D(0.0, 0.0))
        sesion.pelota.fijarAngulo(0.0)
        sesion.pelota.fijarVelocidadAngular(0.0)
        entrada.reiniciar()
        metricas.reiniciarTiro()
        sesion.estado = EstadoSimulacion.Inactivo
    }

    fun cambiarPelota(tipo: TipoPelota) {
        sesion.tipoPelota = tipo
        sesion.pelota = Pelota(sesion.posicionOrigen, tipo)
        posicion.apoyarEnElSueloOSuperficie()
        reiniciar()
    }
}
