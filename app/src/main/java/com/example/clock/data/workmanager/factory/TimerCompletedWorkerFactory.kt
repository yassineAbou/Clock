package com.example.clock.data.workmanager.factory

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.example.clock.data.manager.TimerManager
import com.example.clock.data.workmanager.worker.TimerCompletedWorker
import com.example.clock.util.helper.MediaPlayerHelper
import com.example.clock.util.helper.TimerNotificationHelper
import javax.inject.Inject

class TimerCompletedWorkerFactory @Inject constructor(
    private val mediaPlayerHelper: MediaPlayerHelper,
    private val timerNotificationHelper: TimerNotificationHelper,
    private val timerManager: TimerManager
) : ChildWorkerFactory {

    override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
        return TimerCompletedWorker(mediaPlayerHelper, timerNotificationHelper, timerManager, appContext, params)
    }
}
