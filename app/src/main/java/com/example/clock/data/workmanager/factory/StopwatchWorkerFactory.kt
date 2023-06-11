package com.example.clock.data.workmanager.factory

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.example.clock.data.manager.StopwatchManager
import com.example.clock.data.workmanager.worker.StopwatchWorker
import com.example.clock.util.helper.StopwatchNotificationHelper
import javax.inject.Inject

class StopwatchWorkerFactory @Inject constructor(
    private val stopwatchManager: StopwatchManager,
    private val stopwatchNotificationHelper: StopwatchNotificationHelper,
) : ChildWorkerFactory {

    override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
        return StopwatchWorker(stopwatchManager, stopwatchNotificationHelper, appContext, params)
    }
}
