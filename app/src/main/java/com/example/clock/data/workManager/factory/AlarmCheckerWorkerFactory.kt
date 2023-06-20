package com.example.clock.data.workManager.factory

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.example.clock.data.manager.WorkRequestManager
import com.example.clock.data.repository.AlarmRepository
import com.example.clock.data.workManager.worker.AlarmCheckerWorker
import com.example.clock.util.helper.AlarmNotificationHelper
import javax.inject.Inject

class AlarmCheckerWorkerFactory @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val alarmNotificationHelper: AlarmNotificationHelper,
    private val workRequestManager: WorkRequestManager,
) : ChildWorkerFactory {

    override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
        return AlarmCheckerWorker(alarmRepository, alarmNotificationHelper, workRequestManager,  appContext, params)
    }
}
