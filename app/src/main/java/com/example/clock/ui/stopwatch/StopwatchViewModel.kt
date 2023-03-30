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
) : ViewModel(), StopwatchActions {

    val stopwatchState = stopwatchManager.stopwatchState.asLiveData()

    val lapTimes = stopwatchManager.lapTimes

    override fun start() {
        stopwatchManager.start()
    }

    override fun stop() {
        stopwatchManager.stop()
    }

    override fun lap() {
        stopwatchManager.lap()
    }

    override fun clear() {
        stopwatchManager.clear()
    }

    override fun reset() {
        stopwatchManager.reset()
    }
}

