package com.example.clock.data.manager

import androidx.compose.runtime.mutableStateListOf
import com.example.clock.data.service.StopwatchService
import com.example.clock.util.Constants.TIME_FORMAT
import com.zhuinden.flowcombinetuplekt.combineTuple
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

data class StopwatchState(
    val second: String,
    val minute: String,
    val hour: String,
    val isPlaying: Boolean,
    val isReset: Boolean,
)

@OptIn(ExperimentalTime::class)
@Singleton
class StopwatchManager @Inject constructor(
    private val serviceManager: ServiceManager
) {

    var listTimes = mutableStateListOf<String>()
        private set

    private val secondFlow = MutableStateFlow("00")
    private val minuteFlow = MutableStateFlow("00")
    private val hourFlow = MutableStateFlow("00")
    private val isPlayingFlow =  MutableStateFlow(false)
    private val isResetFlow = MutableStateFlow(true)

    val stopwatchState = combineTuple(
        secondFlow,
        minuteFlow,
        hourFlow,
        isPlayingFlow,
        isResetFlow
    ).map { ( second, minute, hour, isPlaying, isReset) ->
        StopwatchState(
           second = second, minute = minute, hour = hour, isPlaying = isPlaying, isReset = isReset
        )
    }


    private var duration: Duration = Duration.ZERO
    private var timer: Timer? = null

    fun start() {
        timer = fixedRateTimer(initialDelay = 1000L, period = 1000L) {
            duration = duration.plus(1.seconds)
            updateStopwatchState()
        }
        isPlayingFlow.value = true
        isResetFlow.value= false
    }

    private fun updateStopwatchState() {
        duration.toComponents { hours, minutes, seconds, _ ->
            secondFlow.value = seconds.pad()
            minuteFlow.value = minutes.pad()
            hourFlow.value = hours.toInt().pad()
        }
    }


    fun addTime() {
        val time =  duration.toComponents { hours, minutes, seconds, _ ->
            String.format(TIME_FORMAT, hours, minutes, seconds)
        }
        listTimes.add(time)
    }


    fun clearListTimes() {
        listTimes.clear()
    }

    private fun Int.pad(): String {
        return this.toString().padStart(2, '0')
    }

    fun stop() {
        timer?.cancel()
        isPlayingFlow.value = false
    }

    fun reset() {
        isResetFlow.value = true
        stop()
        duration = Duration.ZERO
        updateStopwatchState()
        serviceManager.stopService(StopwatchService::class.java)
    }
}

