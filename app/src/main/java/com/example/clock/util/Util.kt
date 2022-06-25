package com.example.clock.util

import androidx.core.text.isDigitsOnly
import com.example.clock.data.Alarm
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


