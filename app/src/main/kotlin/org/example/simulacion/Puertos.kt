package org.example.simulacion

import org.example.entrada.InputHandler
import org.example.modelo.Pared
import org.example.modelo.Pelota
import org.example.modelo.TipoPelota
import org.example.modelo.Vector2D
import org.example.modos.CondicionPrevia
import org.example.modos.ModoSimulacion

/**
 * Puertos que el nucleo de simulacion usa para hablar con sus colaboradores.
 * Al depender de estas abstracciones, las clases concretas pueden sustituirse
 * (pruebas, otras fisicas, otras metricas) sin tocar la coordinacion.
 */
interface SesionJuego {
    fun estado(): EstadoSimulacion
    fun cambiarEstado(nuevo: EstadoSimulacion)
    fun pausado(): Boolean
    fun pausar()
    fun reanudar()
    fun alternarPausa(): Boolean
    fun tipoPelota(): TipoPelota
    fun factorGravedad(): Double
    fun modo(): ModoSimulacion
    fun pelota(): Pelota
    fun posicionInicial(): Vector2D
    fun posicionOrigen(): Vector2D
    fun cambiarTipoPelota(tipo: TipoPelota)
    fun cambiarModo(nuevo: ModoSimulacion)
    fun cambiarGravedad(nuevoFactor: Double)
    fun colocarPelota(posicion: Vector2D)
    fun reubicarPelota(posicion: Vector2D)
    fun detenerPelota()
}

interface Metricas {
    fun tiempoVuelo(): Double
    fun distanciaRecorrida(): Double
    fun alturaMaxima(): Double
    fun distanciaX(): Double
    fun paredesDestruidas(): Int
    fun reiniciarTiro()
    fun reiniciarPartida()
    fun avanzarTiempo(deltaTime: Double)
    fun sumarDistancia(distancia: Double)
    fun registrarAltura(altura: Double)
    fun registrarAlcance(alcance: Double)
    fun registrarRoturas(cantidad: Int)
    fun resultado(): ResultadoSimulacion
}

interface Efectos {
    fun lista(): List<EfectoRotura>
    fun agregarRoturas(paredes: List<Pared>, duracion: Double)
    fun avanzar(deltaTime: Double)
    fun limpiar()
}

interface SuperficiesApoyo {
    fun agregar(pared: Pared)
    fun limpiar()
    fun lista(): List<Pared>
    fun yDeApoyo(x: Double, radio: Double, sueloY: Double): Double
}

interface ControlCamara {
    fun reiniciar()
    fun actualizar(deltaTime: Double, entrada: InputHandler, pelota: Pelota)
}

interface EditorEscenario {
    fun modoParedes(): Boolean
    fun modoSuperficies(): Boolean
    fun paredTemporal(): Pared?
    fun activaCon(estado: EstadoSimulacion): Boolean
    fun alternarParedes(): Boolean
    fun alternarSuperficies(): Boolean
    fun iniciar(punto: Vector2D)
    fun actualizar(punto: Vector2D)
    fun finalizar(punto: Vector2D)
    fun desactivar()
    fun cancelarDibujo()
}

interface Previsualizador {
    fun previsualizar(
        condicion: CondicionPrevia,
        tipoPelota: TipoPelota,
        paredes: List<Pared>
    ): List<Vector2D>
}

interface ObservadoresFin {
    fun agregar(observador: ObservadorFinSimulacion)
    fun quitar(observador: ObservadorFinSimulacion)
    fun notificar(resultado: ResultadoSimulacion)
}

interface Integrador {
    fun avanzar(
        pelota: Pelota,
        deltaTime: Double,
        posicionOrigen: Vector2D,
        alturaActual: () -> Double
    ): Boolean
}

interface UbicadorPelota {
    fun aMundo(punto: Vector2D): Vector2D
    fun preparacionPara(puntoMundo: Vector2D): org.example.modos.Preparacion
    fun alturaActual(): Double
    fun apoyarEnElSueloOSuperficie()
}

interface Mecanica {
    fun iniciarApuntado(punto: Vector2D)
    fun colocarPelotaParaLanzar(punto: Vector2D)
    fun continuarApuntado(punto: Vector2D)
    fun lanzarPelota()
    fun avanzarVuelo(deltaTime: Double): Boolean
    fun reiniciar()
    fun cambiarPelota(tipo: TipoPelota)
}

interface ComandosEditor {
    fun alternarPausa()
    fun alternarModoParedes(): Boolean
    fun alternarModoSuperficies(): Boolean
    fun agregarSuperficieLanzamiento(pared: Pared)
    fun limpiarSuperficiesLanzamiento(): Boolean
    fun seleccionarTipoPared(tipo: org.example.modelo.TipoPared)
    fun agregarPared(pared: Pared)
    fun limpiarParedes()
    fun abrirEdicion()
}

interface ComandosJuego {
    fun seleccionarPelota(tipo: TipoPelota)
    fun aplicarGravedad(nombre: String?)
    fun seleccionarModo(nombre: String?)
    fun nuevaPartida()
    fun volverAlMenu()
}
