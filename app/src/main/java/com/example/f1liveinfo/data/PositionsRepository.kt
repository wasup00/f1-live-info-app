package com.example.f1liveinfo.data

import com.example.f1liveinfo.model.Position
import com.example.f1liveinfo.network.PositionApiService

interface PositionsRepository {
    suspend fun getPositions(): List<Position>
}

class NetworkPositionsRepository(
    private val meetingApiService: PositionApiService
) : PositionsRepository {
    override suspend fun getPositions(): List<Position> = meetingApiService.getPositions()
}