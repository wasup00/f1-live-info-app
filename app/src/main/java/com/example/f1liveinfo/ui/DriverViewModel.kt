package com.example.f1liveinfo.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f1liveinfo.data.DriverApi
import com.example.f1liveinfo.model.Driver
import kotlinx.coroutines.launch

private const val TAG = "DriverViewModel"

class DriverViewModel : ViewModel() {

    var driversUiState: DriversUiState by mutableStateOf(DriversUiState.Loading)
        private set

    init {
        getDriversData()
    }

    private fun getDriversData() {
        viewModelScope.launch {
            driversUiState = try {
                val drivers = DriverApi.retrofitService.getDrivers()
                Log.d(TAG, "getDriversData: $drivers")
                drivers.forEach { driver ->
                    val position =
                        DriverApi.retrofitService.getPositions(driverNumber = driver.driverNumber)
                            .lastOrNull()
                    Log.d(TAG, "getPositionsData for ${driver.lastName}: ${position?.position}")
                    driver.position = position?.position
                }
                val sortedDrivers = drivers.sortedBy { it.position }
                DriversUiState.Success(sortedDrivers)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to retrieve drivers: $e")
                DriversUiState.Error
            }
        }
    }
}

sealed interface DriversUiState {
    data class Success(val drivers: List<Driver>) : DriversUiState
    object Error : DriversUiState
    object Loading : DriversUiState
}