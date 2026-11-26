package cl.duoc.cimucmovil.data.remote

data class CimRemoteDto(
    val id: Int,
    val title: String,
    val body: String?,
    val userId: Int?
)
