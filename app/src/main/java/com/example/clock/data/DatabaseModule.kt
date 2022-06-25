package com.example.clock.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAlarmDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        AlarmDatabase::class.java,
        "alarms_items_database"
    ).build()

    @Singleton
    @Provides
    fun provideAlarmDoe(alarmDatabase: AlarmDatabase):
            AlarmDao {
        return alarmDatabase.getAlarmDoe()
    }


}