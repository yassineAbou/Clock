package com.yassineabou.clock.data.model

data class StopwatchState(
    val second: String = "00",
    val minute: String = "00",
    val hour: String = "00",
    val isPlaying: Boolean = false,
    val isReset: Boolean = true,
)
