package com.example.clock.data.manager

import com.example.clock.data.model.TimerState
import com.example.clock.data.workManager.worker.TIMER_COMPLETED_TAG
import com.example.clock.data.workManager.worker.TIMER_RUNNING_TAG
import com.example.clock.data.workManager.worker.TimerCompletedWorker
import com.example.clock.data.workManager.worker.TimerRunningWorker
import com.example.clock.util.GlobalProperties.TIME_FORMAT
import com.example.clock.util.helper.CountDownTimerHelper
import com.zhuinden.flowcombinetuplekt.combineTuple
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Singleton
class TimerManager @Inject constructor(
    private val workRequestManager: WorkRequestManager,
) {

    private val timeInMillisFlow = MutableStateFlow(0L)
    private val timeTextFlow = MutableStateFlow("00:00:00")
    private val hourFlow = MutableStateFlow(0)
    private val minuteFlow = MutableStateFlow(0)
    private val secondFlow = MutableStateFlow(0)
    private val progressFlow = MutableStateFlow(0f)
    private val isPlayingFlow = MutableStateFlow(false)
    private val isDoneFlow = MutableStateFlow(true)

    val timerState = combineTuple(
        timeInMillisFlow,
        timeTextFlow,
        hourFlow,
        minuteFlow,
        secondFlow,
        progressFlow,
        isPlayingFlow,
        isDoneFlow,
    ).map { (timeInMillis, time, hour, minute, second, progress, isPlaying, isDone) ->
        TimerState(
            timeInMillis = timeInMillis,
            hour = hour,
            minute = minute,
            second = second,
            timeText = time,
            progress = progress,
            isPlaying = isPlaying,
            isDone = isDone,
        )
    }

    private var countDownTimerHelper: CountDownTimerHelper? = null

    fun setTHour(hour: Int) {
        hourFlow.value = hour
    }

    fun setMinute(minute: Int) {
        minuteFlow.value = minute
    }

    fun setSecond(second: Int) {
        secondFlow.value = second
    }

    fun setCountDownTimer() {
        timeInMillisFlow.value =
            (hourFlow.value.hours + minuteFlow.value.minutes + secondFlow.value.seconds).inWholeMilliseconds
        countDownTimerHelper = object : CountDownTimerHelper(timeInMillisFlow.value, 1000) {
            override fun onTimerTick(millisUntilFinished: Long) {
                val progressValue = millisUntilFinished.toFloat() / timeInMillisFlow.value
                handleTimerValues(true, millisUntilFinished.formatTime(), progressValue)
            }
            override fun onTimerFinish() {
                workRequestManager.enqueueWorker<TimerCompletedWorker>(TIMER_COMPLETED_TAG)
                reset()
            }
        }
    }

    fun pause() {
        countDownTimerHelper?.pause()
        isPlayingFlow.value = false
    }

    fun reset() {
        countDownTimerHelper?.restart()
        handleTimerValues(false, timeInMillisFlow.value.formatTime(), 0f)
        isDoneFlow.value = true
        workRequestManager.cancelWorker(TIMER_RUNNING_TAG)
    }

    fun start() {
        countDownTimerHelper?.start()
        isPlayingFlow.value = true
        if (isDoneFlow.value) {
            progressFlow.value = 1f
            workRequestManager.enqueueWorker<TimerRunningWorker>(TIMER_RUNNING_TAG)
            isDoneFlow.value = false
        }
    }

    private fun handleTimerValues(
        isPlaying: Boolean,
        text: String,
        progress: Float,
    ) {
        isPlayingFlow.value = isPlaying
        timeTextFlow.value = text
        progressFlow.value = progress
    }

    fun Long.formatTime(): String = String.format(
        TIME_FORMAT,
        TimeUnit.MILLISECONDS.toHours(this),
        TimeUnit.MILLISECONDS.toMinutes(this) % 60,
        TimeUnit.MILLISECONDS.toSeconds(this) % 60,
    )
}
