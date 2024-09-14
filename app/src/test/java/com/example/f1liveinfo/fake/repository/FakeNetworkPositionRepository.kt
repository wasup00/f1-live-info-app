package com.example.f1liveinfo.fake.repository

import com.example.f1liveinfo.data.PositionRepository
import com.example.f1liveinfo.fake.data.FakePositionDataSource
import com.example.f1liveinfo.model.Position

class FakeNetworkPositionRepository : PositionRepository {
//    override suspend fun getPositions(): List<Position> {
//        return FakePositionDataSource.positionsFromData
//    }

    override suspend fun getPositions(sessionKey: String?): List<Position> {
        return FakePositionDataSource.positionsFromData
    }
}