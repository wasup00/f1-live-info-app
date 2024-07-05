package com.example.f1liveinfo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.f1liveinfo.data.MeetingUiState
import com.example.f1liveinfo.model.Driver
import com.example.f1liveinfo.model.Meeting
import com.example.f1liveinfo.ui.DriversViewModel
import com.example.f1liveinfo.ui.MeetingViewModel
import com.example.f1liveinfo.ui.theme.F1LiveInfoTheme

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            F1LiveInfoTheme {
                F1App()
            }
        }
    }
}

fun parseColor(colorStr: String): Color {
    val hexStr = "#${colorStr}"
    return Color(android.graphics.Color.parseColor(hexStr))
}

@Composable
private fun F1App(
    meetingViewModel: MeetingViewModel = viewModel(),
    driverViewModel: DriversViewModel = viewModel()
) {
    val meetingUiState by meetingViewModel.meetingUiState.collectAsState()
    val drivers = driverViewModel.drivers.observeAsState(emptyList()).value

    Log.d(TAG, "Drivers: $drivers")
    F1App(
        meetingUiState = meetingUiState,
        drivers = drivers
    )
}

@Composable
fun F1App(
    meetingUiState: MeetingUiState,
    drivers: List<Driver>,
    modifier: Modifier = Modifier
) {

    Log.d(TAG, "F1App: $meetingUiState")

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            when (meetingUiState) {
                is MeetingUiState.Loading -> {
                    F1TopBar(errorMessage = "")
                }

                is MeetingUiState.Success -> {
                    F1TopBar(meeting = meetingUiState.meeting)
                }

                is MeetingUiState.Error -> {
                    F1TopBar(errorMessage = meetingUiState.message)
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            ListOfDrivers(drivers = drivers)
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun F1TopBar(errorMessage: String, modifier: Modifier = Modifier) {
    // TODO: Modify TopBar to display race status
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Red,
            titleContentColor = Color.Black,
        ),
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = errorMessage,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },

        //Might be useful later
        /* navigationIcon = {
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        },
        actions = {
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Localized description"
                )
            }
        },*/
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun F1TopBar(meeting: Meeting, modifier: Modifier = Modifier) {
    // TODO: Modify TopBar to display race status
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Red,
            titleContentColor = Color.Black,
        ),
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = meeting.meetingName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${meeting.location}, ${meeting.countryName}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        },

        //Might be useful later
        /* navigationIcon = {
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        },
        actions = {
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Localized description"
                )
            }
        },*/
    )
}

@Composable
fun ListOfDrivers(drivers: List<Driver>, modifier: Modifier = Modifier) {
    LazyColumn {
        items(drivers) { driver ->
            DriverCard(driver = driver)
        }
    }
}

@Composable
fun DriverCard(driver: Driver, modifier: Modifier = Modifier) {

    val color = parseColor(driver.teamColour)

    Card(
        colors = CardDefaults.cardColors(color),
        modifier = modifier
            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
            .fillMaxWidth()
    ) {
        Row {
            AsyncImage(
                model = driver.headshotUrl,
                contentDescription = null,
                modifier = modifier.size(80.dp)
            )
            Spacer(modifier = modifier.padding(6.dp))
            Column(modifier = modifier.padding(8.dp)) {
                Text(
                    text = "${driver.firstName} ${driver.lastName} ${driver.driverNumber}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )
                Text(
                    text = driver.teamName,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Black
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun F1AppPreview() {
    F1LiveInfoTheme {
        F1App()
    }
}