package com.example.f1liveinfo.data


import com.example.f1liveinfo.model.Driver
import com.example.f1liveinfo.model.Position
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

const val SESSION = "latest"
const val BASE_URL = "https://api.openf1.org/v1/"
private val json = Json { ignoreUnknownKeys = true }

private val retrofit = Retrofit.Builder()
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface DriverApiService {
    @GET("drivers")
    suspend fun getDrivers(@Query("session_key") sessionKey: String = SESSION): List<Driver>

    @GET("position")
    suspend fun getPositions(
        @Query("session_key") sessionKey: String = SESSION,
        @Query("driver_number") driverNumber: Int? = null
    ): List<Position>
}

object DriverApi {
    val retrofitService: DriverApiService by lazy {
        retrofit.create(DriverApiService::class.java)
    }
}