package com.example.f1liveinfo

import com.example.f1liveinfo.fake.data.FakeDriverDataSource
import com.example.f1liveinfo.fake.repository.FakeNetworkDriverRepository
import com.example.f1liveinfo.fake.repository.FakeNetworkIntervalRepository
import com.example.f1liveinfo.fake.repository.FakeNetworkLapRepository
import com.example.f1liveinfo.fake.repository.FakeNetworkPositionRepository
import com.example.f1liveinfo.rules.TestDispatcherRule
import com.example.f1liveinfo.viewmodel.DriverViewModel
import com.example.f1liveinfo.viewmodel.DriversUiState
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class DriverViewModelTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    @Test
    fun driverViewModel_getDriversData_verifyUiStateSuccess() = runTest {
        val driverViewModel = DriverViewModel(
            driverRepository = FakeNetworkDriverRepository(),
            positionRepository = FakeNetworkPositionRepository(),
            lapRepository = FakeNetworkLapRepository(),
            intervalRepository = FakeNetworkIntervalRepository()
        )

        testDispatcher.advanceUntilIdle()

        assertEquals(
            DriversUiState.Success(FakeDriverDataSource.expectedDriversAndPositions),
            driverViewModel.driversUiState
        )
    }
}