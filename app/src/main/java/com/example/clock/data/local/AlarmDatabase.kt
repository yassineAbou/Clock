package com.example.clock.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.clock.data.model.Alarm

@Database(entities = [Alarm::class], version = 1, exportSchema = false)
abstract class AlarmDatabase : RoomDatabase() {

    abstract fun getAlarmDao(): AlarmDao
}