package com.example.f1liveinfo.fake.repository

import com.example.f1liveinfo.data.LapRepository
import com.example.f1liveinfo.model.Lap
import com.example.f1liveinfo.network.ApiResult

class FakeNetworkLapRepository : LapRepository {
    override suspend fun getLaps(sessionKey: String?): ApiResult<List<Lap>> {
        return ApiResult.Success(emptyList())
    }

    override suspend fun getLaps(sessionKey: String?, driverNumber: Int): ApiResult<List<Lap>> {
        return ApiResult.Success(emptyList())
    }
}