package com.example.clock.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.clock.data.manager.ScheduleAlarmManager
import com.example.clock.data.manager.ServiceManager
import com.example.clock.data.service.RescheduleAlarmsService
import com.example.clock.util.safeLet
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class AlarmBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var serviceManager: ServiceManager

    @Inject
    lateinit var scheduleAlarmManager: ScheduleAlarmManager

    override fun onReceive(p0: Context?, p1: Intent?) {

        safeLet(p0, p1) { context, intent ->

            when {

                Intent.ACTION_BOOT_COMPLETED == intent.action -> {
                    Toast.makeText(context, "Alarm Reboot", Toast.LENGTH_SHORT).show()
                    serviceManager.startService(RescheduleAlarmsService::class.java)
                }
                !intent.getBooleanExtra(IS_RECURRING, false) -> serviceManager.startAlarmService(intent)
                alarmIsToday(intent) ->  serviceManager.startAlarmService(intent)


            }

        }

    }

    private fun alarmIsToday(intent: Intent): Boolean {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        intent.apply {
            return when (calendar[Calendar.DAY_OF_WEEK]) {
                Calendar.MONDAY -> getBooleanExtra(IS_MONDAY, false)
                Calendar.TUESDAY -> getBooleanExtra(IS_TUESDAY, false)
                Calendar.WEDNESDAY -> getBooleanExtra(IS_WEDNESDAY, false)
                Calendar.THURSDAY -> getBooleanExtra(IS_THURSDAY, false)
                Calendar.FRIDAY -> getBooleanExtra(IS_FRIDAY, false)
                Calendar.SATURDAY -> getBooleanExtra(IS_SATURDAY, false)
                Calendar.SUNDAY -> getBooleanExtra(IS_SUNDAY, false)
                else -> false
            }
        }
    }
}

const val IS_MONDAY = "IS_MONDAY"
const val IS_TUESDAY = "IS_TUESDAY"
const val IS_WEDNESDAY = "IS_WEDNESDAY"
const val IS_THURSDAY = "IS_THURSDAY"
const val IS_FRIDAY = "IS_FRIDAY"
const val IS_SATURDAY = "IS_SATURDAY"
const val IS_SUNDAY = "IS_SUNDAY"
const val IS_RECURRING = "IS_RECURRING"
const val TITLE = "TITLE"
private const val TAG = "AlarmBroadcastReceiver"
const val HOUR = "HOUR"
const val MINUTE = "MINUTE"