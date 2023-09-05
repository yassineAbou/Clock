package com.yassineabou.clock.data.workManager.factory

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.yassineabou.clock.data.manager.StopwatchManager
import com.yassineabou.clock.data.workManager.worker.StopwatchWorker
import com.yassineabou.clock.util.helper.StopwatchNotificationHelper
import javax.inject.Inject

class StopwatchWorkerFactory @Inject constructor(
    private val stopwatchManager: StopwatchManager,
    private val stopwatchNotificationHelper: StopwatchNotificationHelper,
) : ChildWorkerFactory {

    override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
        return StopwatchWorker(stopwatchManager, stopwatchNotificationHelper, appContext, params)
    }
}
