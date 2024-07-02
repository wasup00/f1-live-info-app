package com.example.f1liveinfo

import android.os.Bundle
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.f1liveinfo.model.Driver
import com.example.f1liveinfo.model.Meeting
import com.example.f1liveinfo.ui.theme.F1LiveInfoTheme

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
fun F1App(modifier: Modifier = Modifier) {
    // Values of drivers are here for test purposes
    val driver1 = Driver(
        lastName = "Verstappen",
        firstName = "Max",
        countryCode = "NL",
        teamName = "Red Bull Racing",
        driverNumber = 1,
        teamColor = "3671C6",
        headshotUrl = "https://www.formula1.com/content/dam/fom-website/drivers/M/MAXVER01_Max_Verstappen/maxver01.png.transform/1col/image.png"
    )

    val driver2 = Driver(
        lastName = "Leclerc",
        firstName = "Charles",
        countryCode = "MC",
        teamName = "Scuderia Ferrari",
        driverNumber = 16,
        teamColor = "F91536",
        headshotUrl = "https://www.formula1.com/content/dam/fom-website/drivers/C/CHALEC01_Charles_Leclerc/chalec01.png.transform/1col/image.png"
    )

    val meeting = Meeting(
        location = "Spielberg",
        countryName = "Austria",
        meetingName = "Austrian Grand Prix",
        gmtOffset = "02:00:00",
        dateStart = "2024-06-28T10:30:00+00:00",
        year = 2024
    )

    val drivers = listOf(driver1, driver2)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { F1TopBar(meeting = meeting) },
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
                    meeting.meetingName,
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

    val color = parseColor(driver.teamColor)

    Card(
        colors = CardDefaults.cardColors(color),
        modifier = modifier
            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
            .fillMaxWidth()
    ) {
        Row {
            if (driver.headshotUrl != null) {
                AsyncImage(
                    model = driver.headshotUrl,
                    contentDescription = null,
                    modifier = modifier.size(80.dp)
                )
                Spacer(modifier = modifier.padding(6.dp))
            }
            Column(modifier = modifier.padding(8.dp)) {
                Text(
                    text = "${driver.firstName} ${driver.lastName} ${driver.driverNumber}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = driver.teamName,
                    style = MaterialTheme.typography.labelSmall
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