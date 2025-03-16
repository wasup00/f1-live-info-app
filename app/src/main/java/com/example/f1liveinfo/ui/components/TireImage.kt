package com.example.f1liveinfo.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.f1liveinfo.R
import com.example.f1liveinfo.model.Tire

@Composable
fun TireImage(tire: Tire, modifier: Modifier = Modifier) {
    val tireImageRes = when (tire) {
        Tire.SOFT -> R.drawable.soft
        Tire.MEDIUM -> R.drawable.medium
        Tire.HARD -> R.drawable.hard
        Tire.INTERMEDIATE -> R.drawable.intermediate
        Tire.WET -> R.drawable.wet
        Tire.UNKNOWN -> R.drawable.unknown
    }

    Image(
        painter = painterResource(id = tireImageRes),
        contentDescription = "Tire compound: ${tire.name}",
        modifier = modifier.size(24.dp)
    )
}