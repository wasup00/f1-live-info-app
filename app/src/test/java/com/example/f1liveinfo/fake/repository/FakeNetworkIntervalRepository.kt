package com.example.f1liveinfo.fake.repository

import com.example.f1liveinfo.data.IntervalRepository
import com.example.f1liveinfo.model.Interval
import com.example.f1liveinfo.network.ApiResult

class FakeNetworkIntervalRepository : IntervalRepository {
    override suspend fun getIntervals(sessionKey: Int?): ApiResult<List<Interval>> {
        return ApiResult.Success(emptyList())
    }

    override suspend fun getIntervals(
        sessionKey: Int?,
        driverNumber: Int
    ): ApiResult<List<Interval>> {
        return ApiResult.Success(emptyList())
    }

}
