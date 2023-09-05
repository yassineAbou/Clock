package com.yassineabou.clock.di

import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import com.yassineabou.clock.data.workManager.factory.AlarmCheckerWorkerFactory
import com.yassineabou.clock.data.workManager.factory.AlarmWorkerFactory
import com.yassineabou.clock.data.workManager.factory.ChildWorkerFactory
import com.yassineabou.clock.data.workManager.factory.RescheduleAlarmWorkerFactory
import com.yassineabou.clock.data.workManager.factory.StopwatchWorkerFactory
import com.yassineabou.clock.data.workManager.factory.TimerCompletedWorkerFactory
import com.yassineabou.clock.data.workManager.factory.TimerRunningWorkerFactory
import com.yassineabou.clock.data.workManager.factory.WrapperWorkerFactory
import com.yassineabou.clock.data.workManager.worker.AlarmCheckerWorker
import com.yassineabou.clock.data.workManager.worker.AlarmWorker
import com.yassineabou.clock.data.workManager.worker.RescheduleAlarmWorker
import com.yassineabou.clock.data.workManager.worker.StopwatchWorker
import com.yassineabou.clock.data.workManager.worker.TimerCompletedWorker
import com.yassineabou.clock.data.workManager.worker.TimerRunningWorker
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@MapKey
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class WorkerKey(val value: KClass<out ListenableWorker>)

@InstallIn(SingletonComponent::class)
@Module
abstract class WorkerModule {

    @Binds
    abstract fun bindWorkerFactoryModule(workerFactory: WrapperWorkerFactory): WorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(AlarmWorker::class)
    abstract fun bindAlarmWorker(alarmWorkerFactory: AlarmWorkerFactory): ChildWorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(StopwatchWorker::class)
    abstract fun bindStopwatchWorker(stopwatchWorkerFactory: StopwatchWorkerFactory): ChildWorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(RescheduleAlarmWorker::class)
    abstract fun bindRescheduleAlarmWorker(rescheduleAlarmWorkerFactory: RescheduleAlarmWorkerFactory): ChildWorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(TimerRunningWorker::class)
    abstract fun bindTimerRunningWorker(timerRunningWorkerFactory: TimerRunningWorkerFactory): ChildWorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(TimerCompletedWorker::class)
    abstract fun bindTimerCompletedWorker(timerCompletedWorkerFactory: TimerCompletedWorkerFactory): ChildWorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(AlarmCheckerWorker::class)
    abstract fun bindAlarmCheckerWorker(alarmCheckerWorkerFactory: AlarmCheckerWorkerFactory): ChildWorkerFactory
}
