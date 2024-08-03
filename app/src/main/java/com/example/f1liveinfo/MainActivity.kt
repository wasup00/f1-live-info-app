package com.example.f1liveinfo

import android.graphics.Color.parseColor
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.f1liveinfo.model.Driver
import com.example.f1liveinfo.model.Meeting
import com.example.f1liveinfo.ui.DriverViewModel
import com.example.f1liveinfo.ui.DriversUiState
import com.example.f1liveinfo.ui.MeetingUiState
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

fun convertToColor(colorStr: String): Color {
    val hexStr = "#${colorStr}"
    return Color(parseColor(hexStr))
}

@Composable
fun F1App(
    meetingViewModel: MeetingViewModel = viewModel(),
    driverViewModel: DriverViewModel = viewModel(),
    modifier: Modifier = Modifier
) {

//    //Hide status bar
//    val systemUiController = rememberSystemUiController()
//    systemUiController.isStatusBarVisible = false
//
//    //Hide navigation bar
//    val view = LocalView.current
//    ViewCompat.getWindowInsetsController(view)?.hide(WindowInsetsCompat.Type.systemBars())

    Log.d(TAG, "Drivers: ${driverViewModel.driversUiState}")

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            F1TopBar(
                meetingViewModel = meetingViewModel,
                driverViewModel = driverViewModel
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            DriversScreen(driversUiState = driverViewModel.driversUiState)
        }

    }
}

@Composable
fun DriversScreen(
    driversUiState: DriversUiState,
    modifier: Modifier = Modifier
) {
    when (driversUiState) {
        is DriversUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is DriversUiState.Success -> ListOfDrivers(
            driversUiState.drivers,
            modifier = modifier.fillMaxWidth()
        )
        is DriversUiState.Error -> ErrorScreen(modifierDriver = modifier.fillMaxSize())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun F1TopBar(
    meetingViewModel: MeetingViewModel,
    driverViewModel: DriverViewModel,
    modifier: Modifier = Modifier
) {
    val meetingUiState = meetingViewModel.meetingUiState
    val driversUiState = driverViewModel.driversUiState

    // Check if either meeting or driver data is loading
    val isLoading =
        meetingUiState is MeetingUiState.Loading || driversUiState is DriversUiState.Loading

    // TODO: Modify TopBar to display race status
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Red,
            titleContentColor = Color.Black,
        ),
        title = {
            when (meetingUiState) {
                is MeetingUiState.Loading -> {
                    LoadingScreen(modifier = Modifier.size(200.dp))
                }

                is MeetingUiState.Success -> {
                    MeetingContent(meeting = meetingUiState.meeting)
                }

                is MeetingUiState.Error -> {
                    ErrorScreen(modifierMeeting = modifier.size(30.dp))
                }
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
        },*/
        actions = {
            IconButton(
                onClick = {
                    refreshData(
                        meetingViewModel = meetingViewModel,
                        driverViewModel = driverViewModel
                    )
                },
                enabled = !isLoading,

            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Refresh",
                    tint = Color.Black
                )
            }
        }
    )
}

@Composable
fun MeetingContent(meeting: Meeting, modifier: Modifier = Modifier) {
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
}

@Composable
fun ListOfDrivers(
    drivers: List<Driver>,
    modifier: Modifier = Modifier
) {
    LazyColumn {
        items(drivers) { driver ->
            DriverCard(driver = driver)
        }
    }
}

@Composable
fun DriverCard(driver: Driver, modifier: Modifier = Modifier) {

    val color = convertToColor(driver.teamColor)

    Card(
        colors = CardDefaults.cardColors(color),
        modifier = modifier
            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = modifier.fillMaxSize()
        ) {
            Text(
                text = "${driver.position}.",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black,
                modifier = modifier
                    .padding(start = 10.dp)
                    .align(Alignment.CenterVertically)
            )
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

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier,
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

@Composable
fun ErrorScreen(modifierDriver: Modifier = Modifier, modifierMeeting: Modifier = Modifier) {
    Column(
        modifier = modifierDriver,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error),
            contentDescription = "",
            modifier = modifierMeeting
        )
        Text(
            text = stringResource(R.string.loading_failed),
        )
    }
}

private fun refreshData(meetingViewModel: MeetingViewModel, driverViewModel: DriverViewModel) {
    meetingViewModel.getMeetingData()
    driverViewModel.getDriversData()
}

