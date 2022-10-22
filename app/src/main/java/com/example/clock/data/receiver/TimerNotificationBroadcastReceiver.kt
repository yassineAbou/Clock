package com.example.clock.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.clock.data.manager.ServiceManager
import com.example.clock.data.manager.TimerManager
import com.example.clock.data.service.TimerCompletedService
import com.example.clock.data.service.TimerRunningService
import com.example.clock.util.helper.TimerNotificationHelper
import com.example.clock.util.isBackgroundRunning
import com.example.clock.util.safeLet
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class TimerNotificationBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var timerManager: TimerManager

    @Inject
   lateinit var timerNotificationHelper: TimerNotificationHelper

    @Inject
    lateinit var serviceManager: ServiceManager

    override fun onReceive(p0: Context?, intent: Intent?) {
        val isPlaying = intent?.getBooleanExtra(TIMER_RUNNING_IS_PLAYING_EXTRA, false)
        val isDone = intent?.getBooleanExtra(TIMER_RUNNING_IS_DONE_EXTRA, false)
        val time = intent?.getStringExtra(TIMER_RUNNING_TIME_EXTRA)
        val action = intent?.action

            action?.let {
                when (it) {
                    TIMER_RUNNING_CANCEL_ACTION -> {
                        timerManager.resetTimer()
                        serviceManager.stopService(TimerRunningService::class.java)
                    }
                    TIMER_COMPLETED_DISMISS_ACTION -> serviceManager.stopService(TimerCompletedService::class.java)
                    TIMER_COMPLETED_RESTART_ACTION -> {
                        serviceManager.stopService(TimerCompletedService::class.java)
                        timerManager.startTimer()
                        if (p0?.isBackgroundRunning() == true) {
                            serviceManager.startService(TimerRunningService::class.java)
                        }
                    }
                }
            }


        safeLet(isPlaying, time, isDone) { safeIsPlaying, safeTime, safeIsDone ->
            if (!safeIsDone) {
                timerNotificationHelper.updateTimerServiceNotification(
                    isPlaying = safeIsPlaying,
                    time = safeTime,
                    isDone = safeIsDone
                )
                timerManager.handleCountDownTimer()
            }
        }



    }
}

const val TIMER_RUNNING_TIME_EXTRA = "TIMER_RUNNING_TIME_EXTRA"
const val TIMER_RUNNING_IS_PLAYING_EXTRA = "TIMER_RUNNING_IS_PLAYING_EXTRA"
const val TIMER_RUNNING_IS_DONE_EXTRA = "TIMER_RUNNING_IS_DONE_EXTRA"
const val TIMER_RUNNING_CANCEL_ACTION = "TIMER_RUNNING_CANCEL_ACTION"
const val TIMER_COMPLETED_DISMISS_ACTION = "TIMER_COMPLETED_DISMISS_ACTION"
const val TIMER_COMPLETED_RESTART_ACTION = "TIMER_COMPLETED_RESTART_ACTION"

