package org.example.simulacion

import org.example.entrada.InputHandler
import org.example.modelo.Pared
import org.example.modelo.Proyectil
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
interface LecturaEstadoJuego {
    fun estado(): EstadoSimulacion
    fun pausado(): Boolean
}

interface LecturaConfiguracionJuego {
    fun tipoPelota(): TipoPelota
    fun tipoPared(): TipoPared
    fun factorGravedad(): Double
    fun modo(): ModoSimulacion
    fun nombresModos(): List<String>
    fun rompeParedSeleccionada(): Boolean
}

interface LecturaEscenarioJuego {
    fun modoParedes(): Boolean
    fun modoSuperficies(): Boolean
    fun paredTemporal(): Pared?
    fun paredes(): List<Pared>
    fun superficiesLanzamientoLista(): List<Pared>
    fun efectos(): List<EfectoRotura>
}

interface LecturaProyectilJuego {
    fun pelota(): Proyectil
    fun offsetCamara(): Vector2D
    fun lecturaModo(): LecturaModo
    fun etiquetaColision(): String
}

interface LecturaMetricasJuego {
    fun tiempoVuelo(): Double
    fun distanciaRecorrida(): Double
    fun alturaMaxima(): Double
    fun distanciaX(): Double
    fun paredesDestruidas(): Int
    fun resultadoFinal(): ResultadoSimulacion
}

interface LecturaFisicaJuego {
    fun sueloY(): Double
    fun gravedad(): Vector2D
}

interface EntradaJuego {
    fun entrada(): InputHandler
    fun actualizar(deltaTime: Double)
    fun pulsar(punto: Vector2D)
    fun arrastrar(punto: Vector2D)
    fun soltar(punto: Vector2D)
}

interface ControlVueloJuego {
    fun cambiarA(nuevo: EstadoSimulacion)
    fun colocarPelotaParaLanzar(punto: Vector2D)
    fun iniciarApuntado(punto: Vector2D)
    fun continuarApuntado(punto: Vector2D)
    fun lanzarPelota()
    fun avanzarVuelo(deltaTime: Double)
}

interface AccionesEscenarioJuego {
    fun alternarModoParedes()
    fun alternarModoSuperficies()
    fun agregarSuperficieLanzamiento(pared: Pared)
    fun limpiarSuperficiesLanzamiento()
    fun agregarPared(pared: Pared)
    fun limpiarParedes()
    fun abrirEdicion()
}

interface AccionesSeleccionJuego {
    fun seleccionarPelota(tipo: TipoPelota)
    fun seleccionarTipoPared(tipo: TipoPared)
    fun aplicarGravedad(nombre: String?)
    fun seleccionarModo(nombre: String?)
}

interface AccionesPartidaJuego {
    fun alternarPausa()
    fun reiniciar()
    fun nuevaPartida()
    fun volverAlMenu()
    fun agregarObservadorFinSimulacion(observador: ObservadorFinSimulacion)
    fun quitarObservadorFinSimulacion(observador: ObservadorFinSimulacion)
}

interface NavegacionSesionJuego :
    LecturaEstadoJuego,
    AccionesEscenarioJuego,
    AccionesPartidaJuego

interface LecturaPanelJuego :
    LecturaEstadoJuego,
    LecturaConfiguracionJuego,
    LecturaEscenarioJuego

interface ControlConsolaJuego :
    LecturaEstadoJuego,
    LecturaProyectilJuego,
    LecturaMetricasJuego,
    LecturaConfiguracionJuego,
    LecturaEscenarioJuego,
    EntradaJuego,
    ControlVueloJuego

interface ControlEntradaJuego :
    EntradaJuego,
    LecturaEstadoJuego,
    LecturaEscenarioJuego,
    AccionesEscenarioJuego,
    AccionesPartidaJuego

interface ContextoJuego :
    LecturaEstadoJuego,
    LecturaConfiguracionJuego,
    LecturaEscenarioJuego,
    LecturaProyectilJuego,
    LecturaMetricasJuego,
    LecturaFisicaJuego,
    EntradaJuego,
    ControlVueloJuego,
    AccionesEscenarioJuego,
    AccionesSeleccionJuego,
    AccionesPartidaJuego,
    NavegacionSesionJuego,
    LecturaPanelJuego,
    ControlConsolaJuego,
    ControlEntradaJuego {
    fun ancho(): Double
    fun alto(): Double
    fun previsualizarTrayectoria(): List<Vector2D>
}
