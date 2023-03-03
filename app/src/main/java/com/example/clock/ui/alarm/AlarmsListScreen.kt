package com.example.clock.ui.alarm

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlarmOn
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.AlarmOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.clock.R
import com.example.clock.data.model.Alarm
import com.example.clock.util.checkDate
import com.example.clock.util.components.ClockAppBar
import com.google.accompanist.insets.statusBarsPadding
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun AlarmsListScreen(
    modifier: Modifier = Modifier,
    alarmViewModel: AlarmViewModel,
    navigateToCreateAlarm: () -> Unit = {},
) {
    //val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarScrollState())
    val alarmListState by alarmViewModel.alarmsListState.observeAsState()

    Surface(modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxSize()
                    //.nestedScroll(scrollBehavior.nestedScrollConnection),
            ) {
                AlarmsListAppBar(
                    modifier = Modifier.statusBarsPadding(),
                    //scrollBehavior = scrollBehavior,
                    clearAlarmsList = { alarmViewModel.clearAlarmsList() },
                )
                alarmListState?.let {
                    AlarmsList(
                        alarmsListSate = it,
                        alarmViewModel = alarmViewModel,
                        navigateToCreateAlarm = navigateToCreateAlarm,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlarmsListAppBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    clearAlarmsList: () -> Unit,
) {
    var showMenu by rememberSaveable { mutableStateOf(false) }
    ClockAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = stringResource(id = R.string.alarm),
                style = MaterialTheme.typography.titleLarge,
            )
        },
        actions = {
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null,
                )
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(id = R.string.clear_alarms)) },
                    onClick = {
                        clearAlarmsList()
                        showMenu = false
                    },
                )
            }
        },
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun AlarmsList(
    modifier: Modifier = Modifier,
    alarmViewModel: AlarmViewModel,
    alarmsListSate: List<Alarm>,
    navigateToCreateAlarm: () -> Unit,
) {
    val scrollState = rememberLazyListState()
    BoxWithConstraints(modifier = modifier) {
        LazyColumn(
            state = scrollState,
        ) {
            items(alarmsListSate) { item ->
                var isScheduled by rememberSaveable { mutableStateOf(item.isScheduled) }
                var description by rememberSaveable { mutableStateOf(item.description) }

                val delete = SwipeAction(
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.inversePrimary,
                        )
                    },
                    isUndo = false,
                    background = MaterialTheme.colorScheme.error,
                    onSwipe = { alarmViewModel.remove(alarm = item) },
                )
                SwipeableActionsBox(
                    endActions = listOf(delete),
                    backgroundUntilSwipeThreshold = MaterialTheme.colorScheme.background,
                    swipeThreshold = maxWidth / 2,
                ) {
                    Alarm(
                        alarm = item,
                        isScheduled = isScheduled,
                        onScheduledChange = {
                            isScheduled = it
                            if (description.any { char -> char in "0123456789" }) {
                                description = item.checkDate()
                            }
                            alarmViewModel.onScheduledChange(
                                item.copy(
                                    isScheduled = isScheduled,
                                    description = description,
                                ),
                            )
                        },
                        changeCreateAlarmState = { alarmViewModel.changeCreateAlarmState(it) },
                        navigateToCreateAlarm = navigateToCreateAlarm,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Alarm(
    alarm: Alarm,
    isScheduled: Boolean,
    navigateToCreateAlarm: () -> Unit,
    onScheduledChange: (Boolean) -> Unit,
    changeCreateAlarmState: (Alarm) -> Unit,

) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        onClick = {
            navigateToCreateAlarm()
            changeCreateAlarmState(alarm)
        },

    ) {
        Row(
            modifier = Modifier
                .padding(20.dp),
            verticalAlignment = CenterVertically,
        ) {
            AlarmTime(
                modifier = Modifier.weight(2f),
                time = "${alarm.hour}:${alarm.minute}",
                title = alarm.title,
                isScheduled = isScheduled,
            )

            Text(
                modifier = Modifier
                    .weight(2f)
                    .padding(start = 5.dp),
                text = alarm.description.substringAfter("-"),
            )
            IconToggleButton(
                modifier = Modifier.weight(1f),
                checked = isScheduled,
                onCheckedChange = {
                    onScheduledChange(it)
                },
            ) {
                if (isScheduled) {
                    Icon(
                        modifier = Modifier.size(35.dp),
                        imageVector = Icons.Filled.AlarmOn,
                        contentDescription = "AlarmOn",
                    )
                } else {
                    Icon(
                        modifier = Modifier.size(35.dp),
                        imageVector = Icons.Outlined.AlarmOff,
                        contentDescription = "AlarmOff",
                    )
                }
            }
        }
    }
}

@Composable
private fun AlarmTime(
    modifier: Modifier = Modifier,
    time: String,
    title: String,
    isScheduled: Boolean,
) {
    val textColor by animateColorAsState(
        targetValue = if (isScheduled) {
            LocalContentColor.current
        } else {
            LocalContentColor.current.copy(
                alpha = 0.5f,
            )
        },
    )

    Column(modifier = modifier) {
        Text(
            text = time,
            style = MaterialTheme.typography.headlineLarge,
            color = textColor,
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = textColor,
        )
    }
}

private const val TAG = "AlarmsListScreen"
