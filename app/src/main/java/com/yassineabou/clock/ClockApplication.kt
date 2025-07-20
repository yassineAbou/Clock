package com.yassineabou.clock

import android.app.Application
import androidx.work.Configuration
import com.yassineabou.clock.data.workManager.factory.WrapperWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ClockApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: WrapperWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .setWorkerFactory(workerFactory)
            .build()
}

