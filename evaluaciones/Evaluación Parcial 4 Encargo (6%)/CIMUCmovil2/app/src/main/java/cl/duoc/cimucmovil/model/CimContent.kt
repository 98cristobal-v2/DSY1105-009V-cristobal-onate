package cl.duoc.cimucmovil.model

enum class ContentType {
    PROYECTO,
    CAPACIDAD_ID,
    INTEGRANTE,
    PUBLICACION,
    ACTIVIDAD
}

data class CimContent(
    val id: String,
    val type: ContentType,
    val title: String,
    val shortDescription: String,
    val areaTematica: String,
    val year: String? = null,
    val status: String? = null,
    val associatedName: String? = null, // Organización o integrante asociado
    val contactEmail: String? = "contacto.cim@uc.cl", // Datos de contacto simulados
    val relatedIds: List<String> = emptyList() // Relaciones entre contenidos
)
