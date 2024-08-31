package com.example.f1liveinfo.data

import com.example.f1liveinfo.model.Position
import com.example.f1liveinfo.network.PositionApiService

interface PositionRepository {
    suspend fun getPositions(sessionKey: String?): List<Position>
}

class NetworkPositionRepository(
    private val positionApiService: PositionApiService
) : PositionRepository {
    override suspend fun getPositions(sessionKey: String?): List<Position> =
        positionApiService.getPositions(sessionKey = sessionKey)
}