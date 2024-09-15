package com.example.f1liveinfo.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.f1liveinfo.model.Driver
import com.example.f1liveinfo.model.Lap
import com.example.f1liveinfo.utils.Utils
import com.example.f1liveinfo.utils.Utils.convertLapDurationToString

@Composable
fun ExpandableDriverCard(isRace: Boolean, driver: Driver, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    val teamColor = Utils.convertToColor(driver.teamColor, 0.9f)

    Card(
        colors = CardDefaults.cardColors(teamColor),
        modifier = modifier
            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PositionCard(
                modifier = Modifier
                    .padding(start = 6.dp, end = 6.dp),//.weight(1f),
                isRace = isRace,
                driver = driver
            )
            AsyncImage(
                model = driver.headshotUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                //.weight(1f)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 6.dp, end = 9.dp)
            ) {
                Text(
                    text = driver.fullName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )
                Text(
                    text = driver.teamName,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
            }
            driver.latestLap?.let {
                Text(
                    text = it.convertLapDurationToString(),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black,
                    modifier = Modifier.weight(0.4f)
                )
            }
            ExpandIcon(
                expanded = expanded,
                onClick = { expanded = !expanded },
                modifier = Modifier
                    .padding(8.dp)
                    .weight(0.2f),
            )
        }
        if (expanded) {
            ExpandedDriverContent(driver = driver)
        }

    }
}

@Preview(showBackground = false)
@Composable
fun ExpandableDriverCardPreview() {
    MaterialTheme {
        ExpandableDriverCard(
            isRace = false,
            driver = Driver(
                firstName = "Lewis",
                lastName = "Hamilton",
                countryCode = "British",
                teamName = "Mercedes",
                teamColor = "00D2BE",
                driverNumber = 44,
                currentPosition = 1,
                startingPosition = 1,
                fullName = "Lewis HAMILTON",
                latestLap = Lap(
                    lapNumber = 1,
                    driverNumber = 44,
                    lapDuration = 1.234f,
                    sector1 = null,
                    sector2 = null,
                    sector3 = null
                )
            )
        )
    }
}

@Preview(showBackground = false)
@Composable
fun ExpandableDriverCardForRacePreview() {
    MaterialTheme {
        ExpandableDriverCard(
            isRace = true,
            driver = Driver(
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
        )
    }
}