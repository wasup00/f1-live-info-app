package com.example.f1liveinfo.data

import com.example.f1liveinfo.model.Meeting

sealed class MeetingUiState {
    object Loading : MeetingUiState()
    data class Success(val meeting: Meeting) : MeetingUiState()
    data class Error(val message: String) : MeetingUiState()
}