package org.example.modelo

enum class TipoPared(
    private val nombre: String,
    private val descripcion: String,
    private val grosor: Double,
    private val color: Int,
    private val colorBorde: Int,
    private val resistencia: Double,
    private val pelotasQueLaRompen: Set<TipoPelota>
) {
    CARTON("Carton", "Papel y cinta: se rompe de un simple golpe", 6.0, 0xD9C39A, 0xA98A5B, 800.0, setOf(TipoPelota.GOMA, TipoPelota.TENIS, TipoPelota.RUGBY, TipoPelota.BEISBOL, TipoPelota.FUTBOL, TipoPelota.POLIEDRO)),
    MADERA("Madera", "Tablones: aguantan a las pelotas blandas, ceden ante las duras", 10.0, 0xC98A4B, 0x8B5A2B, 15000.0, setOf(TipoPelota.GOMA, TipoPelota.RUGBY, TipoPelota.POLIEDRO, TipoPelota.BLOQUE, TipoPelota.BOLICHE)),
    LADRILLO("Ladrillo", "Muro de obra: solo cede ante un impacto de piedra", 14.0, 0xB5523F, 0x7E2F22, 70000.0, setOf(TipoPelota.BLOQUE, TipoPelota.BOLICHE)),
    HORMIGON("Hormigon armado", "Blindaje: solo la boliche a pleno tiro lo atraviesa", 18.0, 0x9AA3AB, 0x5C646C, 400000.0, setOf(TipoPelota.BOLICHE)),
    ACERO("Acero", "Indestructible: rebota cualquier pelota sin abrirse", 12.0, 0x6C7A89, 0x2E3A47, Double.MAX_VALUE, emptySet());

    fun nombre(): String = nombre
    fun descripcion(): String = descripcion
    fun grosor(): Double = grosor
    fun color(): Int = color
    fun colorBorde(): Int = colorBorde
    fun resistencia(): Double = resistencia
    fun pelotasQueLaRompen(): Set<TipoPelota> = pelotasQueLaRompen
    fun indestructible(): Boolean = pelotasQueLaRompen.isEmpty()
    fun registraDanio(tipoPelota: TipoPelota): Boolean = tipoPelota in pelotasQueLaRompen
    fun textoRompible(): String = if (indestructible()) "Indestructible: no hay pelota que la rompa" else "La rompe: " + pelotasQueLaRompen.joinToString(", ") { it.nombre() }
    fun puedeSerRompidaPor(tipoPelota: TipoPelota): Boolean = registraDanio(tipoPelota)

    companion object {
        fun porDefecto(): TipoPared = MADERA
    }
}
