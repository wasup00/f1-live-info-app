package com.example.f1liveinfo.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.f1liveinfo.model.Driver

@Composable
fun ExpandedDriverContent(driver: Driver, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(
            start = 16.dp,
            top = 8.dp,
            bottom = 16.dp,
            end = 16.dp
        )
    ) {
        // Add more driver information here
        Text("Country: ${driver.countryCode}")
        Text("Driver Number: ${driver.driverNumber}")
        // Add more fields as needed
    }
}