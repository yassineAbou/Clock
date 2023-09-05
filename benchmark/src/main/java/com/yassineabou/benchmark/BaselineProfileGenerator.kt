package com.yassineabou.benchmark

import androidx.benchmark.macro.ExperimentalBaselineProfilesApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalBaselineProfilesApi::class)
@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {

    @get:Rule
    val baselineRule = BaselineProfileRule()

    @Test
    fun generateBaselineProfile(): Unit = baselineRule.collectBaselineProfile(
        packageName = "com.yassineabou.clock",
    ) {
        pressHome()
        startActivityAndWait()

        clickOnText("Clock")
        clickOnText("Stopwatch")
        clickOnText("Timer")
        clickOnText("Alarm")
        clickOnId("AddAlarm")
    }
}
