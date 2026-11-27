package org.example.simulacion

import org.example.entrada.InputHandler
import org.example.modelo.Pared
import org.example.modelo.Proyectil
import org.example.modelo.TipoPelota
import org.example.modelo.Vector2D
import org.example.modos.CondicionPrevia
import org.example.modos.ModoSimulacion

interface EstadoSesionJuego {
    fun estado(): EstadoSimulacion
    fun cambiarEstado(nuevo: EstadoSimulacion)
    fun pausado(): Boolean
    fun pausar()
    fun reanudar()
    fun alternarPausa(): Boolean
    fun permiteEditar(): Boolean = estado().permiteEditar()
    fun permitePausa(): Boolean = estado().permitePausa()
    fun estaEnVuelo(): Boolean = estado().estaEnVuelo()
    fun permitePrevisualizar(): Boolean = estado().permitePrevisualizar()
}

interface ConfiguracionSesionJuego {
    fun tipoPelota(): TipoPelota
    fun factorGravedad(): Double
    fun modo(): ModoSimulacion
    fun cambiarTipoPelota(tipo: TipoPelota)
    fun cambiarModo(nuevo: ModoSimulacion)
    fun cambiarGravedad(nuevoFactor: Double)
}

interface ProyectilSesionJuego {
    fun pelota(): Proyectil
    fun posicionInicial(): Vector2D
    fun posicionOrigen(): Vector2D
    fun colocarPelota(posicion: Vector2D)
    fun reubicarPelota(posicion: Vector2D)
    fun detenerPelota()
}

interface EntradaSesionJuego : EstadoSesionJuego {
    fun alPulsar(simulacion: ContextoJuego, punto: Vector2D) = estado().alPulsar(simulacion, punto)
    fun alArrastrar(simulacion: ContextoJuego, punto: Vector2D) = estado().alArrastrar(simulacion, punto)
    fun alSoltar(simulacion: ContextoJuego, punto: Vector2D) = estado().alSoltar(simulacion, punto)
    fun alActualizar(simulacion: ContextoJuego, deltaTime: Double) = estado().alActualizar(simulacion, deltaTime)
    fun alActivarHerramienta(simulacion: ContextoJuego) = estado().alActivarHerramienta(simulacion)
    fun alAbrirEdicion(simulacion: ContextoJuego) = estado().alAbrirEdicion(simulacion)
}

interface LecturaProyectilSesionJuego {
    fun pelota(): Proyectil
    fun modo(): ModoSimulacion
    fun posicionPelota(): Vector2D = pelota().posicion()
    fun velocidadPelota(): Vector2D = pelota().velocidad()
    fun etiquetaColision(): String = pelota().forma().tipo().etiqueta()
    fun condicionDePrevia(entrada: InputHandler): CondicionPrevia? =
        modo().condicionDePrevia(entrada, pelota().posicion())
}

interface SesionJuego :
    MecanicaSesionJuego,
    ConsultaSesionJuego,
    EstadoSesionJuego,
    ConfiguracionSesionJuego,
    ProyectilSesionJuego,
    EntradaSesionJuego,
    LecturaProyectilSesionJuego

interface LecturaMetricasTiro {
    fun tiempoVuelo(): Double
    fun distanciaRecorrida(): Double
    fun alturaMaxima(): Double
    fun distanciaX(): Double
    fun paredesDestruidas(): Int
}

interface ResultadoMetricas {
    fun resultado(): ResultadoSimulacion
}

interface ReinicioMetricas {
    fun reiniciarTiro()
    fun reiniciarPartida()
}

interface AvanceMetricas {
    fun avanzarTiempo(deltaTime: Double)
    fun sumarDistancia(distancia: Double)
    fun registrarAltura(altura: Double)
    fun registrarAlcance(alcance: Double)
    fun registrarRoturas(cantidad: Int)
}

interface Metricas :
    LecturaMetricasTiro,
    ResultadoMetricas,
    ReinicioMetricas,
    AvanceMetricas

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
    fun actualizar(deltaTime: Double, entrada: InputHandler, pelota: Proyectil)
}

interface EstadoEditorEscenario {
    fun modoParedes(): Boolean
    fun modoSuperficies(): Boolean
    fun paredTemporal(): Pared?
    fun activaCon(estado: EstadoSimulacion): Boolean
}

interface ModosEditorEscenario {
    fun alternarParedes(): Boolean
    fun alternarSuperficies(): Boolean
}

