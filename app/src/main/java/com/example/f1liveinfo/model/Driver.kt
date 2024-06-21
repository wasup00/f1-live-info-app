package com.example.f1liveinfo.model

data class Driver(
    val lastName: String,
    val firstName: String,
    val countryCode: String,
    val teamName: String,
    val driverNumber: Int,
    val teamColor: String,
    val headshotUrl: String? = null
)