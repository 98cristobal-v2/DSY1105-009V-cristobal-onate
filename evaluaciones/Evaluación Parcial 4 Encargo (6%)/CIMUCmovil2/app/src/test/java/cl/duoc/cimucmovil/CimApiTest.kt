package cl.duoc.cimucmovil

import cl.duoc.cimucmovil.data.remote.CimRemoteDto
import org.junit.Assert.*
import org.junit.Test

class CimApiTest {

    @Test
    fun testCimRemoteDtoProperties() {
        val dto = CimRemoteDto(id = 1, title = "Proyecto Madera", body = "Descripción de prueba", userId = 101)
        assertEquals(1, dto.id)
        assertEquals("Proyecto Madera", dto.title)
        assertEquals("Descripción de prueba", dto.body)
        assertEquals(101, dto.userId)
    }
}
