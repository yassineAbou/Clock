package com.example.clock.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "alarm_Item_table",
    indices = [Index(value = ["hour", "minute", "title", "targetDay",
          "sunday", "monday", "tuesday", "wednesday",  "thursday", "saturday"                                                     ],
    unique = true)]
)
data class Alarm(
    @PrimaryKey()
    var alarmId: Int = 0,
    var hour: String = "00",
    var minute: String = "00",
    var title: String = "",
    var targetDay: String = "",
    var started: Boolean = true,
    var recurring: Boolean = false,
    var created: Boolean = false,
    var sunday: Boolean = false,
    var monday: Boolean = false,
    var tuesday: Boolean = false,
    var wednesday: Boolean = false,
    var thursday: Boolean = false,
    var friday: Boolean = false,
    var saturday: Boolean = false,
)



