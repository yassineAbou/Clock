package com.example.clock.util

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import androidx.core.text.isDigitsOnly
import com.example.clock.data.model.Alarm
import com.example.clock.util.Constants.dateTimeFormatter
import com.example.clock.util.Constants.nextDay
import java.time.LocalDateTime
import java.util.*

fun String?.parseInt(): Int {
    return if (this == null || this.isEmpty()) 0 else this.toInt()
}

fun String.checkNumberPicker(maxNumber: Int): Boolean {
    return this.length <= 2 && this.isDigitsOnly() && this.parseInt() <= maxNumber
}

fun Alarm.checkDate(): String {
    val currentTime = Calendar.getInstance()
    val timeToMatch = Calendar.getInstance()
    val currentDay = LocalDateTime.now()
    timeToMatch[Calendar.HOUR_OF_DAY] = this.hour.parseInt()
    timeToMatch[Calendar.MINUTE] = this.minute.parseInt()

    return when {
        currentTime < timeToMatch -> "Today-${currentDay.format(dateTimeFormatter)}"
        currentTime >= timeToMatch -> "Tomorrow-${nextDay.format(dateTimeFormatter)}"
        else -> this.description
    }
}

fun Class<out BroadcastReceiver>?.setIntentAction(
    actionName: String,
    requestCode: Int,
    context: Context,
): PendingIntent {
    val broadcastIntent =
        Intent(context, this).apply {
            action = actionName
        }
    return PendingIntent.getBroadcast(
        context,
        requestCode,
        broadcastIntent,
        Constants.pendingIntentFlags,
    )
}

fun <T> Context.isServiceRunning(service: Class<T>) =
    (getSystemService(ACTIVITY_SERVICE) as ActivityManager)
        .getRunningServices(Integer.MAX_VALUE)
        .any { it.service.className == service.name }

fun Context.isBackgroundRunning(): Boolean {
    val am = this.getSystemService(ACTIVITY_SERVICE) as ActivityManager
    val runningProcesses = am.runningAppProcesses
    for (processInfo in runningProcesses) {
        if (processInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
            for (activeProcess in processInfo.pkgList) {
                if (activeProcess == this.packageName) {
                    return false
                }
            }
        }
    }
    return true
}

inline fun <T1 : Any, T2 : Any, R : Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}
inline fun <T1 : Any, T2 : Any, T3 : Any, R : Any> safeLet(p1: T1?, p2: T2?, p3: T3?, block: (T1, T2, T3) -> R?): R? {
    return if (p1 != null && p2 != null && p3 != null) block(p1, p2, p3) else null
}
inline fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any, R : Any> safeLet(p1: T1?, p2: T2?, p3: T3?, p4: T4?, block: (T1, T2, T3, T4) -> R?): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null) block(p1, p2, p3, p4) else null
}
