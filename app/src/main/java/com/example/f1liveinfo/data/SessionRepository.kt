package com.example.f1liveinfo.data

import android.util.Log
import com.example.f1liveinfo.model.Session
import com.example.f1liveinfo.network.ApiResult
import com.example.f1liveinfo.network.SessionApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Collections

private const val TAG = "SessionsRepository"

interface SessionRepository {
    suspend fun getSessions(): ApiResult<List<Session>>
    suspend fun getSessions(
        meetingKey: Int? = null,
        sessionKey: Int? = null
    ): ApiResult<List<Session>>

    suspend fun getSessions(sessionKey: String): ApiResult<List<Session>>
    //suspend fun getSessions(sessionKey: Int): Session
}

class NetworkSessionRepository(
    private val sessionApiService: SessionApiService
) : SessionRepository {
    override suspend fun getSessions(): ApiResult<List<Session>> =
        withContext(Dispatchers.IO) {
            try {
                val sessions = sessionApiService.getSessions()
                ApiResult.Success(sessions)
            } catch (e: IOException) {
                ApiResult.Error(Exception("Network error. Please check your internet connection."))
            } catch (e: Exception) {
                ApiResult.Error(e)
            }
        }

    override suspend fun getSessions(meetingKey: Int?, sessionKey: Int?): ApiResult<List<Session>> {
        var sessions: List<Session> = Collections.emptyList()
        if (meetingKey == null && sessionKey == null) {
            Log.e(TAG, "meetingKey == null && sessionKey == null")
            return ApiResult.Error(Exception("Should not go here."))

        }
        if (sessionKey != null) {
            withContext(Dispatchers.IO) {
                try {
                    sessions = sessionApiService.getSessions(sessionKey = sessionKey.toString())
                    ApiResult.Success(sessions)
                } catch (e: IOException) {
                    ApiResult.Error(Exception("Network error. Please check your internet connection."))
                } catch (e: Exception) {
                    ApiResult.Error(e)
                }
            }
        } else {
            withContext(Dispatchers.IO) {
                try {
                    sessions = sessionApiService.getSessions(meetingKey = meetingKey.toString())
                    ApiResult.Success(sessions)
                } catch (e: IOException) {
                    ApiResult.Error(Exception("Network error. Please check your internet connection."))
                } catch (e: Exception) {
                    ApiResult.Error(e)
                }
            }
        }
        return ApiResult.Success(sessions)
    }

    override suspend fun getSessions(sessionKey: String): ApiResult<List<Session>> =
        withContext(Dispatchers.IO) {
            try {
                val sessions = sessionApiService.getSessions(sessionKey = sessionKey)
                ApiResult.Success(sessions)
            } catch (e: IOException) {
                ApiResult.Error(Exception("Network error. Please check your internet connection."))
            } catch (e: Exception) {
                ApiResult.Error(e)
            }
        }
}