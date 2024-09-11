package com.example.f1liveinfo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.KeyboardDoubleArrowDown
import androidx.compose.material.icons.filled.KeyboardDoubleArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.f1liveinfo.model.Driver
import com.example.f1liveinfo.model.Meeting
import com.example.f1liveinfo.model.Session
import com.example.f1liveinfo.model.SessionName
import com.example.f1liveinfo.model.SessionType
import com.example.f1liveinfo.ui.screens.DriverViewModel
import com.example.f1liveinfo.ui.screens.DriversUiState
import com.example.f1liveinfo.ui.screens.MeetingUiState
import com.example.f1liveinfo.ui.screens.MeetingViewModel
import com.example.f1liveinfo.ui.theme.F1LiveInfoTheme
import com.example.f1liveinfo.utils.Utils
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            F1LiveInfoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    F1App()
                }
            }
        }
    }
}

@Composable
fun F1App(
    modifier: Modifier = Modifier,
    meetingViewModel: MeetingViewModel = viewModel(factory = MeetingViewModel.Factory),
    driverViewModel: DriverViewModel = viewModel(factory = DriverViewModel.Factory),
) {

    //Hide status and navigation bar
//    val systemUiController = rememberSystemUiController()
//    systemUiController.isStatusBarVisible = false
//    systemUiController.isNavigationBarVisible = false

    //Log.d(TAG, "Drivers: ${driverViewModel.driversUiState}")

    F1App(
        meetingUiState = meetingViewModel.meetingUiState,
        driversUiState = driverViewModel.driversUiState,
        onRefresh = {
            driverViewModel.getDriversData()
            meetingViewModel.getMeetingData()
        },
        fetchSession = { meetingViewModel.getSessionsData() },
        modifyMeetingSessionKey = { sessionKey ->
            meetingViewModel.modifyMeetingSessionKey(
                sessionKey = sessionKey
            )
        },
        getDriversForSession = { sessionKey ->
            driverViewModel.getDriversData(sessionKey = sessionKey.toString())
        },
    )
}

@Composable
fun F1App(
    meetingUiState: MeetingUiState,
    driversUiState: DriversUiState,
    onRefresh: () -> Unit,
    fetchSession: () -> Unit,
    modifyMeetingSessionKey: (Int) -> Unit,
    getDriversForSession: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var isRace = false

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            when (meetingUiState) {
                is MeetingUiState.Success -> {
                    val meeting = meetingUiState.meeting
                    isRace =
                        meeting.sessions.first { it.sessionKey == meeting.sessionKey }.sessionName == SessionName.Race
                    SessionDrawerList(
                        sessions = meetingUiState.meeting.sessions,
                        closeDrawer = { scope.launch { drawerState.close() } },
                        modifyMeetingSessionKey = modifyMeetingSessionKey,
                        getDriversForSession = getDriversForSession

                    )
                }

                is MeetingUiState.Error -> {}

                is MeetingUiState.Loading -> {
                    CircularProgressIndicator()
                }
            }
        }
    ) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                F1TopBar(
                    drawerState = drawerState,
                    coroutineScope = scope,
                    meetingUiState = meetingUiState,
                    fetchSession = fetchSession
                )
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                DriversScreen(
                    isRace = isRace,
                    driversUiState = driversUiState,
                    onRefresh = onRefresh
                )
            }
        }
    }
}

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun F1TopBar(
    modifier: Modifier = Modifier,
    meetingUiState: MeetingUiState,
    fetchSession: () -> Unit,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
) {
    var sessions = listOf<Session>()
    // TODO: Modify TopBar to display race status
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Red,
            titleContentColor = Color.Black,
        ),
        title = {
            when (meetingUiState) {
                is MeetingUiState.Loading -> LoadingScreen(modifier = Modifier.size(200.dp))
                is MeetingUiState.Success -> {
                    sessions = meetingUiState.meeting.sessions
                    MeetingContent(meeting = meetingUiState.meeting)
                }

                is MeetingUiState.Error -> Text(
                    "Error: ${meetingUiState.message}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },

        //Might be useful later
        navigationIcon = {
            IconButton(onClick = {
                coroutineScope.launch {
                    if (sessions.size < 2) {
                        fetchSession()
                    }
                    if (meetingUiState is MeetingUiState.Success) {
                        drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu"
                )
            }
        }
    )
}

@Composable
fun SessionDrawerList(
    sessions: List<Session>,
    modifier: Modifier = Modifier,
    closeDrawer: () -> Unit,
    modifyMeetingSessionKey: (Int) -> Unit,
    getDriversForSession: (Int) -> Unit
) {
    ModalDrawerSheet(modifier = modifier.fillMaxHeight()) {
        Spacer(modifier = Modifier.padding(5.dp))
        LazyColumn(modifier = modifier) {
            items(sessions) { session ->
                val sessionKey = session.sessionKey
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = session.sessionName.value,
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    selected = true,
                    onClick = {
                        getDriversForSession(sessionKey)
                        modifyMeetingSessionKey(sessionKey)
                        closeDrawer()
                    },
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }
    }
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
                DriverCard(isRace = isRace, driver = driver)
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

@Composable
fun DriverCard(isRace: Boolean, driver: Driver, modifier: Modifier = Modifier) {

    val teamColor = Utils.convertToColor(driver.teamColor, 0.9f)

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
                modifier = modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .fillMaxHeight(),
                isRace = isRace,
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
        }
    }
}

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

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorScreen(
    onRefresh: () -> Unit,
    errorMessage: String,
    modifier: Modifier = Modifier
) {
    val refreshing by remember { mutableStateOf(false) }
    val state = rememberPullRefreshState(refreshing = refreshing, onRefresh = onRefresh)
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .pullRefresh(state)
            .verticalScroll(rememberScrollState())
    ) {

        Column(
            modifier = modifier
                .fillMaxSize(),
            //verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_connection_error),
                contentDescription = "Error icon",
                modifier = Modifier.size(120.dp)
            )
            Text(
                text = stringResource(R.string.loading_failed),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp)
            )
        }
        PullRefreshIndicator(
            refreshing = refreshing,
            state = state,
            modifier = Modifier
                .align(Alignment.TopCenter)
        )
    }
}


