package com.example.f1liveinfo.data

import com.example.f1liveinfo.network.DriverApiService
import com.example.f1liveinfo.network.MeetingApiService
import com.example.f1liveinfo.network.PositionApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val driversRepository: DriversRepository
    val meetingsRepository: MeetingsRepository
    val positionsRepository: PositionsRepository
}

class DefaultAppContainer : AppContainer {

    private val baseUrl =
        "https://api.openf1.org/v1/"


    private val json = Json { ignoreUnknownKeys = true }

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitDriverService: DriverApiService by lazy {
        retrofit.create(DriverApiService::class.java)
    }

    override val driversRepository: DriversRepository by lazy {
        NetworkDriversRepository(retrofitDriverService)
    }

    private val retrofitMeetingService: MeetingApiService by lazy {
        retrofit.create(MeetingApiService::class.java)
    }

    override val meetingsRepository: MeetingsRepository by lazy {
        NetworkMeetingsRepository(retrofitMeetingService)
    }

    private val retrofitPositionService: PositionApiService by lazy {
        retrofit.create(PositionApiService::class.java)
    }

    override val positionsRepository: PositionsRepository by lazy {
        NetworkPositionsRepository(retrofitPositionService)
    }
}