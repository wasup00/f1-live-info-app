package com.example.f1liveinfo.network

import com.example.f1liveinfo.model.Position
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.jar.Pack200.Packer.LATEST

interface PositionApiService {
    @GET("position")
    suspend fun getPositions(
        @Query("session_key") sessionKey: String = LATEST,
        @Query("driver_number") driverNumber: Int? = null
    ): List<Position>
}