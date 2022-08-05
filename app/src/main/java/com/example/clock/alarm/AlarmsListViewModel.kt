package com.example.clock.alarm

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.clock.data.Alarm
import com.example.clock.repository.AlarmRepository
import com.example.clock.util.Global.defaultValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


private const val TAG = "AlarmsListViewModel"

@HiltViewModel
class AlarmsListViewModel @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val alarmManager: com.example.clock.alarm.AlarmManager
) : ViewModel(), AlarmsListScreenActions{

    val alarmsListState = alarmRepository.alarmsItems.asLiveData()
    var alarmState by mutableStateOf(defaultValue)
        private set

    override fun update(alarm: Alarm) {
        viewModelScope.launch {
            alarmRepository.update(alarm)
        }
    }

    override fun clear() {
        viewModelScope.launch {
            alarmsListState.value?.let { alarmManager.cancelAlarms(it) }
           alarmRepository.clear()

        }
    }

    override fun delete(alarm: Alarm) { viewModelScope.launch { alarmRepository.delete(alarm) } }

    override fun insert() {
        val alarmId = Random().nextInt(Int.MAX_VALUE)
            if (alarmState.created) {
                alarmState.started = true
                alarmManager.scheduleAlarm(alarmState)
                update(alarmState)
            } else {
                viewModelScope.launch {
                    alarmState.alarmId = alarmId
                    alarmManager.scheduleAlarm(alarmState)
                    alarmRepository.insert(alarmState)
                    Log.e(TAG, "insert: $alarmState")
                }
            }
    }

    override fun save(alarm: Alarm) {
        alarmState = alarm
        Log.e(TAG, "save: $alarmState")
    }

    fun test(alarm: Alarm) {
        alarmState = alarm
    }

    override fun onChangeCreated(created: Boolean) { alarmState.created = created }

    override fun setHour(hour: String) { alarmState.hour = hour }

    override fun setMinute(minute: String) { alarmState.minute = minute }

    override fun onChangeTitle(title: String) { alarmState.title = title }

    override fun onChangeTargetDay(targetDay: String) { alarmState.targetDay = targetDay }

    override fun onChangeDays(days: List<Boolean>) {
        alarmState.sunday = days[0]; alarmState.monday = days[1]; alarmState.tuesday = days[2]; alarmState.wednesday = days[3]
        alarmState.thursday = days[4]; alarmState.friday = days[5]; alarmState.saturday = days[6]
        if (days.none { it }) {
            alarmState.recurring = false
            Log.e(TAG, "NONE")
        }
        if (days.any { it }) {
            alarmState.recurring = true
            Log.e(TAG, "ANY")
        }
    }

    override fun cancel(alarm: Alarm) {
        viewModelScope.launch {
                alarmManager.cancelAlarm(alarm)

        }
    }

    override fun schedule(alarm: Alarm) {
        viewModelScope.launch {
            alarmManager.scheduleAlarm(alarm)
        }
    }

}

