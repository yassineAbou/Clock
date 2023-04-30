package com.example.clock.util

import android.app.PendingIntent
import android.os.Build
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object GlobalProperties {

    const val TIME_FORMAT = "%02d:%02d:%02d"

    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE,MMMdd")
    val nextDay: LocalDateTime = LocalDateTime.now().plus(1, ChronoUnit.DAYS)

    val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }
}
