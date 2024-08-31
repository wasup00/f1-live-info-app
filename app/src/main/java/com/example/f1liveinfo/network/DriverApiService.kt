package com.example.f1liveinfo.network


import com.example.f1liveinfo.model.Driver
import com.example.f1liveinfo.utils.Utils.LATEST
import retrofit2.http.GET
import retrofit2.http.Query

interface DriverApiService {
    @GET("drivers")
    suspend fun getDrivers(@Query("session_key") sessionKey: String? = LATEST): List<Driver>
}