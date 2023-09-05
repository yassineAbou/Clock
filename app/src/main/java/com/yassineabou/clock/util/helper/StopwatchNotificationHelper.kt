package com.yassineabou.clock.util.helper

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.yassineabou.clock.R
import com.yassineabou.clock.data.receiver.STOPWATCH_IS_PLAYING
import com.yassineabou.clock.data.receiver.STOPWATCH_LAP_ACTION
import com.yassineabou.clock.data.receiver.STOPWATCH_LAST_INDEX
import com.yassineabou.clock.data.receiver.STOPWATCH_RESET_ACTION
import com.yassineabou.clock.data.receiver.STOPWATCH_TIME
import com.yassineabou.clock.data.receiver.StopwatchNotificationBroadcastReceiver
import com.yassineabou.clock.ui.MainActivity
import com.yassineabou.clock.util.GlobalProperties.pendingIntentFlags
import com.yassineabou.clock.util.setIntentAction
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StopwatchNotificationHelper @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
) {
    private val notificationManager = NotificationManagerCompat.from(applicationContext)

    private val stopwatchNotificationBroadcastReceiver =
        StopwatchNotificationBroadcastReceiver::class.java

    private val openStopwatchIntent = Intent(
        Intent.ACTION_VIEW,
        "https://www.clock.com/Stopwatch".toUri(),
        applicationContext,
        MainActivity::class.java,
    ).apply {
        flags = FLAG_ACTIVITY_SINGLE_TOP
    }

    private val openStopwatchPendingIntent = PendingIntent.getActivity(
        applicationContext,
        0,
        openStopwatchIntent,
        pendingIntentFlags,
    )

    init {
        createStopwatchNotificationChannel()
    }

    fun getStopwatchBaseNotification() =
        NotificationCompat.Builder(applicationContext, STOPWATCH_WORKER_CHANNEL)
            .setContentTitle("Stopwatch")
            .setSmallIcon(R.drawable.ic_timer)
            .setColor(ContextCompat.getColor(applicationContext, R.color.blue))
            .setContentIntent(openStopwatchPendingIntent)
            .setOngoing(true)
            .setAutoCancel(true)

    @SuppressLint("MissingPermission")
    fun updateStopwatchWorkerNotification(
        time: String,
        isPlaying: Boolean,
        lastLapIndex: Int,
    ) {
        val lapIntentAction = stopwatchNotificationBroadcastReceiver.setIntentAction(
            actionName = STOPWATCH_LAP_ACTION,
            requestCode = 2,
            context = applicationContext,
        )
        val resetIntentAction = stopwatchNotificationBroadcastReceiver.setIntentAction(
            actionName = STOPWATCH_RESET_ACTION,
            requestCode = 3,
            context = applicationContext,
        )
        val stopResumeIntentAction = stopResumeIntentAction(time, isPlaying, lastLapIndex)

        val lapResetIntentAction = if (isPlaying) lapIntentAction else resetIntentAction
        val lapResetLabel = if (isPlaying) "Lap" else "Reset"
        val lapResetIcon = if (isPlaying) R.drawable.ic_close else R.drawable.ic_timer
        val stopResumeLabel = if (isPlaying) "Stop" else "Resume"
        val stopResumeIcon = if (isPlaying) R.drawable.ic_stop else R.drawable.ic_play
        val lastLapIndexText = if (isPlaying && lastLapIndex != -1) "\nLap  $lastLapIndex" else ""
        val isPlayingText = if (!isPlaying) "\nPaused" else ""

        val notificationUpdate = getStopwatchBaseNotification()
            .setContentText("$time  $lastLapIndexText$isPlayingText")
            .addAction(stopResumeIcon, stopResumeLabel, stopResumeIntentAction)
            .addAction(lapResetIcon, lapResetLabel, lapResetIntentAction)
            .build()
        notificationManager.notify(STOPWATCH_WORKER_NOTIFICATION_ID, notificationUpdate)
    }

    fun removeStopwatchNotification() {
        notificationManager.cancel(STOPWATCH_WORKER_NOTIFICATION_ID)
    }

    private fun stopResumeIntentAction(
        time: String,
        isPlaying: Boolean,
        lastLapIndex: Int,
    ): PendingIntent {
        val broadcastIntent =
            Intent(applicationContext, stopwatchNotificationBroadcastReceiver).apply {
                putExtra(STOPWATCH_TIME, time)
                putExtra(STOPWATCH_IS_PLAYING, isPlaying)
                putExtra(STOPWATCH_LAST_INDEX, lastLapIndex)
            }
        return PendingIntent.getBroadcast(
            applicationContext,
            4,
            broadcastIntent,
            pendingIntentFlags,
        )
    }

    private fun createStopwatchNotificationChannel() {
        val stopwatchWorkerChannel = NotificationChannelCompat.Builder(
            STOPWATCH_WORKER_CHANNEL,
            NotificationManagerCompat.IMPORTANCE_DEFAULT,
        )
            .setName(applicationContext.getString(R.string.stopwatch_channel))
            .setDescription(applicationContext.getString(R.string.stopwatch_worker_channel_description))
            .setSound(null, null)
            .build()

        notificationManager.createNotificationChannel(stopwatchWorkerChannel)
    }
}

private const val STOPWATCH_WORKER_CHANNEL = "stopwatch_worker_channel"
const val STOPWATCH_WORKER_NOTIFICATION_ID = 1
