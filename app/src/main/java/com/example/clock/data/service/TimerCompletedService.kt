package com.example.clock.data.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.wifi.WifiManager
import android.os.*
import android.provider.Settings
import com.example.clock.R
import com.example.clock.util.helper.TIMER_COMPLETED_NOTIFICATION_ID
import com.example.clock.util.helper.TimerNotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.system.measureNanoTime

@AndroidEntryPoint
class TimerCompletedService : Service() {

    @Inject
    lateinit var timerNotificationHelper: TimerNotificationHelper
    private val serviceScope = CoroutineScope(SupervisorJob())
    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null


    override fun onCreate() {
        super.onCreate()

        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()
            )
            isLooping = true
            setDataSource(applicationContext, Settings.System.DEFAULT_ALARM_ALERT_URI)
            prepare()
        }

        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(TIMER_COMPLETED_NOTIFICATION_ID, timerNotificationHelper.showTimerCompletedNotification())

        serviceScope.launch {
            mediaPlayer?.start()

            val pattern = longArrayOf(0, 500, 1000)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createWaveform(pattern, 0))
            } else {
                vibrator?.vibrate(pattern, 0)
            }
        }
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onDestroy() {
        mediaPlayer?.release()
        vibrator?.cancel()
        timerNotificationHelper.removeTimerCompletedNotification()
        super.onDestroy()
    }
}