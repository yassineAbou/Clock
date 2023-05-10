package com.example.clock.data.receiver

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.example.clock.data.manager.ScheduleAlarmManager
import com.example.clock.data.manager.ServiceManager
import com.example.clock.data.service.AlarmService
import com.example.clock.data.service.JOB_ID
import com.example.clock.data.service.RescheduleAlarmJobService
import com.example.clock.util.safeLet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class AlarmBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var serviceManager: ServiceManager

    @Inject
    lateinit var scheduleAlarmManager: ScheduleAlarmManager

    private val broadcastReceiverScope = CoroutineScope(SupervisorJob())

    override fun onReceive(p0: Context?, p1: Intent?) {
        val pendingResult: PendingResult = goAsync()
        broadcastReceiverScope.launch(Dispatchers.Default) {
            try {
                safeLet(p0, p1) { context, intent ->
                    when (intent.action) {
                        "android.intent.action.BOOT_COMPLETED" -> {
                            val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
                            val jobInfo = JobInfo.Builder(JOB_ID, ComponentName(context, RescheduleAlarmJobService::class.java))
                                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                                .setRequiresCharging(false)
                                .build()

                            jobScheduler.schedule(jobInfo)
                        }
                        ACTION_DISMISS -> serviceManager.stopService(AlarmService::class.java)
                        ACTION_SNOOZE -> {
                            scheduleAlarmManager.snooze()
                            serviceManager.stopService(AlarmService::class.java)
                        }
                        else -> {
                            val isRecurring = intent.getBooleanExtra(IS_RECURRING, false)
                            val shouldStartService = !isRecurring || alarmIsToday(intent)
                            if (shouldStartService) {
                                serviceManager.startAlarmService(intent)
                            } else {
                            }
                        }
                    }
                }
            } finally {
                pendingResult.finish()
                broadcastReceiverScope.cancel()
            }
        }
    }

    private fun alarmIsToday(intent: Intent): Boolean {
        val daysSelected = intent.extras?.getSerializable(DAYS_SELECTED) as? HashMap<String, Boolean>
        val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        return daysSelected?.get(getDayOfWeek(today)) ?: false
    }

    private fun getDayOfWeek(day: Int): String {
        return when (day) {
            Calendar.MONDAY -> "Mon"
            Calendar.TUESDAY -> "Tue"
            Calendar.WEDNESDAY -> "Wed"
            Calendar.THURSDAY -> "Thu"
            Calendar.FRIDAY -> "Fri"
            Calendar.SATURDAY -> "Sat"
            Calendar.SUNDAY -> "Sun"
            else -> ""
        }
    }
}

const val IS_RECURRING = "IS_RECURRING"
const val DAYS_SELECTED = "DAYS_SELECTED"
const val TITLE = "TITLE"
const val HOUR = "HOUR"
const val MINUTE = "MINUTE"
const val ACTION_DISMISS = "ACTION_DISMISS"
const val ACTION_SNOOZE = "ACTION_SNOOZE"