interface TrazoEditorEscenario {
    fun iniciar(punto: Vector2D)
    fun actualizar(punto: Vector2D)
    fun finalizar(punto: Vector2D)
    fun desactivar()
    fun cancelarDibujo()
}

interface EditorEscenario :
    EstadoEditorEscenario,
    ModosEditorEscenario,
    TrazoEditorEscenario

interface Previsualizador {
    fun previsualizar(condicion: CondicionPrevia, tipoPelota: TipoPelota, paredes: List<Pared>): List<Vector2D>
}

interface ObservadoresFin {
    fun agregar(observador: ObservadorFinSimulacion)
    fun quitar(observador: ObservadorFinSimulacion)
    fun notificar(resultado: ResultadoSimulacion)
}

interface Integrador {
    fun avanzar(contexto: ContextoVuelo): Boolean
}

class ContextoVuelo(
    proyectil: Proyectil,
    deltaTime: Double,
    posicionOrigen: Vector2D,
    alturaActual: () -> Double
) {
    private val datos = DatosContextoVuelo(proyectil, deltaTime, posicionOrigen, alturaActual)

    fun proyectil(): Proyectil = datos.proyectil
    fun deltaTime(): Double = datos.deltaTime
    fun posicionOrigen(): Vector2D = datos.posicionOrigen
    fun alturaActual(): Double = datos.alturaActual()
    fun alcanceActual(): Double = datos.alcanceActual()
    fun esFinPorAltura(): Boolean = datos.esFinPorAltura()

    private data class DatosContextoVuelo(
        val proyectil: Proyectil,
        val deltaTime: Double,
        val posicionOrigen: Vector2D,
        val alturaActual: () -> Double
    ) {
        fun alturaActual(): Double = alturaActual.invoke()

        fun alcanceActual(): Double = proyectil.posicion().x() - posicionOrigen.x()

        fun esFinPorAltura(): Boolean =
            proyectil.posicion().y() > posicionOrigen.y() + 1e-3 && proyectil.velocidad().y() > 0

        fun descripcion(): String =
            "vuelo(dt=$deltaTime, origen=$posicionOrigen, posicion=${proyectil.posicion()}, altura=${alturaActual()})"

        fun esValido(): Boolean =
            deltaTime > 0 && posicionOrigen.x().isFinite() && alturaActual() >= 0 && proyectil.masa() > 0

        fun resumen(): String =
            "${proyectil.javaClass.simpleName} dt=$deltaTime origen=$posicionOrigen alcance=${alcanceActual()} altura=${alturaActual()}"
    }
}

interface UbicadorPelota {
    fun aMundo(punto: Vector2D): Vector2D
    fun preparacionPara(puntoMundo: Vector2D): org.example.modos.Preparacion
    fun alturaActual(): Double
    fun apoyarEnElSueloOSuperficie()
}

interface ApuntadoMecanica {
    fun iniciarApuntado(punto: Vector2D)
    fun colocarPelotaParaLanzar(punto: Vector2D)
    fun continuarApuntado(punto: Vector2D)
}

interface VueloMecanica {
    fun lanzarPelota()
    fun avanzarVuelo(deltaTime: Double): Boolean
}

interface ReinicioMecanica {
    fun reiniciar()
    fun cambiarPelota(tipo: TipoPelota)
}

interface Mecanica : ApuntadoMecanica, VueloMecanica, ReinicioMecanica

interface ControlPausaEditor {
    fun alternarPausa()
}

interface ControlModosEditor {
    fun alternarModoParedes(): Boolean
    fun alternarModoSuperficies(): Boolean
}

interface ComandosSuperficiesEditor {
    fun agregarSuperficieLanzamiento(pared: Pared)
    fun limpiarSuperficiesLanzamiento(): Boolean
}

interface ComandosParedesEditor {
    fun seleccionarTipoPared(tipo: org.example.modelo.TipoPared)
    fun agregarPared(pared: Pared)
    fun limpiarParedes()
    fun abrirEdicion()
}

interface ComandosEditor :
    ControlPausaEditor,
    ControlModosEditor,
    ComandosSuperficiesEditor,
    ComandosParedesEditor

interface ComandosJuego {
    fun seleccionarPelota(tipo: TipoPelota)
    fun aplicarGravedad(nombre: String?)
    fun seleccionarModo(nombre: String?)
    fun nuevaPartida()
    fun volverAlMenu()
}
