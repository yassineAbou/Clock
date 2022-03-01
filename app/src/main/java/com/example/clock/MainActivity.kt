package com.example.clock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.clock.alarm.AlarmScreen
import com.example.clock.components.BottomNavigationBar
import com.example.clock.components.bottomBarItems
import com.example.clock.stopwatch.StopwatchScreen
import com.example.clock.timer.TimerScreen
import com.example.clock.ui.theme.ClockTheme
import com.example.clock.world.WorldClockScreen

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

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

                    ClockApp(navController = navController)

                }

            }

        }
    }
}

@Composable
fun ClockApp(navController: NavHostController) {

    NavHost(navController = navController, startDestination = Screen.Alarm.route) {
        composable(Screen.Alarm.route) {
            AlarmScreen()
        }
        composable(Screen.WorldClock.route) {
            WorldClockScreen()
        }
        composable(Screen.Stopwatch.route) {
            StopwatchScreen()
        }
        composable(Screen.Timer.route) {
            TimerScreen()
        }
    }
}






