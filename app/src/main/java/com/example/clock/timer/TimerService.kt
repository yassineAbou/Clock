package com.example.clock.timer

import android.app.Service
import android.content.Intent
import android.os.IBinder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TimerService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob())

    @Inject
    lateinit var timerManager: TimerManager

    @Inject
    lateinit var notificationHelper: NotificationHelper


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(TIMER_SERVICE_NOTIFICATION_ID, notificationHelper.getBaseNotification().build())

        serviceScope.launch {
            timerManager.timerState.collectLatest {
                if (!it.isDone && it.progress != 1.0F) {
                    notificationHelper.updateTimerServiceNotification(
                        timerRunning = it.isPlaying,
                        time = it.time,
                        isDone = it.isDone
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
        stopForeground(STOP_FOREGROUND_REMOVE)
        timerManager.onChangeDone()
        //notificationHelper.removeTimerServiceNotification()
    }
}

private const val TAG = "TimerService"
