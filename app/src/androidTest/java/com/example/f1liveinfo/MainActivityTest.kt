package com.example.f1liveinfo

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription

import androidx.compose.ui.test.onNodeWithText
import com.example.f1liveinfo.model.Driver
import com.example.f1liveinfo.model.Meeting
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun meetingContent_isDisplayed() {
        val meeting = Meeting(
            meetingName = "Belgian Grand Prix",
            location = "Spa-Francorchamps",
            countryName = "BEL",
            dateStart = "2024-07-26T11:30:00+00:00",
            gmtOffset = "02:00:00",
            year = 2024
        )
        composeTestRule.setContent {
            MeetingContent(meeting = meeting)
        }
        composeTestRule.onNodeWithText("Belgian Grand Prix").assertIsDisplayed()
        composeTestRule.onNodeWithText("Spa-Francorchamps, BEL").assertIsDisplayed()
    }

    @Test
    fun driverStatus_isDisplayed() {
        val driver = Driver(
            lastName = "Verstappen",
            firstName = "Max",
            countryCode = "NED",
            teamName = "Red Bull Racing",
            driverNumber = 1,
            teamColor = "3671C6",
            position = 15
        )
        composeTestRule.setContent {
            DriverStatus(driver = driver)
        }
        composeTestRule.onNodeWithText("15.").assertIsDisplayed()
        composeTestRule.onNodeWithText("Max Verstappen 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Red Bull Racing").assertIsDisplayed()
    }

    @Test
    fun loadingScreen_isDisplayed() {
        composeTestRule.setContent {
            LoadingScreen()
        }
        composeTestRule.onNodeWithContentDescription("Loading").assertIsDisplayed()
    }

    @Test
    fun errorScreen_isDisplayed() {
        composeTestRule.setContent {
            ErrorScreen()
        }
        //composeTestRule.onNodeWithContentDescription("").assertIsDisplayed()
        composeTestRule.onNodeWithText("Failed to load").assertIsDisplayed()
    }
}