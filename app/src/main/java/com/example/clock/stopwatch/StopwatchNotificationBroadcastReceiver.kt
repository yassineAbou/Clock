package com.example.clock.stopwatch

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.clock.util.safeLet
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StopwatchNotificationBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var stopwatchManager: StopwatchManager

    @Inject
    lateinit var stopwatchNotificationHelper: StopwatchNotificationHelper

    @Inject
    lateinit var stopwatchServiceManager: StopwatchServiceManager

    override fun onReceive(p0: Context?, p1: Intent?) {
        val timerRunning = p1?.getBooleanExtra(EXTRA_STOPWATCH_RUNNING, false)
        val isDone = p1?.getBooleanExtra(EXTRA_STOPWATCH_IS_DONE, false)
        val time = p1?.getStringExtra(EXTRA_STOPWATCH_TIME)
        val lastIndex = p1?.getIntExtra(EXTRA_STOPWATCH_LAST_INDEX, 0)
        val action = p1?.action

        action?.let {
            when (it) {
                com.example.clock.stopwatch.ACTION_RESET -> {
                    stopwatchManager.onClear()
                    stopwatchManager.stop()
                }
                ACTION_LAP -> stopwatchManager.onLap()
            }
        }


        safeLet(time, isDone, timerRunning, lastIndex) { currentTime, isZero, isPlaying, last ->
            stopwatchNotificationHelper.updateStopwatchServiceNotification(
                timerRunning = isPlaying,
                time = currentTime,
                isDone = isZero,
                lastIndex = last
            )
            if (isPlaying) {
                stopwatchManager.pause()
            } else {
                stopwatchManager.start()
            }
        }
    }



}


const val EXTRA_STOPWATCH_TIME = "EXTRA_STOPWATCH_TIME"
const val EXTRA_STOPWATCH_RUNNING = "EXTRA_STOPWATCH_RUNNING"
const val EXTRA_STOPWATCH_IS_DONE = "EXTRA_STOPWATCH_IS_DONE"
const val ACTION_RESET = "ACTION_RESET"
private const val TAG = "StopwatchNotificationBr"
const val ACTION_LAP = "ACTION_LAP"
const val EXTRA_STOPWATCH_LAST_INDEX = "EXTRA_STOPWATCH_LAST_INDEX"