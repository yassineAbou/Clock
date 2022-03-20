package com.example.clock.stopwatch

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.*
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
class StopwatchViewModel : ViewModel() {


    private var time: Duration = Duration.ZERO
    private lateinit var timer: Timer

    var seconds by mutableStateOf("00")
        private set
    var minutes by mutableStateOf("00")
        private set
    var hours by mutableStateOf("00")
        private set
    var isPlaying by mutableStateOf(false)
        private set
    var isZero by mutableStateOf(true)
        private set

    var lapItems = mutableStateListOf<Lap>()
        private set

    fun start() {
        timer = fixedRateTimer(initialDelay = 1000L, period = 1000L) {
            time = time.plus(Duration.seconds(1))
            updateTimeStates()
        }
        isPlaying = true
        isZero = false
    }

    private fun updateTimeStates() {
        time.toComponents { hours, minutes, seconds, _ ->
            this.seconds = seconds.pad()
            this.minutes = minutes.pad()
            this.hours = hours.toInt().pad()
        }
    }

    fun onLap() {
        val timeString =  time.toComponents { hours, minutes, seconds, _ ->
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        }
        val lap = Lap(timeString)
        lapItems.add(lap)
        println(lapItems)
    }

    fun onClear() {
       lapItems.clear()
    }

    private fun Int.pad(): String {
        return this.toString().padStart(2, '0')
    }

    fun pause() {
        timer.cancel()
        isPlaying = false
    }

    fun stop() {
        pause()
        time = Duration.ZERO
        updateTimeStates()
        isZero = true
    }

}

data class Lap(val currentTime: String)

val initialLapItems = listOf(
    Lap("00:00:01"),
    Lap("00:00:02"),
    Lap("00:00:03"),
    Lap("00:00:04"),
    Lap("00:00:05"),
    Lap("00:00:06"),
    Lap("00:00:07"),
    Lap("00:00:08"),
    Lap("00:00:09"),
    Lap("00:00:10")
)