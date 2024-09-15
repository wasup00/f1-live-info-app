package com.example.f1liveinfo.network


import com.example.f1liveinfo.model.Interval
import com.example.f1liveinfo.utils.Utils.LATEST
import retrofit2.http.GET
import retrofit2.http.Query

interface IntervalApiService {
    @GET("intervals")
    suspend fun getIntervals(
        @Query("session_key") sessionKey: String? = LATEST,
        @Query("driver_number") driverNumber: Int
    ): List<Interval>

    @GET("intervals")
    suspend fun getIntervals(@Query("session_key") sessionKey: String? = LATEST): List<Interval>
}