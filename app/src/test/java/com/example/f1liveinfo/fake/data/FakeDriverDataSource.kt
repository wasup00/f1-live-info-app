package com.example.f1liveinfo.fake.data

import com.example.f1liveinfo.model.Driver
import kotlinx.serialization.json.Json
import java.io.InputStream

object FakeDriverDataSource {

    private val json = Json { ignoreUnknownKeys = true }
    private val inputStream: InputStream =
        javaClass.classLoader!!.getResourceAsStream("data_drivers.json")
    private val jsonString = inputStream.bufferedReader().use { it.readText() }

    val driversFromData = json.decodeFromString<List<Driver>>(jsonString)

    val expectedDriversAndPositions = listOf(
        Driver(
            lastName = "Verstappen",
            firstName = "Max",
            countryCode = "NED",
            teamName = "Red Bull Racing",
            driverNumber = 1,
            teamColor = "3671C6",
            currentPosition = 5
        ), Driver(
            lastName = "Sargeant",
            firstName = "Logan",
            countryCode = "USA",
            teamName = "Williams",
            driverNumber = 2,
            teamColor = "64C4FF",
            currentPosition = 19
        ), Driver(
            lastName = "Ricciardo",
            firstName = "Daniel",
            countryCode = "AUS",
            teamName = "RB",
            driverNumber = 3,
            teamColor = "6692FF",
            currentPosition = 11
        ), Driver(
            lastName = "Norris",
            firstName = "Lando",
            countryCode = "GBR",
            teamName = "McLaren",
            driverNumber = 4,
            teamColor = "FF8000",
            currentPosition = 6
        ), Driver(
            lastName = "Gasly",
            firstName = "Pierre",
            countryCode = "FRA",
            teamName = "Alpine",
            driverNumber = 10,
            teamColor = "0093CC",
            currentPosition = 14
        ), Driver(
            lastName = "Perez",
            firstName = "Sergio",
            countryCode = "MEX",
            teamName = "Red Bull Racing",
            driverNumber = 11,
            teamColor = "3671C6",
            currentPosition = 8
        ), Driver(
            lastName = "Alonso",
            firstName = "Fernando",
            countryCode = "ESP",
            teamName = "Aston Martin",
            driverNumber = 14,
            teamColor = "229971",
            currentPosition = 9
        ), Driver(
            lastName = "Leclerc",
            firstName = "Charles",
            countryCode = "MON",
            teamName = "Ferrari",
            driverNumber = 16,
            teamColor = "E80020",
            currentPosition = 4
        ), Driver(
            lastName = "Stroll",
            firstName = "Lance",
            countryCode = "CAN",
            teamName = "Aston Martin",
            driverNumber = 18,
            teamColor = "229971",
            currentPosition = 12
        ), Driver(
            lastName = "Magnussen",
            firstName = "Kevin",
            countryCode = "DEN",
            teamName = "Haas F1 Team",
            driverNumber = 20,
            teamColor = "B6BABD",
            currentPosition = 15
        ), Driver(
            lastName = "Tsunoda",
            firstName = "Yuki",
            countryCode = "JPN",
            teamName = "RB",
            driverNumber = 22,
            teamColor = "6692FF",
            currentPosition = 17
        ), Driver(
            lastName = "Albon",
            firstName = "Alexander",
            countryCode = "THA",
            teamName = "Williams",
            driverNumber = 23,
            teamColor = "64C4FF",
            currentPosition = 13
        ), Driver(
            lastName = "Zhou",
            firstName = "Guanyu",
            countryCode = "CHN",
            teamName = "Kick Sauber",
            driverNumber = 24,
            teamColor = "52E252",
            currentPosition = 20
        ), Driver(
            lastName = "Hulkenberg",
            firstName = "Nico",
            countryCode = "GER",
            teamName = "Haas F1 Team",
            driverNumber = 27,
            teamColor = "B6BABD",
            currentPosition = 18
        ), Driver(
            lastName = "Ocon",
            firstName = "Esteban",
            countryCode = "FRA",
            teamName = "Alpine",
            driverNumber = 31,
            teamColor = "0093CC",
            currentPosition = 10
        ), Driver(
            lastName = "Hamilton",
            firstName = "Lewis",
            countryCode = "GBR",
            teamName = "Mercedes",
            driverNumber = 44,
            teamColor = "27F4D2",
            currentPosition = 2
        ), Driver(
            lastName = "Sainz",
            firstName = "Carlos",
            countryCode = "ESP",
            teamName = "Ferrari",
            driverNumber = 55,
            teamColor = "E80020",
            currentPosition = 7
        ), Driver(
            lastName = "Russell",
            firstName = "George",
            countryCode = "GBR",
            teamName = "Mercedes",
            driverNumber = 63,
            teamColor = "27F4D2",
            currentPosition = 1
        ), Driver(
            lastName = "Bottas",
            firstName = "Valtteri",
            countryCode = "FIN",
            teamName = "Kick Sauber",
            driverNumber = 77,
            teamColor = "52E252",
            currentPosition = 16
        ), Driver(
            lastName = "Piastri",
            firstName = "Oscar",
            countryCode = "AUS",
            teamName = "McLaren",
            driverNumber = 81,
            teamColor = "FF8000",
            currentPosition = 3
        )
    ).sortedBy { it.currentPosition }
}