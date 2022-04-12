package com.example.clock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
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
import com.example.clock.world.CurrentTimeScreen
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class MainActivity : ComponentActivity() {

    private val stopwatchViewModel: StopwatchViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
           ProvideWindowInsets {
                ClockTheme {
                    val navController = rememberNavController()
                    Scaffold(
                        bottomBar = {
                            BottomNavigationBar(
                                modifier = Modifier.navigationBarsPadding(),
                                items = bottomBarItems,
                                navController = navController,
                                onItemClick = {
                                    navController.navigate(it.route)
                                },
                            )
                        }
                    ) {
                        Navigation(navController = navController, stopwatchViewModel)
                    }
                }
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
            CurrentTimeScreen()
        }
        composable(Screen.Stopwatch.route) {
            StopwatchScreen()
        }
        composable(Screen.Timer.route) {
            TimerScreen()
        }
    }
}







