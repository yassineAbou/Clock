package com.example.clock.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.clock.data.manager.StopwatchManager
import com.example.clock.data.service.StopwatchNotificationHelper
import com.example.clock.util.safeLet
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StopwatchNotificationBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var stopwatchManager: StopwatchManager

    @Inject
    lateinit var stopwatchNotificationHelper: StopwatchNotificationHelper


    override fun onReceive(p0: Context?, p1: Intent?) {
        val isPlaying = p1?.getBooleanExtra(EXTRA_STOPWATCH_IS_PLAYING, false)
        val isReset = p1?.getBooleanExtra(EXTRA_STOPWATCH_IS_RESET, false)
        val time = p1?.getStringExtra(EXTRA_STOPWATCH_TIME)
        val lastIndex = p1?.getIntExtra(EXTRA_STOPWATCH_LAST_INDEX, 0)
        val action = p1?.action

        action?.let {
            when (it) {
                ACTION_RESET -> {
                    stopwatchManager.clearListTimes()
                    stopwatchManager.reset()
                }
                ACTION_LAP -> stopwatchManager.addTime()
            }
        }


        safeLet(time, isReset, isPlaying, lastIndex) { safeTime, safeIsReset, safeIsPlaying, safeLastIndex ->
            stopwatchNotificationHelper.updateStopwatchServiceNotification(
                isPlaying = safeIsPlaying,
                time = safeTime,
                isReset = safeIsReset,
                lastIndex = safeLastIndex
            )
            if (safeIsPlaying) {
                stopwatchManager.stop()
            } else {
                stopwatchManager.start()
            }
        }
    }



}


const val EXTRA_STOPWATCH_TIME = "EXTRA_STOPWATCH_TIME"
const val EXTRA_STOPWATCH_IS_PLAYING = "EXTRA_STOPWATCH_IS_PLAYING"
const val EXTRA_STOPWATCH_IS_RESET = "EXTRA_STOPWATCH_IS_RESET"
const val ACTION_RESET = "ACTION_RESET"
const val ACTION_LAP = "ACTION_LAP"
const val EXTRA_STOPWATCH_LAST_INDEX = "EXTRA_STOPWATCH_LAST_INDEX"