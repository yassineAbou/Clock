package com.example.clock.data.workmanager.factory

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.example.clock.data.manager.TimerManager
import com.example.clock.data.workmanager.worker.TimerRunningWorker
import com.example.clock.util.helper.TimerNotificationHelper
import javax.inject.Inject

class TimerRunningWorkerFactory @Inject constructor(
    private val timerManager: TimerManager,
    private val timerNotificationHelper: TimerNotificationHelper,
) : ChildWorkerFactory {

    override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
        return TimerRunningWorker(timerManager, timerNotificationHelper, appContext, params)
    }
}
