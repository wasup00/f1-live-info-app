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
import com.example.f1liveinfo.utils.Utils.LATEST
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

private const val TAG = "MeetingViewModel"


class MeetingViewModel(
    private val meetingRepository: MeetingRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    var meetingUiState: MeetingUiState by mutableStateOf(MeetingUiState.Loading)
        private set

    init {
        getMeetingData()
    }

    fun getMeetingData(sessionKey: String = LATEST) {
        meetingUiState = MeetingUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            meetingUiState = try {
                val meetingDeferred = async { meetingRepository.getMeetings().first() }
                val latestSessionDeferred =
                    async { sessionRepository.getSessions(sessionKey = sessionKey) }

                val meeting = meetingDeferred.await()
                val latestSession = latestSessionDeferred.await()
                //Log.d(TAG, "getMeetingData: $meeting")
                MeetingUiState.Success(
                    meeting.copy(
                        sessions = latestSession,
                        sessionKey = latestSession.first().sessionKey
                    )
                )
            } catch (e: Exception) {
                Log.e(TAG, "Failed to retrieve meeting: $e")
                MeetingUiState.Error
            }
        }
    }

    fun getSessionsData() {
        if (meetingUiState is MeetingUiState.Success) {
            viewModelScope.launch {
                val currentMeeting = (meetingUiState as MeetingUiState.Success).meeting
                val sessions = sessionRepository.getSessions(meetingKey = currentMeeting.meetingKey)
                //Log.d(TAG, "getSessionsData: $sessions")
                meetingUiState = MeetingUiState.Success(currentMeeting.copy(sessions = sessions))
            }
        }
    }

    fun modifyMeetingSessionKey(sessionKey: Int) {
        if (meetingUiState is MeetingUiState.Success) {
            val currentMeeting = (meetingUiState as MeetingUiState.Success).meeting
            val updatedMeeting = currentMeeting.copy(sessionKey = sessionKey)
            meetingUiState = MeetingUiState.Success(updatedMeeting)
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