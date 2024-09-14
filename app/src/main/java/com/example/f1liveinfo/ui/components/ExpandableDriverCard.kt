package com.example.f1liveinfo.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.f1liveinfo.model.Driver
import com.example.f1liveinfo.utils.Utils

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
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PositionCard(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .fillMaxHeight(),
                    isRace = isRace,
                    driver = driver
                )
                AsyncImage(
                    model = driver.headshotUrl,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${driver.fullName} ${driver.driverNumber}",
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
                ExpandIcon(
                    expanded = expanded,
                    onClick = { expanded = !expanded },
                    modifier = Modifier.padding(16.dp)
                )
            }
            if (expanded) {
                ExpandedDriverContent(driver = driver)
            }
        }
    }
}