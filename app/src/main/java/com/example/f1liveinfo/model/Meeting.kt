package com.example.f1liveinfo.model

data class Meeting(
//    val meetingKey: Int,
    val location: String,
    val countryName: String,
    val meetingName: String,
    val gmtOffset: String,
    val dateStart: String,
    val year: Int
)
