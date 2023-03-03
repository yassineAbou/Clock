package com.example.clock.data.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.clock.data.receiver.HOUR
import com.example.clock.data.receiver.MINUTE
import com.example.clock.data.receiver.TITLE
import com.example.clock.data.repository.AlarmRepository
import com.example.clock.util.helper.ALARM_SERVICE_NOTIFICATION_ID
import com.example.clock.util.helper.AlarmNotificationHelper
import com.example.clock.util.helper.MediaPlayerHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlarmService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob())

    @Inject
    lateinit var alarmNotificationHelper: AlarmNotificationHelper

    @Inject
    lateinit var alarmAlarmRepository: AlarmRepository

    @Inject
    lateinit var mediaPlayerHelper: MediaPlayerHelper

    override fun onCreate() {
        super.onCreate()
        mediaPlayerHelper.prepare()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val title = intent?.getStringExtra(TITLE).toString()
        val time = "${intent?.getStringExtra(HOUR)}:${intent?.getStringExtra(MINUTE)}"
        startForeground(
            ALARM_SERVICE_NOTIFICATION_ID,
            alarmNotificationHelper.getAlarmBaseNotification(title, time).build(),
        )

        serviceScope.launch {
            mediaPlayerHelper.start()

            /*
            alarmAlarmRepository.getAlarmByTime(
                time.substringBefore(':'),
                time.substringAfter(':'),
                false
            ).collectLatest {
                it?.let {
                    it.isScheduled = false
                    alarmAlarmRepository.update(it)
                    Log.e(TAG, "onStartCommand: started = ${it.isScheduled}")
                }
            }

             */
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        mediaPlayerHelper.release()
        stopForeground(STOP_FOREGROUND_REMOVE)
        Log.e(TAG, "onDestroy: ALARM SERVICE")
    }

    override fun onBind(p0: Intent?): IBinder? = null
}

private const val TAG = "AlarmService"
