package com.example.f1liveinfo.fake.apiservice

import com.example.f1liveinfo.fake.data.FakePositionDataSource
import com.example.f1liveinfo.model.Position
import com.example.f1liveinfo.network.PositionApiService

class FakePositionApiService : PositionApiService {
    override suspend fun getPositions(sessionKey: String?, driverNumber: Int?): List<Position> {
        return FakePositionDataSource.positionsFromData
    }

}