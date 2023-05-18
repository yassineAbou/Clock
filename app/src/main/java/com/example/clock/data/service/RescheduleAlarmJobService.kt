package com.example.clock.data.service

import android.app.job.JobParameters
import android.app.job.JobService
import com.example.clock.data.manager.ScheduleAlarmManager
import com.example.clock.data.repository.AlarmRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RescheduleAlarmJobService : JobService() {

    private val jobServiceScope = CoroutineScope(SupervisorJob())

    @Inject
    lateinit var scheduleAlarmManager: ScheduleAlarmManager

    @Inject
    lateinit var alarmRepository: AlarmRepository

    var jobCanceled = false

    override fun onStartJob(p0: JobParameters?): Boolean {
        jobServiceScope.launch {
            val scheduledAlarms = alarmRepository.alarmsList
                .map { alarmList ->
                    alarmList.filter { alarm -> alarm.isScheduled }
                }
                .firstOrNull { it.isNotEmpty() }
            scheduledAlarms?.forEach { scheduleAlarmManager.schedule(it) }
            if (jobCanceled) {
                return@launch
            }
            jobFinished(p0, false)
            jobServiceScope.cancel()
        }
        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        jobServiceScope.cancel()
        jobCanceled = true
        return true
    }
}

const val JOB_ID = 16
