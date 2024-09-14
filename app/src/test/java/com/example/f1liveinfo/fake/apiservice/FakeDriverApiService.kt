package com.example.f1liveinfo.fake.apiservice

import com.example.f1liveinfo.fake.data.FakeDriverDataSource
import com.example.f1liveinfo.model.Driver
import com.example.f1liveinfo.network.DriverApiService

class FakeDriverApiService : DriverApiService {
    override suspend fun getDrivers(sessionKey: String?): List<Driver> {
        return FakeDriverDataSource.driversFromData
    }
}