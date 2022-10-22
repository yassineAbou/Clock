package com.example.clock.util

import android.app.PendingIntent
import android.os.Build

object Constants {

    const val TIME_FORMAT = "%02d:%02d:%02d"

    val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }
}