package com.example.f1liveinfo.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.f1liveinfo.viewmodel.MeetingUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun F1TopBar(
    modifier: Modifier = Modifier,
    meetingUiState: MeetingUiState,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
) {

    // TODO: Modify TopBar to display race status
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Red,
            titleContentColor = Color.Black,
        ),
        title = {
            when (meetingUiState) {
                is MeetingUiState.Loading -> LoadingScreen(modifier = Modifier.size(200.dp))
                is MeetingUiState.Success -> MeetingContent(meeting = meetingUiState.meeting)

                is MeetingUiState.Error -> Text(
                    "Error: ${meetingUiState.message}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },

        navigationIcon = {
            IconButton(onClick = {
                coroutineScope.launch {
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