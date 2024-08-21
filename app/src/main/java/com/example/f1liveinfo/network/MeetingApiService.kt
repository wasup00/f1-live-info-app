package com.example.f1liveinfo.network

import com.example.f1liveinfo.model.Meeting
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.jar.Pack200.Packer.LATEST

interface MeetingApiService {
    @GET("meetings")
    suspend fun getMeetings(@Query("meeting_key") sessionKey: String = LATEST): List<Meeting>
}