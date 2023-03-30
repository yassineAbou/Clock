package com.example.clock.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.clock.data.manager.StopwatchManager
import com.example.clock.util.helper.StopwatchNotificationHelper
import com.example.clock.util.safeLet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StopwatchNotificationBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var stopwatchManager: StopwatchManager

    @Inject
    lateinit var stopwatchNotificationHelper: StopwatchNotificationHelper

    private val broadcastReceiverScope = CoroutineScope(SupervisorJob())

    override fun onReceive(p0: Context?, p1: Intent?) {
        val pendingResult: PendingResult = goAsync()
        broadcastReceiverScope.launch(Dispatchers.Default) {
            try {
                val isPlaying = p1?.getBooleanExtra(STOPWATCH_IS_PLAYING_EXTRA, false)
                val time = p1?.getStringExtra(STOPWATCH_TIME_EXTRA)
                val lastLapIndex = p1?.getIntExtra(STOPWATCH_LAST_INDEX_EXTRA, 0)
                val action = p1?.action

                action?.let {
                    when (it) {
                        STOPWATCH_RESET_ACTION -> {
                            stopwatchManager.clear()
                            stopwatchManager.reset()
                        }
                        STOPWATCH_LAP_ACTION -> stopwatchManager.lap()
                    }
                }

                safeLet(
                    time,
                    isPlaying,
                    lastLapIndex,
                ) { safeTime, safeIsPlaying, safeLastIndex ->
                    stopwatchNotificationHelper.updateStopwatchServiceNotification(
                        isPlaying = safeIsPlaying,
                        time = safeTime,
                        lastLapIndex = safeLastIndex,
                    )
                    if (safeIsPlaying) {
                        stopwatchManager.stop()
                    } else {
                        stopwatchManager.start()
                    }
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}

const val STOPWATCH_TIME_EXTRA = "STOPWATCH_TIME_EXTRA"
const val STOPWATCH_IS_PLAYING_EXTRA = "STOPWATCH_IS_PLAYING_EXTRA"
const val STOPWATCH_RESET_ACTION = "STOPWATCH_RESET_ACTION"
const val STOPWATCH_LAP_ACTION = "STOPWATCH_LAP_ACTION"
const val STOPWATCH_LAST_INDEX_EXTRA = "STOPWATCH_LAST_INDEX_EXTRA"
