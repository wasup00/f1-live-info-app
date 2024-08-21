package com.example.f1liveinfo.data

import com.example.f1liveinfo.model.Driver
import com.example.f1liveinfo.network.DriverApiService

interface DriversRepository {
    suspend fun getDrivers(): List<Driver>
}

class NetworkDriversRepository(
    private val driverApiService: DriverApiService
) : DriversRepository {
    override suspend fun getDrivers(): List<Driver> = driverApiService.getDrivers()
}