package com.example.clock.alarm

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.clock.MainActivity
import com.example.clock.R
import com.example.clock.timer.ACTION_DELETE
import com.example.clock.timer.TimerNotificationBroadcastReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmNotificationHelper @Inject constructor(
    @ApplicationContext private val applicationContext: Context
) {
    private val  notificationManager = NotificationManagerCompat.from(applicationContext)

    private val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }

    private val openAlarmListIntent = Intent(
        Intent.ACTION_VIEW,
        "https://www.clock.com/AlarmsList".toUri(),
        applicationContext,
        MainActivity::class.java
    )

    private val openAlarmPendingIntent = PendingIntent.getActivity(
        applicationContext, 0, openAlarmListIntent, pendingIntentFlags
    )

    init {
        createAlarmNotificationChannels()
    }

    fun getAlarmBaseNotification(alarmTitle:String, alarmTime: String) = NotificationCompat.Builder(applicationContext, ALARM_SERVICE_CHANNEL_ID)
            .setContentTitle(alarmTime)
            .setContentText(alarmTitle)
            .setSmallIcon(R.drawable.ic_baseline_alarm_24)
            .setShowWhen(false)
            .setContentIntent(openAlarmPendingIntent)
            .setColor(ContextCompat.getColor(applicationContext, R.color.blue))
            .addAction(R.drawable.ic_close, "Dismiss", dismiss())
            .addAction(R.drawable.ic_baseline_snooze_24, "Snooze", snooze())
            .setSilent(true)
            .setAutoCancel(true)


    private fun createAlarmNotificationChannels() {
        val alarmServiceChannel = NotificationChannelCompat.Builder(
            ALARM_SERVICE_CHANNEL_ID,
            NotificationManagerCompat.IMPORTANCE_DEFAULT
        )
            .setName(applicationContext.getString(R.string.alarm_service_channel_name))
            .setDescription(applicationContext.getString(R.string.alarm_service_channel_description))
            .setSound(null, null)
            .build()


        notificationManager.createNotificationChannelsCompat(
            listOf(
                alarmServiceChannel
            )
        )
    }

    fun removeAlarmNotification() {
        notificationManager.cancel(ALARM_SERVICE_NOTIFICATION_ID)
    }

    private fun dismiss() : PendingIntent {
        val broadcastIntent =
            Intent(applicationContext,AlarmActionsBroadcastReceiver::class.java).apply {
                action = ACTION_DISMISS
            }
        return PendingIntent.getBroadcast(
            applicationContext,
            3,
            broadcastIntent,
            pendingIntentFlags
        )
    }

    private fun snooze() : PendingIntent {
        val broadcastIntent =
            Intent(applicationContext,AlarmActionsBroadcastReceiver::class.java).apply {
                action = ACTION_SNOOZE
            }
        return PendingIntent.getBroadcast(
            applicationContext,
            2,
            broadcastIntent,
            pendingIntentFlags
        )
    }

}

private const val ALARM_SERVICE_CHANNEL_ID = "alarm_service_channel"
const val ALARM_SERVICE_NOTIFICATION_ID = -1