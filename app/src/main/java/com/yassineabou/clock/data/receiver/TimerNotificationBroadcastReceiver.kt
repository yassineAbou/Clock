package com.yassineabou.clock.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.yassineabou.clock.data.manager.TimerManager
import com.yassineabou.clock.data.manager.WorkRequestManager
import com.yassineabou.clock.data.workManager.worker.TIMER_COMPLETED_TAG
import com.yassineabou.clock.util.helper.TimerNotificationHelper
import com.yassineabou.clock.util.safeLet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TimerNotificationBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var timerManager: TimerManager

    @Inject
    lateinit var timerNotificationHelper: TimerNotificationHelper

    @Inject
    lateinit var workRequestManager: WorkRequestManager

    private val broadcastReceiverScope = CoroutineScope(SupervisorJob())

    override fun onReceive(p0: Context?, intent: Intent?) {
        val pendingResult: PendingResult = goAsync()
        broadcastReceiverScope.launch(Dispatchers.Default) {
            try {
                val isPlaying = intent?.getBooleanExtra(TIMER_RUNNING_IS_PLAYING, false)
                val timeText = intent?.getStringExtra(TIMER_RUNNING_TIME_TEXT)
                val action = intent?.action

                action?.let {
                    when (it) {
                        TIMER_RUNNING_CANCEL_ACTION -> timerManager.reset()
                        TIMER_COMPLETED_DISMISS_ACTION -> workRequestManager.cancelWorker(TIMER_COMPLETED_TAG)
                        TIMER_COMPLETED_RESTART_ACTION -> {
                            workRequestManager.cancelWorker(TIMER_COMPLETED_TAG)
                            timerManager.start()
                        }
                    }
                }

                safeLet(isPlaying, timeText) { safeIsPlaying, safeTime ->
                    timerNotificationHelper.updateTimerServiceNotification(
                        isPlaying = safeIsPlaying,
                        timeText = safeTime,
                    )

                    if (safeIsPlaying) {
                        timerManager.pause()
                    } else {
                        timerManager.start()
                    }
                }
            } finally {
                pendingResult.finish()
                broadcastReceiverScope.cancel()
            }
        }
    }
}

const val TIMER_RUNNING_TIME_TEXT = "TIMER_RUNNING_TIME_TEXT"
const val TIMER_RUNNING_IS_PLAYING = "TIMER_RUNNING_IS_PLAYING"
const val TIMER_RUNNING_CANCEL_ACTION = "TIMER_RUNNING_CANCEL_ACTION"
const val TIMER_COMPLETED_DISMISS_ACTION = "TIMER_COMPLETED_DISMISS_ACTION"
const val TIMER_COMPLETED_RESTART_ACTION = "TIMER_COMPLETED_RESTART_ACTION"
