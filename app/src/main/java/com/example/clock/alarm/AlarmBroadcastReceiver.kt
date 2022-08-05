package com.example.clock.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.clock.timer.TimerManager
import com.example.clock.util.safeLet
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class AlarmBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmServiceManager: AlarmServiceManager

    @Inject
    lateinit var alarmManager: AlarmManager

    override fun onReceive(p0: Context?, p1: Intent?) {

        safeLet(p0, p1) { context, intent ->

            if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
                Toast.makeText(context, "Alarm Reboot", Toast.LENGTH_SHORT).show()
               alarmServiceManager.startRescheduleAlarmsService()
            } else {
                Toast.makeText(context, "Alarm Received", Toast.LENGTH_SHORT).show()
                if (!intent.getBooleanExtra(RECURRING, false)) {
                    alarmServiceManager.startAlarmService(intent)
                }
                if (alarmIsToday(intent)) {
                    alarmServiceManager.startAlarmService(intent)
                }
            }

        }

    }

    private fun alarmIsToday(intent: Intent): Boolean {
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY ->  return intent.getBooleanExtra(MONDAY, false)
            Calendar.TUESDAY -> return intent.getBooleanExtra(TUESDAY, false)
            Calendar.WEDNESDAY -> return intent.getBooleanExtra(WEDNESDAY, false)
            Calendar.THURSDAY -> return intent.getBooleanExtra(THURSDAY, false)
            Calendar.FRIDAY -> return intent.getBooleanExtra(FRIDAY, false)
            Calendar.SATURDAY -> return intent.getBooleanExtra(SATURDAY, false)
            Calendar.SUNDAY -> return intent.getBooleanExtra(SUNDAY, false)
        }
        return false
    }
}

const val MONDAY = "MONDAY"
const val TUESDAY = "TUESDAY"
const val WEDNESDAY = "WEDNESDAY"
const val THURSDAY = "THURSDAY"
const val FRIDAY = "FRIDAY"
const val SATURDAY = "SATURDAY"
const val SUNDAY = "SUNDAY"
const val RECURRING = "RECURRING"
const val TITLE = "TITLE"
private const val TAG = "AlarmBroadcastReceiver"