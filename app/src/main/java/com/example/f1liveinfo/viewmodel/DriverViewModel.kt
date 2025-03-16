package com.example.f1liveinfo.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.ViewModelProvider.Factory
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.f1liveinfo.F1LiveInfoApplication
import com.example.f1liveinfo.data.DriverRepository
import com.example.f1liveinfo.data.IntervalRepository
import com.example.f1liveinfo.data.LapRepository
import com.example.f1liveinfo.data.PositionRepository
import com.example.f1liveinfo.model.Driver
import com.example.f1liveinfo.model.Interval
import com.example.f1liveinfo.model.Lap
import com.example.f1liveinfo.model.Position
import com.example.f1liveinfo.network.ApiResult
import com.example.f1liveinfo.utils.Utils.LATEST
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

private const val TAG = "DriverViewModel"

class DriverViewModel(
    private val driverRepository: DriverRepository,
    private val positionRepository: PositionRepository,
    private val lapRepository: LapRepository,
    private val intervalRepository: IntervalRepository
) : ViewModel() {

    var driversUiState: DriversUiState by mutableStateOf(DriversUiState.Loading)
        private set

    init {
        getDriversData()
    }


    fun getDriversData(sessionKey: String? = LATEST) {
        driversUiState = DriversUiState.Loading
        viewModelScope.launch {
            when (val driversResult = driverRepository.getDrivers(sessionKey)) {
                is ApiResult.Success -> {
                    val drivers = driversResult.data

                    val positionsDeferred = async { positionRepository.getPositions(sessionKey) }
                    val lapsDeferred = async { lapRepository.getLaps(sessionKey) }
                    val intervalsDeferred = async { intervalRepository.getIntervals(sessionKey) }

                    val positionsResult = positionsDeferred.await()
                    val lapsResult = lapsDeferred.await()
                    val intervalsResult = intervalsDeferred.await()

                    driversUiState = processResults(
                        drivers,
                        positionsResult,
                        lapsResult,
                        intervalsResult
                    )
                }
                is ApiResult.Error -> {
                    driversResult.exception.message?.let { Log.e("$TAG-DRIVER", it) }
                    driversUiState = DriversUiState.Error(
                        driversResult.exception.message ?: "Failed to retrieve drivers"
                    )
                }
            }
        }
    }

    private fun processResults(
        drivers: List<Driver>,
        positionsResult: ApiResult<List<Position>>,
        lapsResult: ApiResult<List<Lap>>,
        intervalsResult: ApiResult<List<Interval>>
    ): DriversUiState {
        // Process positions
        val driversWithPositions = when (positionsResult) {
            is ApiResult.Success -> {
                val positions = positionsResult.data
                drivers.map { driver ->
                    val driverPositions =
                        positions.filter { it.driverNumber == driver.driverNumber }
                    driver.copy(
                        startingPosition = driverPositions.firstOrNull()?.position ?: 0,
                        currentPosition = driverPositions.maxByOrNull { it.date }?.position ?: 0
                    )
                }
            }
            is ApiResult.Error -> {
                positionsResult.exception.message?.let { Log.e("$TAG-POSITION", it) }
                return DriversUiState.Error(
                    positionsResult.exception.message ?: "Failed to retrieve positions"
                )
            }
        }

        // Process laps
        val driversWithLaps = when (lapsResult) {
            is ApiResult.Success -> {
                val laps = lapsResult.data
                driversWithPositions.map { driver ->
                    val driverFastestLaps = laps.filter {
                        it.driverNumber == driver.driverNumber && it.lapDuration != null
                    }
                    val driverLatestLaps = laps.filter {
                        it.driverNumber == driver.driverNumber
                    }
                    driver.copy(
                        fastestLap = driverFastestLaps.minByOrNull { it.lapDuration!! },
                        latestLap = driverLatestLaps.maxByOrNull { it.lapNumber }
                    )
                }
            }
            is ApiResult.Error -> {
                lapsResult.exception.message?.let { Log.e("$TAG-LAP", it) }
                return DriversUiState.Error(
                    lapsResult.exception.message ?: "Failed to retrieve laps"
                )
            }
        }

        // Process intervals
        return when (intervalsResult) {
            is ApiResult.Success -> {
                val intervals = intervalsResult.data
                val driversWithIntervals = driversWithLaps.map { driver ->
                    val driverInterval = intervals.filter {
                        it.driverNumber == driver.driverNumber &&
                                it.gapToDriverAhead != null &&
                                it.gapToLeader != null
                    }
                    driver.copy(
                        interval = driverInterval.maxByOrNull { it.date }
                    )
                }
                DriversUiState.Success(driversWithIntervals.sortedBy { it.currentPosition })
            }
            is ApiResult.Error -> {
                intervalsResult.exception.message?.let { Log.e("$TAG-INTERVAL", it) }
                DriversUiState.Error(
                    intervalsResult.exception.message ?: "Failed to retrieve intervals"
                )
            }
        }
    }


    companion object {
        val Factory: Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as F1LiveInfoApplication)
                val driverRepository = application.container.driverRepository
                val positionRepository = application.container.positionRepository
                val lapRepository = application.container.lapRepository
                val intervalRepository = application.container.intervalRepository
                DriverViewModel(
                    driverRepository = driverRepository,
                    positionRepository = positionRepository,
                    lapRepository = lapRepository,
                    intervalRepository = intervalRepository
                )
            }
        }
    }
}

sealed interface DriversUiState {
    data class Success(val drivers: List<Driver>) : DriversUiState
    data class Error(val message: String) : DriversUiState
    object Loading : DriversUiState
}