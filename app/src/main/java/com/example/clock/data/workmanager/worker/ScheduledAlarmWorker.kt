package com.example.clock.data.workmanager.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.clock.data.repository.AlarmRepository
import com.example.clock.util.helper.AlarmNotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map


@HiltWorker
class ScheduledAlarmWorker @AssistedInject constructor(
    @Assisted private val alarmRepository: AlarmRepository,
    @Assisted private val alarmNotificationHelper: AlarmNotificationHelper,
    @Assisted ctx: Context,
    @Assisted params: WorkerParameters,
) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        return try {
            val scheduledAlarms = alarmRepository.alarmsList
                .map { alarmList ->
                    alarmList.filter { alarm -> alarm.isScheduled }
                }.firstOrNull()

            if (scheduledAlarms?.isNotEmpty() == true && !alarmNotificationHelper.isNotificationVisible()) {
                alarmNotificationHelper.displayScheduledAlarmNotification()
            }
            if (scheduledAlarms?.isEmpty() == true) {
                alarmNotificationHelper.removeScheduledAlarmNotification()
            }

            WorkManager.getInstance(applicationContext).cancelAllWorkByTag(SCHEDULED_ALARM_TAG)

            Result.success()
        } catch (throwable: Throwable) {
            Result.failure()
        }
    }
}

const val SCHEDULED_ALARM_TAG = "scheduledAlarmTag"

private const val TAG = "ScheduledAlarmWorker"
