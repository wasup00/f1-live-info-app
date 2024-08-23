package com.example.f1liveinfo.network

import com.example.f1liveinfo.model.Meeting
import com.example.f1liveinfo.utils.Utils.LATEST
import retrofit2.http.GET
import retrofit2.http.Query

interface MeetingApiService {
    @GET("meetings")
    suspend fun getMeetings(@Query("meeting_key") sessionKey: String = LATEST): List<Meeting>
}