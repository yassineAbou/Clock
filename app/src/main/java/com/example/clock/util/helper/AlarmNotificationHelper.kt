package com.example.clock.util.helper

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri

import com.example.clock.R
import com.example.clock.data.receiver.*

import com.example.clock.ui.MainActivity
import com.example.clock.util.Constants.pendingIntentFlags
import com.example.clock.util.setIntentAction
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmNotificationHelper @Inject constructor(
    @ApplicationContext private val applicationContext: Context
) {
    private val  notificationManager = NotificationManagerCompat.from(applicationContext)

    private val alarmActionsBroadcastReceiver = AlarmActionsBroadcastReceiver::class.java

    private val openAlarmListIntent = Intent(
        Intent.ACTION_VIEW,
        "https://www.clock.com/AlarmsList".toUri(),
        applicationContext,
        MainActivity::class.java
    )

    private val openAlarmPendingIntent = PendingIntent.getActivity(
        applicationContext, 0, openAlarmListIntent, pendingIntentFlags
    )

    private val dismissIntentAction = alarmActionsBroadcastReceiver.setIntentAction(
        actionName = ACTION_DISMISS, requestCode = 10, context = applicationContext
    )

    private val snoozeIntentAction = alarmActionsBroadcastReceiver.setIntentAction(
        actionName = ACTION_SNOOZE, requestCode = 11, context = applicationContext
    )

    init {
        createAlarmNotificationChannel()
    }

    fun getAlarmBaseNotification(title:String, time: String) = NotificationCompat.Builder(applicationContext, ALARM_SERVICE_CHANNEL_ID)
            .setContentTitle(time)
            .setContentText(title)
            .setSmallIcon(R.drawable.ic_baseline_alarm_24)
            .setShowWhen(false)
            .setFullScreenIntent(openAlarmPendingIntent, true)
            .setColor(ContextCompat.getColor(applicationContext, R.color.blue))
            .addAction(R.drawable.ic_close, "Dismiss", dismissIntentAction)
            .addAction(R.drawable.ic_baseline_snooze_24, "Snooze", snoozeIntentAction)
            .setOngoing(true)
            .setAutoCancel(true)


    private fun createAlarmNotificationChannel() {
        val alarmServiceChannel = NotificationChannelCompat.Builder(
            ALARM_SERVICE_CHANNEL_ID,
            NotificationManagerCompat.IMPORTANCE_MAX
        )
            .setName(applicationContext.getString(R.string.alarm_service_channel_name))
            .setDescription(applicationContext.getString(R.string.alarm_service_channel_description))
            .setSound(null, null)
            .build()


        notificationManager.createNotificationChannel(alarmServiceChannel)
    }

    fun removeAlarmNotification() {
        notificationManager.cancel(ALARM_SERVICE_NOTIFICATION_ID)
    }




}

private const val ALARM_SERVICE_CHANNEL_ID = "alarm_service_channel"
const val ALARM_SERVICE_NOTIFICATION_ID = 2778