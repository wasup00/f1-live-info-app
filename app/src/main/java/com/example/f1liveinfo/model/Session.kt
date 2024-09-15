package com.example.f1liveinfo.model

import com.example.f1liveinfo.utils.LocalDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Session(
    @SerialName("session_key")
    val sessionKey: Int,
    @SerialName("meeting_key")
    val meetingKey: Int,
    @SerialName("session_name")
    val sessionName: SessionName,
    @SerialName("session_type")
    val sessionType: SessionType,
//    var location: String,
//    @SerialName("country_name")
//    var countryName: String,
//    @SerialName("meeting_name")
//    var meetingName: String,
    @SerialName("gmt_offset")
    var gmtOffset: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    @SerialName("date_start")
    var dateStart: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    @SerialName("date_end")
    var dateEnd: LocalDateTime,
//    var year: Int
)