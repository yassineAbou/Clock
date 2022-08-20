package com.example.clock.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.lifecycle.viewModelScope
import com.example.clock.data.Alarm
import com.example.clock.repository.AlarmRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmManager @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val alarmRepository: AlarmRepository
) {

    private val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }

     fun scheduleAlarm(alarmState: Alarm) {

         Log.e(TAG, "scheduleAlarm: ${alarmState.alarmId}")

            val alarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            val intent = Intent(applicationContext, AlarmBroadcastReceiver::class.java).apply {
                putExtra(RECURRING, alarmState.recurring) ; putExtra(MONDAY, alarmState.monday)
                putExtra(TUESDAY, alarmState.tuesday) ; putExtra(WEDNESDAY, alarmState.wednesday)
                putExtra(THURSDAY, alarmState.thursday) ; putExtra(FRIDAY, alarmState.friday)
                putExtra(SATURDAY, alarmState.saturday) ; putExtra(SUNDAY, alarmState.sunday)
                putExtra(TITLE, alarmState.title) ; putExtra(HOUR, alarmState.hour)
                putExtra(MINUTE, alarmState.minute)
            }

            val alarmPendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                alarmState.alarmId, intent, pendingIntentFlags
            )
            val calendar: Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, alarmState.hour.toInt())
                set(Calendar.MINUTE, alarmState.minute.toInt())
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)

                Log.e(TAG, "scheduleAlarm: ${alarmState.hour}:${alarmState.minute}")
            }


                if (calendar.timeInMillis <= System.currentTimeMillis())  {
                    calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1)
                }
                if (!alarmState.recurring)  {
                    val toastText = "One Time Alarm ${alarmState.title} scheduled for" +
                            " ${alarmState.targetDay.substringAfter('-')} at ${alarmState.hour}:${alarmState.minute}"

                         Toast.makeText(applicationContext, toastText, Toast.LENGTH_LONG).show()
                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        alarmPendingIntent
                    )
                } else  {
                    val toastText = "Recurring Alarm ${alarmState.title} scheduled for" +
                            " ${alarmState.targetDay} at ${alarmState.hour}:${alarmState.minute}"

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

     fun cancelAlarm(alarmState: Alarm) {

         Log.e(TAG, "cancelAlarm: ${alarmState.alarmId}")

        val alarmManager =
            applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(applicationContext, AlarmBroadcastReceiver::class.java)
        val alarmPendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            alarmState.alarmId,
            intent,
            pendingIntentFlags
        )
        alarmManager.cancel(alarmPendingIntent)

        val toastText = "Alarm canceled for ${alarmState.hour}:${alarmState.minute}"
        Toast.makeText(applicationContext, toastText, Toast.LENGTH_SHORT).show()
        Log.i("cancel", toastText)
    }

    fun schedule10Min() {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.MINUTE, 10)

        val alarm = Alarm(
            alarmId = Random().nextInt(Integer.MAX_VALUE), hour = calendar.get(Calendar.HOUR_OF_DAY).toString(),
            minute = calendar.get(Calendar.MINUTE).toString(), title = "Snooze", started = true, targetDay = "Today"
        )

        scheduleAlarm(alarm)
    }

    fun cancelAlarms(alarms: List<Alarm>) {
            for (alarm in alarms) {
                if (alarm.started) {
                    cancelAlarm(alarm)
                }
            }
    }

}

private const val TAG = "AlarmManager"
