package com.example.clock.di

import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import com.example.clock.data.workmanager.factory.AlarmWorkerFactory
import com.example.clock.data.workmanager.factory.ChildWorkerFactory
import com.example.clock.data.workmanager.factory.RescheduleAlarmWorkerFactory
import com.example.clock.data.workmanager.factory.ScheduledAlarmWorkerFactory
import com.example.clock.data.workmanager.factory.StopwatchWorkerFactory
import com.example.clock.data.workmanager.factory.TimerCompletedWorkerFactory
import com.example.clock.data.workmanager.factory.TimerRunningWorkerFactory
import com.example.clock.data.workmanager.factory.WrapperWorkerFactory
import com.example.clock.data.workmanager.worker.AlarmWorker
import com.example.clock.data.workmanager.worker.RescheduleAlarmWorker
import com.example.clock.data.workmanager.worker.ScheduledAlarmWorker
import com.example.clock.data.workmanager.worker.StopwatchWorker
import com.example.clock.data.workmanager.worker.TimerCompletedWorker
import com.example.clock.data.workmanager.worker.TimerRunningWorker
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
    @WorkerKey(ScheduledAlarmWorker::class)
    abstract fun bindScheduledAlarmWorker(scheduledAlarmWorkerFactory: ScheduledAlarmWorkerFactory): ChildWorkerFactory
}
