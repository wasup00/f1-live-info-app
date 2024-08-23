package com.example.f1liveinfo.ui.screens

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
import com.example.f1liveinfo.data.DriversRepository
import com.example.f1liveinfo.data.PositionsRepository
import com.example.f1liveinfo.model.Driver
import com.example.f1liveinfo.utils.Utils
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

private const val TAG = "DriverViewModel"

class DriverViewModel(
    private val driversRepository: DriversRepository,
    private val positionsRepository: PositionsRepository
) : ViewModel() {

    var driversUiState: DriversUiState by mutableStateOf(DriversUiState.Loading)

    init {
        getDriversData()
    }

    fun getDriversData() {
        driversUiState = DriversUiState.Loading
        viewModelScope.launch {
            driversUiState = try {
                val drivers = async { driversRepository.getDrivers() }
                val positions = async { positionsRepository.getPositions() }
                val driverPositionsDeferred = drivers.await().map { driver ->
                    val latestPosition = positions.await()
                        .filter { it.driverNumber == driver.driverNumber }
                        .maxByOrNull {
                            Utils.formatStringToDate(date = it.date)
                        }
                    if (driver.startingPosition == null) {
                        val startingPosition =
                            positions.await().firstOrNull { it.driverNumber == driver.driverNumber }
                        if (startingPosition == null) {
                            driver.startingPosition = 0
                        } else {
                            driver.startingPosition = startingPosition.position
                        }
                    }
                    //Log.d(TAG, "position of driver number ${driver.driverNumber}: $latestPosition")
                    println("position of driver number ${driver.driverNumber}: $latestPosition")
                    driver.currentPosition = latestPosition?.position!!
                    driver
                }
                val sortedDrivers = driverPositionsDeferred.sortedBy { it.currentPosition }
                DriversUiState.Success(sortedDrivers)
            } catch (e: Exception) {
                //Log.e(TAG, "Failed to retrieve drivers: $e")
                DriversUiState.Error
            }
        }
    }

    companion object {
        val Factory: Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as F1LiveInfoApplication)
                val driversRepository = application.container.driversRepository
                val positionsRepository = application.container.positionsRepository
                DriverViewModel(
                    driversRepository = driversRepository,
                    positionsRepository = positionsRepository
                )
            }
        }
    }
}

sealed interface DriversUiState {
    data class Success(val drivers: List<Driver>) : DriversUiState
    data object Error : DriversUiState
    data object Loading : DriversUiState
}