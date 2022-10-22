package com.example.clock.ui

import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAlarm
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.clock.R
import com.example.clock.alarm.AlarmsListScreen
import com.example.clock.alarm.AlarmsListViewModel
import com.example.clock.alarm.CreateAlarmScreen
import com.example.clock.data.Alarm
import com.example.clock.data.manager.ServiceManager
import com.example.clock.data.receiver.TimerNotificationBroadcastReceiver
import com.example.clock.data.service.StopwatchService
import com.example.clock.data.service.TimerRunningService
import com.example.clock.ui.clock.ClockScreen
import com.example.clock.ui.stopwatch.StopwatchScreen
import com.example.clock.ui.stopwatch.StopwatchViewModel
import com.example.clock.ui.theme.ClockTheme
import com.example.clock.ui.timer.TimerScreen
import com.example.clock.ui.timer.TimerViewModel
import com.example.clock.util.components.BottomNavigationBar
import com.example.clock.util.components.listBottomBarItems
import com.example.clock.util.isServiceRunning
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private val stopwatchViewModel: StopwatchViewModel by viewModels()
    private val timerViewModel: TimerViewModel by viewModels()
    @Inject
    lateinit var serviceManager: ServiceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ClockTheme {
                ProvideWindowInsets {
                    val systemUiController = rememberSystemUiController()
                    val useDarkIcons = !isSystemInDarkTheme()
                    SideEffect {
                        systemUiController.setStatusBarColor(
                            color = Color.Transparent,
                            darkIcons = useDarkIcons
                        )
                    }
                    ClockApp()
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            if (this@MainActivity.isServiceRunning(StopwatchService::class.java)) {
                async {
                    serviceManager.stopService(StopwatchService::class.java)
                }
            }
            if (this@MainActivity.isServiceRunning(TimerRunningService::class.java)) {
                async {
                    serviceManager.stopService(TimerRunningService::class.java)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        lifecycleScope.launch {
            val isReset  = stopwatchViewModel.stopwatchState.asFlow().stateIn(this).value.isReset
            val isDone = timerViewModel.timerState.asFlow().stateIn(this).value.isDone
            if (!isReset) {
                async {
                    serviceManager.startService(StopwatchService::class.java)
                }

            }
            if (!isDone) {
                async {
                    serviceManager.startService(TimerRunningService::class.java)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
private fun ClockApp() {

    val navController = rememberNavController()
    var isFloatButtonVisible by rememberSaveable { (mutableStateOf(true)) }
    var isBottomBarVisible by rememberSaveable { (mutableStateOf(true)) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val alarmListViewModel: AlarmsListViewModel = viewModel()

    LaunchedEffect(navBackStackEntry?.destination?.route) {
        isFloatButtonVisible = when (navBackStackEntry?.destination?.route) {
            Screen.AlarmsList.route -> true
            else -> false
        }
        isBottomBarVisible = when (navBackStackEntry?.destination?.route) {
            Screen.CreateAlarm.route -> false
            else -> true
        }
    }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = isBottomBarVisible,
                enter = scaleIn(),
                exit = scaleOut(),
            ) {
                BottomNavigationBar(
                    modifier = Modifier.navigationBarsPadding(),
                    listBottomBarItems = listBottomBarItems,
                    navController = navController,
                )
            }
        },

        floatingActionButton = {
            AnimatedVisibility(
                visible = isFloatButtonVisible,
                enter = scaleIn(),
                exit = scaleOut(),
            ) {
                ExtendedFloatingActionButton(
                    text = { Text(text = stringResource(id = R.string.add_alarm)) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AddAlarm,
                            contentDescription = null
                        )
                    },
                    onClick = {
                        navController.navigate(Screen.CreateAlarm.route)
                        alarmListViewModel.test(Alarm())
                        },
                    elevation = FloatingActionButtonDefaults.elevation(8.dp)
                ) }

        },
        content = {
            Navigation(navController = navController, modifier = Modifier.padding(it))
        }
    )
}


@OptIn(ExperimentalTime::class)
@Composable
fun Navigation(navController: NavHostController, modifier: Modifier = Modifier) {

    val alarmListViewModel: AlarmsListViewModel = viewModel()
    val alarmListState by alarmListViewModel.alarmsListState.observeAsState()

    NavHost(navController = navController, startDestination = Screen.AlarmsList.route) {
        composable(
           route = Screen.AlarmsList.route,
            deepLinks = Screen.alarmListDeepLink
        ) {
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
        composable(Screen.Clock.route) {
            ClockScreen(modifier = modifier)
        }
        composable(
           route = Screen.Stopwatch.route,
           deepLinks = Screen.stopwatchDeepLink
        ) {
           StopwatchScreen(modifier = modifier)
        }
        composable(
           route = Screen.Timer.route,
            deepLinks = Screen.timerDeepLink
        ) {
           TimerScreen(modifier = modifier)
        }
        composable(Screen.CreateAlarm.route) {
            alarmListViewModel.alarmState?.let { it ->
                CreateAlarmScreen(
                    alarmsListScreenActions = alarmListViewModel,
                    alarmState = it,
                    navigateToAlarmsList = {
                        navController.navigate(Screen.AlarmsList.route)
                    },
                    //targetDay = alarmListViewModel.targetDay
                )
            }
        }
    }
}

private const val TAG = "MainActivity"









