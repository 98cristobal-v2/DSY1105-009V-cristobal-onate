package cl.duoc.cimucmovil

import cl.duoc.cimucmovil.data.UserEntity
import cl.duoc.cimucmovil.model.User
import org.junit.Assert.*
import org.junit.Test

class UserDomainTest {

    @Test
    fun testAdminUserRole() {
        val admin = User(id = 1, username = "admin", name = "Administrador", role = User.ROLE_ADMIN)
        assertTrue(admin.isAdmin)
    }

    @Test
    fun testCommonUserRole() {
        val commonUser = User(id = 2, username = "cristobal", name = "Cristóbal", role = User.ROLE_USER)
        assertFalse(commonUser.isAdmin)
    }

    @Test
    fun testUserEntityToDomainMapping() {
        val entity = UserEntity(
            id = 5,
            username = "profe_evaluador",
            password = "password123",
            name = "Profesor",
            role = User.ROLE_USER
        )
        val domain = entity.toDomain()

        assertEquals(5L, domain.id)
        assertEquals("profe_evaluador", domain.username)
        assertEquals("Profesor", domain.name)
        assertEquals(User.ROLE_USER, domain.role)
        assertFalse(domain.isAdmin)
    }
}
