package com.example.f1liveinfo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Meeting(
//    val meetingKey: Int,
    var location: String = "",
    @SerialName("country_name")
    var countryName: String = "",
    @SerialName("meeting_name")
    var meetingName: String = "",
    @SerialName("gmt_offset")
    var gmtOffset: String = "",
    @SerialName("date_start")
    var dateStart: String = "",
    var year: Int = 2024
) {
    companion object {
        @Volatile
        private var instance: Meeting? = null

        fun getInstance(): Meeting {
            return instance ?: synchronized(this) {
                instance ?: Meeting().also { instance = it }
            }
        }
    }
}