package com.example.f1liveinfo.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.f1liveinfo.utils.Utils
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

@Preview(showBackground = true)
@Composable
fun DriversScreenPreview() {
    DriversScreen(
        isRace = true,
        driversUiState = DriversUiState.Success(
            Utils.readDriversDataFromJson().take(5)
        ),
        onRefresh = {},
        modifier = Modifier
    )
}