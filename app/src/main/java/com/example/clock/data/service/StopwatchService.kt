package com.example.clock.data.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.clock.data.manager.StopwatchManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StopwatchService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob())

    @Inject
    lateinit var stopwatchManager: StopwatchManager

    @Inject
    lateinit var stopwatchNotificationHelper: StopwatchNotificationHelper


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(
            STOPWATCH_SERVICE_NOTIFICATION_ID, stopwatchNotificationHelper.
            getStopwatchBaseNotification().build()
        )
        serviceScope.launch {
            stopwatchManager.stopwatchState.collectLatest {
                 if (!it.isReset) {
                     stopwatchNotificationHelper.updateStopwatchServiceNotification(
                         time = "${it.hour}:${it.minute}:${it.second}",
                         isPlaying = it.isPlaying,
                         isReset = it.isReset,
                         lastIndex = stopwatchManager.listTimes.lastIndex
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
        stopwatchNotificationHelper.removeStopwatchNotification()

    }
}