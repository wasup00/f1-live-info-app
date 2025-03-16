package com.example.f1liveinfo.utils

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import com.example.f1liveinfo.model.Driver
import com.example.f1liveinfo.model.Interval
import com.example.f1liveinfo.model.Lap
import com.example.f1liveinfo.model.Session
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.time.ZoneId


object Utils {

    const val LATEST = "latest"

    /**
     * Converts a color string to a [Color] object.
     *
     * This function takes a color string in hexadecimal format (e.g., "FF0000" for red)
     * and converts it into a [Color] object. You can optionally specify an alpha value
     * for the color.
     *
     * @param colorStr The color string in hexadecimal format (without the '#').
     * @param alpha The alpha value for the color (default is 1.0f).
     * @return A [Color] object representing the converted color.
     */
    fun convertToColor(colorStr: String, alpha: Float = 1.0f): Color {
        val hexStr = "#${colorStr}"
        val colorInt = hexStr.toColorInt()
        return Color(colorInt).copy(alpha = alpha)
    }

    fun Lap.convertLapDurationToString(): String {
        if (lapDuration == null) {
            return ""
        }
        val milliseconds = ((lapDuration % 1) * 1000).toInt()
        val seconds = lapDuration.toInt() % 60
        val minutes = lapDuration.toInt() / 60

        val millisecondsString = milliseconds.toString().padStart(3, '0')
        val secondsString = seconds.toString().padStart(2, '0')
        val minutesString = minutes.toString().padStart(2, '0')
        return "${minutesString}:${secondsString}.${millisecondsString}"
    }


    fun Session.adjustForGmtOffset(): Session {
        val zoneId = ZoneId.systemDefault()
        return copy(
            dateStart = dateStart.atZone(ZoneId.of("UTC")).withZoneSameInstant(zoneId)
                .toLocalDateTime(),
            dateEnd = dateEnd.atZone(ZoneId.of("UTC")).withZoneSameInstant(zoneId).toLocalDateTime()
        )
    }

    fun Session.getFormatedDate(): String {
        val month = dateStart.monthValue.toString().padStart(2, '0')
        val day = dateStart.dayOfMonth.toString().padStart(2, '0')
        return "$day-$month-${dateStart.year}"
    }

    fun Session.getFormatedTime(): String {
        val hour = dateStart.hour.toString().padStart(2, '0')
        val minute = dateStart.minute.toString().padStart(2, '0')
        val second = dateStart.second.toString().padStart(2, '0')

        return "${hour}:${minute}:${second}"
    }

    fun Interval.convertGapToDriverAheadToString(): String {
        if (gapToDriverAhead == null) {
            return ""
        }

        if (gapToDriverAhead.endsWith(" L")) {
            val laps = gapToDriverAhead.removeSuffix(" L")
            return "+$laps LAP${if (laps != "1") "S" else ""}"
        }

        try {
            val gapFloat = gapToDriverAhead.toFloat()
            val milliseconds = ((gapFloat % 1) * 1000).toInt()
            val seconds = gapFloat.toInt() % 60
            val minutes = gapFloat.toInt() / 60

            val millisecondsString = milliseconds.toString().padStart(3, '0')
            val secondsString = seconds.toString().padStart(2, '0')
            val minutesString = minutes.toString().padStart(2, '0')
            return "${minutesString}:${secondsString}.${millisecondsString}"
        } catch (e: NumberFormatException) {
            return gapToDriverAhead
        }
    }

    fun Interval.getFormatedIntervalGapToLeader(): String {
        if (gapToLeader == null) {
            return ""
        }

        if (gapToLeader.endsWith(" L")) {
            val laps = gapToLeader.removeSuffix(" L")
            return "+$laps LAP${if (laps != "1") "S" else ""}"
        }

        try {
            val gapFloat = gapToLeader.toFloat()
            val milliseconds = ((gapFloat % 1) * 1000).toInt()
            val seconds = gapFloat.toInt() % 60
            val minutes = gapFloat.toInt() / 60

            val millisecondsString = milliseconds.toString().padStart(3, '0')
            val secondsString = seconds.toString().padStart(2, '0')
            val minutesString = minutes.toString().padStart(2, '0')
            return "${minutesString}:${secondsString}.${millisecondsString}"
        } catch (e: NumberFormatException) {
            return gapToLeader
        }
    }

    fun readDriversDataFromJson(): List<Driver> {
        val json = Json { ignoreUnknownKeys = true }
        val inputStream: InputStream =
            javaClass.classLoader!!.getResourceAsStream("data_drivers.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }

        return json.decodeFromString<List<Driver>>(jsonString)
    }
}