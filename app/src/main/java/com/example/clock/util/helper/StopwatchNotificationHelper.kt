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
class StopwatchNotificationHelper @Inject constructor(
    @ApplicationContext private val applicationContext: Context
) {
    private val  notificationManager = NotificationManagerCompat.from(applicationContext)

    private val stopwatchNotificationBroadcastReceiver = StopwatchNotificationBroadcastReceiver::class.java

    private val openStopwatchIntent = Intent(
        Intent.ACTION_VIEW,
        "https://www.clock.com/Stopwatch".toUri(),
        applicationContext,
        MainActivity::class.java
    )

    private val openStopwatchPendingIntent = PendingIntent.getActivity(
        applicationContext, 0, openStopwatchIntent, pendingIntentFlags
    )

    init {
        createStopwatchNotificationChannel()
    }

    fun getStopwatchBaseNotification() = NotificationCompat.Builder(applicationContext, STOPWATCH_SERVICE_CHANNEL)
        .setContentTitle("Stopwatch")
        .setSmallIcon(R.drawable.ic_timer)
        .setContentIntent(openStopwatchPendingIntent)
        .setColor(ContextCompat.getColor(applicationContext, R.color.blue))
        .setColorized(true)
        .setOngoing(true)
        .setAutoCancel(true)
    

    fun updateStopwatchServiceNotification(
        time: String,
        isPlaying: Boolean,
        isReset: Boolean,
        lastIndex: Int
    ) {
        val lapIntentAction = stopwatchNotificationBroadcastReceiver.setIntentAction(
            actionName = STOPWATCH_LAP_ACTION, requestCode = 3, context = applicationContext
        )
        val resetIntentAction  = stopwatchNotificationBroadcastReceiver.setIntentAction(
            actionName = STOPWATCH_RESET_ACTION, requestCode = 2, context = applicationContext
        )
        val stopResumeIntentAction = stopResumeIntentAction(time, isPlaying, isReset, lastIndex)
        val stopResumeLabel = if (isPlaying) "Stop" else "Resume"
        val stopResumeIcon = if (isPlaying) R.drawable.ic_stop else R.drawable.ic_play
        val lapResetIntentAction = if (isPlaying) lapIntentAction else resetIntentAction
        val lapResetLabel = if (isPlaying) "Lap" else "Reset"
        val lapResetIcon = if (isPlaying) R.drawable.ic_close else R.drawable.ic_timer
        val lastLap = if (isPlaying && lastIndex!= -1) "\nLap  $lastIndex" else ""
        val isPaused = if (isPlaying) "" else "\nPaused"

        val notificationUpdate = getStopwatchBaseNotification()
            .setContentText("$time  $lastLap$isPaused")
            .addAction(stopResumeIcon, stopResumeLabel, stopResumeIntentAction)
            .addAction(lapResetIcon, lapResetLabel, lapResetIntentAction)
            .build()
        notificationManager.notify(STOPWATCH_SERVICE_NOTIFICATION_ID , notificationUpdate)
    }

    fun removeStopwatchNotification() {
        notificationManager.cancel(STOPWATCH_SERVICE_NOTIFICATION_ID)
    }

    private fun stopResumeIntentAction(
        time: String,
        isPlaying: Boolean,
        isReset: Boolean,
        lastIndex: Int
    ): PendingIntent {
        val broadcastIntent =
            Intent(applicationContext, stopwatchNotificationBroadcastReceiver).apply {
                putExtra(STOPWATCH_TIME_EXTRA, time)
                putExtra(STOPWATCH_IS_PLAYING_EXTRA, isPlaying)
                putExtra(STOPWATCH_IS_RESET_EXTRA, isReset)
                putExtra(STOPWATCH_LAST_INDEX_EXTRA, lastIndex)
            }
        return PendingIntent.getBroadcast(
            applicationContext,
            1,
            broadcastIntent,
            pendingIntentFlags
        )
    }


    private fun createStopwatchNotificationChannel() {
        val stopwatchServiceChannel = NotificationChannelCompat.Builder(
            STOPWATCH_SERVICE_CHANNEL,
            NotificationManagerCompat.IMPORTANCE_DEFAULT
        )
            .setName(applicationContext.getString(R.string.stopwatch_channel))
            .setDescription(applicationContext.getString(R.string.stopwatch_service_channel_description))
            .setSound(null, null)
            .build()

        notificationManager.createNotificationChannel(stopwatchServiceChannel)
    }
}

private const val STOPWATCH_SERVICE_CHANNEL = "stopwatch_service_channel"
const val STOPWATCH_SERVICE_NOTIFICATION_ID = 121412
