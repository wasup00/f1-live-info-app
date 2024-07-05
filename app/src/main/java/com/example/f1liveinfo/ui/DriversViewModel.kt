package com.example.f1liveinfo.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f1liveinfo.model.Driver
import com.example.f1liveinfo.model.Position
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.net.URL

private const val TAG = "DriversViewModel"

class DriversViewModel : ViewModel() {
    private val _drivers = MutableLiveData<List<Driver>>()
    val drivers: LiveData<List<Driver>> = _drivers

    init {
        fetchDriversData()
    }

    private fun fetchDriversData() {
        viewModelScope.launch {
            // Fetch the list of drivers from your data source (e.g., network, database)
            try {
                val fetchedDrivers = getDriversDataFromNetwork()
                fetchedDrivers.forEach { driver ->
                    val position = getDriverPositionDataFromNetwork(driver.driverNumber)
                    driver.position = position
                    Log.d(TAG, "Driver: $driver")
                }
                _drivers.value = fetchedDrivers.sortedBy { it.position }
            } catch (e: Exception) {
                Log.d(TAG, "fetchDriversData: $e")
                _drivers.value = emptyList()
            }
        }
    }

    private suspend fun getDriversDataFromNetwork(): List<Driver> {
        return withContext(Dispatchers.IO) {
            val json = Json { ignoreUnknownKeys = true }

            val url = "https://api.openf1.org/v1/drivers?session_key=latest"

            val response: String

            try {
                response = URL(url).readText()
                Log.d(TAG, "getDriversDataFromNetwork: $response")
            } catch (e: Exception) {
                throw e
            }

            try {
                val driverList = json.decodeFromString<List<Driver>>(response)
                Log.d(TAG, "Driver list: $driverList")
                driverList
            } catch (e: Exception) {
                throw e
            }
        }
    }

    private suspend fun getDriverPositionDataFromNetwork(driverNumber: Int): Int {
        return withContext(Dispatchers.IO) {
            val json = Json { ignoreUnknownKeys = true }

            val url =
                "https://api.openf1.org/v1/position?session_key=latest&driver_number=$driverNumber"

            val response: String

            try {
                response = URL(url).readText()
                Log.d(TAG, "getDriverPositionDataFromNetwork: $response")
            } catch (e: Exception) {
                throw e
            }

            try {
                val positionList = json.decodeFromString<List<Position>>(response)
                Log.d(TAG, "position: ${positionList.last()}")
                positionList.last().position
            } catch (e: Exception) {
                throw e
            }
        }
    }
}