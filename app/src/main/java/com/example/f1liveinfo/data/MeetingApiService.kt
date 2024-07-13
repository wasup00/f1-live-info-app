package com.example.f1liveinfo.data

import com.example.f1liveinfo.model.Meeting
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

private val json = Json { ignoreUnknownKeys = true }

private val retrofit = Retrofit.Builder()
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface MeetingApiService {
    @GET("meetings")
    suspend fun getMeetings(@Query("meeting_key") sessionKey: String = LATEST): List<Meeting>
}

object MeetingApi {
    val retrofitService: MeetingApiService by lazy {
        retrofit.create(MeetingApiService::class.java)
    }
}