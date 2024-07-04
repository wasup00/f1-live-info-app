package com.example.f1liveinfo.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f1liveinfo.data.MeetingUiState
import com.example.f1liveinfo.model.Meeting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.net.URL

private const val TAG = "MeetingViewModel"

class MeetingViewModel : ViewModel() {
    private val _meetingUiState = MutableStateFlow<MeetingUiState>(MeetingUiState.Loading)
    val meetingUiState: StateFlow<MeetingUiState> = _meetingUiState.asStateFlow()

    init {
        fetchMeetingData()
    }

    private fun fetchMeetingData() {
        viewModelScope.launch {
            try {
                // Simulate network fetch (replace with your actual data fetching logic)
                val meeting = getMeetingDataFromNetwork()
                _meetingUiState.value = MeetingUiState.Success(meeting)

            } catch (e: Exception) {
                Log.d(TAG, "Failed to retrieve meeting: $e")
                _meetingUiState.value = MeetingUiState.Error("Error while loading meeting")
            }
        }
    }

    // Replace this with your actual network or data source call
    private suspend fun getMeetingDataFromNetwork(): Meeting {
        // ... your network call to fetch Meeting data
        return withContext(Dispatchers.IO) {
            val json = Json { ignoreUnknownKeys = true }

            val url = "https://api.openf1.org/v1/meetings?meeting_key=latest"

            val response: String

            try {
                response = URL(url).readText()
                Log.d(TAG, "Response: $response")
            } catch (e: Exception) {
                throw e
            }

            try {
                val meetingList = json.decodeFromString<List<Meeting>>(response)
                meetingList.firstOrNull() ?: Meeting()
            } catch (e: Exception) {
                throw e
            }
        }
    }
}