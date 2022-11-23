package com.example.clock.data.service

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.example.clock.data.manager.ScheduleAlarmManager
import com.example.clock.data.repository.AlarmRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RescheduleAlarmsService : LifecycleService() {

    @Inject
    lateinit var scheduleAlarmManager: ScheduleAlarmManager

    @Inject
    lateinit var alarmRepository: AlarmRepository

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        lifecycleScope.launch {
            alarmRepository.alarmsList.collect { alarms ->
                for (alarm in alarms) {
                    if (alarm.isScheduled) {
                        scheduleAlarmManager.schedule(alarm)
                    }
                }
            }
        }
        return START_STICKY
    }

}

private const val TAG = "RescheduleAlarmsService"