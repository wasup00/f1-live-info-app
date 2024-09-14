package com.example.f1liveinfo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.f1liveinfo.ui.components.F1App
import com.example.f1liveinfo.ui.theme.F1LiveInfoTheme
import com.example.f1liveinfo.viewmodel.DriverViewModel
import com.example.f1liveinfo.viewmodel.MeetingViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            F1LiveInfoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {

                    val meetingViewModel: MeetingViewModel =
                        viewModel(factory = MeetingViewModel.Factory)
                    val driverViewModel: DriverViewModel =
                        viewModel(factory = DriverViewModel.Factory)

                    //Hide status and navigation bar
//                    val systemUiController = rememberSystemUiController()
//                    systemUiController.isStatusBarVisible = false
//                    systemUiController.isNavigationBarVisible = false

                    F1App(
                        meetingUiState = meetingViewModel.meetingUiState,
                        driversUiState = driverViewModel.driversUiState,
                        onRefresh = {
                            driverViewModel.getDriversData()
                            meetingViewModel.getMeetingData()
                        },
                        fetchSessions = { meetingViewModel.getSessionsData() },
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
            }
        }
    }
}