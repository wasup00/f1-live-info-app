package com.example.f1liveinfo.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.f1liveinfo.model.Driver
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RefreshableListOfDrivers(
    isRace: Boolean,
    drivers: List<Driver>,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val refreshing by remember { mutableStateOf(false) }
    val state = rememberPullRefreshState(refreshing = refreshing, onRefresh = onRefresh)
    Box(modifier = modifier.pullRefresh(state)) {
        LazyColumn(
            modifier = Modifier,
        ) {
            items(drivers) { driver ->
                ExpandableDriverCard(isRace = isRace, driver = driver)
            }
        }
        PullRefreshIndicator(
            refreshing = refreshing,
            state = state,
            modifier = Modifier
                .align(Alignment.TopCenter)
        )
    }
}