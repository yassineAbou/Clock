package com.example.clock.timer

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltViewModel
class TimerViewModel @Inject constructor(
    private val timerManager: TimerManager
) : ViewModel(), TimerScreenActions {

    val timerState = timerManager.timerState.asLiveData()

    override fun setHours(hours: Long) {
        timerManager.setTHours(hours)
    }

    override fun setMinutes(minutes: Long) {
        timerManager.setMinutes(minutes)
    }

    override fun setSeconds(seconds: Long) {
        timerManager.setSeconds(seconds)
    }

    override fun setTime() {
        timerManager.setTime()
    }

    override fun handleCountDownTimer() {
        timerManager.handleCountDownTimer()
    }

    override fun resetTimer() {
        timerManager.resetTimer()
    }

    override fun onChangeDone() {
        timerManager.onChangeDone()
    }

    override fun stopService() {
        timerManager.stopService()
    }


}