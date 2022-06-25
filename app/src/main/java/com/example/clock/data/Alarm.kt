package com.example.clock.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.clock.util.Global.current
import com.example.clock.util.Global.formatter

@Entity(tableName = "alarm_Item_table")
data class Alarm(
    @PrimaryKey(autoGenerate = true)
    val alarmId: Long = 0L,
    var hour: String = "",
    var minute: String = "",
    var title: String = "",
    var targetDay: String = "",
    var started: Boolean = true,
    var created: Boolean = false,
    var sunday: Boolean = false,
    var monday: Boolean = false,
    var tuesday: Boolean = false,
    var wednesday: Boolean = false,
    var thursday: Boolean = false,
    var friday: Boolean = false,
    var saturday: Boolean = false,
)



