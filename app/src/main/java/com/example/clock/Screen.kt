package com.example.clock

sealed class Screen(val route: String) {
    object Alarm: Screen("Alarm")
    object WorldClock: Screen("WorldClock")
    object Stopwatch: Screen("Stopwatch")
    object Timer: Screen("Timer")
}
