package cl.duoc.cimucmovil.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import cl.duoc.cimucmovil.model.User

@Entity(
    tableName = "users",
    indices = [Index(value = ["username"], unique = true)]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val username: String,
    val password: String,
    val name: String,
    val role: String = User.ROLE_USER
) {
    fun toDomain(): User {
        return User(
            id = id,
            username = username,
            name = name,
            role = role
        )
    }

    companion object {
        fun fromDomain(user: User, passwordHashOrPlain: String): UserEntity {
            return UserEntity(
                id = user.id,
                username = user.username,
                password = passwordHashOrPlain,
                name = user.name,
                role = user.role
            )
        }
    }
}
