package com.example.clock.ui.alarm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.clock.data.manager.ScheduleAlarmManager
import com.example.clock.data.model.Alarm
import com.example.clock.data.repository.AlarmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val scheduleAlarmManager: ScheduleAlarmManager,
) : ViewModel(), AlarmActions {

    val alarmsListState = alarmRepository.alarmsList.asLiveData()

    var alarmCreationState by mutableStateOf(Alarm())
        private set

    override fun updateAlarmCreationState(alarm: Alarm) {
        alarm.setDaysSelected(alarm.daysSelected)
        alarmCreationState = alarm
    }

    override fun update(alarm: Alarm) {
        viewModelScope.launch {
            listOf(
                async {
                    alarm.setDaysSelected(alarm.daysSelected)
                    alarmRepository.update(alarm)
                },
                async {
                    if (alarm.isScheduled) {
                        scheduleAlarmManager.schedule(alarm)
                    } else {
                        scheduleAlarmManager.cancel(alarm)
                    }
                },
            )
        }
    }

    override fun remove(alarm: Alarm) {
        viewModelScope.launch {
            listOf(
                async { alarmRepository.delete(alarm) },
                async {
                    if (alarm.isScheduled) {
                        scheduleAlarmManager.cancel(alarm)
                    }
                },
            )
        }
    }

    override fun save() {
        viewModelScope.launch {
            val lastId = alarmRepository.getLastId()
            val alarm = alarmRepository.getAlarmById(alarmCreationState.id)

            if (!alarmCreationState.isScheduled) {
                alarmCreationState.isScheduled = true
            }

            listOf(
                async {
                    if (alarm?.id == alarmCreationState.id) {
                        update(alarmCreationState)
                    } else {
                        alarmCreationState.id = lastId?.plus(1) ?: 1
                        alarmCreationState.setDaysSelected(alarmCreationState.daysSelected)
                        alarmRepository.insert(alarmCreationState)
                    }
                },
                async { scheduleAlarmManager.schedule(alarmCreationState) },
            )
        }
    }

    override fun clear() {
        viewModelScope.launch {
            alarmsListState.value?.let {
                listOf(
                    async { alarmRepository.clear() },
                    async { scheduleAlarmManager.clearScheduledAlarms(it) },
                )
            }
        }
    }
}
