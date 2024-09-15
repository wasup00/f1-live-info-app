package com.example.f1liveinfo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Driver(
    @SerialName("last_name")
    val lastName: String = "defaultLastName",
    @SerialName("first_name")
    val firstName: String = "defaultFirstName",
    @SerialName("country_code")
    val countryCode: String = "defaultCountryCode",
    @SerialName("full_name")
    val fullName: String = "defaultFullName",
    @SerialName("team_name")
    val teamName: String = "defaultTeamName",
    @SerialName("driver_number")
    val driverNumber: Int = 0,
    @SerialName("team_colour")
    val teamColor: String = "FFFFFF",
    @SerialName("headshot_url")
    val headshotUrl: String? = null,
    var currentPosition: Int? = null,
    var startingPosition: Int? = null,
    val latestLap: Lap? = null
)
//{
//    companion object {
//        fun driversToDriverPositionsMap(drivers: List<Driver>): HashMap<Int, Int> {
//            val driverPositions = HashMap<Int, Int>()
////            for (driver in drivers) {driverPositions[driver.driverNumber] = driver.position!!
////            }
//            drivers.forEach { driver ->
//                driverPositions[driver.driverNumber] = driver.currentPosition!!
//            }
//            return driverPositions
//        }
//    }
//}