package com.example.clock.alarm

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.core.app.ServiceCompat.stopForeground
import com.example.clock.R
import com.example.clock.repository.AlarmRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.String
import javax.inject.Inject


@AndroidEntryPoint
class AlarmService : Service()  {

    private val serviceScope = CoroutineScope(SupervisorJob())
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var vibrator: Vibrator
    @Inject
    lateinit var alarmNotificationHelper: AlarmNotificationHelper
    @Inject
    lateinit var alarmAlarmRepository: AlarmRepository

    override fun onCreate() {
        super.onCreate()

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm)

          serviceScope.launch {
              mediaPlayer.isLooping = true

              vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                  val vibratorManager =
                      getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                  vibratorManager.defaultVibrator
              } else {
                  @Suppress("DEPRECATION")
                  getSystemService(VIBRATOR_SERVICE) as Vibrator
              }
          }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val alarmTitle = intent?.getStringExtra(TITLE).toString()
        val alarmTime = "${intent?.getStringExtra(HOUR)}:${intent?.getStringExtra(MINUTE)}"
        startForeground(ALARM_SERVICE_NOTIFICATION_ID, alarmNotificationHelper.getAlarmBaseNotification(alarmTitle, alarmTime).build())
        serviceScope.launch {

            mediaPlayer.start()

            alarmAlarmRepository.getAlarmByTime(
                alarmTime.substringBefore(':'),
                alarmTime.substringAfter(':'),
                false
            ).collectLatest {
                it?.let {
                    it.started = false
                    alarmAlarmRepository.update(it)
                    Log.e(TAG, "onStartCommand: started = ${it.started}")
                }
            }

            val pattern = longArrayOf(0, 100, 1000)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0))
            } else {
                vibrator.vibrate(pattern, 0)
            }


       }
        return START_STICKY


    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        mediaPlayer.stop()
        vibrator.cancel()
        stopForeground(STOP_FOREGROUND_REMOVE)
        Log.e(TAG, "onDestroy: ALARM SERVICE")
    }

    override fun onBind(p0: Intent?): IBinder? = null
}

private const val TAG = "AlarmService"