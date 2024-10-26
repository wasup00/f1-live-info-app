package com.example.f1liveinfo.data

import com.example.f1liveinfo.model.Session
import com.example.f1liveinfo.network.ApiResult
import com.example.f1liveinfo.network.SessionApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

private const val TAG = "SessionsRepository"

interface SessionRepository {
    suspend fun getSessions(meetingKey: Int): ApiResult<List<Session>>
}

class NetworkSessionRepository(
    private val sessionApiService: SessionApiService
) : SessionRepository {

    override suspend fun getSessions(meetingKey: Int): ApiResult<List<Session>> =
        withContext(Dispatchers.IO) {
            try {
                ApiResult.Success(sessionApiService.getSessions(meetingKey = meetingKey.toString()))
            } catch (e: IOException) {
                ApiResult.Error(Exception("Network error. Please check your internet connection."))
            } catch (e: Exception) {
                ApiResult.Error(e)
            }
        }
}