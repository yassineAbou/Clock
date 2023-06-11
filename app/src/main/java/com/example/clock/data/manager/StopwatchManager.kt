package com.example.clock.data.manager

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.clock.data.model.StopwatchState
import com.example.clock.data.workmanager.worker.STOPWATCH_TAG
import com.example.clock.data.workmanager.worker.StopwatchWorker
import com.example.clock.util.GlobalProperties.TIME_FORMAT
import com.zhuinden.flowcombinetuplekt.combineTuple
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.util.Timer
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Singleton
class StopwatchManager @Inject constructor(
    @ApplicationContext val applicationContext: Context,
) {

    var lapTimes = mutableStateListOf<String>()
        private set

    private val secondFlow = MutableStateFlow("00")
    private val minuteFlow = MutableStateFlow("00")
    private val hourFlow = MutableStateFlow("00")
    private val isPlayingFlow = MutableStateFlow(false)
    private val isResetFlow = MutableStateFlow(true)

    val stopwatchState = combineTuple(
        secondFlow,
        minuteFlow,
        hourFlow,
        isPlayingFlow,
        isResetFlow,
    ).map { (second, minute, hour, isPlaying, isReset) ->
        StopwatchState(
            second = second,
            minute = minute,
            hour = hour,
            isPlaying = isPlaying,
            isReset = isReset,
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
        if (isResetFlow.value) {
            isResetFlow.value = false
            val thirdWorkRequest =
                OneTimeWorkRequestBuilder<StopwatchWorker>().addTag(
                    STOPWATCH_TAG,
                ).build()
            WorkManager.getInstance(applicationContext).enqueue(thirdWorkRequest)
        }
    }

    private fun updateStopwatchState() {
        duration.toComponents { hours, minutes, seconds, _ ->
            secondFlow.value = seconds.pad()
            minuteFlow.value = minutes.pad()
            hourFlow.value = hours.toInt().pad()
        }
    }

    fun lap() {
        val time = duration.toComponents { hours, minutes, seconds, _ ->
            String.format(TIME_FORMAT, hours, minutes, seconds)
        }
        lapTimes.add(time)
    }

    fun clear() {
        lapTimes.clear()
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
        WorkManager.getInstance(applicationContext).cancelAllWorkByTag(STOPWATCH_TAG)
        stop()
        duration = Duration.ZERO
        updateStopwatchState()
    }
}
