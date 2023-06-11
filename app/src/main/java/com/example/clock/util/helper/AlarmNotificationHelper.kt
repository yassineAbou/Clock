package com.example.clock.util.helper

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.net.toUri
import com.example.clock.R
import com.example.clock.data.receiver.ACTION_DISMISS
import com.example.clock.data.receiver.ACTION_SNOOZE
import com.example.clock.data.receiver.AlarmBroadcastReceiver
import com.example.clock.ui.MainActivity
import com.example.clock.util.GlobalProperties.pendingIntentFlags
import com.example.clock.util.setIntentAction
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
        createAlarmNotificationChannel()
    }

    fun getAlarmBaseNotification(title: String, time: String) =
        NotificationCompat.Builder(applicationContext, ALARM_SERVICE_CHANNEL_ID)
            .setContentTitle(time)
            .setContentText(title)
            .setSmallIcon(R.drawable.ic_baseline_alarm_24)
            .setShowWhen(false)
            .setFullScreenIntent(openAlarmPendingIntent, true)
            .setColor(ContextCompat.getColor(applicationContext, R.color.blue))
            .addAction(R.drawable.ic_close, "Dismiss", dismissIntentAction)
            .addAction(R.drawable.ic_baseline_snooze_24, "Snooze", snoozeIntentAction)
            .setOngoing(true)

    fun displayScheduledAlarmNotification() {
        val scheduledAlarmNotification = NotificationCompat.Builder(applicationContext, SCHEDULED_ALARM_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_alarm_24)
            .setShowWhen(false)
            .setOngoing(true)
            .build()
        notificationManager.notify(SCHEDULED_ALARM_NOTIFICATION_ID, scheduledAlarmNotification)
    }

    fun isNotificationVisible(): Boolean {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val activeNotifications = notificationManager.activeNotifications

        for (notification in activeNotifications) {
            if (notification.id == SCHEDULED_ALARM_NOTIFICATION_ID) {
                return true
            }
        }

        return false
    }

    private fun createAlarmNotificationChannel() {
        val alarmServiceChannel = NotificationChannelCompat.Builder(
            ALARM_SERVICE_CHANNEL_ID,
            NotificationManagerCompat.IMPORTANCE_MAX,
        )
            .setName(applicationContext.getString(R.string.alarm_service_channel_name))
            .setDescription(applicationContext.getString(R.string.alarm_service_channel_description))
            .setSound(null, null)
            .build()

        val scheduledAlarmChannel = NotificationChannelCompat.Builder(
            SCHEDULED_ALARM_CHANNEL_ID,
            NotificationManagerCompat.IMPORTANCE_MAX,
        )
            .setName(applicationContext.getString(R.string.scheduled_alarm_channel_name))
            .setDescription(applicationContext.getString(R.string.scheduled_alarm_channel_description))
            .setSound(null, null)
            .build()

        notificationManager.createNotificationChannelsCompat(
            listOf(
                alarmServiceChannel,
                scheduledAlarmChannel,
            ),
        )
    }

    fun removeAlarmServiceNotification() {
        notificationManager.cancel(ALARM_SERVICE_NOTIFICATION_ID)
    }

    fun removeScheduledAlarmNotification() {
        notificationManager.cancel(SCHEDULED_ALARM_NOTIFICATION_ID)
    }
}

private const val ALARM_SERVICE_CHANNEL_ID = "alarm_service_channel"
const val ALARM_SERVICE_NOTIFICATION_ID = 12
private const val SCHEDULED_ALARM_CHANNEL_ID = "scheduled_alarm_channel"
const val SCHEDULED_ALARM_NOTIFICATION_ID = 17
