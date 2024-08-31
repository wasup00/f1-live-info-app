package com.example.f1liveinfo.utils

import android.graphics.Color.parseColor
import androidx.compose.ui.graphics.Color
import com.example.f1liveinfo.ui.screens.DriverViewModel


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
    fun refreshMeetingAndDriverData(driverViewModel: DriverViewModel) {
        driverViewModel.getDriversData()
    }

    fun fetchDriverData(
        driverViewModel: DriverViewModel,
        sessionKey: String
    ) {
        driverViewModel.getDriversData(sessionKey = sessionKey)
    }
}