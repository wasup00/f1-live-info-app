package com.example.f1liveinfo.data

import com.example.f1liveinfo.network.DriverApiService
import com.example.f1liveinfo.network.IntervalApiService
import com.example.f1liveinfo.network.LapApiService
import com.example.f1liveinfo.network.LoggingInterceptor
import com.example.f1liveinfo.network.MeetingApiService
import com.example.f1liveinfo.network.PositionApiService
import com.example.f1liveinfo.network.SessionApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

interface AppContainer {
    val driverRepository: DriverRepository
    val meetingRepository: MeetingRepository
    val positionRepository: PositionRepository
    val sessionRepository: SessionRepository
    val lapRepository: LapRepository
    val intervalRepository: IntervalRepository
}

class DefaultAppContainer : AppContainer {

    private val baseUrl =
        "https://api.openf1.org/v1/"


    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(LoggingInterceptor())
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .client(client)
        .baseUrl(baseUrl)
        .build()

    private val retrofitDriverService: DriverApiService by lazy {
        retrofit.create(DriverApiService::class.java)
    }

    override val driverRepository: DriverRepository by lazy {
        NetworkDriverRepository(retrofitDriverService)
    }

    private val retrofitMeetingService: MeetingApiService by lazy {
        retrofit.create(MeetingApiService::class.java)
    }

    override val meetingRepository: MeetingRepository by lazy {
        NetworkMeetingRepository(retrofitMeetingService)
    }

    private val retrofitPositionService: PositionApiService by lazy {
        retrofit.create(PositionApiService::class.java)
    }

    override val positionRepository: PositionRepository by lazy {
        NetworkPositionRepository(retrofitPositionService)
    }

    private val retrofitSessionService: SessionApiService by lazy {
        retrofit.create(SessionApiService::class.java)
    }

    override val sessionRepository: SessionRepository by lazy {
        NetworkSessionRepository(retrofitSessionService)
    }

    private val retrofitLapService: LapApiService by lazy {
        retrofit.create(LapApiService::class.java)
    }

    override val lapRepository: LapRepository by lazy {
        NetworkLapRepository(retrofitLapService)
    }

    private val retrofitIntervalService: IntervalApiService by lazy {
        retrofit.create(IntervalApiService::class.java)
    }

    override val intervalRepository: IntervalRepository by lazy {
        NetworkIntervalRepository(retrofitIntervalService)
    }
}