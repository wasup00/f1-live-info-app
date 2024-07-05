package com.example.f1liveinfo.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f1liveinfo.model.Driver
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
                _drivers.value = fetchedDrivers
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
                Log.d(TAG, "Response: $response")
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
}