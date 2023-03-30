package com.example.clock.data.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.clock.data.manager.TimerManager
import com.example.clock.util.helper.TIMER_RUNNING_NOTIFICATION_ID
import com.example.clock.util.helper.TimerNotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TimerRunningService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob())

    @Inject
    lateinit var timerManager: TimerManager

    @Inject
    lateinit var timerNotificationHelper: TimerNotificationHelper

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(
            TIMER_RUNNING_NOTIFICATION_ID,
            timerNotificationHelper.getTimerBaseNotification().build(),
        )

        serviceScope.launch(Dispatchers.Main) {
            timerManager.timerState.collectLatest {
                if (!it.isDone) {
                    timerNotificationHelper.updateTimerServiceNotification(
                        isPlaying = it.isPlaying,
                        timeText = it.timeText,
                    )
                }
            }
        }

        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        timerNotificationHelper.removeTimerRunningNotification()
    }
}
