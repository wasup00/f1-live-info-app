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
import com.example.f1liveinfo.data.MeetingRepository
import com.example.f1liveinfo.data.SessionRepository
import com.example.f1liveinfo.model.Meeting
import com.example.f1liveinfo.network.ApiResult
import com.example.f1liveinfo.utils.Utils.LATEST
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
        viewModelScope.launch {
            meetingUiState = when (val meetingResult = meetingRepository.getMeetings()) {
                is ApiResult.Success -> {
                    val meeting = meetingResult.data.firstOrNull()
                    if (meeting != null) {
                        when (val sessionResult =
                            sessionRepository.getSessions(sessionKey = sessionKey)) {
                            is ApiResult.Success -> {
                                val latestSession = sessionResult.data
                                MeetingUiState.Success(
                                    meeting.copy(
                                        sessions = latestSession,
                                        sessionKey = latestSession.firstOrNull()?.sessionKey ?: 0
                                    )
                                )
                            }

                            is ApiResult.Error -> MeetingUiState.Error(
                                sessionResult.exception.message ?: "Failed to retrieve sessions"
                            )
                        }
                    } else {
                        MeetingUiState.Error("No meetings found")
                    }
                }

                is ApiResult.Error -> MeetingUiState.Error(
                    meetingResult.exception.message ?: "Failed to retrieve meetings"
                )
            }
        }
    }

    fun getSessionsData() {
        if (meetingUiState is MeetingUiState.Success) {
            viewModelScope.launch {
                val currentMeeting = (meetingUiState as MeetingUiState.Success).meeting
                when (val sessionResult =
                    sessionRepository.getSessions(meetingKey = currentMeeting.meetingKey)) {
                    is ApiResult.Success -> {
                        val sessions = sessionResult.data
                        meetingUiState =
                            MeetingUiState.Success(currentMeeting.copy(sessions = sessions))
                    }

                    is ApiResult.Error -> meetingUiState = MeetingUiState.Error(
                        sessionResult.exception.message ?: "Failed to retrieve sessions"
                    )
                }
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
    data class Error(val message: String) : MeetingUiState
    object Loading : MeetingUiState
}