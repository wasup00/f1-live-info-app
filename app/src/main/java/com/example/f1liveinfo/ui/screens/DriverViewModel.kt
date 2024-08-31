package com.example.f1liveinfo.ui.screens

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
import com.example.f1liveinfo.data.PositionRepository
import com.example.f1liveinfo.model.Driver
import com.example.f1liveinfo.utils.Utils.LATEST
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

private const val TAG = "DriverViewModel"

class DriverViewModel(
    private val driverRepository: DriverRepository,
    private val positionRepository: PositionRepository
) : ViewModel() {

    var driversUiState: DriversUiState by mutableStateOf(DriversUiState.Loading)
        private set

    init {
        getDriversData()
    }

    fun getDriversData(sessionKey: String? = LATEST) {
        driversUiState = DriversUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            driversUiState = try {
                val driversDeferred = async { driverRepository.getDrivers(sessionKey = sessionKey) }
                val positionsDeferred =
                    async { positionRepository.getPositions(sessionKey = sessionKey) }
                val drivers = driversDeferred.await()
                val positions = positionsDeferred.await()

                val driverPositions = drivers.map { driver ->
                    val driverPositions =
                        positions.filter { it.driverNumber == driver.driverNumber }
                    driver.startingPosition = driverPositions.firstOrNull()?.position ?: 0
                    driver.currentPosition = driverPositions.maxByOrNull { it.date }?.position ?: 0
                    driver
                }
                DriversUiState.Success(driverPositions.sortedBy { it.currentPosition })
            } catch (e: Exception) {
                Log.e(TAG, "Failed to retrieve drivers: $e")
                DriversUiState.Error
            }
        }
    }

    companion object {
        val Factory: Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as F1LiveInfoApplication)
                val driverRepository = application.container.driverRepository
                val positionRepository = application.container.positionRepository
                DriverViewModel(
                    driverRepository = driverRepository,
                    positionRepository = positionRepository
                )
            }
        }
    }
}

sealed interface DriversUiState {
    data class Success(val drivers: List<Driver>) : DriversUiState
    object Error : DriversUiState
    object Loading : DriversUiState
}