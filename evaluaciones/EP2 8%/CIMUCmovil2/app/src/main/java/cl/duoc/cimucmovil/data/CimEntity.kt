package cl.duoc.cimucmovil.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import cl.duoc.cimucmovil.model.CimContent
import cl.duoc.cimucmovil.model.ContentType

@Entity(tableName = "cim_contents")
data class CimEntity(
    @PrimaryKey
    val id: String,
    val type: String,
    val title: String,
    val shortDescription: String,
    val areaTematica: String,
    val year: String? = null,
    val status: String? = null,
    val associatedName: String? = null,
    val contactEmail: String? = "contacto.cim@uc.cl",
    val relatedIdsString: String = ""
) {
    fun toDomain(): CimContent {
        return CimContent(
            id = id,
            type = ContentType.valueOf(type),
            title = title,
            shortDescription = shortDescription,
            areaTematica = areaTematica,
            year = year,
            status = status,
            associatedName = associatedName,
            contactEmail = contactEmail,
            relatedIds = if (relatedIdsString.isBlank()) emptyList() else relatedIdsString.split(",").map { it.trim() }
        )
    }

    companion object {
        fun fromDomain(content: CimContent): CimEntity {
            return CimEntity(
                id = content.id,
                type = content.type.name,
                title = content.title,
                shortDescription = content.shortDescription,
                areaTematica = content.areaTematica,
                year = content.year,
                status = content.status,
                associatedName = content.associatedName,
                contactEmail = content.contactEmail,
                relatedIdsString = content.relatedIds.joinToString(",")
            )
        }
    }
}
