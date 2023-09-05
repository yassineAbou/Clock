package com.yassineabou.clock.data.workManager.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.yassineabou.clock.data.manager.TimerManager
import com.yassineabou.clock.util.helper.MediaPlayerHelper
import com.yassineabou.clock.util.helper.TIMER_COMPLETED_NOTIFICATION_ID
import com.yassineabou.clock.util.helper.TimerNotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.collectLatest

@HiltWorker
class TimerCompletedWorker @AssistedInject constructor(
    @Assisted private val mediaPlayerHelper: MediaPlayerHelper,
    @Assisted private val timerNotificationHelper: TimerNotificationHelper,
    @Assisted private val timerManager: TimerManager,
    @Assisted ctx: Context,
    @Assisted params: WorkerParameters,
) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        return try {
            mediaPlayerHelper.prepare()
            mediaPlayerHelper.start()

            val foregroundInfo = ForegroundInfo(
                TIMER_COMPLETED_NOTIFICATION_ID,
                timerNotificationHelper.showTimerCompletedNotification(),
            )
            setForeground(foregroundInfo)

            // Ensure the work lasts at least 5 seconds for the notification to be shown
            // because shorter durations may not provide enough time for the notification to be visible.
            timerManager.timerState.collectLatest {}

            Result.success()
        } catch (e: CancellationException) {
            mediaPlayerHelper.release()
            timerNotificationHelper.removeTimerCompletedNotification()
            Result.failure()
        }
    }
}

const val TIMER_COMPLETED_TAG = "timerCompletedTag"
