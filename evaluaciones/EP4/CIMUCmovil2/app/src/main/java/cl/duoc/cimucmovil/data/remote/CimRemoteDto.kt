package cl.duoc.cimucmovil.data.remote

import com.google.gson.annotations.SerializedName

data class CimRemoteDto(
    val id: Int? = 0,
    @SerializedName(value = "title", alternate = ["titulo", "nombre"])
    val title: String? = null,
    @SerializedName(value = "body", alternate = ["descripcion", "contenido", "detail"])
    val body: String? = null,
    val userId: Int? = null
)

