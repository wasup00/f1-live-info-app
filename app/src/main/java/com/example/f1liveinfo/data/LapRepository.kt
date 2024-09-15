package com.example.f1liveinfo.data

import com.example.f1liveinfo.model.Lap
import com.example.f1liveinfo.network.ApiResult
import com.example.f1liveinfo.network.LapApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

interface LapRepository {
    suspend fun getLaps(sessionKey: String?): ApiResult<List<Lap>>
    suspend fun getLaps(sessionKey: String?, driverNumber: Int): ApiResult<List<Lap>>
}

class NetworkLapRepository(
    private val lapApiService: LapApiService
) : LapRepository {
    override suspend fun getLaps(sessionKey: String?): ApiResult<List<Lap>> =
        withContext(Dispatchers.IO) {
            try {
                val laps = lapApiService.getLaps(sessionKey = sessionKey)
                ApiResult.Success(laps)
            } catch (e: IOException) {
                ApiResult.Error(Exception("Network error. Please check your internet connection."))
            } catch (e: Exception) {
                ApiResult.Error(e)
            }
        }

    override suspend fun getLaps(sessionKey: String?, driverNumber: Int): ApiResult<List<Lap>> =
        withContext(Dispatchers.IO) {
            try {
                val laps =
                    lapApiService.getLaps(sessionKey = sessionKey, driverNumber = driverNumber)
                ApiResult.Success(laps)
            } catch (e: IOException) {
                ApiResult.Error(Exception("Network error. Please check your internet connection."))
            } catch (e: Exception) {
                ApiResult.Error(e)
            }
        }
}