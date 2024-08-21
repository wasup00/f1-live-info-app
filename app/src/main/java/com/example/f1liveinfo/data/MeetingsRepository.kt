package com.example.f1liveinfo.data

import com.example.f1liveinfo.model.Meeting
import com.example.f1liveinfo.network.MeetingApiService

interface MeetingsRepository {
    suspend fun getMeetings(): List<Meeting>
}

class NetworkMeetingsRepository(
    private val meetingApiService: MeetingApiService
) : MeetingsRepository {
    override suspend fun getMeetings(): List<Meeting> = meetingApiService.getMeetings()
}