package com.example.f1liveinfo.data

import com.example.f1liveinfo.model.Driver
import com.example.f1liveinfo.network.DriverApiService

interface DriverRepository {
    suspend fun getDrivers(sessionKey: String?): List<Driver>
}

class NetworkDriverRepository(
    private val driverApiService: DriverApiService
) : DriverRepository {
    override suspend fun getDrivers(sessionKey: String?): List<Driver> =
        driverApiService.getDrivers(sessionKey = sessionKey)
}