package org.example.simulacion

import org.example.coordenadas.Camara
import org.example.entrada.InputHandler
import org.example.fisica.PhysicsEngine
import org.example.fisica.Tierra
import org.example.modelo.Pared
import org.example.modelo.Proyectil
import org.example.modelo.TipoPelota
import org.example.modelo.TipoPared
import org.example.modelo.Vector2D
import org.example.modelo.creadores.CreadorParedes
import org.example.modos.FabricaModos
import org.example.modos.LecturaModo
import org.example.modos.ModoSimulacion

internal class SimulacionConsultas(
    private val ancho: Double,
    private val alto: Double,
    private val entrada: InputHandler,
    private val sesion: SesionJuego,
    private val edicion: EditorEscenario,
    private val creadorParedes: CreadorParedes,
    private val fabricaModos: FabricaModos,
    private val camara: Camara,
    private val metricas: Metricas,
    private val efectos: Efectos,
    private val superficies: SuperficiesApoyo,
    private val motor: PhysicsEngine,
    private val posicionPelota: UbicadorPelota,
    private val previsualizador: Previsualizador
) {
    fun ancho(): Double = ancho
    fun alto(): Double = alto
    fun entrada(): InputHandler = entrada
    fun estado(): EstadoSimulacion = sesion.estado()
    fun pausado(): Boolean = sesion.pausado()
    fun modoParedes(): Boolean = edicion.modoParedes()
    fun modoSuperficies(): Boolean = edicion.modoSuperficies()
    fun tipoPelota(): TipoPelota = sesion.tipoPelota()
    fun tipoPared(): TipoPared = creadorParedes.tipoParedActual()
    fun factorGravedad(): Double = sesion.factorGravedad()
    fun modo(): ModoSimulacion = sesion.modo()
    fun nombresModos(): List<String> = fabricaModos.nombres()
    fun pelota(): Proyectil = sesion.pelota()
    fun offsetCamara(): Vector2D = camara.offsetCamara()
    fun tiempoVuelo(): Double = metricas.tiempoVuelo()
    fun distanciaRecorrida(): Double = metricas.distanciaRecorrida()
    fun alturaMaxima(): Double = metricas.alturaMaxima()
    fun distanciaX(): Double = metricas.distanciaX()
    fun paredesDestruidas(): Int = metricas.paredesDestruidas()
    fun paredTemporal(): Pared? = edicion.paredTemporal()
    fun paredes(): List<Pared> = motor.obtenerParedes()
    fun superficiesLanzamientoLista(): List<Pared> = superficies.lista()
    fun efectos(): List<EfectoRotura> = efectos.lista()
    fun sueloY(): Double = motor.sueloY()
    fun gravedad(): Vector2D = Vector2D(0.0, Tierra.GRAVEDAD_PIXELES * sesion.factorGravedad())
    fun resultadoFinal(): ResultadoSimulacion = metricas.resultado()
    fun rompeParedSeleccionada(): Boolean = tipoPared().registraDanio(sesion.tipoPelota())
    fun etiquetaColision(): String = sesion.pelota().forma().tipo().etiqueta()
    fun lecturaModo(): LecturaModo = LecturaModo(
        posicion = sesion.pelota().posicion(),
        velocidad = sesion.pelota().velocidad(),
        distancia = metricas.distanciaRecorrida(),
        altura = posicionPelota.alturaActual(),
        tiempo = metricas.tiempoVuelo(),
        angulo = entrada.anguloLanzamiento(),
        fuerza = entrada.fuerzaLanzamiento()
    )
    fun previsualizarTrayectoria(): List<Vector2D> {
        if (!sesion.estado().permitePrevisualizar()) return emptyList()
        val condicion = sesion.modo().condicionDePrevia(entrada, sesion.pelota().posicion()) ?: return emptyList()
        return previsualizador.previsualizar(condicion, sesion.tipoPelota(), motor.obtenerParedes())
    }
}
