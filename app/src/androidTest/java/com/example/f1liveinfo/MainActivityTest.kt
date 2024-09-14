package com.example.f1liveinfo

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.f1liveinfo.model.Driver
import com.example.f1liveinfo.model.Meeting
import com.example.f1liveinfo.model.Session
import com.example.f1liveinfo.model.SessionName
import com.example.f1liveinfo.model.SessionType
import com.example.f1liveinfo.ui.screens.MeetingUiState
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

class MainActivityTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun meetingContent_isDisplayed() {
        val session = Session(
            sessionKey = 1,
            sessionName = SessionName.Practice_1,
            meetingKey = 1242,
            sessionType = SessionType.Practice,
            gmtOffset = "02:00:00",
            dateStart = LocalDateTime.parse("2024-07-26T11:30:00"),
            dateEnd = LocalDateTime.parse("2024-07-26T12:30:00")
        )

        val meeting = Meeting(
            meetingName = "Belgian Grand Prix",
            location = "Spa-Francorchamps",
            countryName = "BEL",
            dateStart = "2024-07-26T11:30:00+00:00",
            gmtOffset = "02:00:00",
            year = 2024,
            sessionKey = 1,
            sessions = listOf(session),
            meetingKey = 1,

        )
        composeTestRule.setContent {
            MeetingContent(meeting = meeting)
        }
        composeTestRule.onNodeWithText("Belgian Grand Prix").assertIsDisplayed()
        composeTestRule.onNodeWithText("Practice 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Date: 7-26-2024").assertIsDisplayed()
        composeTestRule.onNodeWithText("Time: 11:30:0").assertIsDisplayed()
    }

    @Test
    fun driverStatus_isDisplayed() {
        val driver = Driver(
            fullName = "Max VERSTAPPEN",
            lastName = "Verstappen",
            firstName = "Max",
            countryCode = "NED",
            teamName = "Red Bull Racing",
            driverNumber = 1,
            teamColor = "3671C6",
            currentPosition = 15,
            startingPosition = 10
        )
        composeTestRule.setContent {
            ExpandableDriverCard(driver = driver, isRace = true)
        }
        composeTestRule.onNodeWithText("15").assertIsDisplayed()
        composeTestRule.onNodeWithText("5").assertIsDisplayed()
        composeTestRule.onNodeWithText("Max VERSTAPPEN 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Red Bull Racing").assertIsDisplayed()
    }

    @Test
    fun errorScreen_isDisplayed() {
        composeTestRule.setContent {
            ErrorScreen(onRefresh = {}, errorMessage = "Error during data retrieval")
        }
        composeTestRule.onNodeWithContentDescription("Error icon").assertIsDisplayed()
        composeTestRule.onNodeWithText("Failed to load").assertIsDisplayed()
        composeTestRule.onNodeWithText("Error during data retrieval").assertIsDisplayed()
    }


    @Test
    fun expandableDriverCard_expandsAndCollapsesOnClick() {
        val driver = Driver(
            fullName = "Lewis HAMILTON",
            lastName = "Hamilton",
            firstName = "Lewis",
            countryCode = "GBR",
            teamName = "Mercedes",
            driverNumber = 44,
            teamColor = "27F4D2",
            currentPosition = 1,
            startingPosition = 2
        )
        composeTestRule.setContent {
            ExpandableDriverCard(driver = driver, isRace = true)
        }

        // Initially, expanded content should not be visible
        composeTestRule.onNodeWithText("Country: GBR").assertDoesNotExist()

        // Click to expand
        composeTestRule.onNodeWithText("Lewis HAMILTON 44").performClick()

        // Now, expanded content should be visible
        composeTestRule.onNodeWithText("Country: GBR").assertIsDisplayed()
        composeTestRule.onNodeWithText("Driver Number: 44").assertIsDisplayed()

        // Click to collapse
        composeTestRule.onNodeWithText("Lewis HAMILTON 44").performClick()

        // Expanded content should not be visible again
        composeTestRule.onNodeWithText("Country: GBR").assertDoesNotExist()
    }

    @Test
    fun positionCard_displaysCorrectInfo_forRace() {
        val driver = Driver(
            fullName = "Valtteri BOTTAS",
            lastName = "Bottas",
            firstName = "Valtteri",
            countryCode = "FIN",
            teamName = "Alfa Romeo",
            driverNumber = 77,
            teamColor = "52E252",
            currentPosition = 2,
            startingPosition = 10
        )
        composeTestRule.setContent {
            PositionCard(isRace = true, driver = driver)
        }

        composeTestRule.onNodeWithText("2").assertIsDisplayed()
        composeTestRule.onNodeWithText("8").assertIsDisplayed() // Gained 5 positions
    }

    @Test
    fun positionCard_displaysCorrectInfo_forNonRace() {
        val driver = Driver(
            fullName = "Charles LECLERC",
            lastName = "Leclerc",
            firstName = "Charles",
            countryCode = "MON",
            teamName = "Ferrari",
            driverNumber = 16,
            teamColor = "E80020",
            currentPosition = 3,
            startingPosition = null
        )
        composeTestRule.setContent {
            PositionCard(isRace = false, driver = driver)
        }

        composeTestRule.onNodeWithText("3").assertIsDisplayed()
        composeTestRule.onNodeWithText("3").assertIsDisplayed()
    }

    @Test
    fun loadingScreen_displaysCircularProgressIndicator() {
        composeTestRule.setContent {
            LoadingScreen()
        }

        composeTestRule.onNode(hasTestTag("LoadingIndicator")).assertIsDisplayed()
    }

    @Test
    fun f1TopBar_displaysCorrectInfo_onSuccess() {
        val session = Session(
            sessionKey = 1,
            sessionName = SessionName.Race,
            meetingKey = 1242,
            sessionType = SessionType.Race,
            gmtOffset = "02:00:00",
            dateStart = LocalDateTime.parse("2024-07-28T14:00:00"),
            dateEnd = LocalDateTime.parse("2024-07-28T16:00:00")
        )

        val meeting = Meeting(
            meetingName = "Belgian Grand Prix",
            location = "Spa-Francorchamps",
            countryName = "BEL",
            dateStart = "2024-07-28T14:00:00+00:00",
            gmtOffset = "02:00:00",
            year = 2024,
            sessionKey = 1,
            sessions = listOf(session),
            meetingKey = 1,
        )

        val meetingUiState = MeetingUiState.Success(meeting)

        composeTestRule.setContent {
            F1TopBar(meetingUiState = meetingUiState)
        }

        composeTestRule.onNodeWithText("Belgian Grand Prix").assertIsDisplayed()
        composeTestRule.onNodeWithText("Race").assertIsDisplayed()
        composeTestRule.onNodeWithText("Date: 7-28-2024").assertIsDisplayed()
        composeTestRule.onNodeWithText("Time: 14:0:0").assertIsDisplayed()
    }

    @Test
    fun f1TopBar_displaysError_onError() {
        val meetingUiState = MeetingUiState.Error("Failed to fetch meeting data")

        composeTestRule.setContent {
            F1TopBar(meetingUiState = meetingUiState)
        }

        composeTestRule.onNodeWithText("Error: Failed to fetch meeting data").assertIsDisplayed()
    }

    @Test
    fun f1TopBar_displaysLoading_onLoading() {
        val meetingUiState = MeetingUiState.Loading

        composeTestRule.setContent {
            F1TopBar(meetingUiState = meetingUiState)
        }

        composeTestRule.onNode(hasTestTag("LoadingIndicator")).assertIsDisplayed()
    }
}