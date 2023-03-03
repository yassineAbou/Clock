package com.example.clock.data.manager

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.clock.data.receiver.HOUR
import com.example.clock.data.receiver.MINUTE
import com.example.clock.data.receiver.TITLE
import com.example.clock.data.service.AlarmService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ServiceManager @Inject constructor(
    @ApplicationContext val applicationContext: Context,
) {
    fun startService(service: Class<*>?) {
        val serviceIntent = Intent(applicationContext, service)
        ContextCompat.startForegroundService(applicationContext, serviceIntent)
    }

    fun stopService(service: Class<*>?) {
        val serviceIntent = Intent(applicationContext, service)
        applicationContext.stopService(serviceIntent)
    }

    fun startAlarmService(intent: Intent) {
        val alarmServiceIntent = Intent(applicationContext, AlarmService::class.java).apply {
            putExtra(TITLE, intent.getStringExtra(TITLE))
            putExtra(HOUR, intent.getStringExtra(HOUR))
            putExtra(MINUTE, intent.getStringExtra(MINUTE))
        }
        ContextCompat.startForegroundService(applicationContext, alarmServiceIntent)
    }
}
