package com.example.f1liveinfo.data

import com.example.f1liveinfo.model.Driver
import com.example.f1liveinfo.network.ApiResult
import com.example.f1liveinfo.network.DriverApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

interface DriverRepository {
    suspend fun getDrivers(sessionKey: String?): ApiResult<List<Driver>>
}

class NetworkDriverRepository(
    private val driverApiService: DriverApiService
) : DriverRepository {
    override suspend fun getDrivers(sessionKey: String?): ApiResult<List<Driver>> =
        withContext(Dispatchers.IO) {
            try {
                val drivers = driverApiService.getDrivers(sessionKey = sessionKey)
                ApiResult.Success(drivers)
            } catch (e: IOException) {
                ApiResult.Error(Exception("Network error. Please check your internet connection."))
            } catch (e: Exception) {
                ApiResult.Error(e)
            }
        }
}