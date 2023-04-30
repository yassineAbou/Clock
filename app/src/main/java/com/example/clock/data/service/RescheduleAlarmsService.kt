package com.example.clock.data.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.clock.data.manager.ScheduleAlarmManager
import com.example.clock.data.repository.AlarmRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RescheduleAlarmsService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob())

    @Inject
    lateinit var scheduleAlarmManager: ScheduleAlarmManager

    @Inject
    lateinit var alarmRepository: AlarmRepository

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        serviceScope.launch {
            alarmRepository.alarmsList.onCompletion {
                stopForeground(STOP_FOREGROUND_REMOVE)
            }
                .buffer().collect { alarms ->
                    for (alarm in alarms) {
                        if (alarm.isScheduled) {
                            scheduleAlarmManager.schedule(alarm)
                        }
                    }
                }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? = null
}
