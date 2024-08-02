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
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

private const val TAG = "DriverViewModel"

class DriverViewModel : ViewModel() {

    var driversUiState: DriversUiState by mutableStateOf(DriversUiState.Loading)
        private set

    lateinit var driverApiService: DriverApiService

    init {
        driverApiService = DriverApi.retrofitService
        getDriversData()
    }

    fun getDriversData() {
        viewModelScope.launch {
            driversUiState = try {
                val drivers = driverApiService.getDrivers()
                val driverPositionsDeferred = drivers.map { driver ->
                    async {
                        val position =
                            driverApiService.getPositions(driverNumber = driver.driverNumber)
                                .lastOrNull()
                        driver.position = position?.position
                        driver
                    }
                }
                val updatedDrivers = driverPositionsDeferred.awaitAll()
                val sortedDrivers = updatedDrivers.sortedBy { it.position }
                DriversUiState.Success(sortedDrivers)
            } catch (e: Exception) {
                //Log.e(TAG, "Failed to retrieve drivers: $e")
                DriversUiState.Error
            }
        }
    }

//    fun getDriversData() {
//        viewModelScope.launch {
//            driversUiState = try {
//                val drivers = driverApiService.getDrivers()
//                Log.d(TAG, "getDriversData: $drivers")
//                println("getDriversData: $drivers")
//                drivers.forEach { driver ->
//                    val position =
//                        driverApiService.getPositions(driverNumber = driver.driverNumber)
//                            .lastOrNull()
//                    Log.d(TAG, "getPositionsData for ${driver.lastName}: ${position?.position}")
//                    driver.position = position?.position
//                }
//                val sortedDrivers = drivers.sortedBy { it.position }
//                DriversUiState.Success(sortedDrivers)
//            } catch (e: Exception) {
//                Log.e(TAG, "Failed to retrieve drivers: $e")
//                DriversUiState.Error
//            }
//        }
//    }
}

sealed interface DriversUiState {
    data class Success(val drivers: List<Driver>) : DriversUiState
    data object Error : DriversUiState
    data object Loading : DriversUiState
}