package com.example.clock

import androidx.navigation.NavDeepLink
import androidx.navigation.navDeepLink

sealed class Screen(val route: String) {
    object AlarmsList: Screen("AlarmsList")
    object CreateAlarm: Screen("CreateAlarm")
    object WorldClock: Screen("WorldClock")
    object Stopwatch: Screen("Stopwatch")
    object Timer: Screen("Timer")

    companion object {
        val timerDeepLink: List<NavDeepLink> = listOf(
            navDeepLink {
                uriPattern = "https://www.clock.com/Timer"
            }
        )
    }
}
