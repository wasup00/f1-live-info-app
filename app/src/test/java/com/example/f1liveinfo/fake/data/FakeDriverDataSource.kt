package com.example.f1liveinfo.fake.data

import com.example.f1liveinfo.model.Driver
import com.example.f1liveinfo.utils.Utils

object FakeDriverDataSource {

    val driversFromData =
        Utils.readDriversDataFromJson() //json.decodeFromString<List<Driver>>(jsonString)

    val expectedDriversAndPositions = listOf(
        Driver(
            lastName = "Verstappen",
            firstName = "Max",
            countryCode = "NED",
            fullName = "Max VERSTAPPEN",
            teamName = "Red Bull Racing",
            driverNumber = 1,
            teamColor = "3671C6",
            currentPosition = 5,
            startingPosition = 11
        ), Driver(
            lastName = "Sargeant",
            firstName = "Logan",
            countryCode = "USA",
            fullName = "Logan SARGEANT",
            teamName = "Williams",
            driverNumber = 2,
            teamColor = "64C4FF",
            currentPosition = 19,
            startingPosition = 18
        ), Driver(
            lastName = "Ricciardo",
            firstName = "Daniel",
            countryCode = "AUS",
            fullName = "Daniel RICCIARDO",
            teamName = "RB",
            driverNumber = 3,
            teamColor = "6692FF",
            currentPosition = 11,
            startingPosition = 13
        ), Driver(
            lastName = "Norris",
            firstName = "Lando",
            countryCode = "GBR",
            fullName = "Lando NORRIS",
            teamName = "McLaren",
            driverNumber = 4,
            teamColor = "FF8000",
            currentPosition = 6,
            startingPosition = 4
        ), Driver(
            lastName = "Gasly",
            firstName = "Pierre",
            countryCode = "FRA",
            fullName = "Pierre GASLY",
            teamName = "Alpine",
            driverNumber = 10,
            teamColor = "0093CC",
            currentPosition = 14,
            startingPosition = 12
        ), Driver(
            lastName = "Perez",
            firstName = "Sergio",
            countryCode = "MEX",
            fullName = "Sergio PEREZ",
            teamName = "Red Bull Racing",
            driverNumber = 11,
            teamColor = "3671C6",
            currentPosition = 8,
            startingPosition = 2
        ), Driver(
            lastName = "Alonso",
            firstName = "Fernando",
            countryCode = "ESP",
            fullName = "Fernando ALONSO",
            teamName = "Aston Martin",
            driverNumber = 14,
            teamColor = "229971",
            currentPosition = 9,
            startingPosition = 8
        ), Driver(
            lastName = "Leclerc",
            firstName = "Charles",
            countryCode = "MON",
            fullName = "Charles LECLERC",
            teamName = "Ferrari",
            driverNumber = 16,
            teamColor = "E80020",
            currentPosition = 4,
            startingPosition = 1
        ), Driver(
            lastName = "Stroll",
            firstName = "Lance",
            countryCode = "CAN",
            fullName = "Lance STROLL",
            teamName = "Aston Martin",
            driverNumber = 18,
            teamColor = "229971",
            currentPosition = 12,
            startingPosition = 15
        ), Driver(
            lastName = "Magnussen",
            firstName = "Kevin",
            countryCode = "DEN",
            fullName = "Kevin MAGNUSSEN",
            teamName = "Haas F1 Team",
            driverNumber = 20,
            teamColor = "B6BABD",
            currentPosition = 15,
            startingPosition = 17
        ), Driver(
            lastName = "Tsunoda",
            firstName = "Yuki",
            countryCode = "JPN",
            fullName = "Yuki TSUNODA",
            teamName = "RB",
            driverNumber = 22,
            teamColor = "6692FF",
            currentPosition = 17,
            startingPosition = 20
        ), Driver(
            lastName = "Albon",
            firstName = "Alexander",
            countryCode = "THA",
            fullName = "Alexander ALBON",
            teamName = "Williams",
            driverNumber = 23,
            teamColor = "64C4FF",
            currentPosition = 13,
            startingPosition = 10
        ), Driver(
            lastName = "Zhou",
            firstName = "Guanyu",
            countryCode = "CHN",
            fullName = "ZHOU Guanyu",
            teamName = "Kick Sauber",
            driverNumber = 24,
            teamColor = "52E252",
            currentPosition = 20,
            startingPosition = 19
        ), Driver(
            lastName = "Hulkenberg",
            firstName = "Nico",
            countryCode = "GER",
            fullName = "Nico HULKENBERG",
            teamName = "Haas F1 Team",
            driverNumber = 27,
            teamColor = "B6BABD",
            currentPosition = 18,
            startingPosition = 16
        ), Driver(
            lastName = "Ocon",
            firstName = "Esteban",
            countryCode = "FRA",
            fullName = "Esteban OCON",
            teamName = "Alpine",
            driverNumber = 31,
            teamColor = "0093CC",
            currentPosition = 10,
            startingPosition = 9
        ), Driver(
            lastName = "Hamilton",
            firstName = "Lewis",
            countryCode = "GBR",
            fullName = "Lewis HAMILTON",
            teamName = "Mercedes",
            driverNumber = 44,
            teamColor = "27F4D2",
            currentPosition = 2,
            startingPosition = 3
        ), Driver(
            lastName = "Sainz",
            firstName = "Carlos",
            countryCode = "ESP",
            fullName = "Carlos SAINZ",
            teamName = "Ferrari",
            driverNumber = 55,
            teamColor = "E80020",
            currentPosition = 7,
            startingPosition = 7
        ), Driver(
            lastName = "Russell",
            firstName = "George",
            countryCode = "GBR",
            fullName = "George RUSSELL",
            teamName = "Mercedes",
            driverNumber = 63,
            teamColor = "27F4D2",
            currentPosition = 1,
            startingPosition = 6
        ), Driver(
            lastName = "Bottas",
            firstName = "Valtteri",
            countryCode = "FIN",
            fullName = "Valtteri BOTTAS",
            teamName = "Kick Sauber",
            driverNumber = 77,
            teamColor = "52E252",
            currentPosition = 16,
            startingPosition = 14
        ), Driver(
            lastName = "Piastri",
            firstName = "Oscar",
            countryCode = "AUS",
            fullName = "Oscar PIASTRI",
            teamName = "McLaren",
            driverNumber = 81,
            teamColor = "FF8000",
            currentPosition = 3,
            startingPosition = 5
        )
    ).sortedBy { it.currentPosition }
}