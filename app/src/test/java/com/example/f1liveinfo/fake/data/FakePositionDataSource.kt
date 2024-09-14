package com.example.f1liveinfo.fake.data

import com.example.f1liveinfo.model.Position
import kotlinx.serialization.json.Json
import java.io.InputStream

object FakePositionDataSource {

    private val json = Json { ignoreUnknownKeys = true }
    private val inputStream: InputStream =
        javaClass.classLoader!!.getResourceAsStream("data_positions.json")
    private val jsonString = inputStream.bufferedReader().use { it.readText() }

    val positionsFromData = json.decodeFromString<List<Position>>(jsonString)
}