package org.example.simulacion

import org.example.entrada.InputHandler
import org.example.modelo.Pared
import org.example.modelo.Pelota
import org.example.modelo.TipoPelota
import org.example.modelo.TipoPared
import org.example.modelo.Vector2D
import org.example.modos.LecturaModo
import org.example.modos.ModoSimulacion

/**
 * Vista de solo lectura y comandos que necesitan los estados y la presentacion.
 * Vivir detras de una interfaz evita que las reglas de estado y los dibujantes
 * queden atados a la clase concreta [Simulacion].
 */
interface ContextoJuego {
    fun ancho(): Double
    fun alto(): Double
    fun entrada(): InputHandler
    fun estado(): EstadoSimulacion
    fun pausado(): Boolean
    fun modoParedes(): Boolean
    fun modoSuperficies(): Boolean
    fun tipoPelota(): TipoPelota
    fun tipoPared(): TipoPared
    fun factorGravedad(): Double
    fun modo(): ModoSimulacion
    fun nombresModos(): List<String>
    fun pelota(): Pelota
    fun offsetCamara(): Vector2D
    fun tiempoVuelo(): Double
    fun distanciaRecorrida(): Double
    fun alturaMaxima(): Double
    fun distanciaX(): Double
    fun paredesDestruidas(): Int
    fun paredTemporal(): Pared?
    fun paredes(): List<Pared>
    fun superficiesLanzamientoLista(): List<Pared>
    fun efectos(): List<EfectoRotura>
    fun sueloY(): Double
    fun gravedad(): Vector2D
    fun actualizar(deltaTime: Double)
    fun pulsar(punto: Vector2D)
    fun arrastrar(punto: Vector2D)
    fun soltar(punto: Vector2D)
    fun cambiarA(nuevo: EstadoSimulacion)
    fun colocarPelotaParaLanzar(punto: Vector2D)
    fun iniciarApuntado(punto: Vector2D)
    fun continuarApuntado(punto: Vector2D)
    fun lanzarPelota()
    fun avanzarVuelo(deltaTime: Double)
    fun alternarPausa()
    fun alternarModoParedes()
    fun alternarModoSuperficies()
    fun agregarSuperficieLanzamiento(pared: Pared)
    fun limpiarSuperficiesLanzamiento()
    fun seleccionarPelota(tipo: TipoPelota)
    fun seleccionarTipoPared(tipo: TipoPared)
    fun aplicarGravedad(nombre: String?)
    fun seleccionarModo(nombre: String?)
    fun agregarPared(pared: Pared)
    fun limpiarParedes()
    fun abrirEdicion()
    fun reiniciar()
    fun nuevaPartida()
    fun volverAlMenu()
    fun agregarObservadorFinSimulacion(observador: ObservadorFinSimulacion)
    fun quitarObservadorFinSimulacion(observador: ObservadorFinSimulacion)
    fun resultadoFinal(): ResultadoSimulacion
    fun rompeParedSeleccionada(): Boolean
    fun etiquetaColision(): String
    fun lecturaModo(): LecturaModo
    fun previsualizarTrayectoria(): List<Vector2D>
}
