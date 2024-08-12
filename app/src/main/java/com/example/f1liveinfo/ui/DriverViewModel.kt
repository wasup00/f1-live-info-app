package com.example.f1liveinfo.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f1liveinfo.data.DriverApi
import com.example.f1liveinfo.data.DriverApiService
import com.example.f1liveinfo.model.Driver
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val TAG = "DriverViewModel"

class DriverViewModel : ViewModel() {

    var driversUiState: DriversUiState by mutableStateOf(DriversUiState.Loading)

    var driverApiService: DriverApiService = DriverApi.retrofitService

    init {
        getDriversData()
    }

    fun getDriversData() {
        driversUiState = DriversUiState.Loading
        viewModelScope.launch {
            driversUiState = try {
                val drivers = async { driverApiService.getDrivers() }
                val positions = async { driverApiService.getPositions() }
                val driverPositionsDeferred = drivers.await().map { driver ->
                    val latestPosition = positions.await()
                        .filter { it.driverNumber == driver.driverNumber }
                        .maxByOrNull {
                            SimpleDateFormat(
                                "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
                                Locale.getDefault()
                            ).parse(it.date) ?: Date(0)
                        }
                    //Log.d(TAG, "position of driver number ${driver.driverNumber}: $latestPosition")
                    println("position of driver number ${driver.driverNumber}: $latestPosition")
                    driver.position = latestPosition?.position
                    driver
                }
                val sortedDrivers = driverPositionsDeferred.sortedBy { it.position }
                DriversUiState.Success(sortedDrivers)
            } catch (e: Exception) {
                //Log.e(TAG, "Failed to retrieve drivers: $e")
                DriversUiState.Error
            }
        }
    }
}

sealed interface DriversUiState {
    data class Success(val drivers: List<Driver>) : DriversUiState
    data object Error : DriversUiState
    data object Loading : DriversUiState
}