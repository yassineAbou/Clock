package com.example.clock.alarm

import android.provider.SyncStateContract.Helpers.update
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.clock.data.Alarm
import com.example.clock.repository.AlarmRepository
import com.example.clock.util.Global.current
import com.example.clock.util.Global.defaultValue
import com.example.clock.util.Global.formatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

private const val TAG = "AlarmsListViewModel"

@HiltViewModel
class AlarmsListViewModel @Inject constructor(
    private val alarmRepository: AlarmRepository
) : ViewModel(), AlarmsListScreenActions{

    val alarmsListState = alarmRepository.alarmsItems.asLiveData()
    var alarmState by mutableStateOf(defaultValue)
        private set


    override fun update(alarm: Alarm) { viewModelScope.launch { alarmRepository.update(alarm) } }

    override fun clear() { viewModelScope.launch { alarmRepository.clear() } }


    override fun delete(alarm: Alarm) { viewModelScope.launch { alarmRepository.delete(alarm) } }

    override fun insert() {
            if (alarmState.created) {
                alarmState.started = true
                update(alarmState)
            } else {
                viewModelScope.launch {
                    alarmRepository.insert(alarmState)
                }
            }
    }

    override fun save(alarm: Alarm) { alarmState = alarm }

    override fun onChangeCreated(created: Boolean) { alarmState.created = created }

    override fun setHour(hour: String) { alarmState.hour = hour }

    override fun setMinute(minute: String) { alarmState.minute = minute }

    override fun onChangeTitle(title: String) { alarmState.title = title }

    override fun onChangeTargetDay(targetDay: String) { alarmState.targetDay = targetDay }

    override fun onChangeDays(days: List<Boolean>) {
        alarmState.sunday = days[0]; alarmState.monday = days[1]; alarmState.tuesday = days[2]; alarmState.wednesday = days[3]
        alarmState.thursday = days[4]; alarmState.friday = days[5]; alarmState.saturday = days[6]
    }


}