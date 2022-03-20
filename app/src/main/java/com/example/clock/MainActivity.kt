package com.example.clock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.clock.alarm.AlarmScreen
import com.example.clock.components.BottomNavigationBar
import com.example.clock.components.bottomBarItems
import com.example.clock.stopwatch.StopwatchScreen
import com.example.clock.stopwatch.StopwatchViewModel
import com.example.clock.timer.TimerScreen
import com.example.clock.ui.theme.ClockTheme
import com.example.clock.world.WorldClockScreen
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class MainActivity : ComponentActivity() {

    private val stopwatchViewModel: StopwatchViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
           // ProvideWindowInsets {
                ClockTheme {
                    val navController = rememberNavController()
                    Scaffold(
                        bottomBar = {
                            BottomNavigationBar(
                                items = bottomBarItems,
                                navController = navController,
                                onItemClick = {
                                    navController.navigate(it.route)
                                }
                            )
                        }
                    ) {
                        Navigation(navController = navController, stopwatchViewModel)
                    }
               // }
            }
        }
    }
}


@OptIn(ExperimentalTime::class)
@Composable
fun Navigation(navController: NavHostController, stopwatchViewModel: StopwatchViewModel) {

    NavHost(navController = navController, startDestination = Screen.Alarm.route) {
        composable(Screen.Alarm.route) {
            AlarmScreen()
        }
        composable(Screen.WorldClock.route) {
            WorldClockScreen()
        }
        composable(Screen.Stopwatch.route) {
            StopwatchScreen(
                /*
                isPlaying = stopwatchViewModel.isPlaying,
                isZero = stopwatchViewModel.isZero,
                seconds = stopwatchViewModel.seconds,
                minutes = stopwatchViewModel.minutes,
                hours = stopwatchViewModel.hours,
                onStart = { stopwatchViewModel.start() },
                onPause = { stopwatchViewModel.pause() },
                onStop = { stopwatchViewModel.stop()  },
                onLap = { stopwatchViewModel.onLap() },
                onClear = { stopwatchViewModel.onClear() },
                lapItems = stopwatchViewModel.lapItems

                 */
            )
        }
        composable(Screen.Timer.route) {
            TimerScreen()
        }
    }
}

// TODO: compose viewModel before commit
// TODO: fix Experimental annotations
// TODO: use weight instead of dp






