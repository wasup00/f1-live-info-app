package com.example.f1liveinfo.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.f1liveinfo.model.Driver
import com.example.f1liveinfo.model.Meeting
import com.example.f1liveinfo.model.Session
import com.example.f1liveinfo.model.SessionName
import com.example.f1liveinfo.model.SessionType
import com.example.f1liveinfo.model.adjustForGmtOffset
import com.example.f1liveinfo.viewmodel.DriversUiState
import com.example.f1liveinfo.viewmodel.MeetingUiState
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@Composable
fun F1App(
    meetingUiState: MeetingUiState,
    driversUiState: DriversUiState,
    onRefresh: () -> Unit,
    fetchSessions: () -> Unit,
    modifyMeetingSessionKey: (Int) -> Unit,
    getDriversForSession: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var isRace by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            when (meetingUiState) {
                is MeetingUiState.Success -> {
                    val meeting = meetingUiState.meeting
                    isRace =
                        meeting.sessions.firstOrNull { it.sessionKey == meeting.sessionKey }?.sessionName == SessionName.Race
                    ResultDrawerList(
                        meetingName = meeting.meetingName,
                        sessions = meeting.sessions,
                        closeDrawer = { scope.launch { drawerState.close() } },
                        modifyMeetingSessionKey = modifyMeetingSessionKey,
                        getDriversForSession = getDriversForSession,
                        fetchSessions = fetchSessions
                    )
                }

                is MeetingUiState.Error -> {
                    Text("Error: ${meetingUiState.message}")
                }

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
                    meetingUiState = meetingUiState
                )
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding),
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

@Preview(
    showBackground = true,
)
@Composable
fun F1AppPreviewOnSuccess() {

    val session = Session(
        sessionKey = 1,
        sessionName = SessionName.Practice1,
        meetingKey = 1242,
        sessionType = SessionType.Practice,
        gmtOffset = "02:00:00",
        dateStart = LocalDateTime.parse("2024-07-26T11:30:00"),
        dateEnd = LocalDateTime.parse("2024-07-26T12:30:00")
    )

    session.adjustForGmtOffset()
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
        fetchSessions = { },
        modifyMeetingSessionKey = { },
        getDriversForSession = { },

        )
}

@Preview(
    showBackground = true,
)
@Composable
fun F1AppPreviewOnLoading() {

    val meetingUiState = MeetingUiState.Loading
    val driversUiState = DriversUiState.Loading

    F1App(
        meetingUiState = meetingUiState,
        driversUiState = driversUiState,
        onRefresh = { },
        fetchSessions = {},
        getDriversForSession = {},
        modifyMeetingSessionKey = {})

}

@Preview(
    showBackground = true,
)
@Composable
fun F1AppPreviewOnError() {
    val meetingUiState = MeetingUiState.Error("Error Meeting")
    val driversUiState = DriversUiState.Error("Error Driver")
    F1App(
        meetingUiState = meetingUiState,
        driversUiState = driversUiState,
        onRefresh = { },
        fetchSessions = {},
        getDriversForSession = {},
        modifyMeetingSessionKey = {})

}