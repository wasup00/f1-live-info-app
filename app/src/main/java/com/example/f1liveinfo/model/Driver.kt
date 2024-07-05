package com.example.f1liveinfo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Driver(
    @SerialName("last_name")
    val lastName: String,
    @SerialName("first_name")
    val firstName: String,
    @SerialName("country_code")
    val countryCode: String,
    @SerialName("team_name")
    val teamName: String,
    @SerialName("driver_number")
    val driverNumber: Int,
    @SerialName("team_colour")
    val teamColour: String,
    @SerialName("headshot_url")
    val headshotUrl: String? = null
)