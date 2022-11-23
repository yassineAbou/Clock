package com.example.clock.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "alarms_list_table",
    indices = [Index(value = ["hour", "minute", "title", "description",
        "isSunday", "isMonday", "isTuesday", "isWednesday",  "isFriday", "isSaturday"                                                     ],
        unique = true)]
)
data class Alarm(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var hour: String = "00",
    var minute: String = "00",
    var title: String = "",
    var description: String = "",
    var isScheduled: Boolean = false,
    var isRecurring: Boolean = false,
    var isSunday: Boolean = false,
    var isMonday: Boolean = false,
    var isTuesday: Boolean = false,
    var isWednesday: Boolean = false,
    var isThursday: Boolean = false,
    var isFriday: Boolean = false,
    var isSaturday: Boolean = false,
)






