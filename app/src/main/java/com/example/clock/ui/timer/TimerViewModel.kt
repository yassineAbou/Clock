package com.example.clock.ui.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.clock.data.manager.TimerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val timerManager: TimerManager,
) : ViewModel() {

    val timerState = timerManager.timerState.asLiveData()

    fun setHour(hour: Int) {
        timerManager.setTHour(hour)
    }

    fun setMinute(minute: Int) {
        timerManager.setMinute(minute)
    }

    fun setSecond(second: Int) {
        timerManager.setSeconds(second)
    }

    fun setCountDownTimer() {
        timerManager.setCountDownTimer()
    }

    fun handleCountDownTimer() {
        timerManager.handleCountDownTimer()
    }

    fun resetTimer() {
        timerManager.resetTimer()
    }
}
