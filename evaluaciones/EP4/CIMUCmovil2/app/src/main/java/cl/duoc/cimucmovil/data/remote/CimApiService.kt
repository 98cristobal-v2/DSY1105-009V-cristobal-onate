package cl.duoc.cimucmovil.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// 1. MICROSERVICIO PROPIO (Spring Boot local en puerto 8080)
interface CimApiService {
    @GET("posts")
    suspend fun getPosts(): List<CimRemoteDto>

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

// 2. API EXTERNA PÚBLICA (JSONPlaceholder / Internet)
interface ExternalApiService {
    @GET("posts")
    suspend fun getExternalPosts(): List<CimRemoteDto>

    companion object {
        private const val EXTERNAL_BASE_URL = "https://jsonplaceholder.typicode.com/"

        fun create(): ExternalApiService {
            return Retrofit.Builder()
                .baseUrl(EXTERNAL_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ExternalApiService::class.java)
        }
    }
}