@Preview(
    showBackground = true,
)
@Composable
fun F1AppPreviewOnSuccess() {

    val session = Session(
        sessionKey = 1,
        sessionName = SessionName.Practice_1,
        meetingKey = 1242,
        sessionType = SessionType.Practice
    )

    val meetingUiState = MeetingUiState.Success(
        Meeting(
            meetingKey = 1242,
            meetingName = "Belgian Grand Prix",
            location = "Spa-Francorchamps",
            countryName = "Belgium",
            dateStart = "2024-07-26T11:30:00+00:00",
            gmtOffset = "02:00:00",
            year = 2024,
            sessionKey = 1,
            sessions = listOf(session)
        )
    )

    val driversUiState = DriversUiState.Success(
        listOf(
            Driver(
                lastName = "Verstappen",
                firstName = "Max",
                fullName = "Max Verstappen",
                countryCode = "NED",
                teamName = "Red Bull Racing",
                driverNumber = 1,
                teamColor = "3671C6",
                currentPosition = 1,
                startingPosition = 1
            ), Driver(
                lastName = "Sargeant",
                firstName = "Logan",
                fullName = "Logan Sargeant",
                countryCode = "USA",
                teamName = "Williams",
                driverNumber = 2,
                teamColor = "64C4FF",
                currentPosition = 2,
                startingPosition = 10
            ),
//            Driver(
//                lastName = "Ricciardo",
//                firstName = "Daniel",
//                fullName = "Daniel Ricciardo",
//                countryCode = "AUS",
//                teamName = "RB",
//                driverNumber = 3,
//                teamColor = "6692FF",
//                currentPosition = 3,
//                startingPosition = 2
//            ),
            Driver(
                lastName = "Norris",
                firstName = "Lando",
                fullName = "Lando Norris",
                countryCode = "GBR",
                teamName = "McLaren",
                driverNumber = 4,
                teamColor = "FF8000",
                currentPosition = 4,
                startingPosition = 3
            ), Driver(
                lastName = "Gasly",
                firstName = "Pierre",
                fullName = "Pierre Gasly",
                countryCode = "FRA",
                teamName = "Alpine",
                driverNumber = 10,
                teamColor = "0093CC",
                currentPosition = 5,
                startingPosition = 15
            ), Driver(
                lastName = "Perez",
                firstName = "Sergio",
                fullName = "Sergio Perez",
                countryCode = "MEX",
                teamName = "Red Bull Racing",
                driverNumber = 11,
                teamColor = "3671C6",
                currentPosition = 6,
                startingPosition = 13
            ), Driver(
                lastName = "Alonso",
                firstName = "Fernando",
                fullName = "Fernando Alonso",
                countryCode = "ESP",
                teamName = "Aston Martin",
                driverNumber = 14,
                teamColor = "229971",
                currentPosition = 7,
                startingPosition = 12
            ), Driver(
                lastName = "Leclerc",
                firstName = "Charles",
                fullName = "Charles Leclerc",
                countryCode = "MON",
                teamName = "Ferrari",
                driverNumber = 16,
                teamColor = "E80020",
                currentPosition = 8,
                startingPosition = 19
            ), Driver(
                lastName = "Stroll",
                firstName = "Lance",
                fullName = "Lance Stroll",
                countryCode = "CAN",
                teamName = "Aston Martin",
                driverNumber = 18,
                teamColor = "229971",
                currentPosition = 9,
                startingPosition = 20
            ), Driver(
                lastName = "Magnussen",
                firstName = "Kevin",
                fullName = "Kevin Magnussen",
                countryCode = "DEN",
                teamName = "Haas F1 Team",
                driverNumber = 20,
                teamColor = "B6BABD",
                currentPosition = 10,
                startingPosition = 18
            ), Driver(
                lastName = "Tsunoda",
                firstName = "Yuki",
                fullName = "Yuki Tsunoda",
                countryCode = "JPN",
                teamName = "RB",
                driverNumber = 22,
                teamColor = "6692FF",
                currentPosition = 11,
                startingPosition = 17
            ), Driver(
                lastName = "Albon",
                firstName = "Alexander",
                fullName = "Alexander Albon",
                countryCode = "THA",
                teamName = "Williams",
                driverNumber = 23,
                teamColor = "64C4FF",
                currentPosition = 12,
                startingPosition = 16
            ), Driver(
                lastName = "Zhou",
                firstName = "Guanyu",
                fullName = "Guanyu Zhou",
                countryCode = "CHN",
                teamName = "Kick Sauber",
                driverNumber = 24,
                teamColor = "52E252",
                currentPosition = 13,
                startingPosition = 14
            ), Driver(
                lastName = "Hulkenberg",
                firstName = "Nico",
                fullName = "Nico Hulkenberg",
                countryCode = "GER",
                teamName = "Haas F1 Team",
                driverNumber = 27,
                teamColor = "B6BABD",
                currentPosition = 14,
                startingPosition = 13
            ), Driver(
                lastName = "Ocon",
                firstName = "Esteban",
                fullName = "Esteban Ocon",
                countryCode = "FRA",
                teamName = "Alpine",
                driverNumber = 31,
                teamColor = "0093CC",
                currentPosition = 15,
                startingPosition = 7
            ), Driver(
                lastName = "Hamilton",
                firstName = "Lewis",
                fullName = "Lewis Hamilton",
                countryCode = "GBR",
                teamName = "Mercedes",
                driverNumber = 44,
                teamColor = "27F4D2",
                currentPosition = 16,
                startingPosition = 8
            ), Driver(
                lastName = "Sainz",
                firstName = "Carlos",
                fullName = "Carlos Sainz",
                countryCode = "ESP",
                teamName = "Ferrari",
                driverNumber = 55,
                teamColor = "E80020",
                currentPosition = 17,
                startingPosition = 4
            ), Driver(
                lastName = "Russell",
                firstName = "George",
                fullName = "George Russell",
                countryCode = "GBR",
                teamName = "Mercedes",
                driverNumber = 63,
                teamColor = "27F4D2",
                currentPosition = 18,
                startingPosition = 5
            ), Driver(
                lastName = "Bottas",
                firstName = "Valtteri",
                fullName = "Valtteri Bottas",
                countryCode = "FIN",
                teamName = "Kick Sauber",
                driverNumber = 77,
                teamColor = "52E252",
                currentPosition = 19,
                startingPosition = 3
            ), Driver(
                lastName = "Piastri",
                firstName = "Oscar",
                fullName = "Oscar Piastri",
                countryCode = "AUS",
                teamName = "McLaren",
                driverNumber = 81,
                teamColor = "FF8000",
                currentPosition = 20,
                startingPosition = 1
            )
        )
    )
    F1App(
        meetingUiState = meetingUiState,
        driversUiState = driversUiState,
        onRefresh = { },
        fetchSession = { },
        modifyMeetingSessionKey = { },
        getDriversForSession = { })
}

/*@Preview(
    showBackground = true,
)
@Composable
fun F1AppPreviewOnLoading() {

    val meetingUiState = MeetingUiState.Loading
    val driversUiState = DriversUiState.Loading

    F1App(meetingUiState = meetingUiState, driversUiState = driversUiState, onRefresh = { })

}

@Preview(
    showBackground = true,
)
@Composable
fun F1AppPreviewOnError() {
    val meetingUiState = MeetingUiState.Error
    val driversUiState = DriversUiState.Error
    F1App(meetingUiState = meetingUiState, driversUiState = driversUiState, onRefresh = { })

}*/