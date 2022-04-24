package com.example.clock.stopwatch

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.zhuinden.flowcombinetuplekt.combineTuple
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

data class Lap(val currentTime: String)

data class StopwatchState(
    val seconds: String,
    val minutes: String,
    val hours: String,
    val isPlaying: Boolean,
    val isZero: Boolean,
)

@OptIn(ExperimentalTime::class)
@Singleton
class StopwatchManager @Inject constructor() {
    var lapItems = mutableStateListOf<Lap>()
        private set

    private val secondsFlow = MutableStateFlow("00")
    private val minutesFlow = MutableStateFlow("00")
    private val hoursFlow = MutableStateFlow("00")
    private val isPlayingFlow =  MutableStateFlow(false)
    private val isZeroFlow = MutableStateFlow(false)

    val stopwatchState = combineTuple(
        secondsFlow,
        minutesFlow,
        hoursFlow,
        isPlayingFlow,
        isZeroFlow
    ).map { (seconds, minutes, hours, isPlaying, isZero) ->
        StopwatchState(
            seconds = seconds, minutes = minutes, hours = hours, isPlaying = isPlaying, isZero = isZero
        )
    }


    private var time: Duration = Duration.ZERO
    private lateinit var timer: Timer

    fun start() {
        timer = fixedRateTimer(initialDelay = 1000L, period = 1000L) {
            time = time.plus(Duration.seconds(1))
            updateTimeStates()
        }
        isPlayingFlow.value = true
        isZeroFlow.value= false
    }

    private fun updateTimeStates() {
        time.toComponents { hours, minutes, seconds, _ ->
            secondsFlow.value = seconds.pad()
            minutesFlow.value = minutes.pad()
            hoursFlow.value = hours.toInt().pad()
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
        isPlayingFlow.value = false
    }

    fun stop() {
        pause()
        time = Duration.ZERO
        updateTimeStates()
        isZeroFlow.value = true
    }
}