package com.example.f1liveinfo.data

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.f1liveinfo.model.Meeting
import kotlinx.serialization.json.Json
import java.net.URL

class FetchMeetingWorker(appContext: Context, workerParams: WorkerParameters):
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {

        val json = Json { ignoreUnknownKeys = true }
        val response = try {
            //Replace with your actual network call
            URL("https://api.openf1.org/v1/meetings?meeting_key=latest").readText()
        } catch (e: Exception) {
            return Result.failure()
        }

        Log.d("URL", "doWork1: $response")

        val meetingData = json.decodeFromString<List<Meeting>>(response)[0]
        Meeting.getInstance().apply {
            location = meetingData.location
            countryName = meetingData.countryName
            meetingName = meetingData.meetingName
            gmtOffset = meetingData.gmtOffset
            dateStart = meetingData.dateStart
            year = meetingData.year
        }

        Log.d("URL", "doWork2: $meetingData")


        return Result.success()
    }
}