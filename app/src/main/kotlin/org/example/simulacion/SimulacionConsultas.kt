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

interface ConsultaSesionJuego :
    EstadoSesionJuego,
    ConfiguracionSesionJuego,
    ProyectilSesionJuego,
    LecturaProyectilSesionJuego

internal class ContextoConsultas(
    val entrada: InputHandler,
    val sesion: ConsultaSesionJuego,
    val edicion: EditorEscenario,
    val creadorParedes: CreadorParedes,
    val fabricaModos: FabricaModos,
    val camara: Camara,
    val metricas: Metricas,
    val efectos: Efectos,
    val superficies: SuperficiesApoyo,
    val motor: PhysicsEngine,
    val posicionPelota: UbicadorPelota,
    val previsualizador: Previsualizador
) {
    fun gravedad(): Vector2D = Vector2D(0.0, Tierra.GRAVEDAD_PIXELES * sesion.factorGravedad())

    fun descripcion(): String =
        "consultas(sesion=${sesion.javaClass.simpleName}, edicion=${edicion.javaClass.simpleName}, camara=${camara.javaClass.simpleName}, motor=${motor.javaClass.simpleName})"

    fun componentes(): List<Any> = listOf(
        entrada, sesion, edicion, creadorParedes, fabricaModos, camara,
        metricas, efectos, superficies, motor, posicionPelota, previsualizador
    )
}

