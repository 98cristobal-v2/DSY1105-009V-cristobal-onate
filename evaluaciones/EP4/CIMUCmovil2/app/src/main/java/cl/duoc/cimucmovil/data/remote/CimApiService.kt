package cl.duoc.cimucmovil.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface CimApiService {
    @GET("posts")
    suspend fun getRemoteContents(): List<CimRemoteDto>

    companion object {
        private const val BASE_URL = "http://10.0.2.2:8080/api/cim/"

        fun create(): CimApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CimApiService::class.java)
        }
    }
}
