package com.example.clock.ui.alarm

import com.example.clock.data.model.Alarm

interface AlarmActions {
    fun updateAlarmCreationState(alarm: Alarm) {}
    fun update(alarm: Alarm) {}
    fun remove(alarm: Alarm) {}
    fun save() {}
    fun clear() {}
}
