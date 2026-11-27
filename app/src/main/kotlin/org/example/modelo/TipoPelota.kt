package org.example.modelo

import org.example.modelo.colision.FormaCapsula
import org.example.modelo.colision.FormaCaja
import org.example.modelo.colision.FormaCirculo
import org.example.modelo.colision.FormaColision
import org.example.modelo.colision.FormaPoligono

enum class TipoPelota(private val datos: DatosTipoPelota) {
    GOMA(
        DatosTipoPelota(
            "Pelota de goma", "Equilibrada: rebota bien y casi no se frena en el aire", 0.50, 0.80, 4.0,
            0.000015, 0.35, 12.0, 0xE74C3C, 0xC0392B
        ) { t -> FormaCirculo(t) }
    ),
    TENIS(
        DatosTipoPelota(
            "Pelota de tenis", "Muy ligera y con mucho rozamiento: pierde velocidad volando", 0.06, 0.72, 5.0,
            0.00028, 0.35, 7.0, 0xD4E157, 0xF7F7F7
        ) { t -> FormaCirculo(t) }
    ),
    RUGBY(
        DatosTipoPelota(
            "Pelota de rugby", "Ovalada: rebota poco, frena mucho y se orienta al volar", 0.46, 0.48, 7.0,
            0.00009, 1.0, 12.0, 0x8B5A2B, 0xF2E8DC
        ) { t -> FormaCapsula(t * 1.6, t * 0.8) }
    ),
    BEISBOL(
        DatosTipoPelota(
            "Pelota de beisbol", "Pequena y densa: sale disparada y rebota seco", 0.15, 0.52, 6.0,
            0.00007, 0.40, 8.0, 0xF7F4EC, 0xC0392B
        ) { t -> FormaCirculo(t) }
    ),
    FUTBOL(
        DatosTipoPelota(
            "Pelota de futbol", "El clasico: buen rebote y rodadura corta en el cesped", 0.43, 0.66, 6.5,
            0.00004, 0.35, 13.0, 0xF7F7F7, 0x1B1B1B
        ) { t -> FormaCirculo(t) }
    ),
    BOLICHE(
        DatosTipoPelota(
            "Bola de boliche", "Muy pesada y dura: aplasta el impacto pero casi no rebota", 0.72, 0.35, 5.0,
            0.000008, 0.30, 11.0, 0x23234A, 0x4A4A6A
        ) { t -> FormaCirculo(t) }
    ),
    BLOQUE(
        DatosTipoPelota(
            "Caja de madera", "Colision de caja: se tumba contra el suelo y no rueda", 0.60, 0.25, 9.0,
            0.00001, 0.55, 12.0, 0xC98A4B, 0x8B5A2B
        ) { t -> FormaCaja(t * 1.45, t * 0.9) }
    ),
    POLIEDRO(
        DatosTipoPelota(
            "Balon poliedrico", "Colision poligonal: rebote anguloso en las esquinas", 0.40, 0.60, 5.5,
            0.00003, 0.45, 14.0, 0x2F6FED, 0xFFFFFF
        ) { t -> FormaPoligono(6, t) }
    );

    fun nombre(): String = datos.nombre
    fun descripcion(): String = datos.descripcion
    fun masa(): Double = datos.masa
    fun coeficienteRestitucion(): Double = datos.coeficienteRestitucion
    fun coeficienteFriccion(): Double = datos.coeficienteFriccion
    fun coeficienteArrastre(): Double = datos.coeficienteArrastre
    fun giro(): Double = datos.giro
    fun tamano(): Double = datos.tamano
    fun color(): Int = datos.color
    fun colorSecundario(): Int = datos.colorSecundario
    fun crearForma(): FormaColision = datos.crearForma()
    fun esRedonda(): Boolean = datos.esRedonda()
    fun resumen(): String = datos.resumen()

    private data class DatosTipoPelota(
        val nombre: String,
        val descripcion: String,
        val masa: Double,
        val coeficienteRestitucion: Double,
        val coeficienteFriccion: Double,
        val coeficienteArrastre: Double,
        val giro: Double,
        val tamano: Double,
        val color: Int,
        val colorSecundario: Int,
        val creadorForma: (Double) -> FormaColision
    ) {
        fun crearForma(): FormaColision = creadorForma(tamano)

        fun esRedonda(): Boolean = crearForma().dibujaBrillo()

        fun resumen(): String = "$nombre (${String.format("%.2f", masa)}kg)"

        fun descripcionCompleta(): String =
            "$nombre: $descripcion (masa=$masa, restitucion=$coeficienteRestitucion, friccion=$coeficienteFriccion, arrastre=$coeficienteArrastre, giro=$giro, tamano=$tamano, color=$color/$colorSecundario)"

        fun esValida(): Boolean =
            nombre.isNotBlank() && descripcion.isNotBlank() && masa > 0 && coeficienteRestitucion >= 0 &&
                coeficienteFriccion >= 0 && coeficienteArrastre >= 0 && tamano > 0

        fun datosCompletos(): List<Any> =
            listOf(
                nombre,
                descripcion,
                masa,
                coeficienteRestitucion,
                coeficienteFriccion,
                coeficienteArrastre,
                giro,
                tamano,
                color,
                colorSecundario,
                creadorForma
            )

        fun propiedades(): Map<String, Any> = mapOf(
            "nombre" to nombre,
            "descripcion" to descripcion,
            "masa" to masa,
            "restitucion" to coeficienteRestitucion,
            "friccion" to coeficienteFriccion,
            "arrastre" to coeficienteArrastre,
            "giro" to giro,
            "tamano" to tamano,
            "color" to color,
            "colorSecundario" to colorSecundario,
            "forma" to crearForma()
        )
    }

    companion object {
        fun porDefecto(): TipoPelota = GOMA
    }
}
