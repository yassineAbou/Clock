package com.example.clock.alarm

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class AlarmServiceManager @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
) {

     fun startAlarmService(intent: Intent) {
        val intentService = Intent(applicationContext, AlarmService::class.java).apply {
            putExtra(TITLE, intent.getStringExtra(TITLE))
            putExtra(HOUR, intent.getStringExtra(HOUR))
            putExtra(MINUTE, intent.getStringExtra(MINUTE))
        }
        ContextCompat.startForegroundService(applicationContext, intentService)

    }

    fun stopAlarmService() {
        val serviceIntent = Intent(applicationContext, AlarmService::class.java)
        applicationContext.stopService(serviceIntent)

    }

    fun startRescheduleAlarmsService() {
        val intentService = Intent(applicationContext, RescheduleAlarmsService::class.java)
        ContextCompat.startForegroundService(applicationContext, intentService)

    }
}