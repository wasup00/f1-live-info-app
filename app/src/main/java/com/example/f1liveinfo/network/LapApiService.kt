package com.example.f1liveinfo.network


import com.example.f1liveinfo.model.Lap
import com.example.f1liveinfo.utils.Utils.LATEST
import retrofit2.http.GET
import retrofit2.http.Query

interface LapApiService {
    @GET("laps")
    suspend fun getLaps(
        @Query("session_key") sessionKey: String? = LATEST,
        @Query("driver_number") driverNumber: Int
    ): List<Lap>

    @GET("laps")
    suspend fun getLaps(@Query("session_key") sessionKey: String? = LATEST): List<Lap>
}