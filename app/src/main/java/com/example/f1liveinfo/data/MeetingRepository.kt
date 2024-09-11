package com.example.f1liveinfo.data

import com.example.f1liveinfo.model.Meeting
import com.example.f1liveinfo.network.ApiResult
import com.example.f1liveinfo.network.MeetingApiService
import com.example.f1liveinfo.utils.Utils.LATEST
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

interface MeetingRepository {
    suspend fun getMeetings(): ApiResult<List<Meeting>>
    suspend fun getMeetings(year: Int): ApiResult<List<Meeting>>
}

class NetworkMeetingRepository(
    private val meetingApiService: MeetingApiService
) : MeetingRepository {
    override suspend fun getMeetings(): ApiResult<List<Meeting>> =
        withContext(Dispatchers.IO) {
            try {
                val meetings = meetingApiService.getMeetings(meetingKey = LATEST)
                ApiResult.Success(meetings)
            } catch (e: IOException) {
                ApiResult.Error(Exception("Network error. Please check your internet connection."))
            } catch (e: Exception) {
                ApiResult.Error(e)
            }
        }

    override suspend fun getMeetings(year: Int): ApiResult<List<Meeting>> =
        withContext(Dispatchers.IO) {
            try {
                val meetings = meetingApiService.getMeetings(year = year)
                ApiResult.Success(meetings)
            } catch (e: IOException) {
                ApiResult.Error(Exception("Network error. Please check your internet connection."))
            } catch (e: Exception) {
                ApiResult.Error(e)
            }
        }
}