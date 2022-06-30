package com.example.clock.timer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.example.clock.MainActivity
import com.example.clock.R
import com.example.clock.Screen
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val applicationContext: Context
) {
    private val  notificationManager = NotificationManagerCompat.from(applicationContext)

    private val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }

    private val openTimerIntent = Intent(
        Intent.ACTION_VIEW,
        "https://www.clock.com/Timer".toUri(),
        applicationContext,
        MainActivity::class.java
    )

    private val openTimerPendingIntent = PendingIntent.getActivity(
        applicationContext, 0, openTimerIntent, pendingIntentFlags
    )



    init {
        createNotificationChannels()
    }

    fun getBaseNotification() = NotificationCompat.Builder(applicationContext, TIMER_SERVICE_CHANNEL_ID)
        .setContentTitle("Timer")
        .setSmallIcon(R.drawable.ic_baseline_hourglass_empty_24)
        .setContentIntent(openTimerPendingIntent)
        .setSilent(true)
        .setOnlyAlertOnce(true)

     fun updateNotificationChannel(timerState: TimerState) {
        val notificationUpdate = getBaseNotification()
            .setContentText(timerState.time)
            .build()
        notificationManager.notify(TIMER_SERVICE_NOTIFICATION_ID, notificationUpdate)
    }

     fun showTimerCompletedNotification(time: String) {
            val timerCompletedNotification = NotificationCompat.Builder(applicationContext, TIMER_COMPLETED_CHANNEL_ID)
                .setContentTitle("Time's up")
                .setContentText(time)
                .setContentIntent(openTimerPendingIntent)
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

    private fun createNotificationChannels() {
        val timerServiceChannel = NotificationChannelCompat.Builder(
            TIMER_SERVICE_CHANNEL_ID,
            NotificationManagerCompat.IMPORTANCE_DEFAULT
        )
            .setName(applicationContext.getString(R.string.timer_service_channel_name))
            .setDescription(applicationContext.getString(R.string.timer_service_channel_description))
            .setSound(null, null)
            .build()

        val timerCompletedChannel = NotificationChannelCompat.Builder(
            TIMER_COMPLETED_CHANNEL_ID,
            NotificationManagerCompat.IMPORTANCE_HIGH
        )
            .setName(applicationContext.getString(R.string.timer_completed_channel_name))
            .setDescription(applicationContext.getString(R.string.timer_completed_channel_description))
            .build()

        notificationManager.createNotificationChannelsCompat(
            listOf(
                timerServiceChannel,
                timerCompletedChannel
            )
        )
    }
}

private const val TIMER_SERVICE_CHANNEL_ID = "timer_service_channel"
private const val TIMER_COMPLETED_CHANNEL_ID = "timer_completed_notification_channel"
private const val TIMER_COMPLETED_NOTIFICATION_ID = 124
const val TIMER_SERVICE_NOTIFICATION_ID = 123