internal class SimulacionConsultas(
    ancho: Double,
    alto: Double,
    contexto: ContextoConsultas
) {
    private val datos = DatosSimulacionConsultas(ancho, alto, contexto)

    fun ancho(): Double = datos.ancho
    fun alto(): Double = datos.alto
    fun entrada(): InputHandler = datos.contexto.entrada
    fun estado(): EstadoSimulacion = datos.estado()
    fun pausado(): Boolean = datos.pausado()
    fun modoParedes(): Boolean = datos.modoParedes()
    fun modoSuperficies(): Boolean = datos.modoSuperficies()
    fun tipoPelota(): TipoPelota = datos.tipoPelota()
    fun tipoPared(): TipoPared = datos.tipoPared()
    fun factorGravedad(): Double = datos.factorGravedad()
    fun modo(): ModoSimulacion = datos.modo()
    fun nombresModos(): List<String> = datos.nombresModos()
    fun pelota(): Proyectil = datos.pelota()
    fun offsetCamara(): Vector2D = datos.offsetCamara()
    fun tiempoVuelo(): Double = datos.tiempoVuelo()
    fun distanciaRecorrida(): Double = datos.distanciaRecorrida()
    fun alturaMaxima(): Double = datos.alturaMaxima()
    fun distanciaX(): Double = datos.distanciaX()
    fun paredesDestruidas(): Int = datos.paredesDestruidas()
    fun paredTemporal(): Pared? = datos.paredTemporal()
    fun paredes(): List<Pared> = datos.paredes()
    fun superficiesLanzamientoLista(): List<Pared> = datos.superficiesLanzamientoLista()
    fun efectos(): List<EfectoRotura> = datos.efectos()
    fun sueloY(): Double = datos.sueloY()
    fun gravedad(): Vector2D = datos.gravedad()
    fun resultadoFinal(): ResultadoSimulacion = datos.resultadoFinal()
    fun rompeParedSeleccionada(): Boolean = datos.rompeParedSeleccionada()
    fun etiquetaColision(): String = datos.etiquetaColision()
    fun lecturaModo(): LecturaModo = datos.lecturaModo()
    fun previsualizarTrayectoria(): List<Vector2D> = datos.previsualizarTrayectoria()

    private data class DatosSimulacionConsultas(
        val ancho: Double,
        val alto: Double,
        val contexto: ContextoConsultas
    ) {
        fun estado(): EstadoSimulacion = contexto.sesion.estado()

        fun pausado(): Boolean = contexto.sesion.pausado()

        fun modoParedes(): Boolean = contexto.edicion.modoParedes()

        fun modoSuperficies(): Boolean = contexto.edicion.modoSuperficies()

        fun tipoPelota(): TipoPelota = contexto.sesion.tipoPelota()

        fun tipoPared(): TipoPared = contexto.creadorParedes.tipoParedActual()

        fun factorGravedad(): Double = contexto.sesion.factorGravedad()

        fun modo(): ModoSimulacion = contexto.sesion.modo()

        fun nombresModos(): List<String> = contexto.fabricaModos.nombres()

        fun pelota(): Proyectil = contexto.sesion.pelota()

        fun offsetCamara(): Vector2D = contexto.camara.offsetCamara()

        fun tiempoVuelo(): Double = contexto.metricas.tiempoVuelo()

        fun distanciaRecorrida(): Double = contexto.metricas.distanciaRecorrida()

        fun alturaMaxima(): Double = contexto.metricas.alturaMaxima()

        fun distanciaX(): Double = contexto.metricas.distanciaX()

        fun paredesDestruidas(): Int = contexto.metricas.paredesDestruidas()

        fun paredTemporal(): Pared? = contexto.edicion.paredTemporal()

        fun paredes(): List<Pared> = contexto.motor.obtenerParedes()

        fun superficiesLanzamientoLista(): List<Pared> = contexto.superficies.lista()

        fun efectos(): List<EfectoRotura> = contexto.efectos.lista()

        fun sueloY(): Double = contexto.motor.sueloY()

        fun gravedad(): Vector2D = contexto.gravedad()

        fun resultadoFinal(): ResultadoSimulacion = contexto.metricas.resultado()

        fun rompeParedSeleccionada(): Boolean = tipoPared().registraDanio(contexto.sesion.tipoPelota())

        fun etiquetaColision(): String = contexto.sesion.etiquetaColision()

        fun lecturaModo(): LecturaModo = LecturaModo(
            posicion = contexto.sesion.posicionPelota(),
            velocidad = contexto.sesion.velocidadPelota(),
            distancia = contexto.metricas.distanciaRecorrida(),
            altura = contexto.posicionPelota.alturaActual(),
            tiempo = contexto.metricas.tiempoVuelo(),
            angulo = contexto.entrada.anguloLanzamiento(),
            fuerza = contexto.entrada.fuerzaLanzamiento()
        )

        fun previsualizarTrayectoria(): List<Vector2D> {
            if (!contexto.sesion.permitePrevisualizar()) return emptyList()
            val condicion = contexto.sesion.condicionDePrevia(contexto.entrada) ?: return emptyList()
            return contexto.previsualizador.previsualizar(
                condicion,
                contexto.sesion.tipoPelota(),
                contexto.motor.obtenerParedes()
            )
        }

        fun descripcion(): String =
            "consultas(${ancho}x${alto}, ${contexto.descripcion()})"

        fun esValido(): Boolean =
            ancho > 0 && alto > 0 && contexto.sesion.javaClass !== contexto.edicion.javaClass &&
                contexto.edicion.javaClass !== contexto.creadorParedes.javaClass &&
                contexto.fabricaModos.javaClass !== contexto.camara.javaClass &&
                contexto.camara.javaClass !== contexto.metricas.javaClass &&
                contexto.metricas.javaClass !== contexto.efectos.javaClass &&
                contexto.efectos.javaClass !== contexto.superficies.javaClass &&
                contexto.superficies.javaClass !== contexto.motor.javaClass &&
                contexto.motor.javaClass !== contexto.posicionPelota.javaClass &&
                contexto.posicionPelota.javaClass !== contexto.previsualizador.javaClass

        fun resumen(): String =
            "${ancho}x${alto} ${estado()} ${tipoPelota()}/${tipoPared()} ${nombresModos().size} modos ${paredes().size} paredes"

        fun componentes(): List<Any> = listOf(ancho, alto) + contexto.componentes()
    }
}
