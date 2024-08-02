package com.example.f1liveinfo.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f1liveinfo.data.MeetingApi
import com.example.f1liveinfo.data.MeetingApiService
import com.example.f1liveinfo.model.Meeting
import kotlinx.coroutines.launch

private const val TAG = "MeetingViewModel"

class MeetingViewModel : ViewModel() {

    var meetingUiState: MeetingUiState by mutableStateOf(MeetingUiState.Loading)
        private set

    lateinit var meetingApiService: MeetingApiService

    init {
        meetingApiService = MeetingApi.retrofitService
        getMeetingData()
    }

    fun getMeetingData() {
        viewModelScope.launch {
            meetingUiState = try {
                val meeting = meetingApiService.getMeetings().first()
                //Log.d(TAG, "getMeetingData: $meeting")
                MeetingUiState.Success(meeting)
            } catch (e: Exception) {
                //Log.e(TAG, "Failed to retrieve meeting: $e")
                MeetingUiState.Error
            }
        }
    }
}

sealed interface MeetingUiState {
    data class Success(val meeting: Meeting) : MeetingUiState
    object Loading : MeetingUiState
    object Error : MeetingUiState
}