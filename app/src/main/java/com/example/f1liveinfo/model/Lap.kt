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

fun Lap.convertLapDurationToString(): String {
    if (lapDuration == null){
        return ""
    }
    val milliseconds = ((lapDuration % 1) * 1000).toInt()
    val seconds = lapDuration.toInt() % 60
    val minutes = lapDuration.toInt() / 60

    val millisecondsString = milliseconds.toString().padStart(3, '0')
    val secondsString = seconds.toString().padStart(2, '0')
    val minutesString = minutes.toString().padStart(2, '0')
    return "${minutesString}:${secondsString}.${millisecondsString}"
}