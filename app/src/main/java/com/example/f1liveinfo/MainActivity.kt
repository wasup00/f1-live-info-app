package com.example.f1liveinfo

import android.graphics.Color.parseColor
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.KeyboardDoubleArrowDown
import androidx.compose.material.icons.filled.KeyboardDoubleArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState
import kotlin.math.absoluteValue

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

fun convertToColor(colorStr: String, alpha: Float = 1.0f): Color {
    val hexStr = "#${colorStr}"
    val colorInt = parseColor(hexStr)
    return Color(colorInt).copy(alpha = alpha)
}

@Composable
fun F1App(
    modifier: Modifier = Modifier,
    meetingViewModel: MeetingViewModel = viewModel(),
    driverViewModel: DriverViewModel = viewModel(),
) {

    //Hide status and navigation bar
//    val systemUiController = rememberSystemUiController()
//    systemUiController.isStatusBarVisible = false
//    systemUiController.isNavigationBarVisible = false

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
            DriversScreen(
                driversUiState = driverViewModel.driversUiState,
                onRefresh = { refreshData(meetingViewModel, driverViewModel) }
            )
        }

    }
}

@Composable
fun DriversScreen(
    driversUiState: DriversUiState,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (driversUiState) {
        is DriversUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is DriversUiState.Success -> RefreshableListOfDrivers(
            driversUiState.drivers,
            onRefresh = onRefresh,
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
                    contentDescription = "Refresh race data",
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RefreshableListOfDrivers(
    drivers: List<Driver>,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        val refreshing by remember { mutableStateOf(false) }
        val state = rememberPullRefreshState(refreshing = refreshing, onRefresh = onRefresh)
        LazyColumn(
            modifier = Modifier.pullRefresh(state),
        ) {
            items(drivers) { driver ->
                DriverCard(driver = driver)
            }
        }
        PullRefreshIndicator(
            refreshing = refreshing, state = state,
            modifier = Modifier
                .align(Alignment.TopCenter)
        )
    }
}

@Composable
fun DriverCard(driver: Driver, modifier: Modifier = Modifier) {

    val teamColor = convertToColor(driver.teamColor, 0.9f)

    Card(
        colors = CardDefaults.cardColors(teamColor),
        modifier = modifier
            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = modifier.fillMaxSize()
        ) {
            PositionCard(
                driver = driver
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
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun PositionCard(driver: Driver, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(start = 8.dp, top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val difference = if (driver.startingPosition != null || driver.currentPosition != null) {
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
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
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
                currentPosition = 1,
                startingPosition = 1
            ), Driver(
                lastName = "Sargeant",
                firstName = "Logan",
                countryCode = "USA",
                teamName = "Williams",
                driverNumber = 2,
                teamColor = "64C4FF",
                currentPosition = 2,
                startingPosition = 10
            ), Driver(
                lastName = "Ricciardo",
                firstName = "Daniel",
                countryCode = "AUS",
                teamName = "RB",
                driverNumber = 3,
                teamColor = "6692FF",
                currentPosition = 3,
                startingPosition = 2
            ), Driver(
                lastName = "Norris",
                firstName = "Lando",
                countryCode = "GBR",
                teamName = "McLaren",
                driverNumber = 4,
                teamColor = "FF8000",
                currentPosition = 4,
                startingPosition = 3
            ), Driver(
                lastName = "Gasly",
                firstName = "Pierre",
                countryCode = "FRA",
                teamName = "Alpine",
                driverNumber = 10,
                teamColor = "0093CC",
                currentPosition = 5,
                startingPosition = 15
            ), Driver(
                lastName = "Perez",
                firstName = "Sergio",
                countryCode = "MEX",
                teamName = "Red Bull Racing",
                driverNumber = 11,
                teamColor = "3671C6",
                currentPosition = 6,
                startingPosition = 13
            ), Driver(
                lastName = "Alonso",
                firstName = "Fernando",
                countryCode = "ESP",
                teamName = "Aston Martin",
                driverNumber = 14,
                teamColor = "229971",
                currentPosition = 7,
                startingPosition = 12
            ), Driver(
                lastName = "Leclerc",
                firstName = "Charles",
                countryCode = "MON",
                teamName = "Ferrari",
                driverNumber = 16,
                teamColor = "E80020",
                currentPosition = 8,
                startingPosition = 19
            ), Driver(
                lastName = "Stroll",
                firstName = "Lance",
                countryCode = "CAN",
                teamName = "Aston Martin",
                driverNumber = 18,
                teamColor = "229971",
                currentPosition = 9,
                startingPosition = 20
            ), Driver(
                lastName = "Magnussen",
                firstName = "Kevin",
                countryCode = "DEN",
                teamName = "Haas F1 Team",
                driverNumber = 20,
                teamColor = "B6BABD",
                currentPosition = 10,
                startingPosition = 18
            ), Driver(
                lastName = "Tsunoda",
                firstName = "Yuki",
                countryCode = "JPN",
                teamName = "RB",
                driverNumber = 22,
                teamColor = "6692FF",
                currentPosition = 11,
                startingPosition = 17
            ), Driver(
                lastName = "Albon",
                firstName = "Alexander",
                countryCode = "THA",
                teamName = "Williams",
                driverNumber = 23,
                teamColor = "64C4FF",
                currentPosition = 12,
                startingPosition = 16
            ), Driver(
                lastName = "Zhou",
                firstName = "Guanyu",
                countryCode = "CHN",
                teamName = "Kick Sauber",
                driverNumber = 24,
                teamColor = "52E252",
                currentPosition = 13,
                startingPosition = 14
            ), Driver(
                lastName = "Hulkenberg",
                firstName = "Nico",
                countryCode = "GER",
                teamName = "Haas F1 Team",
                driverNumber = 27,
                teamColor = "B6BABD",
                currentPosition = 14,
                startingPosition = 13
            ), Driver(
                lastName = "Ocon",
                firstName = "Esteban",
                countryCode = "FRA",
                teamName = "Alpine",
                driverNumber = 31,
                teamColor = "0093CC",
                currentPosition = 15,
                startingPosition = 7
            ), Driver(
                lastName = "Hamilton",
                firstName = "Lewis",
                countryCode = "GBR",
                teamName = "Mercedes",
                driverNumber = 44,
                teamColor = "27F4D2",
                currentPosition = 16,
                startingPosition = 8
            ), Driver(
                lastName = "Sainz",
                firstName = "Carlos",
                countryCode = "ESP",
                teamName = "Ferrari",
                driverNumber = 55,
                teamColor = "E80020",
                currentPosition = 17,
                startingPosition = 4
            ), Driver(
                lastName = "Russell",
                firstName = "George",
                countryCode = "GBR",
                teamName = "Mercedes",
                driverNumber = 63,
                teamColor = "27F4D2",
                currentPosition = 18,
                startingPosition = 5
            ), Driver(
                lastName = "Bottas",
                firstName = "Valtteri",
                countryCode = "FIN",
                teamName = "Kick Sauber",
                driverNumber = 77,
                teamColor = "52E252",
                currentPosition = 19,
                startingPosition = 3
            ), Driver(
                lastName = "Piastri",
                firstName = "Oscar",
                countryCode = "AUS",
                teamName = "McLaren",
                driverNumber = 81,
                teamColor = "FF8000",
                currentPosition = 20,
                startingPosition = 1
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

//@Preview(
//    showBackground = true,
//)
//@Composable
//fun F1AppPreviewOnLoading() {
//    val meetingViewModel: MeetingViewModel = viewModel()
//    val driverViewModel: DriverViewModel = viewModel()
//
//    meetingViewModel.meetingUiState = MeetingUiState.Loading
//    driverViewModel.driversUiState = DriversUiState.Loading
//
//    F1LiveInfoTheme {
//        F1App(
//            meetingViewModel = meetingViewModel,
//            driverViewModel = driverViewModel
//        )
//    }
//}
//
//@Preview(
//    showBackground = true,
//)
//@Composable
//fun F1AppPreviewOnError() {
//    val meetingViewModel: MeetingViewModel = viewModel()
//    val driverViewModel: DriverViewModel = viewModel()
//
//    meetingViewModel.meetingUiState = MeetingUiState.Error
//    driverViewModel.driversUiState = DriversUiState.Error
//    F1LiveInfoTheme {
//        F1App(
//            meetingViewModel = meetingViewModel,
//            driverViewModel = driverViewModel
//        )
//    }
//}