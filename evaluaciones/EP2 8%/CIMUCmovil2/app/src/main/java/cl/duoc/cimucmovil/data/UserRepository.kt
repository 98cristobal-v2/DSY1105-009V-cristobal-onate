package cl.duoc.cimucmovil.data

import cl.duoc.cimucmovil.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepository(private val userDao: UserDao) {

    val allUsers: Flow<List<User>> = userDao.getAllUsers().map { entities ->
        entities.map { it.toDomain() }
    }

    suspend fun authenticate(username: String, password: String): User? {
        return userDao.authenticate(username.trim(), password)?.toDomain()
    }

    suspend fun getUserByUsername(username: String): User? {
        return userDao.getUserByUsername(username.trim())?.toDomain()
    }

    suspend fun getUserById(id: Long): UserEntity? {
        return userDao.getUserById(id)
    }

    suspend fun register(username: String, password: String, name: String, role: String = User.ROLE_USER): Boolean {
        val existing = userDao.getUserByUsername(username.trim())
        if (existing != null) {
            return false
        }
        val entity = UserEntity(
            username = username.trim(),
            password = password,
            name = name.trim(),
            role = role
        )
        return userDao.insertUser(entity) > 0
    }

    suspend fun updateUser(id: Long, name: String, role: String, newPassword: String? = null): Boolean {
        val existing = userDao.getUserById(id) ?: return false
        val updated = existing.copy(
            name = name.trim(),
            role = role,
            password = if (!newPassword.isNullOrBlank()) newPassword.trim() else existing.password
        )
        userDao.updateUser(updated)
        return true
    }

    suspend fun deleteUser(id: Long) {
        userDao.deleteUserById(id)
    }
}
