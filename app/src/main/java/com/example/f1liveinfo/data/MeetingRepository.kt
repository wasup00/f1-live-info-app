package com.example.f1liveinfo.data

import com.example.f1liveinfo.model.Meeting
import com.example.f1liveinfo.network.MeetingApiService
import com.example.f1liveinfo.utils.Utils.LATEST

interface MeetingRepository {
    suspend fun getMeetings(): List<Meeting>
    suspend fun getMeeting(year: Int): List<Meeting>
}

class NetworkMeetingRepository(
    private val meetingApiService: MeetingApiService
) : MeetingRepository {
    override suspend fun getMeetings(): List<Meeting> =
        meetingApiService.getMeetings(meetingKey = LATEST)

    override suspend fun getMeeting(year: Int): List<Meeting> =
        meetingApiService.getMeetings(year = year)
}