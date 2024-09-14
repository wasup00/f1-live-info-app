package com.example.f1liveinfo.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.f1liveinfo.viewmodel.DriversUiState

@Composable
fun DriversScreen(
    isRace: Boolean,
    driversUiState: DriversUiState,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (driversUiState) {
        is DriversUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is DriversUiState.Success -> RefreshableListOfDrivers(
            isRace = isRace,
            drivers = driversUiState.drivers,
            onRefresh = onRefresh,
            modifier = modifier.fillMaxWidth()
        )

        is DriversUiState.Error -> ErrorScreen(
            onRefresh = onRefresh,
            errorMessage = driversUiState.message,
            modifier = modifier.fillMaxSize()
        )
    }
}