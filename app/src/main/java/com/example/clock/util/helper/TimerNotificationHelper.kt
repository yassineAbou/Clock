package com.example.clock.util.helper

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.clock.ui.MainActivity
import com.example.clock.R
import com.example.clock.data.receiver.*
import com.example.clock.util.Constants.pendingIntentFlags
import com.example.clock.util.setIntentAction
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimerNotificationHelper @Inject constructor(
    @ApplicationContext private val applicationContext: Context
) {
    private val  notificationManager = NotificationManagerCompat.from(applicationContext)

    private val timerNotificationBroadcastReceiver = TimerNotificationBroadcastReceiver::class.java

    private val openTimerIntent = Intent(
        Intent.ACTION_VIEW,
        "https://www.clock.com/Timer".toUri(),
        applicationContext,
        MainActivity::class.java
    )

    private val openTimerPendingIntent = PendingIntent.getActivity(
        applicationContext, 0, openTimerIntent, pendingIntentFlags
    )

    private val dismissIntentAction = timerNotificationBroadcastReceiver.setIntentAction(
        actionName = TIMER_COMPLETED_DISMISS_ACTION, requestCode = 7, context = applicationContext
    )

    private val restartIntentAction = timerNotificationBroadcastReceiver.setIntentAction(
        actionName = TIMER_COMPLETED_RESTART_ACTION, requestCode = 8, context = applicationContext
    )

    init {
        createTimerNotificationChannels()
    }

    fun getTimerBaseNotification() = NotificationCompat.Builder(applicationContext, TIMER_RUNNING_CHANNEL)
        .setContentTitle("Timer")
        .setSmallIcon(R.drawable.ic_hourglass_empty)
        .setContentIntent(openTimerPendingIntent)
        .setAutoCancel(true)
        .setColor(ContextCompat.getColor(applicationContext, R.color.blue))
        .setColorized(true)
        .setOngoing(true)

     fun updateTimerServiceNotification(
         time: String,
         isPlaying: Boolean,
         isDone: Boolean
     ) {
         val pauseResumeIntentAction = pauseResumeIntentAction(time, isPlaying, isDone)
         val cancelIntentAction =  timerNotificationBroadcastReceiver.setIntentAction(
             actionName = TIMER_RUNNING_CANCEL_ACTION, requestCode = 4, context = applicationContext
         )
         val pauseResumeIcon = if (isPlaying) R.drawable.ic_stop else R.drawable.ic_play
         val pauseResumeLabel = if (isPlaying) "Pause" else "Resume"

         val notificationUpdate = getTimerBaseNotification()
             .setContentText(time)
             .addAction(
                 pauseResumeIcon,
                 pauseResumeLabel,
                 pauseResumeIntentAction
             )
             .addAction(R.drawable.ic_close, "Cancel", cancelIntentAction)
             .build()
             notificationManager.notify(TIMER_RUNNING_NOTIFICATION_ID, notificationUpdate)
    }

    private fun pauseResumeIntentAction(
        time: String,
        isPlaying: Boolean,
        isDone: Boolean
    ): PendingIntent {
        val broadcastIntent =
            Intent(applicationContext, TimerNotificationBroadcastReceiver::class.java).apply {
                putExtra(TIMER_RUNNING_TIME_EXTRA, time)
                putExtra(TIMER_RUNNING_IS_PLAYING_EXTRA, isPlaying)
                putExtra(TIMER_RUNNING_IS_DONE_EXTRA, isDone)
            }
        return PendingIntent.getBroadcast(
            applicationContext,
            5,
            broadcastIntent,
            pendingIntentFlags
        )
    }


     fun showTimerCompletedNotification() = NotificationCompat.Builder(applicationContext, TIMER_COMPLETED_CHANNEL)
                .setContentTitle("Time's up")
                .setContentText("00:00:00")
                .setFullScreenIntent(openTimerPendingIntent, true)
                .setSmallIcon(R.drawable.ic_hourglass_empty)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(applicationContext, R.color.blue))
                .addAction(R.drawable.ic_close, "Dismiss", dismissIntentAction)
                .addAction(R.drawable.ic_sync, "Restart", restartIntentAction)
                .setOngoing(true)
                .build()




    fun removeTimerCompletedNotification() {
        notificationManager.cancel(TIMER_COMPLETED_NOTIFICATION_ID)
    }

    fun removeTimerRunningNotification() {
        notificationManager.cancel(TIMER_RUNNING_NOTIFICATION_ID)
    }

    private fun createTimerNotificationChannels() {
        val timerServiceChannel = NotificationChannelCompat.Builder(
            TIMER_RUNNING_CHANNEL,
            NotificationManagerCompat.IMPORTANCE_DEFAULT
        )
            .setName(applicationContext.getString(R.string.timer_running_channel_name))
            .setDescription(applicationContext.getString(R.string.timer_running_channel_description))
            .setSound(null, null)
            .build()

        val timerCompletedChannel = NotificationChannelCompat.Builder(
            TIMER_COMPLETED_CHANNEL,
            NotificationManagerCompat.IMPORTANCE_MAX
        )
            .setName(applicationContext.getString(R.string.timer_completed_channel_name))
            .setDescription(applicationContext.getString(R.string.timer_completed_channel_description))
            .setSound(null, null)
            .build()

        notificationManager.createNotificationChannelsCompat(
            listOf(
                timerServiceChannel,
                timerCompletedChannel
            )
        )
    }
}

private const val TIMER_RUNNING_CHANNEL = "timer_running_channel"
private const val TIMER_COMPLETED_CHANNEL = "timer_completed_channel"
const val TIMER_RUNNING_NOTIFICATION_ID = 57766
const val TIMER_COMPLETED_NOTIFICATION_ID = 3425