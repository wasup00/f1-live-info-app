package com.example.f1liveinfo.viewmodel

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
import com.example.f1liveinfo.network.ApiResult
import com.example.f1liveinfo.utils.Utils.LATEST
import com.example.f1liveinfo.utils.Utils.adjustForGmtOffset
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

    fun getMeetingData(meetingKey: String = LATEST) {
        meetingUiState = MeetingUiState.Loading
        viewModelScope.launch {
            meetingUiState =
                when (val meetingResult = meetingRepository.getMeetings(meetingKey = meetingKey)) {
                    is ApiResult.Success -> {
                        val meeting = meetingResult.data.maxByOrNull { it.meetingKey }
                        if (meeting != null) {
                            updateSessions(meeting = meeting)
                        } else {
                            MeetingUiState.Error("No meetings found")
                        }
                    }

                    is ApiResult.Error -> {
                        meetingResult.exception.message?.let { Log.e("$TAG-MEETING", it) }
                        MeetingUiState.Error(
                            meetingResult.exception.message ?: "Failed to retrieve meetings"
                        )
                    }
                }
        }
    }

    private suspend fun updateSessions(meeting: Meeting): MeetingUiState {
        when (val sessionsResult = sessionRepository.getSessions(meetingKey = meeting.meetingKey)) {
            is ApiResult.Success -> {
                val sessions =
                    sessionsResult.data.map { it.adjustForGmtOffset() }
                Log.i("$TAG-SESSION", "latestSession: $sessions")
                return MeetingUiState.Success(
                    meeting.copy(
                        sessions = sessions,
                        sessionKey = sessions.maxByOrNull { it.sessionKey }!!.sessionKey
                    )
                )
            }

            is ApiResult.Error -> {
                sessionsResult.exception.message?.let { Log.e("$TAG-SESSION", it) }
                return MeetingUiState.Error(
                    sessionsResult.exception.message ?: "Failed to retrieve sessions"
                )
            }
        }
    }

    fun modifySessionKeyInMeeting(sessionKey: Int) {
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