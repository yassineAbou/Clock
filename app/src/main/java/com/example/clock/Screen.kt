package com.example.clock

sealed class Screen(val route: String) {
    object AlarmsList: Screen("AlarmsList")
    object CreateAlarm: Screen("CreateAlarm")
    object WorldClock: Screen("WorldClock")
    object Stopwatch: Screen("Stopwatch")
    object Timer: Screen("Timer")
}
