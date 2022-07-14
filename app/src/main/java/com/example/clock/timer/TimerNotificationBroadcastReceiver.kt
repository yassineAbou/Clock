package com.example.clock.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_DELETE
import android.util.Log
import android.widget.Toast
import com.example.clock.util.safeLet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TimerNotificationBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var timerManager: TimerManager

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onReceive(p0: Context?, intent: Intent?) {
        val timerRunning = intent?.getBooleanExtra(EXTRA_TIMER_RUNNING, false)
        val isDone = intent?.getBooleanExtra(EXTRA_IS_DONE, false)
        val time = intent?.getStringExtra(EXTRA_TIME)
        val action = intent?.action

            action?.let {
                Log.e(TAG, "action: $action")
                timerManager.onChangeDone()
                timerManager.stopService()
            }


        safeLet(timerRunning, time, isDone) { isTimeRunning, currentTime, isTimeDone ->
            if (isTimeRunning && !isTimeDone) {
                notificationHelper.updateTimerServiceNotification(
                    timerRunning = false,
                    time = currentTime,
                    isDone = isTimeDone
                )
            }
            if (!isTimeDone) {
                timerManager.handleCountDownTimer()
            }
        }



    }
}




const val EXTRA_TIME = "EXTRA_TIME"
const val EXTRA_TIMER_RUNNING = "EXTRA_TIMER_RUNNING"
const val EXTRA_IS_DONE = "EXTRA_IS_DONE"
const val ACTION_DELETE = "ACTION_DELETE"

private const val TAG = "TimerNotificationBroadc"