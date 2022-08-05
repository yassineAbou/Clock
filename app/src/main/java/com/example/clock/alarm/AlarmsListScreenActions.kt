package com.example.clock.alarm

import com.example.clock.data.Alarm

interface AlarmsListScreenActions {
    fun clear()
    fun update(alarm: Alarm)
    fun delete(alarm: Alarm)
    fun save(alarm: Alarm)
    fun insert()
    fun onChangeCreated(created: Boolean)
    fun setHour(hour: String)
    fun setMinute(minute: String)
    fun onChangeTitle(title: String)
    fun onChangeTargetDay(targetDay: String)
    fun onChangeDays(days: List<Boolean>)
    fun cancel(alarm: Alarm)
    fun schedule(alarm: Alarm)
}