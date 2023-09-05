package com.yassineabou.clock.ui

import androidx.navigation.NavDeepLink
import androidx.navigation.navDeepLink

sealed class Screen(val route: String) {
    object AlarmsList : Screen("AlarmsList")
    object CreateAlarm : Screen("CreateAlarm")
    object Clock : Screen("Clock")
    object Stopwatch : Screen("Stopwatch")
    object Timer : Screen("Timer")

    companion object {
        val timerDeepLink: List<NavDeepLink> = listOf(
            navDeepLink {
                uriPattern = "https://www.clock.com/Timer"
            },
        )
        val alarmListDeepLink: List<NavDeepLink> = listOf(
            navDeepLink {
                uriPattern = "https://www.clock.com/AlarmsList"
            },
        )
        val stopwatchDeepLink: List<NavDeepLink> = listOf(
            navDeepLink {
                uriPattern = "https://www.clock.com/Stopwatch"
            },
        )
    }
}
