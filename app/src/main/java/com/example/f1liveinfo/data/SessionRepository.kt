package com.example.f1liveinfo.data

import android.util.Log
import com.example.f1liveinfo.model.Session
import com.example.f1liveinfo.network.SessionApiService
import java.util.Collections

private const val TAG = "SessionsRepository"

interface SessionRepository {
    suspend fun getSessions(): List<Session>
    suspend fun getSessions(meetingKey: Int? = null, sessionKey: Int? = null): List<Session>
    //suspend fun getSessions(sessionKey: Int): Session
}

class NetworkSessionRepository(
    private val sessionApiService: SessionApiService
) : SessionRepository {
    override suspend fun getSessions(): List<Session> = sessionApiService.getSessions()
    override suspend fun getSessions(meetingKey: Int?, sessionKey: Int?): List<Session> {
        if (meetingKey == null && sessionKey == null) {
            Log.e(TAG, "meetingKey == null && sessionKey == null")
            return Collections.emptyList()
        }
        if (sessionKey != null) {
            return sessionApiService.getSessions(sessionKey = sessionKey.toString())
        }
        return sessionApiService.getSessions(meetingKey = meetingKey.toString())
    }
}