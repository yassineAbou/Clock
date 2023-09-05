package com.yassineabou.clock.data.workManager.factory

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.yassineabou.clock.data.manager.WorkRequestManager
import com.yassineabou.clock.data.repository.AlarmRepository
import com.yassineabou.clock.data.workManager.worker.AlarmCheckerWorker
import com.yassineabou.clock.util.helper.AlarmNotificationHelper
import javax.inject.Inject

class AlarmCheckerWorkerFactory @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val alarmNotificationHelper: AlarmNotificationHelper,
    private val workRequestManager: WorkRequestManager,
) : ChildWorkerFactory {

    override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
        return AlarmCheckerWorker(alarmRepository, alarmNotificationHelper, workRequestManager, appContext, params)
    }
}
