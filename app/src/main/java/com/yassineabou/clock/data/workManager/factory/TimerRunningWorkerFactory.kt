package com.yassineabou.clock.data.workManager.factory

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.yassineabou.clock.data.manager.TimerManager
import com.yassineabou.clock.data.workManager.worker.TimerRunningWorker
import com.yassineabou.clock.util.helper.TimerNotificationHelper
import javax.inject.Inject

class TimerRunningWorkerFactory @Inject constructor(
    private val timerManager: TimerManager,
    private val timerNotificationHelper: TimerNotificationHelper,
) : ChildWorkerFactory {

    override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
        return TimerRunningWorker(timerManager, timerNotificationHelper, appContext, params)
    }
}
