package com.example.f1liveinfo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Lap(
    @SerialName("session_key")
    val sessionKey: Int,
    @SerialName("meeting_key")
    val meetingKey: Int,
    @SerialName("lap_number")
    val lapNumber: Int,
    @SerialName("driver_number")
    val driverNumber: Int,
    @SerialName("lap_duration")
    val lapDuration: Float? = Float.MAX_VALUE,
    @SerialName("duration_sector_1")
    val sector1: Float?,
    @SerialName("duration_sector_2")
    val sector2: Float?,
    @SerialName("duration_sector_3")
    val sector3: Float?,
    @SerialName("is_pit_out_lap")
    val isOutLap: Boolean
)