package com.example.f1liveinfo.fake.repository

import com.example.f1liveinfo.data.DriverRepository
import com.example.f1liveinfo.fake.data.FakeDriverDataSource
import com.example.f1liveinfo.model.Driver
import com.example.f1liveinfo.network.ApiResult

class FakeNetworkDriverRepository : DriverRepository {
    override suspend fun getDrivers(sessionKey: String?): ApiResult<List<Driver>> {
        return ApiResult.Success(FakeDriverDataSource.driversFromData)
    }
}