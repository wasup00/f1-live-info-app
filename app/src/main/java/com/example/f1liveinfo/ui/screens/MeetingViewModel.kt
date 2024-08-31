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
import com.example.f1liveinfo.data.MeetingRepository
import com.example.f1liveinfo.data.SessionRepository
import com.example.f1liveinfo.model.Meeting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "MeetingViewModel"


class MeetingViewModel(
    private val meetingRepository: MeetingRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    var meetingUiState: MeetingUiState by mutableStateOf(MeetingUiState.Loading)

    init {
        getMeetingData()
    }

    fun getMeetingData() {
        meetingUiState = MeetingUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            meetingUiState = try {
                val meeting = meetingRepository.getMeetings().first()
                //Log.d(TAG, "getMeetingData: $meeting")
                MeetingUiState.Success(meeting)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to retrieve meeting: $e")
                MeetingUiState.Error
            }
        }
    }

    fun getSessionData() {
        if (meetingUiState is MeetingUiState.Success) {
            viewModelScope.launch {
                val currentMeeting = (meetingUiState as MeetingUiState.Success).meeting
                val sessions = sessionRepository.getSessions(meetingKey = currentMeeting.meetingKey)
                Log.d(TAG, "getSessionData: $sessions")
                meetingUiState = MeetingUiState.Success(currentMeeting.copy(sessions = sessions))
            }
        }
    }

    companion object {
        val Factory: Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as F1LiveInfoApplication)
                val meetingRepository = application.container.meetingRepository
                val sessionRepository = application.container.sessionRepository
                MeetingViewModel(
                    meetingRepository = meetingRepository,
                    sessionRepository = sessionRepository
                )
            }
        }
    }
}

sealed interface MeetingUiState {
    data class Success(val meeting: Meeting) : MeetingUiState
    object Loading : MeetingUiState
    object Error : MeetingUiState
}