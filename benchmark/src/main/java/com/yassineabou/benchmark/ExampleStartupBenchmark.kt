package com.yassineabou.benchmark

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import junit.framework.TestCase.fail
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This is an example startup benchmark.
 *
 * It navigates to the device's home screen, and launches the default activity.
 *
 * Before running this benchmark:
 * 1) switch your app's active build variant in the Studio (affects Studio runs only)
 * 2) add `<profileable android:shell="true" />` to your app's manifest, within the `<application>` tag
 *
 * Run this benchmark from Studio to see startup measurements, and captured system traces
 * for investigating your app's performance.
 */
@RunWith(AndroidJUnit4::class)
class ExampleStartupBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun startupCompilationNone() = startup(CompilationMode.None())

    @Test
    fun startupCompilationPartial() = startup(CompilationMode.Partial())

    @Test
    fun bottomNavCompilationNone() = bottomNavAction(CompilationMode.None())

    @Test
    fun bottomNavCompilationPartial() = bottomNavAction(CompilationMode.Partial())

    private fun startup(compilationMode: CompilationMode) = benchmarkRule.measureRepeated(
        packageName = "com.yassineabou.clock",
        metrics = listOf(StartupTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.COLD,
        compilationMode = compilationMode,
    ) {
        pressHome()
        startActivityAndWait()
    }

    private fun bottomNavAction(compilationMode: CompilationMode) = benchmarkRule.measureRepeated(
        packageName = "com.yassineabou.clock",
        metrics = listOf(FrameTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.COLD,
        compilationMode = compilationMode,
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

fun MacrobenchmarkScope.clickOnText(text: String) {
    device
        .findObject(By.text(text))
        .click()
}

fun MacrobenchmarkScope.clickOnId(resourceId: String) {
    val selector = By.res(resourceId)
    if (!device.wait(Until.hasObject(selector), 2_500)) {
        fail("Did not find object with id $resourceId")
    }

    device
        .findObject(selector)
        .click()
    Thread.sleep(100)
}
