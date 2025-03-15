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
import com.example.f1liveinfo.network.ApiResult
import com.example.f1liveinfo.utils.Utils.LATEST
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
            driversUiState = when (val driverResult = driverRepository.getDrivers(sessionKey)) {
                is ApiResult.Success -> {
                    val drivers = driverResult.data
                    updateCurrentPositionOfDrivers(sessionKey = sessionKey, drivers = drivers)
                }

                is ApiResult.Error -> {
                    driverResult.exception.message?.let { Log.e("$TAG-POSITION", it) }
                    DriversUiState.Error(
                        driverResult.exception.message ?: "Failed to retrieve drivers"
                    )
                }
            }
        }
    }

    private suspend fun updateCurrentPositionOfDrivers(
        sessionKey: String?,
        drivers: List<Driver>
    ): DriversUiState {
        when (val positionResult = positionRepository.getPositions(sessionKey)) {
            is ApiResult.Success -> {
                val positions = positionResult.data
                val driversWithPositions = drivers.map { driver ->
                    val driverPositions =
                        positions.filter { it.driverNumber == driver.driverNumber }
                    driver.copy(
                        startingPosition = driverPositions.firstOrNull()?.position ?: 0,
                        currentPosition = driverPositions.maxByOrNull { it.date }?.position ?: 0
                    )
                }
                return updateFastestLapOfDrivers(
                    sessionKey = sessionKey,
                    drivers = driversWithPositions
                )
            }

            is ApiResult.Error -> {
                positionResult.exception.message?.let { Log.e("$TAG-POSITION", it) }
                return DriversUiState.Error(
                    positionResult.exception.message ?: "Failed to retrieve positions"
                )
            }
        }
    }

    private suspend fun updateFastestLapOfDrivers(
        sessionKey: String?,
        drivers: List<Driver>
    ): DriversUiState {
        when (val lapResult = lapRepository.getLaps(sessionKey)) {
            is ApiResult.Success -> {
                val laps = lapResult.data
                val driverWithLaps = drivers.map { driver ->
                    val driverLaps =
                        laps.filter { it.driverNumber == driver.driverNumber && it.lapDuration != null }
                    driver.copy(fastestLap = driverLaps.minByOrNull { it.lapDuration!! })
                }
                return DriversUiState.Success(driverWithLaps.sortedBy { it.currentPosition })
            }

            is ApiResult.Error -> {
                lapResult.exception.message?.let { Log.e("$TAG-LAP", it) }
                return DriversUiState.Error(
                    lapResult.exception.message ?: "Failed to retrieve laps"
                )
            }
        }
    }

//    fun fetchInterval(sessionKey: Int) {
//        if (driversUiState is DriversUiState.Success) {
//            viewModelScope.launch {
//                when (val intervalResult = intervalRepository.getIntervals(sessionKey = sessionKey)){
//                    is ApiResult.Success -> {
//                        val intervals = intervalResult.data
//                        val drivers = (driversUiState as DriversUiState.Success).drivers
//                        val driverWithIntervals = drivers.map { driver ->
//                            val driverInterval =
//                                intervals.filter { it.driverNumber == driver.driverNumber && it.lapDuration != null }
//                            driver.copy(latestLap = driverIntervals.minByOrNull { it.lapDuration!! })
//                        }
//                    }
//                    is ApiResult.Error -> {
//                        intervalResult.exception.message?.let { Log.e("$TAG-INTERVAL", it) }
//                    }
//                }
//
//            }
//        }
//    }

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