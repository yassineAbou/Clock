package com.example.clock.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.clock.data.manager.ScheduleAlarmManager
import com.example.clock.data.workmanager.worker.ALARM_TAG
import com.example.clock.data.workmanager.worker.AlarmWorker
import com.example.clock.data.workmanager.worker.RESCHEDULE_ALARM_TAG
import com.example.clock.data.workmanager.worker.RescheduleAlarmWorker
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
    lateinit var scheduleAlarmManager: ScheduleAlarmManager

    private val broadcastReceiverScope = CoroutineScope(SupervisorJob())

    override fun onReceive(p0: Context?, p1: Intent?) {
        val pendingResult: PendingResult = goAsync()
        broadcastReceiverScope.launch(Dispatchers.Default) {
            try {
                safeLet(p0, p1) { context, intent ->
                    when (intent.action) {
                        "android.intent.action.BOOT_COMPLETED" -> {
                            val workRequest =
                                OneTimeWorkRequestBuilder<RescheduleAlarmWorker>().addTag(
                                    RESCHEDULE_ALARM_TAG,
                                ).build()
                            WorkManager.getInstance(context.applicationContext).enqueue(workRequest)
                        }

                        ACTION_DISMISS -> WorkManager.getInstance(context.applicationContext).cancelAllWorkByTag(
                            ALARM_TAG,
                        )
                        ACTION_SNOOZE -> {
                            scheduleAlarmManager.snooze()
                            WorkManager.getInstance(context.applicationContext).cancelAllWorkByTag(
                                ALARM_TAG,
                            )
                        }

                        else -> {
                            val isRecurring = intent.getBooleanExtra(IS_RECURRING, false)
                            val shouldStartService = !isRecurring || alarmIsToday(intent)
                            val inputData = Data.Builder()
                                .putString(TITLE, p1?.getStringExtra(TITLE))
                                .putString(HOUR, p1?.getStringExtra(HOUR))
                                .putString(MINUTE, p1?.getStringExtra(MINUTE))
                                .build()
                            if (shouldStartService) {
                                val secondWorkRequest =
                                    OneTimeWorkRequestBuilder<AlarmWorker>().addTag(
                                        ALARM_TAG,
                                    ).setInputData(inputData).build()
                                WorkManager.getInstance(context.applicationContext).enqueue(secondWorkRequest)
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

const val IS_RECURRING = "IS_RECURRING"
const val DAYS_SELECTED = "DAYS_SELECTED"
const val TITLE = "TITLE"
const val HOUR = "HOUR"
const val MINUTE = "MINUTE"
const val ACTION_DISMISS = "ACTION_DISMISS"
const val ACTION_SNOOZE = "ACTION_SNOOZE"
