package com.yassineabou.clock.data.workManager.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.yassineabou.clock.data.manager.StopwatchManager
import com.yassineabou.clock.util.helper.STOPWATCH_WORKER_NOTIFICATION_ID
import com.yassineabou.clock.util.helper.StopwatchNotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.collectLatest

@HiltWorker
class StopwatchWorker @AssistedInject constructor(
    @Assisted private val stopwatchManager: StopwatchManager,
    @Assisted private val stopwatchNotificationHelper: StopwatchNotificationHelper,
    @Assisted ctx: Context,
    @Assisted params: WorkerParameters,
) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        return try {
            val foregroundInfo = ForegroundInfo(
                STOPWATCH_WORKER_NOTIFICATION_ID,
                stopwatchNotificationHelper.getStopwatchBaseNotification().build(),
            )

            setForeground(foregroundInfo)

            stopwatchManager.stopwatchState.collectLatest {
                if (!it.isReset) {
                    stopwatchNotificationHelper.updateStopwatchWorkerNotification(
                        time = "${it.hour}:${it.minute}:${it.second}",
                        isPlaying = it.isPlaying,
                        lastLapIndex = stopwatchManager.lapTimes.lastIndex,
                    )
                }
            }

            Result.success()
        } catch (e: CancellationException) {
            stopwatchNotificationHelper.removeStopwatchNotification()
            Result.failure()
        }
    }
}

const val STOPWATCH_TAG = "stopwatchTag"
