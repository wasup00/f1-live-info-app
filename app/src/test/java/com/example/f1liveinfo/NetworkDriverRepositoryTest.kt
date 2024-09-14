package com.example.f1liveinfo

import com.example.f1liveinfo.data.NetworkDriverRepository
import com.example.f1liveinfo.fake.apiservice.FakeDriverApiService
import com.example.f1liveinfo.fake.data.FakeDriverDataSource
import com.example.f1liveinfo.utils.Utils.LATEST
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test

class NetworkDriverRepositoryTest {

    @Test
    fun getDrivers_returnsDriversFromApiService() = runTest {
        val repository = NetworkDriverRepository(
            driverApiService = FakeDriverApiService()
        )
        assertEquals(FakeDriverDataSource.driversFromData, repository.getDrivers(LATEST))
    }
}