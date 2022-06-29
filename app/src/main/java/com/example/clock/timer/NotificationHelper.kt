package com.example.clock.timer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.clock.MainActivity
import com.example.clock.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val applicationContext: Context
) {
    private val  notificationManager = NotificationManagerCompat.from(applicationContext)

    private val mutabilityFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }

    private val openActivityIntent = Intent(applicationContext, MainActivity::class.java)
    private val openActivityPendingIntent = PendingIntent.getActivity(
        applicationContext, 0, openActivityIntent, mutabilityFlag
    )

    init {
        createNotificationChannel()
    }

    fun getBaseNotification() = NotificationCompat.Builder(applicationContext, TIMER_SERVICE_CHANNEL_ID)
        .setContentTitle("Timer")
        .setSmallIcon(R.drawable.ic_baseline_hourglass_empty_24)
        .setContentIntent(openActivityPendingIntent)
        .setSilent(true)
        .setOnlyAlertOnce(true)

     fun updateNotificationChannel(timerState: TimerState) {
        val notificationUpdate = getBaseNotification()
            .setContentText(timerState.time)
            .build()
        notificationManager.notify(TIMER_SERVICE_NOTIFICATION_ID, notificationUpdate)
    }

     fun showTimerCompletedNotification(time: String) {
            val timerCompletedNotification = NotificationCompat.Builder(applicationContext, TIMER_SERVICE_CHANNEL_ID)
                .setContentTitle("Time's up")
                .setContentText(time)
                .setContentIntent(openActivityPendingIntent)
                .setSmallIcon(R.drawable.ic_baseline_hourglass_empty_24)
                .build()
            notificationManager.notify(TIMER_COMPLETED_NOTIFICATION_ID, timerCompletedNotification)
    }

    fun removeTimerNotificationService() {
        notificationManager.cancel(TIMER_SERVICE_NOTIFICATION_ID)
    }

    fun removeTimerCompletedNotification() {
        notificationManager.cancel(TIMER_COMPLETED_NOTIFICATION_ID)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val timerServiceChannel = NotificationChannel(
                TIMER_SERVICE_CHANNEL_ID,
                applicationContext.getString(R.string.timer_service_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            timerServiceChannel.apply {
                setSound(null, null)
            }
            notificationManager.createNotificationChannel(timerServiceChannel)
        }
    }
}

private const val TIMER_SERVICE_CHANNEL_ID = "timer_service_channel"
private const val TIMER_COMPLETED_NOTIFICATION_ID = 124
const val TIMER_SERVICE_NOTIFICATION_ID = 123