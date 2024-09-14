package com.example.f1liveinfo.network

import com.example.f1liveinfo.model.Meeting
import retrofit2.http.GET
import retrofit2.http.Query

interface MeetingApiService {
    @GET("meetings")
    suspend fun getMeetings(
        @Query("meeting_key") meetingKey: String? = null,
        @Query("year") year: Int? = null
    ): List<Meeting>
}