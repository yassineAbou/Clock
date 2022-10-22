package com.example.clock.alarm

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.example.clock.repository.AlarmRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RescheduleAlarmsService : LifecycleService() {

    @Inject
    lateinit var alarmManager: AlarmManager

    @Inject
    lateinit var alarmRepository: AlarmRepository

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        lifecycleScope.launch {
            alarmRepository.alarmsItems.collect { alarms ->
                for (alarm in alarms) {
                    if (alarm.started) {
                        alarmManager.scheduleAlarm(alarm)
                        Log.e(TAG, "onStartCommand: ${alarm.alarmId}", )
                    }
                }
            }
        }
        return START_STICKY
    }

}

private const val TAG = "RescheduleAlarmsService"