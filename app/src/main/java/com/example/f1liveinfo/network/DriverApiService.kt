package com.example.f1liveinfo.network


import com.example.f1liveinfo.model.Driver
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.jar.Pack200.Packer.LATEST

interface DriverApiService {
    @GET("drivers")
    suspend fun getDrivers(@Query("session_key") sessionKey: String = LATEST): List<Driver>
}