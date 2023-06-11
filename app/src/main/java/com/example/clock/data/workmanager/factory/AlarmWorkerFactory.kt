package com.example.clock.data.workmanager.factory

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.example.clock.data.repository.AlarmRepository
import com.example.clock.data.workmanager.worker.AlarmWorker
import com.example.clock.util.helper.AlarmNotificationHelper
import com.example.clock.util.helper.MediaPlayerHelper
import javax.inject.Inject

class AlarmWorkerFactory @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val alarmNotificationHelper: AlarmNotificationHelper,
    private val mediaPlayerHelper: MediaPlayerHelper,
) : ChildWorkerFactory {

    override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
        return AlarmWorker(alarmRepository, alarmNotificationHelper, mediaPlayerHelper, appContext, params)
    }
}
