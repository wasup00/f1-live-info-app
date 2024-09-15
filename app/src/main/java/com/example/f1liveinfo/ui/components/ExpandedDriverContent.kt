package com.example.f1liveinfo.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.f1liveinfo.model.Driver
import com.example.f1liveinfo.model.Lap

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
        Text(
            text = "Number: ${driver.driverNumber}",
            style = MaterialTheme.typography.labelSmall,
            color = Color.Black
        )
        Text(
            text = "Country: ${driver.countryCode}",
            style = MaterialTheme.typography.labelSmall,
            color = Color.Black
        )
        driver.latestLap?.let {
            Text(
                text = "sector 1: ${it.sector1}  sector 2: ${it.sector2}  sector 3: ${it.sector3}",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Black
            )
        }


    }
}

@Preview(showBackground = true)
@Composable
fun ExpandedDriverContentPreview() {
    val driver = Driver(
        firstName = "Lewis",
        lastName = "Hamilton",
        countryCode = "British",
        teamName = "Mercedes",
        teamColor = "00D2BE",
        driverNumber = 44,
        currentPosition = 1,
        startingPosition = 5,
        fullName = "Lewis HAMILTON",
        latestLap = Lap(
            lapNumber = 1,
            driverNumber = 44,
            lapDuration = 102.056f,
            sector1 = 35.913f,
            sector2 = 40.978f,
            sector3 = 25.165f
        )
    )
    MaterialTheme {
        ExpandedDriverContent(driver = driver)
    }

}