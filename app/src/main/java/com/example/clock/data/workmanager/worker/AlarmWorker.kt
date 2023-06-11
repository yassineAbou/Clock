package com.example.clock.data.workmanager.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.clock.data.receiver.HOUR
import com.example.clock.data.receiver.MINUTE
import com.example.clock.data.receiver.TITLE
import com.example.clock.data.repository.AlarmRepository
import com.example.clock.util.helper.ALARM_SERVICE_NOTIFICATION_ID
import com.example.clock.util.helper.AlarmNotificationHelper
import com.example.clock.util.helper.MediaPlayerHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.collectLatest
import kotlin.coroutines.cancellation.CancellationException

@HiltWorker
class AlarmWorker @AssistedInject constructor(
    @Assisted private val alarmRepository: AlarmRepository,
    @Assisted private val alarmNotificationHelper: AlarmNotificationHelper,
    @Assisted private val mediaPlayerHelper: MediaPlayerHelper,
    @Assisted ctx: Context,
    @Assisted params: WorkerParameters,
) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        return try {
            mediaPlayerHelper.prepare()
            val title = inputData.getString(TITLE) ?: ""
            val time = "${inputData.getString(HOUR)}:${inputData.getString(MINUTE)}"

            val foregroundInfo = ForegroundInfo(
                ALARM_SERVICE_NOTIFICATION_ID,
                alarmNotificationHelper.getAlarmBaseNotification(title, time).build(),
            )
            setForeground(foregroundInfo)

            mediaPlayerHelper.start()

            alarmRepository.getAlarmByTime(
                hour = time.substringBefore(':'),
                minute = time.substringAfter(':'),
                recurring = false,
            ).collectLatest {
                it?.let {
                    it.isScheduled = false
                    alarmRepository.update(it)
                }
            }

            Result.success()
        } catch (e: CancellationException) {
            alarmNotificationHelper.removeAlarmServiceNotification()
            mediaPlayerHelper.release()
            val workRequest9 =
                OneTimeWorkRequestBuilder<ScheduledAlarmWorker>().addTag(
                    SCHEDULED_ALARM_TAG,
                ).build()
            WorkManager.getInstance(applicationContext).enqueue(workRequest9)
            Result.failure()
        }
    }
}

const val ALARM_TAG = "alarmTag"
