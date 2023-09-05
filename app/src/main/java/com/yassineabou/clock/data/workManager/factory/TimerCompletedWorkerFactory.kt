package com.yassineabou.clock.data.workManager.factory

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.yassineabou.clock.data.manager.TimerManager
import com.yassineabou.clock.data.workManager.worker.TimerCompletedWorker
import com.yassineabou.clock.util.helper.MediaPlayerHelper
import com.yassineabou.clock.util.helper.TimerNotificationHelper
import javax.inject.Inject

class TimerCompletedWorkerFactory @Inject constructor(
    private val mediaPlayerHelper: MediaPlayerHelper,
    private val timerNotificationHelper: TimerNotificationHelper,
    private val timerManager: TimerManager,
) : ChildWorkerFactory {

    override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
        return TimerCompletedWorker(mediaPlayerHelper, timerNotificationHelper, timerManager, appContext, params)
    }
}
