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
import com.example.f1liveinfo.data.MeetingsRepository
import com.example.f1liveinfo.model.Meeting
import kotlinx.coroutines.launch

private const val TAG = "MeetingViewModel"


class MeetingViewModel(private val meetingsRepository: MeetingsRepository) : ViewModel() {

    var meetingUiState: MeetingUiState by mutableStateOf(MeetingUiState.Loading)

    init {
        getMeetingData()
    }

    fun getMeetingData() {
        meetingUiState = MeetingUiState.Loading
        viewModelScope.launch {
            meetingUiState = try {
                val meeting = meetingsRepository.getMeetings().first()
                //Log.d(TAG, "getMeetingData: $meeting")
                MeetingUiState.Success(meeting)
            } catch (e: Exception) {
                //Log.e(TAG, "Failed to retrieve meeting: $e")
                MeetingUiState.Error
            }
        }
    }

    companion object {
        val Factory: Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as F1LiveInfoApplication)
                val meetingsRepository = application.container.meetingsRepository
                MeetingViewModel(meetingsRepository = meetingsRepository)
            }
        }
    }
}

sealed interface MeetingUiState {
    data class Success(val meeting: Meeting) : MeetingUiState
    object Loading : MeetingUiState
    object Error : MeetingUiState
}