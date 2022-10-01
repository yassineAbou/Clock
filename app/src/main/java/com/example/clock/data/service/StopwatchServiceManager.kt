package com.example.clock.data.service

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class StopwatchServiceManager @Inject constructor(
    @ApplicationContext val applicationContext: Context,
) {
    fun startStopwatchService() {
        val serviceIntent = Intent(applicationContext, StopwatchService::class.java)
        ContextCompat.startForegroundService(applicationContext, serviceIntent)
    }

    fun stopStopwatchService() {
        val serviceIntent = Intent(applicationContext, StopwatchService::class.java)
        applicationContext.stopService(serviceIntent)

    }
}