package com.example.clock.util

import android.app.ActivityManager
import android.content.Context
import androidx.core.text.isDigitsOnly
import com.example.clock.data.Alarm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.reduce
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun String?.parseLong(): Long {
    return if (this == null || this.isEmpty()) 0L else this.toLong()
}

fun String?.parseInt(): Int {
    return if (this == null || this.isEmpty()) 0 else this.toInt()
}

fun String.checkTimerInput(number: Int): Boolean {
    return this.length <= 2 && this.isDigitsOnly() &&   this.parseInt() <= number
}

object Global {
    val formatter = DateTimeFormatter.ofPattern("EEE,MMMdd")
    val current = LocalDateTime.now()
     val defaultValue = Alarm(targetDay = "Today-${current.format(formatter)}")
}


inline fun <T1: Any, T2: Any, T3: Any, R: Any> safeLet(p1: T1?, p2: T2?, p3: T3?, block: (T1, T2, T3)->R?): R? {
    return if (p1 != null && p2 != null && p3 != null) block(p1, p2, p3) else null
}

inline fun <T1: Any, T2: Any, R: Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2)->R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}

inline fun <T1: Any, T2: Any, T3: Any, T4: Any, R: Any> safeLet(p1: T1?, p2: T2?, p3: T3?, p4: T4?, block: (T1, T2, T3, T4)->R?): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null) block(p1, p2, p3, p4) else null
}


