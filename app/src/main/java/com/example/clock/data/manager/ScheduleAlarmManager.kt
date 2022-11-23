package com.example.clock.data.manager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.clock.alarm.*
import com.example.clock.data.model.Alarm
import com.example.clock.data.receiver.*
import com.example.clock.data.repository.AlarmRepository
import com.example.clock.util.Constants.pendingIntentFlags
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import java.time.Duration
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.days


@Singleton
class ScheduleAlarmManager @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val alarmRepository: AlarmRepository
) {

     fun schedule(alarm: Alarm) {

            val alarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            val intent = Intent(applicationContext, AlarmBroadcastReceiver::class.java).apply {
                putExtra(IS_RECURRING, alarm.isRecurring) ; putExtra(IS_MONDAY, alarm.isMonday)
                putExtra(IS_TUESDAY, alarm.isTuesday) ; putExtra(IS_WEDNESDAY, alarm.isWednesday)
                putExtra(IS_THURSDAY, alarm.isThursday) ; putExtra(IS_FRIDAY, alarm.isFriday)
                putExtra(IS_SATURDAY, alarm.isSaturday) ; putExtra(IS_SUNDAY, alarm.isSunday)
                putExtra(TITLE, alarm.title) ; putExtra(HOUR, alarm.hour)
                putExtra(MINUTE, alarm.minute)
            }

            val alarmPendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                alarm.id, intent, pendingIntentFlags
            )
            val calendar: Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, alarm.hour.toInt())
                set(Calendar.MINUTE, alarm.minute.toInt())
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            if (calendar.timeInMillis <= System.currentTimeMillis())  {
                calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1)
            }

            when (alarm.isRecurring) {
                false -> {
                    val toastText = "One Time Alarm ${alarm.title} scheduled for" +
                            " ${alarm.description.substringAfter('-')} at ${alarm.hour}:${alarm.minute}"

                    Toast.makeText(applicationContext, "${alarm.id}", Toast.LENGTH_LONG).show()
                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        alarmPendingIntent
                    )
                }
                true -> {
                    val toastText = "Recurring Alarm ${alarm.title} scheduled for" +
                            " ${alarm.description} at ${alarm.hour}:${alarm.minute}"

                    Toast.makeText(applicationContext, toastText, Toast.LENGTH_LONG).show()

                    val runDaily = (24 * 60 * 60 * 1000).toLong()
                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        runDaily,
                        alarmPendingIntent
                    )
                }
            }
    }

     fun cancel(alarm: Alarm) {

        val alarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(applicationContext, AlarmBroadcastReceiver::class.java)
        val alarmPendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            alarm.id,
            intent,
            pendingIntentFlags
        )
         val toastText = "Alarm canceled for ${alarm.hour}:${alarm.minute}"
         Toast.makeText(applicationContext, "${alarm.id}", Toast.LENGTH_SHORT).show()

        alarmManager.cancel(alarmPendingIntent)
    }

    fun snooze() {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.MINUTE, 10)

        val alarm = Alarm(
            id = Random().nextInt(Integer.MAX_VALUE), hour = calendar.get(Calendar.HOUR_OF_DAY).toString(),
            minute = calendar.get(Calendar.MINUTE).toString(), title = "Snooze", isScheduled = true, description = "Today"
        )

        schedule(alarm)
    }

    fun cancelAlarms(alarms: List<Alarm>) {
        for (alarm in alarms) {
            if (alarm.isScheduled) {
                cancel(alarm)
            }
        }
    }

}

private const val TAG = "AlarmManager"
