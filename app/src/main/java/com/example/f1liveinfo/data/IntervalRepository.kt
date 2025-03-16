package com.example.f1liveinfo.data

import com.example.f1liveinfo.model.Interval
import com.example.f1liveinfo.network.ApiResult
import com.example.f1liveinfo.network.IntervalApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

interface IntervalRepository {
    suspend fun getIntervals(sessionKey: String?): ApiResult<List<Interval>>
    suspend fun getIntervals(sessionKey: String?, driverNumber: Int): ApiResult<List<Interval>>
}

class NetworkIntervalRepository(
    private val intervalApiService: IntervalApiService
) : IntervalRepository {
    override suspend fun getIntervals(sessionKey: String?): ApiResult<List<Interval>> =
        withContext(Dispatchers.IO) {
            try {
                val laps = intervalApiService.getIntervals(sessionKey = sessionKey)
                ApiResult.Success(laps)
            } catch (e: IOException) {
                ApiResult.Error(Exception("Network error. Please check your internet connection."))
            } catch (e: Exception) {
                ApiResult.Error(e)
            }
        }

    override suspend fun getIntervals(
        sessionKey: String?,
        driverNumber: Int
    ): ApiResult<List<Interval>> =
        withContext(Dispatchers.IO) {
            try {
                val laps =
                    intervalApiService.getIntervals(
                        sessionKey = sessionKey,
                        driverNumber = driverNumber
                    )
                ApiResult.Success(laps)
            } catch (e: IOException) {
                ApiResult.Error(Exception("Network error. Please check your internet connection."))
            } catch (e: Exception) {
                ApiResult.Error(e)
            }
        }
}