package com.example.f1liveinfo.viewmodel

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
import com.example.f1liveinfo.network.ApiResult
import com.example.f1liveinfo.utils.Utils.LATEST
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
        viewModelScope.launch {
            driversUiState = when (val driverResult = driverRepository.getDrivers(sessionKey)) {
                is ApiResult.Success -> {
                    val drivers = driverResult.data
                    when (val positionResult = positionRepository.getPositions(sessionKey)) {
                        is ApiResult.Success -> {
                            val positions = positionResult.data
                            val driverPositions = drivers.map { driver ->
                                val driverPositions =
                                    positions.filter { it.driverNumber == driver.driverNumber }
                                driver.startingPosition =
                                    driverPositions.firstOrNull()?.position ?: 0
                                driver.currentPosition =
                                    driverPositions.maxByOrNull { it.date }?.position ?: 0
                                driver
                            }
                            DriversUiState.Success(driverPositions.sortedBy { it.currentPosition })
                        }

                        is ApiResult.Error -> DriversUiState.Error(
                            positionResult.exception.message ?: "Failed to retrieve positions"
                        )
                    }
                }

                is ApiResult.Error -> DriversUiState.Error(
                    driverResult.exception.message ?: "Failed to retrieve drivers"
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
    data class Error(val message: String) : DriversUiState
    object Loading : DriversUiState
}