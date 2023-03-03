package com.example.clock.ui.stopwatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.clock.data.manager.StopwatchManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.time.ExperimentalTime

@ExperimentalTime
@HiltViewModel
class StopwatchViewModel @Inject constructor(
    private val stopwatchManager: StopwatchManager,
) : ViewModel() {

    val stopwatchState = stopwatchManager.stopwatchState.asLiveData()

    val listTimes = stopwatchManager.listTimes

    fun start() {
        stopwatchManager.start()
    }

    fun stop() {
        stopwatchManager.stop()
    }

    fun addTime() {
        stopwatchManager.addTime()
    }

    fun clearListTimes() {
        stopwatchManager.clearListTimes()
    }

    fun reset() {
        stopwatchManager.reset()
    }
}

private const val TAG = "StopwatchViewModel"
