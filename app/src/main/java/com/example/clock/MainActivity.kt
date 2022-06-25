package com.example.clock

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAlarm
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.clock.alarm.AlarmsListScreen
import com.example.clock.alarm.AlarmsListViewModel
import com.example.clock.alarm.CreateAlarmScreen
import com.example.clock.components.BottomNavigationBar
import com.example.clock.components.bottomBarItems
import com.example.clock.stopwatch.StopwatchScreen
import com.example.clock.stopwatch.StopwatchViewModel
import com.example.clock.timer.TimerScreen
import com.example.clock.timer.TimerViewModel
import com.example.clock.ui.theme.ClockTheme
import com.example.clock.world.CurrentTimeScreen
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.time.ExperimentalTime

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ProvideWindowInsets {
                ClockTheme {
                    ClockApp()
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
private fun ClockApp() {
    val navController = rememberNavController()
    var isFabVisible by rememberSaveable { (mutableStateOf(true)) }
    var isCreateAlarmVisible by rememberSaveable { (mutableStateOf(true)) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    LaunchedEffect(navBackStackEntry?.destination?.route) {
        isFabVisible = when (navBackStackEntry?.destination?.route) {
            Screen.AlarmsList.route -> true
            else -> false
        }
        when (navBackStackEntry?.destination?.route) {
            Screen.CreateAlarm.route -> {
                isCreateAlarmVisible = false
                Log.e(TAG, "isCreateAlarmVisible: $isCreateAlarmVisible")
            }
            else -> {
                isCreateAlarmVisible = true
                Log.e(TAG, "isCreateAlarmVisible: $isCreateAlarmVisible")
            }
        }
    }


    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = isCreateAlarmVisible,
                enter = scaleIn(),
                exit = scaleOut(),
            ) {
                BottomNavigationBar(
                    modifier = Modifier.navigationBarsPadding(),
                    items = bottomBarItems,
                    navController = navController,
                )
            }
        },
        floatingActionButton = {


            AnimatedVisibility(
                visible = isFabVisible,
                enter = scaleIn(),
                exit = scaleOut(),
            ) {
                ExtendedFloatingActionButton(
                    text = { Text(text = "AddAlarm") },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AddAlarm,
                            contentDescription = null
                        )
                    },
                    onClick = {
                        navController.navigate(Screen.CreateAlarm.route)
                        },
                    elevation = FloatingActionButtonDefaults.elevation(8.dp)
                ) }

        },
        content = {
          Navigation(navController = navController)
        }
    )
}


@OptIn(ExperimentalTime::class)
@Composable
fun Navigation(navController: NavHostController) {

    val stopwatchViewModel: StopwatchViewModel = viewModel()
    val timerViewModel: TimerViewModel = viewModel()
    val alarmListViewModel: AlarmsListViewModel = viewModel()
    val stopwatchState by stopwatchViewModel.stopwatchState.observeAsState()
    val timerState by timerViewModel.timerState.observeAsState()
    val alarmListState by alarmListViewModel.alarmsListState.observeAsState()

    NavHost(navController = navController, startDestination = Screen.AlarmsList.route) {
        composable(Screen.AlarmsList.route) {
            alarmListState?.let {
                AlarmsListScreen(
                    alarmsListState = it,
                    alarmsListScreenActions = alarmListViewModel,
                    navigateToCreateAlarm = {
                        navController.navigate(Screen.CreateAlarm.route)
                    }
                )
            }
        }
        composable(Screen.WorldClock.route) {
            CurrentTimeScreen()
        }
        composable(Screen.Stopwatch.route) {
            stopwatchState?.let {
                StopwatchScreen(
                    stopwatchState = it,
                    stopwatchActions = stopwatchViewModel,
                    lapItems = stopwatchViewModel.lapItems,
                )

            }
        }
        composable(Screen.Timer.route) {
            timerState?.let {
                TimerScreen(
                   timerState = it,
                   timerScreenActions = timerViewModel
                )
            }
        }
        composable(Screen.CreateAlarm.route) {
            alarmListViewModel.alarmState?.let { it ->
                CreateAlarmScreen(
                    alarmsListScreenActions = alarmListViewModel,
                    alarmState = it,
                    navigateToAlarmsList = { navController.navigate(Screen.AlarmsList.route) },
                    //targetDay = alarmListViewModel.targetDay
                )
            }
        }
    }
}








