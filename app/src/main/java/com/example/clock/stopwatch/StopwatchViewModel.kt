package com.example.clock.stopwatch

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
@HiltViewModel
class StopwatchViewModel @Inject constructor(
    private val stopwatchManager: StopwatchManager
) : ViewModel(), StopwatchScreenActions {

    val stopwatchState = stopwatchManager.stopwatchState.asLiveData()

    val lapItems = stopwatchManager.lapItems

    override fun onStart() {
        stopwatchManager.start()
    }

    override fun onLap() {
        stopwatchManager.onLap()
    }

    override fun onClear() {
        stopwatchManager.onClear()
    }

    override fun onPause() {
        stopwatchManager.pause()
    }

    override fun onStop() {
        stopwatchManager.stop()
    }


}



