package com.example.f1liveinfo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SessionName(val value: String) {
    @SerialName("Practice 1")
    Practice1("Practice 1"),

    @SerialName("Practice 2")
    Practice2("Practice 2"),

    @SerialName("Practice 3")
    Practice3("Practice 3"),

    @SerialName("Qualifying")
    Qualifying("Qualifying"),

    @SerialName("Race")
    Race("Race");

    override fun toString(): String {
        return value
    }
}