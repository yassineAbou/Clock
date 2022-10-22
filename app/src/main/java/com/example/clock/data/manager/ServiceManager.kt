package com.example.clock.data.manager

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
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
}