package com.example.f1liveinfo.model

import com.example.f1liveinfo.network.FlexibleValueSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Interval(
    @SerialName("gap_to_leader")
    @Serializable(with = FlexibleValueSerializer::class)
    val gapToLeader: String?,
    @SerialName("interval")
    @Serializable(with = FlexibleValueSerializer::class)
    val gapToDriverAhead: String?,
    @SerialName("meeting_key")
    val meetingKey: Int,
    @SerialName("session_key")
    val sessionKey: Int,
    @SerialName("driver_number")
    val driverNumber: Int,
    val date: String,
)