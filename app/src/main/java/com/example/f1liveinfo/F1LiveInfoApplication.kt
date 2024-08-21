package com.example.f1liveinfo

import android.app.Application
import com.example.f1liveinfo.data.AppContainer
import com.example.f1liveinfo.data.DefaultAppContainer

class F1LiveInfoApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}