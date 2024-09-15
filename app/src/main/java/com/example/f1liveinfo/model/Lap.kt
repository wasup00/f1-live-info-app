package com.example.f1liveinfo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Lap(
    @SerialName("lap_number")
    val lapNumber: Int,
    @SerialName("driver_number")
    val driverNumber: Int,
    @SerialName("lap_duration")
    val lapDuration: Float?,
    @SerialName("duration_sector_1")
    val sector1: Float?,
    @SerialName("duration_sector_2")
    val sector2: Float?,
    @SerialName("duration_sector_3")
    val sector3: Float?,
)