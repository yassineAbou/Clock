package com.example.clock.stopwatch

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.system.Os.remove
import android.util.Log
import android.view.textclassifier.SelectionEvent.ACTION_RESET
import android.widget.Toast
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.clock.MainActivity
import com.example.clock.R
import com.example.clock.timer.*

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
        createStopwatchNotificationChannels()
    }

    fun getStopwatchBaseNotification() = NotificationCompat.Builder(applicationContext, STOPWATCH_SERVICE_CHANNEL_ID)
        .setContentTitle("Stopwatch")
        .setSmallIcon(R.drawable.ic_baseline_timer_24)
        .setContentIntent(openStopwatchPendingIntent)
        .setAutoCancel(true)
        .setColor(ContextCompat.getColor(applicationContext, R.color.blue))
        .setColorized(true)
        .setSilent(true)
        .setOnlyAlertOnce(true)

    fun updateStopwatchServiceNotification(
        time: String,
        timerRunning: Boolean,
        isDone: Boolean,
        lastIndex: Int
    ) {
        val actionIntent = getStopwatchNotificationActionIntent(time, timerRunning, isDone, lastIndex)
        //val reset =  reset()
        val lap = lap()
        val reset =  reset()
        val secondActionText = if (timerRunning) "Lap" else "Rest"
        val secondAction = if (timerRunning) lap else reset
        val lapText = if (timerRunning && lastIndex!= -1) "\nLap  $lastIndex" else ""
        val paused = if (timerRunning) "" else "\nPaused"
        val startStopIcon = if (timerRunning) R.drawable.ic_stop else R.drawable.ic_play
        val startStopLabel = if (timerRunning) "Stop" else "Resume"

        val notificationUpdate = getStopwatchBaseNotification()
            .setStyle(NotificationCompat.BigTextStyle().bigText("$time  $lapText$paused"))
            //.setContentText(time)
            //.setContentText(lapText)
            .addAction(R.drawable.ic_close, secondActionText, secondAction)
            .addAction(
                startStopIcon,
                startStopLabel,
                actionIntent
            )
            .build()
        notificationManager.notify(STOPWATCH_SERVICE_NOTIFICATION_ID , notificationUpdate)
    }

    private fun reset() : PendingIntent {
        val broadcastIntent =
            Intent(applicationContext, StopwatchNotificationBroadcastReceiver::class.java).apply {
                action = com.example.clock.stopwatch.ACTION_RESET
            }
        return PendingIntent.getBroadcast(
            applicationContext,
            2,
            broadcastIntent,
            pendingIntentFlags
        )
    }

    private fun lap() : PendingIntent {
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

    private fun getStopwatchNotificationActionIntent(
        time: String,
        timerRunning: Boolean,
        isDone: Boolean,
        lastIndex: Int
    ): PendingIntent {
        val broadcastIntent =
            Intent(applicationContext, StopwatchNotificationBroadcastReceiver::class.java).apply {
                putExtra(EXTRA_STOPWATCH_TIME, time)
                putExtra(EXTRA_STOPWATCH_RUNNING, timerRunning)
                putExtra(EXTRA_STOPWATCH_IS_DONE, isDone)
                putExtra(EXTRA_STOPWATCH_LAST_INDEX, lastIndex)
            }
        return PendingIntent.getBroadcast(
            applicationContext,
            1,
            broadcastIntent,
            pendingIntentFlags
        )
    }


    private fun createStopwatchNotificationChannels() {
        val timerServiceChannel = NotificationChannelCompat.Builder(
            STOPWATCH_SERVICE_CHANNEL_ID,
            NotificationManagerCompat.IMPORTANCE_DEFAULT
        )
            .setName(applicationContext.getString(R.string.stopwatch_channel))
            .setDescription(applicationContext.getString(R.string.stopwatch_service_channel_description))
            .setSound(null, null)
            .build()

        notificationManager.createNotificationChannelsCompat(
            listOf(
                timerServiceChannel
            )
        )
    }
}

private const val STOPWATCH_SERVICE_CHANNEL_ID = "timer_service_channel"
private const val TIMER_COMPLETED_CHANNEL_ID = "timer_completed_notification_channel"
const val STOPWATCH_SERVICE_NOTIFICATION_ID = -1
private const val TIMER_COMPLETED_NOTIFICATION_ID = -2
private const val TAG = "StopwatchNotificationHe"