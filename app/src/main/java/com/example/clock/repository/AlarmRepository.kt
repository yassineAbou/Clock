package com.example.clock.repository

import com.example.clock.data.Alarm
import com.example.clock.data.AlarmDao
import javax.inject.Inject

class AlarmRepository @Inject constructor(
    private val alarmDao: AlarmDao
) {
    val alarmsItems = alarmDao.getAlarmsItems()

    suspend fun insert(alarm: Alarm) = alarmDao.insert(alarm)

    suspend fun update(alarm: Alarm) = alarmDao.update(alarm)

    suspend fun delete(alarm: Alarm) = alarmDao.delete(alarm)

    suspend fun clear() = alarmDao.clear()

    suspend fun getAlarmItem(alarmId: Long) = alarmDao.getAlarmItem(alarmId)

}