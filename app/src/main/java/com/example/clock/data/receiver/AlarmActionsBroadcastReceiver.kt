package com.example.clock.data.receiver

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import com.example.clock.data.manager.ScheduleAlarmManager
import com.example.clock.data.manager.ServiceManager
import com.example.clock.data.service.AlarmService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmActionsBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var serviceManager: ServiceManager

    @Inject
    lateinit var scheduleAlarmManager: ScheduleAlarmManager

    override fun onReceive(p0: Context?, p1: Intent?) {
        val alarmActions = p1?.action

        alarmActions?.let {
            when (it) {
                ACTION_DISMISS ->  serviceManager.stopService(AlarmService::class.java)
                ACTION_SNOOZE -> {
                    scheduleAlarmManager.snooze()
                    serviceManager.stopService(AlarmService::class.java)
                }
                else -> {}
            }
        }
    }
}

const val ACTION_DISMISS = "ACTION_DISMISS"
const val ACTION_SNOOZE = "ACTION_SNOOZE"