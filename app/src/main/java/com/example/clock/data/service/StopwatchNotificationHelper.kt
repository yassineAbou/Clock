package com.example.clock.data.service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.clock.ui.MainActivity
import com.example.clock.R
import com.example.clock.data.receiver.*

import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StopwatchNotificationHelper @Inject constructor(
    @ApplicationContext private val applicationContext: Context
) {
    private val  notificationManager = NotificationManagerCompat.from(applicationContext)

    private val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }

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
        .setSmallIcon(R.drawable.ic_baseline_timer_24)
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
        val stopResumeExtraField = getExtraFieldIntent(time, isPlaying, isReset, lastIndex)
        val stopResumeLabel = if (isPlaying) "Stop" else "Resume"
        val stopResumeIcon = if (isPlaying) R.drawable.ic_stop else R.drawable.ic_play
        val lapResetAction = if (isPlaying) lapActionIntent() else resetActionIntent()
        val lapResetLabel = if (isPlaying) "Lap" else "Reset"
        val lapResetIcon = if (isPlaying) R.drawable.ic_close else R.drawable.ic_baseline_timer_24
        val lastLap = if (isPlaying && lastIndex!= -1) "\nLap  $lastIndex" else ""
        val isPaused = if (isPlaying) "" else "\nPaused"



        val notificationUpdate = getStopwatchBaseNotification()
            .setContentText("$time  $lastLap$isPaused")
            .addAction(stopResumeIcon, stopResumeLabel, stopResumeExtraField)
            .addAction(lapResetIcon, lapResetLabel, lapResetAction)
            .build()
        notificationManager.notify(STOPWATCH_SERVICE_NOTIFICATION_ID , notificationUpdate)
    }

    fun removeStopwatchNotification() {
        notificationManager.cancel(STOPWATCH_SERVICE_NOTIFICATION_ID)
    }

    private fun resetActionIntent() : PendingIntent {
        val broadcastIntent =
            Intent(applicationContext, StopwatchNotificationBroadcastReceiver::class.java).apply {
                action = ACTION_RESET
            }
        return PendingIntent.getBroadcast(
            applicationContext,
            2,
            broadcastIntent,
            pendingIntentFlags
        )
    }

    private fun lapActionIntent() : PendingIntent {
        val broadcastIntent =
            Intent(applicationContext, StopwatchNotificationBroadcastReceiver::class.java).apply {
                action = ACTION_LAP
            }
        return PendingIntent.getBroadcast(
            applicationContext,
            3,
            broadcastIntent,
            pendingIntentFlags
        )
    }

    private fun getExtraFieldIntent(
        time: String,
        isPlaying: Boolean,
        isReset: Boolean,
        lastIndex: Int
    ): PendingIntent {
        val broadcastIntent =
            Intent(applicationContext, StopwatchNotificationBroadcastReceiver::class.java).apply {
                putExtra(EXTRA_STOPWATCH_TIME, time)
                putExtra(EXTRA_STOPWATCH_IS_PLAYING, isPlaying)
                putExtra(EXTRA_STOPWATCH_IS_RESET, isReset)
                putExtra(EXTRA_STOPWATCH_LAST_INDEX, lastIndex)
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
const val STOPWATCH_SERVICE_NOTIFICATION_ID = 4
