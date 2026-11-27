package cl.duoc.cimucmovil

import cl.duoc.cimucmovil.model.CimContent
import cl.duoc.cimucmovil.model.ContentType
import org.junit.Assert.*
import org.junit.Test

class CimContentTest {

    @Test
    fun testCimContentCreation() {
        val content = CimContent(
            id = "TEST_1",
            type = ContentType.PROYECTO,
            title = "Torre Experimental",
            shortDescription = "Edificio en CLT",
            areaTematica = "Construcción Sustentable"
        )

        assertEquals("TEST_1", content.id)
        assertEquals(ContentType.PROYECTO, content.type)
        assertEquals("Torre Experimental", content.title)
        assertEquals("contacto.cim@uc.cl", content.contactEmail)
        assertTrue(content.relatedIds.isEmpty())
    }

    @Test
    fun testContentTypeEnumValues() {
        val types = ContentType.entries
        assertEquals(5, types.size)
        assertTrue(types.contains(ContentType.PROYECTO))
        assertTrue(types.contains(ContentType.PUBLICACION))
        assertTrue(types.contains(ContentType.INTEGRANTE))
    }

    @Test
    fun testCimContentRelations() {
        val content = CimContent(
            id = "TEST_2",
            type = ContentType.PUBLICACION,
            title = "Artículo Técnico",
            shortDescription = "Estudio de uniones",
            areaTematica = "Ingeniería",
            relatedIds = listOf("P1", "P2")
        )

        assertEquals(2, content.relatedIds.size)
        assertEquals("P1", content.relatedIds[0])
        assertEquals("P2", content.relatedIds[1])
    }
}
