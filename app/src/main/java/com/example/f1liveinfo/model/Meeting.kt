package com.example.f1liveinfo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Meeting(
    @SerialName("meeting_key")
    val meetingKey: Int,
    var location: String,
    @SerialName("country_name")
    var countryName: String,
    @SerialName("meeting_name")
    var meetingName: String,
    @SerialName("gmt_offset")
    var gmtOffset: String,
    @SerialName("date_start")
    var dateStart: String,
    var year: Int,
    var sessions: List<Session> = listOf()
)