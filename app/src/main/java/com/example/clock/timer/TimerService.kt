package com.example.clock.timer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.clock.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
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
            timerManager.timerState.collect {
                notificationHelper.updateNotificationChannel(it)
            }
        }

        return START_STICKY
    }


    override fun onBind(p0: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        notificationHelper.removeTimerNotificationService()
    }
}
