package com.example.f1liveinfo.data

import com.example.f1liveinfo.model.Position
import com.example.f1liveinfo.network.ApiResult
import com.example.f1liveinfo.network.PositionApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

interface PositionRepository {
    suspend fun getPositions(sessionKey: String?): ApiResult<List<Position>>
}

class NetworkPositionRepository(
    private val positionApiService: PositionApiService
) : PositionRepository {
    override suspend fun getPositions(sessionKey: String?): ApiResult<List<Position>> =
        withContext(Dispatchers.IO) {
            try {
                val positions = positionApiService.getPositions(sessionKey = sessionKey)
                ApiResult.Success(positions)
            } catch (e: IOException) {
                ApiResult.Error(Exception("Network error. Please check your internet connection."))
            } catch (e: Exception) {
                ApiResult.Error(e)
            }
        }
}