package com.yassineabou.clock.ui.alarm

import com.yassineabou.clock.data.model.Alarm

interface AlarmActions {
    fun updateAlarmCreationState(alarm: Alarm) {}
    fun update(alarm: Alarm) {}
    fun remove(alarm: Alarm) {}
    fun save() {}
    fun clear() {}
}
