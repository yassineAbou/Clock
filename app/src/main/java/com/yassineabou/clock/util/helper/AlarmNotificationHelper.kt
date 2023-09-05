package com.yassineabou.clock.util.helper

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.yassineabou.clock.R
import com.yassineabou.clock.data.receiver.ACTION_DISMISS
import com.yassineabou.clock.data.receiver.ACTION_SNOOZE
import com.yassineabou.clock.data.receiver.AlarmBroadcastReceiver
import com.yassineabou.clock.ui.MainActivity
import com.yassineabou.clock.util.GlobalProperties.pendingIntentFlags
import com.yassineabou.clock.util.setIntentAction
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmNotificationHelper @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
) {
    private val notificationManager = NotificationManagerCompat.from(applicationContext)

    private val alarmBroadcastReceiver = AlarmBroadcastReceiver::class.java

    private val openAlarmListIntent = Intent(
        Intent.ACTION_VIEW,
        "https://www.clock.com/AlarmsList".toUri(),
        applicationContext,
        MainActivity::class.java,
    ).apply {
        flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
    }

    private val openAlarmPendingIntent = PendingIntent.getActivity(
        applicationContext,
        13,
        openAlarmListIntent,
        pendingIntentFlags,
    )

    private val dismissIntentAction = alarmBroadcastReceiver.setIntentAction(
        actionName = ACTION_DISMISS,
        requestCode = 14,
        context = applicationContext,
    )

    private val snoozeIntentAction = alarmBroadcastReceiver.setIntentAction(
        actionName = ACTION_SNOOZE,
        requestCode = 15,
        context = applicationContext,
    )

    init {
        createAlarmNotificationChannels()
    }

    fun getAlarmBaseNotification(title: String, time: String) =
        NotificationCompat.Builder(applicationContext, ALARM_WORKER_CHANNEL_ID)
            .setContentTitle(time)
            .setContentText(title)
            .setSmallIcon(R.drawable.ic_baseline_alarm_24)
            .setShowWhen(false)
            .setFullScreenIntent(openAlarmPendingIntent, true)
            .setColor(ContextCompat.getColor(applicationContext, R.color.blue))
            .addAction(R.drawable.ic_close, "Dismiss", dismissIntentAction)
            .addAction(R.drawable.ic_baseline_snooze_24, "Snooze", snoozeIntentAction)
            .setOngoing(true)

    @SuppressLint("MissingPermission")
    fun displayAlarmCheckerNotification() {
        val alarmCheckerNotification = NotificationCompat.Builder(applicationContext, ALARM_CHECKER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_alarm_24)
            .setShowWhen(false)
            .setOngoing(true)
            .build()
        notificationManager.notify(ALARM_CHECKER_NOTIFICATION_ID, alarmCheckerNotification)
    }

    fun alarmCheckerNotificationPresent(): Boolean {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val activeNotifications = notificationManager.activeNotifications

        for (notification in activeNotifications) {
            if (notification.id == ALARM_CHECKER_NOTIFICATION_ID) {
                return true
            }
        }

        return false
    }

    private fun createAlarmNotificationChannels() {
        val alarmWorkerChannel = NotificationChannelCompat.Builder(
            ALARM_WORKER_CHANNEL_ID,
            NotificationManagerCompat.IMPORTANCE_MAX,
        )
            .setName(applicationContext.getString(R.string.alarm_worker_channel_name))
            .setDescription(applicationContext.getString(R.string.alarm_worker_channel_description))
            .setSound(null, null)
            .build()

        val alarmCheckerChannel = NotificationChannelCompat.Builder(
            ALARM_CHECKER_CHANNEL_ID,
            NotificationManagerCompat.IMPORTANCE_DEFAULT,
        )
            .setName(applicationContext.getString(R.string.alarm_checker_channel_name))
            .setDescription(applicationContext.getString(R.string.alarm_checker_channel_description))
            .setSound(null, null)
            .build()

        notificationManager.createNotificationChannelsCompat(
            listOf(
                alarmWorkerChannel,
                alarmCheckerChannel,
            ),
        )
    }

    fun removeAlarmWorkerNotification() {
        notificationManager.cancel(ALARM_WORKER_NOTIFICATION_ID)
    }

    fun removeScheduledAlarmNotification() {
        notificationManager.cancel(ALARM_CHECKER_NOTIFICATION_ID)
    }
}

private const val ALARM_WORKER_CHANNEL_ID = "alarm_worker_channel"
const val ALARM_WORKER_NOTIFICATION_ID = 12
private const val ALARM_CHECKER_CHANNEL_ID = "alarm_checker_channel"
const val ALARM_CHECKER_NOTIFICATION_ID = 17
