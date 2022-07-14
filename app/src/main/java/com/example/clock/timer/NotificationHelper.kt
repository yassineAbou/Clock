package com.example.clock.timer

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.system.Os.remove
import android.util.Log
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
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
        .setSmallIcon(R.drawable.ic_hourglass_empty)
        .setContentIntent(openTimerPendingIntent)
        .setAutoCancel(true)
        .setColor(ContextCompat.getColor(applicationContext, R.color.blue))
        .setColorized(true)
        .setSilent(true)
        .setOnlyAlertOnce(true)

     fun updateTimerServiceNotification(
         time: String,
         timerRunning: Boolean,
         isDone: Boolean
     ) {
         val actionIntent = getTimerNotificationActionIntent(time, timerRunning, isDone)
         val remove =  remove()
         val startStopIcon = if (timerRunning) R.drawable.ic_stop else R.drawable.ic_play
         val startStopLabel = if (timerRunning) "Pause" else "Resume"

         val notificationUpdate = getBaseNotification()
             .setContentText(time)
             .addAction(R.drawable.ic_close, "Cancel", remove)
             .addAction(
                 startStopIcon,
                 startStopLabel,
                 actionIntent
             )
             .build()
             notificationManager.notify(TIMER_SERVICE_NOTIFICATION_ID, notificationUpdate)
    }

    private fun remove() : PendingIntent {
        val broadcastIntent =
            Intent(applicationContext, TimerNotificationBroadcastReceiver::class.java).apply {
                action = ACTION_DELETE
            }
        return PendingIntent.getBroadcast(
            applicationContext,
            2,
            broadcastIntent,
            pendingIntentFlags
        )
    }

    private fun getTimerNotificationActionIntent(
        time: String,
        timerRunning: Boolean,
        isDone: Boolean
    ): PendingIntent {
        val broadcastIntent =
            Intent(applicationContext, TimerNotificationBroadcastReceiver::class.java).apply {
                putExtra(EXTRA_TIME, time)
                putExtra(EXTRA_TIMER_RUNNING, timerRunning)
                putExtra(EXTRA_IS_DONE, isDone)
            }
        return PendingIntent.getBroadcast(
            applicationContext,
            1,
            broadcastIntent,
            pendingIntentFlags
        )
    }

     fun showTimerCompletedNotification(time: String) {
            val timerCompletedNotification = NotificationCompat.Builder(applicationContext, TIMER_COMPLETED_CHANNEL_ID)
                .setContentTitle("Time's up")
                .setContentText(time)
                .setContentIntent(openTimerPendingIntent)
                .setSmallIcon(R.drawable.ic_hourglass_empty)
                .build()
            notificationManager.notify(TIMER_COMPLETED_NOTIFICATION_ID, timerCompletedNotification)
    }

    fun removeTimerCompletedNotification() {
        notificationManager.cancel(TIMER_COMPLETED_NOTIFICATION_ID)
    }

    fun removeTimerServiceNotification() {
        notificationManager.cancel(TIMER_SERVICE_NOTIFICATION_ID)
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
const val TIMER_SERVICE_NOTIFICATION_ID = -1
private const val TIMER_COMPLETED_NOTIFICATION_ID = -2
private const val TAG = "NotificationHelper"