@Preview(
    showBackground = true,
)
@Composable
fun F1AppPreviewOnSuccess() {
    val meetingViewModel: MeetingViewModel = viewModel()
    val driverViewModel: DriverViewModel = viewModel()

    meetingViewModel.meetingUiState = MeetingUiState.Success(
        Meeting(
            meetingName = "Belgian Grand Prix",
            location = "Spa-Francorchamps",
            countryName = "Belgium",
            dateStart = "2024-07-26T11:30:00+00:00",
            gmtOffset = "02:00:00",
            year = 2024
        )
    )

    driverViewModel.driversUiState = DriversUiState.Success(
        listOf(
            Driver(
                lastName = "Verstappen",
                firstName = "Max",
                countryCode = "NED",
                teamName = "Red Bull Racing",
                driverNumber = 1,
                teamColor = "3671C6",
                position = 1
            ), Driver(
                lastName = "Sargeant",
                firstName = "Logan",
                countryCode = "USA",
                teamName = "Williams",
                driverNumber = 2,
                teamColor = "64C4FF",
                position = 2
            ), Driver(
                lastName = "Ricciardo",
                firstName = "Daniel",
                countryCode = "AUS",
                teamName = "RB",
                driverNumber = 3,
                teamColor = "6692FF",
                position = 3
            ), Driver(
                lastName = "Norris",
                firstName = "Lando",
                countryCode = "GBR",
                teamName = "McLaren",
                driverNumber = 4,
                teamColor = "FF8000",
                position = 4
            ), Driver(
                lastName = "Gasly",
                firstName = "Pierre",
                countryCode = "FRA",
                teamName = "Alpine",
                driverNumber = 10,
                teamColor = "0093CC",
                position = 5
            ), Driver(
                lastName = "Perez",
                firstName = "Sergio",
                countryCode = "MEX",
                teamName = "Red Bull Racing",
                driverNumber = 11,
                teamColor = "3671C6",
                position = 6
            ), Driver(
                lastName = "Alonso",
                firstName = "Fernando",
                countryCode = "ESP",
                teamName = "Aston Martin",
                driverNumber = 14,
                teamColor = "229971",
                position = 7
            ), Driver(
                lastName = "Leclerc",
                firstName = "Charles",
                countryCode = "MON",
                teamName = "Ferrari",
                driverNumber = 16,
                teamColor = "E80020",
                position = 8
            ), Driver(
                lastName = "Stroll",
                firstName = "Lance",
                countryCode = "CAN",
                teamName = "Aston Martin",
                driverNumber = 18,
                teamColor = "229971",
                position = 9
            ), Driver(
                lastName = "Magnussen",
                firstName = "Kevin",
                countryCode = "DEN",
                teamName = "Haas F1 Team",
                driverNumber = 20,
                teamColor = "B6BABD",
                position = 10
            ), Driver(
                lastName = "Tsunoda",
                firstName = "Yuki",
                countryCode = "JPN",
                teamName = "RB",
                driverNumber = 22,
                teamColor = "6692FF",
                position = 11
            ), Driver(
                lastName = "Albon",
                firstName = "Alexander",
                countryCode = "THA",
                teamName = "Williams",
                driverNumber = 23,
                teamColor = "64C4FF",
                position = 12
            ), Driver(
                lastName = "Zhou",
                firstName = "Guanyu",
                countryCode = "CHN",
                teamName = "Kick Sauber",
                driverNumber = 24,
                teamColor = "52E252",
                position = 13
            ), Driver(
                lastName = "Hulkenberg",
                firstName = "Nico",
                countryCode = "GER",
                teamName = "Haas F1 Team",
                driverNumber = 27,
                teamColor = "B6BABD",
                position = 14
            ), Driver(
                lastName = "Ocon",
                firstName = "Esteban",
                countryCode = "FRA",
                teamName = "Alpine",
                driverNumber = 31,
                teamColor = "0093CC",
                position = 15
            ), Driver(
                lastName = "Hamilton",
                firstName = "Lewis",
                countryCode = "GBR",
                teamName = "Mercedes",
                driverNumber = 44,
                teamColor = "27F4D2",
                position = 16
            ), Driver(
                lastName = "Sainz",
                firstName = "Carlos",
                countryCode = "ESP",
                teamName = "Ferrari",
                driverNumber = 55,
                teamColor = "E80020",
                position = 17
            ), Driver(
                lastName = "Russell",
                firstName = "George",
                countryCode = "GBR",
                teamName = "Mercedes",
                driverNumber = 63,
                teamColor = "27F4D2",
                position = 18
            ), Driver(
                lastName = "Bottas",
                firstName = "Valtteri",
                countryCode = "FIN",
                teamName = "Kick Sauber",
                driverNumber = 77,
                teamColor = "52E252",
                position = 19
            ), Driver(
                lastName = "Piastri",
                firstName = "Oscar",
                countryCode = "AUS",
                teamName = "McLaren",
                driverNumber = 81,
                teamColor = "FF8000",
                position = 20
            )
        )
    )

    F1LiveInfoTheme {
        F1App(
            meetingViewModel = meetingViewModel,
            driverViewModel = driverViewModel
        )
    }
}

@Preview(
    showBackground = true,
)
@Composable
fun F1AppPreviewOnLoading() {
    val meetingViewModel: MeetingViewModel = viewModel()
    val driverViewModel: DriverViewModel = viewModel()

    meetingViewModel.meetingUiState = MeetingUiState.Loading
    driverViewModel.driversUiState = DriversUiState.Loading

    F1LiveInfoTheme {
        F1App(
            meetingViewModel = meetingViewModel,
            driverViewModel = driverViewModel
        )
    }
}

@Preview(
    showBackground = true,
)
@Composable
fun F1AppPreviewOnError() {
    val meetingViewModel: MeetingViewModel = viewModel()
    val driverViewModel: DriverViewModel = viewModel()

    meetingViewModel.meetingUiState = MeetingUiState.Error
    driverViewModel.driversUiState = DriversUiState.Error
    F1LiveInfoTheme {
        F1App(
            meetingViewModel = meetingViewModel,
            driverViewModel = driverViewModel
        )
    }
}