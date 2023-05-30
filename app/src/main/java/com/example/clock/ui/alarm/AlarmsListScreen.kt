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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.clock.R
import com.example.clock.data.model.Alarm
import com.example.clock.util.checkDate
import com.example.clock.util.components.ClockAppBar
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import kotlin.time.ExperimentalTime

/*
@Preview(device = Devices.TABLET, uiMode = Configuration.ORIENTATION_PORTRAIT, widthDp = 768, heightDp = 1024)
@Composable
private fun AlarmsListScreenPreview() {
    ClockTheme {
        AlarmsListScreen(
            alarmActions = object : AlarmActions {},
            alarmsListState = alarmsListPreview,
        )
    }
}

@Preview(device = Devices.PIXEL_4_XL)
@Composable
private fun AlarmsListScreenDarkPreview() {
    ClockTheme(darkTheme = true) {
        AlarmsListScreen(
            alarmActions = object : AlarmActions {},
            alarmsListState = alarmsListPreview,
        )
    }
}

 */

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun AlarmsListScreen(
    modifier: Modifier = Modifier,
    alarmsListState: List<Alarm>,
    alarmActions: AlarmActions,
    navigateToCreateAlarm: () -> Unit = {},
) {
    Surface(modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxSize(),
            ) {
                AlarmsListAppBar(
                    modifier = Modifier.statusBarsPadding(),
                    clear = { alarmActions.clear() },
                )
                AlarmsList(
                    alarmsListSate = alarmsListState,
                    alarmActions = alarmActions,
                    navigateToCreateAlarm = navigateToCreateAlarm,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlarmsListAppBar(
    modifier: Modifier = Modifier,
    clear: () -> Unit,
) {
    var showMenu by rememberSaveable { mutableStateOf(false) }
    ClockAppBar(
        modifier = modifier,
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
                        clear()
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
    alarmActions: AlarmActions,
    alarmsListSate: List<Alarm>,
    navigateToCreateAlarm: () -> Unit,
) {
    val scrollState = rememberLazyListState()

    BoxWithConstraints(modifier = modifier) {
        LazyColumn(
            state = scrollState,
        ) {
            items(items = alarmsListSate, key = { alarmItem -> alarmItem.id }) { item ->
                var isScheduled by rememberSaveable { mutableStateOf(item.isScheduled) }
                val removeAlarm = SwipeAction(
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.inversePrimary,
                        )
                    },
                    isUndo = false,
                    background = MaterialTheme.colorScheme.error,
                    onSwipe = { alarmActions.remove(alarm = item) },
                )
                SwipeableActionsBox(
                    endActions = listOf(removeAlarm),
                    backgroundUntilSwipeThreshold = MaterialTheme.colorScheme.background,
                    swipeThreshold = maxWidth / 2,
                ) {
                    Alarm(
                        alarm = item,
                        onScheduledChange = {
                            isScheduled = it
                            val description = if (item.description.any { char -> char.isDigit() }) item.checkDate() else item.description
                            alarmActions.update(
                                item.copy(
                                    isScheduled = isScheduled,
                                    description = description,
                                ),
                            )
                        },
                        updateAlarmCreationState = { alarmActions.updateAlarmCreationState(it) },
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
    navigateToCreateAlarm: () -> Unit,
    onScheduledChange: (Boolean) -> Unit,
    updateAlarmCreationState: (Alarm) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        onClick = {
            navigateToCreateAlarm()
            updateAlarmCreationState(alarm)
        },

    ) {
        Row(
            modifier = Modifier
                .padding(20.dp),
            verticalAlignment = CenterVertically,
        ) {
            AlarmInfo(
                modifier = Modifier.weight(2f),
                time = "${alarm.hour}:${alarm.minute}",
                title = alarm.title,
                isScheduled = alarm.isScheduled,
            )

            Text(
                modifier = Modifier
                    .weight(2f)
                    .padding(start = 5.dp),
                text = alarm.description.substringAfter("-"),
            )
            IconToggleButton(
                modifier = Modifier.weight(1f),
                checked = alarm.isScheduled,
                onCheckedChange = { onScheduledChange(it) },
            ) {
                if (alarm.isScheduled) {
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
private fun AlarmInfo(
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

val alarmsListPreview = mutableListOf<Alarm>().apply {
    for (i in 0..20) {
        val hour = String.format("%02d", i % 24) // format hour with leading zero if needed
        val minute = "00" // set minute to zero
        val title = if (i == 0) "zero" else i.toString() // set title to number if not 0
        add(Alarm(id = i, hour = hour, minute = minute, title = title, isScheduled = true))
    }
}.toList()
