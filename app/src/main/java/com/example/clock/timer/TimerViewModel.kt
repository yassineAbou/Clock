package com.example.clock.timer

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.util.concurrent.TimeUnit

 const val TIME_FORMAT = "%02d:%02d:%02d"

class TimerViewModel : ViewModel() {

    private var countDownTimer: CountDownTimerExt? = null
    private val timeInLong = mutableStateOf(0L)
    var time = mutableStateOf(timeInLong.value.formatTime())
        private set
    var progress = mutableStateOf(1.00F)
        private set
    var isPlaying = mutableStateOf(false)
        private set
    var hours = mutableStateOf(0L)
        private set
    var minutes = mutableStateOf(0L)
        private set
    var seconds = mutableStateOf(0L)
        private set

    fun setTHours(hours: Long) {
        this.hours.value = hours.times(3600000L)
    }

    fun setMinutes(minutes: Long) {
        this.minutes.value = minutes.times(60000L)
    }

    fun setSeconds(seconds: Long) {
        this.seconds.value = seconds.times(1000L)
    }

    fun setTime() {
        timeInLong.value = hours.value + minutes.value + seconds.value
        countDownTimer = object : CountDownTimerExt(timeInLong.value, 1000) {
            override fun onTimerTick(millisUntilFinished: Long) {
                val progressValue = millisUntilFinished.toFloat() / timeInLong.value
                handleTimerValues(true, millisUntilFinished.formatTime(), progressValue)
            }

            override fun onTimerFinish() {
                handleTimerValues(false, timeInLong.value.formatTime(), 1.0F)
            }
        }
    }

        fun handleCountDownTimer() {
            if (isPlaying.value) {
                pauseTimer()
            } else {
               startTimer()
            }
        }

         fun pauseTimer() {
            countDownTimer?.pause()
            isPlaying.value = false

        }

        private fun startTimer() {
            countDownTimer?.start()
            isPlaying.value = true
        }

        private fun handleTimerValues(
            isPlaying1: Boolean,
            text: String,
            progress1: Float
        ) {
            isPlaying.value = isPlaying1
            time.value = text
            progress.value = progress1
        }

        fun Long.formatTime(): String = String.format(
            TIME_FORMAT,
            TimeUnit.MILLISECONDS.toHours(this),
            TimeUnit.MILLISECONDS.toMinutes(this) % 60,
            TimeUnit.MILLISECONDS.toSeconds(this) % 60
        )

}