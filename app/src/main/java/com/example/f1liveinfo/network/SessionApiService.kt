package com.example.f1liveinfo.network

import com.example.f1liveinfo.model.Session
import retrofit2.http.GET
import retrofit2.http.Query

interface SessionApiService {

    //Get all sessions
//    @GET("sessions")
//    suspend fun getSessions(): List<Session>
//
//    @GET("sessions")
//    suspend fun getSessions(
//        @Query("meeting_key") meetingKey: String? = null,
//        @Query("session_key") sessionKey: String? = null
//    ): List<Session>
//
//    @GET("sessions")
//    suspend fun getSessions(
//        @Query("meeting_key") meetingKey: Int? = null,
//        @Query("session_key") sessionKey: Int? = null
//    ): List<Session>
//
//    @GET("sessions")
//    suspend fun getSessions(
//        @Query("meeting_key") meetingKey: String? = null,
//        @Query("session_key") sessionKey: Int? = null
//    ): List<Session>
//
//    @GET("sessions")
//    suspend fun getSessions(
//        @Query("meeting_key") meetingKey: Int? = null,
//        @Query("session_key") sessionKey: String? = null
//    ): List<Session>

    @GET("sessions")
    suspend fun getSessions(
        @Query("meeting_key") meetingKey: String? = null,
        @Query("session_key") sessionKey: String? = null
    ): List<Session>
}