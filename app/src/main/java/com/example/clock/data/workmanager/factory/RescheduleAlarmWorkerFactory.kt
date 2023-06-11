package com.example.clock.data.workmanager.factory

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.example.clock.data.manager.ScheduleAlarmManager
import com.example.clock.data.repository.AlarmRepository
import com.example.clock.data.workmanager.worker.RescheduleAlarmWorker
import javax.inject.Inject

class RescheduleAlarmWorkerFactory @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val scheduleAlarmManager: ScheduleAlarmManager,
) : ChildWorkerFactory {

    override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
        return RescheduleAlarmWorker(alarmRepository, scheduleAlarmManager, appContext, params)
    }
}
