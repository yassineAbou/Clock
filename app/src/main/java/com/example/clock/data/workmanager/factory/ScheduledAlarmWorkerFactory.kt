package com.example.clock.data.workmanager.factory

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.example.clock.data.repository.AlarmRepository
import com.example.clock.data.workmanager.worker.ScheduledAlarmWorker
import com.example.clock.util.helper.AlarmNotificationHelper
import javax.inject.Inject

class ScheduledAlarmWorkerFactory @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val alarmNotificationHelper: AlarmNotificationHelper,
) : ChildWorkerFactory {

    override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
        return ScheduledAlarmWorker(alarmRepository, alarmNotificationHelper, appContext, params)
    }
}
