package cl.duoc.cimucmovil.model

data class User(
    val id: Long = 0,
    val username: String,
    val name: String,
    val role: String
) {
    val isAdmin: Boolean
        get() = role.equals("ADMIN", ignoreCase = true)

    companion object {
        const val ROLE_ADMIN = "ADMIN"
        const val ROLE_USER = "USER"
    }
}
