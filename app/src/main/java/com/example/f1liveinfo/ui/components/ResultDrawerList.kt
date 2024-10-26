package com.example.f1liveinfo.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.f1liveinfo.model.Session
import com.example.f1liveinfo.model.SessionName
import com.example.f1liveinfo.model.SessionType
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ResultDrawerList(
    meetingName: String,
    sessions: List<Session>,
    modifier: Modifier = Modifier,
    closeDrawer: () -> Unit,
    modifySessionKeyInMeeting: (Int) -> Unit,
    getDriversForSession: (Int) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    ModalDrawerSheet(
        modifier = modifier.widthIn(min = 200.dp, max = 300.dp),
        drawerContainerColor = MaterialTheme.colorScheme.surface,
        drawerContentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = meetingName,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (expanded) "Collapse" else "Expand"
                    )
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                if (sessions.isEmpty()) {
                    CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
                } else {
                    LazyColumn(
                        modifier = Modifier.wrapContentHeight(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(sessions) { session ->
                            val sessionKey = session.sessionKey
                            NavigationDrawerItem(
                                label = {
                                    Text(
                                        text = session.sessionName.value,
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                },
                                selected = false,
                                onClick = {
                                    getDriversForSession(sessionKey)
                                    modifySessionKeyInMeeting(sessionKey)
                                    closeDrawer()
                                },
                                shape = MaterialTheme.shapes.small,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ResultDrawerListPreview() {
    MaterialTheme {
        ResultDrawerList(
            meetingName = "Meeting Name",
            sessions = listOf(
                Session(
                    sessionKey = 0,
                    sessionName = SessionName.Race,
                    dateEnd = OffsetDateTime.parse(
                        "2024-10-18T17:30:00-05:00",
                        DateTimeFormatter.ISO_OFFSET_DATE_TIME
                    ).toLocalDateTime(),
                    dateStart = OffsetDateTime.parse(
                        "2024-10-18T17:00:00-05:00",
                        DateTimeFormatter.ISO_OFFSET_DATE_TIME
                    ).toLocalDateTime(),
                    gmtOffset = "-05:00:00",
                    meetingKey = 1,
                    sessionType = SessionType.Race
                ),
                Session(
                    sessionKey = 0,
                    sessionName = SessionName.Qualifying,
                    dateEnd = OffsetDateTime.parse(
                        "2024-10-17T17:30:00-05:00",
                        DateTimeFormatter.ISO_OFFSET_DATE_TIME
                    ).toLocalDateTime(),
                    dateStart = OffsetDateTime.parse(
                        "2024-10-17T17:00:00-05:00",
                        DateTimeFormatter.ISO_OFFSET_DATE_TIME
                    ).toLocalDateTime(),
                    gmtOffset = "-05:00:00",
                    meetingKey = 1,
                    sessionType = SessionType.Qualifying
                ),
            ),
            closeDrawer = { },
            modifySessionKeyInMeeting = { },
            getDriversForSession = { },
        )
    }
}