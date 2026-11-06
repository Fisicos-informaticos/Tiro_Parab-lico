package org.example.simulacion

import org.example.entrada.InputHandler
import org.example.modelo.Pelota
import org.example.modelo.TipoPelota
import org.example.modelo.Vector2D

/** Reglas de colocacion, apuntado, lanzamiento y vuelo de la pelota. */
class MecanicaTiro(
    private val sesion: SesionJuego,
    private val entrada: InputHandler,
    private val metricas: Metricas,
    private val integrador: Integrador,
    private val posicion: UbicadorPelota,
) : Mecanica {
    override fun iniciarApuntado(punto: Vector2D) = entrada.iniciarArrastre(punto)

    override fun colocarPelotaParaLanzar(punto: Vector2D) {
        val destino = sesion.modo().colocar(posicion.preparacionPara(posicion.aMundo(punto))) ?: return
        sesion.colocarPelota(destino)
        entrada.iniciarArrastre(punto)
        sesion.cambiarEstado(EstadoSimulacion.Arrojando)
    }

    override fun continuarApuntado(punto: Vector2D) {
        if (!entrada.estaArrastrando()) return
        entrada.actualizarArrastre(punto)
        val nuevaPosicion = sesion.modo().arrastrar(posicion.preparacionPara(posicion.aMundo(punto))) ?: return
        sesion.reubicarPelota(nuevaPosicion)
    }

    override fun lanzarPelota() {
        if (!entrada.estaArrastrando()) return
        val velocidad = sesion.modo().velocidadDeSuelta(entrada)
        if (velocidad == null) {
            entrada.reiniciar()
            return
        }
        val pelota = sesion.pelota()
        pelota.fijarVelocidad(velocidad)
        pelota.fijarPosicion(sesion.posicionOrigen())
        metricas.reiniciarTiro()
        metricas.registrarAltura(posicion.alturaActual())
        sesion.cambiarEstado(EstadoSimulacion.EnVuelo)
        entrada.reiniciar()
    }

    override fun avanzarVuelo(deltaTime: Double): Boolean =
        integrador.avanzar(
            sesion.pelota(), deltaTime, sesion.posicionOrigen()
        ) { posicion.alturaActual() }

    override fun reiniciar() {
        posicion.apoyarEnElSueloOSuperficie()
        sesion.reubicarPelota(sesion.posicionOrigen())
        sesion.detenerPelota()
        entrada.reiniciar()
        metricas.reiniciarTiro()
        sesion.cambiarEstado(EstadoSimulacion.Inactivo)
    }

    override fun cambiarPelota(tipo: TipoPelota) {
        sesion.cambiarTipoPelota(tipo)
        posicion.apoyarEnElSueloOSuperficie()
        reiniciar()
    }
}
