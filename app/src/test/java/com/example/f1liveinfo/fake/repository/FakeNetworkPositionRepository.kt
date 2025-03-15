package com.example.f1liveinfo.fake.repository

import com.example.f1liveinfo.data.PositionRepository
import com.example.f1liveinfo.fake.data.FakePositionDataSource
import com.example.f1liveinfo.model.Position
import com.example.f1liveinfo.network.ApiResult

class FakeNetworkPositionRepository : PositionRepository {
    override suspend fun getPositions(sessionKey: String?): ApiResult<List<Position>> {
        return ApiResult.Success(FakePositionDataSource.positionsFromData)
    }
}