package com.example.f1liveinfo.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.KeyboardDoubleArrowDown
import androidx.compose.material.icons.filled.KeyboardDoubleArrowUp
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.f1liveinfo.model.Driver
import kotlin.math.absoluteValue

@Composable
fun PositionCard(isRace: Boolean, driver: Driver, modifier: Modifier = Modifier) {
    if (isRace) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            val difference =
                if (driver.startingPosition != null || driver.currentPosition != null) {
                    driver.startingPosition?.minus(driver.currentPosition!!)!!
                } else {
                    40
                }
            val color = when {
                difference > 0 -> Color.Green
                difference < 0 -> Color.Red
                else -> Color.Black
            }
            val icon = when {
                difference > 5 -> Icons.Filled.KeyboardDoubleArrowUp
                difference > 0 -> Icons.Filled.ArrowDropUp
                difference < -5 -> Icons.Filled.KeyboardDoubleArrowDown
                difference < 0 -> Icons.Filled.ArrowDropDown
                else -> Icons.Filled.Remove
            }
            Text(
                text = driver.currentPosition.toString(),
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black,
            )
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = modifier.size(15.dp)
            )
            Text(
                text = difference.absoluteValue.toString(),
                style = MaterialTheme.typography.labelSmall,
                color = Color.Black,
            )
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = modifier,
                text = driver.currentPosition.toString(),
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black,
            )
        }
    }
}
