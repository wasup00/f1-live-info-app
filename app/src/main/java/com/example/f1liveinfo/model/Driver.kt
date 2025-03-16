package com.example.f1liveinfo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Driver(
    @SerialName("last_name")
    val lastName: String = "unknownLastName",
    @SerialName("first_name")
    val firstName: String = "unknownFirstName",
    @SerialName("country_code")
    val countryCode: String = "unknownCountryCode",
    @SerialName("full_name")
    val fullName: String = "unknownFullName",
    @SerialName("team_name")
    val teamName: String = "unknownTeamName",
    @SerialName("driver_number")
    val driverNumber: Int = 0,
    @SerialName("team_colour")
    val teamColor: String = "FFFFFF",
    @SerialName("headshot_url")
    val headshotUrl: String? = null,
    var currentPosition: Int? = null,
    var startingPosition: Int? = null,
    val latestLap: Lap? = null,
    val fastestLap: Lap? = null,
    val interval: Interval? = null,
    val tire: Tire = Tire.UNKNOWN
)