package com.example.clock.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmActionsBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmServiceManager: AlarmServiceManager

    @Inject
    lateinit var alarmManager: AlarmManager

    override fun onReceive(p0: Context?, p1: Intent?) {
        val alarmActions = p1?.action

        alarmActions?.let {
            when (it) {
                ACTION_DISMISS ->  alarmServiceManager.stopAlarmService()
                ACTION_SNOOZE -> {
                    alarmManager.schedule10Min()
                    alarmServiceManager.stopAlarmService()
                }
                else -> {}
            }
        }
    }
}

const val ACTION_DISMISS = "ACTION_DISMISS"
const val ACTION_SNOOZE = "ACTION_SNOOZE"