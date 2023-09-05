package com.yassineabou.clock.data.model

data class TimerState(
    val timeInMillis: Long = 0L,
    val timeText: String = "00:00:00",
    val hour: Int = 0,
    val minute: Int = 0,
    val second: Int = 0,
    val progress: Float = 0f,
    val isPlaying: Boolean = false,
    val isDone: Boolean = true,
)
