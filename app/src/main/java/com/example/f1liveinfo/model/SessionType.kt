package com.example.f1liveinfo.model

enum class SessionType(private val value: String) {
    Practice("Practice"),
    Qualifying("Qualifying"),
    Sprint("Sprint"),
    Race("Race");

    override fun toString(): String {
        return value
    }
}