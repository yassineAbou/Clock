package com.example.clock.ui.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.clock.data.manager.TimerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val timerManager: TimerManager,
) : ViewModel(), TimerActions {

    val timerState = timerManager.timerState.asLiveData()

    override fun setHour(hour: Int) {
        timerManager.setTHour(hour)
    }

    override fun setMinute(minute: Int) {
        timerManager.setMinute(minute)
    }

    override fun setSecond(second: Int) {
        timerManager.setSecond(second)
    }

    override fun setCountDownTimer() {
        timerManager.setCountDownTimer()
    }

    override fun start() {
        timerManager.start()
    }

    override fun pause() {
        timerManager.pause()
    }

    override fun reset() {
        timerManager.reset()
    }
}
