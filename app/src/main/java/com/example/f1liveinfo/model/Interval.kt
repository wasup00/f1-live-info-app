package com.example.f1liveinfo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Interval(
    @SerialName("gap_to_leader")
    val gapToLeader: Float?,
    val interval: Float?,
    @SerialName("meeting_key")
    val meetingKey: Int,
    @SerialName("session_key")
    val sessionKey: Int,
    @SerialName("driver_number")
    val driverNumber: Int
)