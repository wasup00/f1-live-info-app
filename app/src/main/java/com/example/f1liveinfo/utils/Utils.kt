package com.example.f1liveinfo.utils

import android.graphics.Color.parseColor
import androidx.compose.ui.graphics.Color
import com.example.f1liveinfo.ui.screens.DriverViewModel
import com.example.f1liveinfo.ui.screens.MeetingViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.util.Log


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
        val colorInt = parseColor(hexStr)
        return Color(colorInt).copy(alpha = alpha)
    }

    /**
     * Refreshes the data for meetings and drivers.
     *
     * This function retrieves the latest meeting and driver data by calling* `getMeetingData` on the provided `meetingViewModel` and `getDriversData`
     * on the provided `driverViewModel`.
     *
     * @param meetingViewModel The ViewModel responsible for managing meeting data.
     * @param driverViewModel The ViewModel responsible for managing driver data.
     */
    fun refreshData(meetingViewModel: MeetingViewModel, driverViewModel: DriverViewModel) {
        meetingViewModel.getMeetingData()
        driverViewModel.getDriversData()
    }

    // Utility function to format date
    fun formatStringToDate(date: String): Date {
        val formatter = defaultFormatter()
        println("formatDate: $date")
        val parsedDate = formatter.parse(date)
        if (parsedDate != null) {
            return parsedDate
        }
        Log.e("formatDate", "Error parsing date: $date")
        return Date(0)
    }

    private fun defaultFormatter(pattern: String = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"): SimpleDateFormat {
        return SimpleDateFormat(pattern, Locale.getDefault())
    }
}