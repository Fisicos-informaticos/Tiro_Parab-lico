package org.example.modelo

enum class TipoPared(private val datos: DatosTipoPared) {
    CARTON(
        DatosTipoPared(
            "Carton", "Papel y cinta: se rompe de un simple golpe", 6.0, 0xD9C39A, 0xA98A5B, 800.0,
            setOf(
                TipoPelota.GOMA, TipoPelota.TENIS, TipoPelota.RUGBY,
                TipoPelota.BEISBOL, TipoPelota.FUTBOL, TipoPelota.POLIEDRO
            )
        )
    ),
    MADERA(
        DatosTipoPared(
            "Madera", "Tablones: aguantan a las pelotas blandas, ceden ante las duras", 10.0, 0xC98A4B, 0x8B5A2B, 15000.0,
            setOf(
                TipoPelota.GOMA, TipoPelota.RUGBY, TipoPelota.POLIEDRO,
                TipoPelota.BLOQUE, TipoPelota.BOLICHE
            )
        )
    ),
    LADRILLO(
        DatosTipoPared(
            "Ladrillo", "Muro de obra: solo cede ante un impacto de piedra", 14.0, 0xB5523F, 0x7E2F22, 70000.0,
            setOf(TipoPelota.BLOQUE, TipoPelota.BOLICHE)
        )
    ),
    HORMIGON(
        DatosTipoPared(
            "Hormigon armado", "Blindaje: solo la boliche a pleno tiro lo atraviesa", 18.0, 0x9AA3AB, 0x5C646C, 400000.0,
            setOf(TipoPelota.BOLICHE)
        )
    ),
    ACERO(
        DatosTipoPared(
            "Acero", "Indestructible: rebota cualquier pelota sin abrirse", 12.0, 0x6C7A89, 0x2E3A47,
            Double.MAX_VALUE, emptySet()
        )
    );

    fun nombre(): String = datos.nombre
    fun descripcion(): String = datos.descripcion
    fun grosor(): Double = datos.grosor
    fun color(): Int = datos.color
    fun colorBorde(): Int = datos.colorBorde
    fun resistencia(): Double = datos.resistencia
    fun pelotasQueLaRompen(): Set<TipoPelota> = datos.pelotasQueLaRompen
    fun indestructible(): Boolean = datos.indestructible()
    fun registraDanio(tipoPelota: TipoPelota): Boolean = datos.registraDanio(tipoPelota)
    fun textoRompible(): String = datos.textoRompible()
    fun puedeSerRompidaPor(tipoPelota: TipoPelota): Boolean = datos.puedeSerRompidaPor(tipoPelota)

    private data class DatosTipoPared(
        val nombre: String,
        val descripcion: String,
        val grosor: Double,
        val color: Int,
        val colorBorde: Int,
        val resistencia: Double,
        val pelotasQueLaRompen: Set<TipoPelota>
    ) {
        fun indestructible(): Boolean = pelotasQueLaRompen.isEmpty()

        fun registraDanio(tipoPelota: TipoPelota): Boolean = tipoPelota in pelotasQueLaRompen

        fun textoRompible(): String =
            if (indestructible()) "Indestructible: no hay pelota que la rompa"
            else "La rompe: " + pelotasQueLaRompen.joinToString(", ") { it.nombre() }

        fun puedeSerRompidaPor(tipoPelota: TipoPelota): Boolean = registraDanio(tipoPelota)

        fun descripcionCompleta(): String =
            "$nombre: $descripcion (grosor=$grosor, color=$color/$colorBorde, resistencia=$resistencia, rompedoras=$pelotasQueLaRompen)"

        fun esValida(): Boolean =
            nombre.isNotBlank() && descripcion.isNotBlank() && grosor > 0 && resistencia > 0

        fun resumen(): String =
            "$nombre | $grosor px | $color/$colorBorde | resistencia=$resistencia | ${textoRompible()}"

        fun datosCompletos(): List<Any> =
            listOf(nombre, descripcion, grosor, color, colorBorde, resistencia, pelotasQueLaRompen)
    }

    companion object {
        fun porDefecto(): TipoPared = MADERA
    }
}
