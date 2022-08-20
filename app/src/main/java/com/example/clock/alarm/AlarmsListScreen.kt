package com.example.clock.alarm

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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.clock.Screen
import com.example.clock.components.ClockAppBar
import com.example.clock.data.Alarm
import com.example.clock.ui.theme.ClockTheme
import com.example.clock.util.Global
import com.example.clock.util.parseInt
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import java.lang.Integer.parseInt
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

@Preview
@Composable
fun AlarmScreenPreview() {
    ClockTheme {
        AlarmsListScreen(
            alarmsListState = alarmListPreview,
            alarmsListScreenActions = object : AlarmsListScreenActions {
                override fun clear() {}
                override fun update(alarm: Alarm) {}
                override fun delete(alarm: Alarm) {}
                override fun save(alarm: Alarm) {}
                override fun insert() {}
                override fun onChangeCreated(created: Boolean) {}
                override fun setHour(hour: String) {}
                override fun setMinute(minute: String) {}
                override fun onChangeTitle(title: String) {}
                override fun onChangeTargetDay(targetDay: String) {}
                override fun onChangeDays(days: List<Boolean>) {}
                override fun cancel(alarm: Alarm) {}
                override fun schedule(alarm: Alarm) {}

            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AlarmsListScreen(
    modifier: Modifier = Modifier,
    alarmsListState: List<Alarm>,
    alarmsListScreenActions: AlarmsListScreenActions,
    navigateToCreateAlarm: () -> Unit = {}
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarScrollState())


    Surface(modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
            ) {
                AlarmTopAppBar(
                    modifier = Modifier.statusBarsPadding(),
                    scrollBehavior = scrollBehavior,
                    onClear = { alarmsListScreenActions.clear() }
                )
                AlarmsList(
                    modifier = Modifier
                        .weight(1f)
                        .navigationBarsPadding(),
                    alarmsList = alarmsListState,
                    onDelete = { alarmsListScreenActions.delete(it) },
                    onUpdate = { alarmsListScreenActions.update(it) },
                    navigateToCreateAlarm = navigateToCreateAlarm,
                    onSave = { alarmsListScreenActions.save(it) },
                    onChangeCreated = { alarmsListScreenActions.onChangeCreated(it) },
                    cancel = { alarmsListScreenActions.cancel(it) },
                    schedule = {alarmsListScreenActions.schedule(it) },
                )
             }
        }
    }

}

@Composable
private fun AlarmTopAppBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onClear: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    ClockAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = "Alarm",
                style = MaterialTheme.typography.titleLarge,
            )
        },
        actions = {
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null
                )
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Clear alarms") },
                    onClick = onClear
                )
            }
        }
    )
}





@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun AlarmsList(
    modifier: Modifier = Modifier,
    alarmsList: List<Alarm>,
    onDelete: (Alarm) -> Unit,
    onUpdate: (Alarm) -> Unit,
    navigateToCreateAlarm: () -> Unit,
    onSave: (Alarm) -> Unit,
    onChangeCreated: (Boolean) -> Unit,
    schedule: (alarm: Alarm) -> Unit,
    cancel: (alarm: Alarm) -> Unit
) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()
    val context = LocalContext.current
    Box(modifier = modifier) {
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .navigationBarsPadding()
                .fillMaxSize()
        ) {
            items(alarmsList) { item ->
                var checked by rememberSaveable { mutableStateOf(item.started) }
                LaunchedEffect(item.started) {
                    checked = item.started
                }
                val delete = SwipeAction(
                    icon = {
                        Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                    },
                    isUndo = true,
                    background = MaterialTheme.colorScheme.error,
                    onSwipe = {
                        onDelete(item)
                        if (item.started) {
                            cancel(item)
                        }
                    }
                )
                SwipeableActionsBox(
                    endActions = listOf(delete),
                    backgroundUntilSwipeThreshold = MaterialTheme.colorScheme.background,
                    swipeThreshold = 150.dp

                ) {
                    Alarm(
                        alarm = item,
                        onCheckedChange = {
                            val currentTime = Calendar.getInstance()
                            val timeToMatch = Calendar.getInstance()
                            val nextDay = LocalDateTime.now().plus(1, ChronoUnit.DAYS)
                            checked = it
                            timeToMatch[Calendar.HOUR_OF_DAY] = item.hour.parseInt()
                            timeToMatch[Calendar.MINUTE] = item.minute.parseInt()
                             var targetDay = item.targetDay

                            if (timeToMatch <= currentTime && item.targetDay.any { it in "0123456789" }) {
                            targetDay = "Tomorrow-${nextDay.format(Global.formatter)}"
                        }

                            when(it) {
                                true -> schedule(item)
                                false-> cancel(item)
                            }

                           onUpdate(item.copy(started = checked, targetDay = targetDay))
                        },
                        checked = checked,
                        navigateToCreateAlarm = navigateToCreateAlarm,
                        onSave = { onSave(it) },
                        onChangeCreated = onChangeCreated
                    )
                }

            }
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Alarm(
    modifier: Modifier =  Modifier,
    alarm: Alarm,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    navigateToCreateAlarm: () -> Unit,
    onSave: (Alarm) -> Unit,
    onChangeCreated: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        onClick = {
            onSave(alarm)
            navigateToCreateAlarm()
            onChangeCreated(true)
        },

    ) {

        Row(
            modifier = Modifier
                .padding(20.dp),
            verticalAlignment = CenterVertically
        ) {

                AlarmTime(
                    modifier = Modifier.weight(2f),
                    time = "${alarm.hour}:${alarm.minute}",
                    title = alarm.title,
                    checked = checked
                )

                Text(
                    modifier = Modifier.weight(1f),
                    text = alarm.targetDay.substringAfter("-")
                )
                IconToggleButton(
                    modifier = Modifier.weight(1f),
                    checked = checked,
                    onCheckedChange = onCheckedChange
                ) {
                    if (checked) {
                        Icon(
                            modifier = Modifier.size(35.dp),
                            imageVector = Icons.Filled.AlarmOn,
                            contentDescription = "Localized description"
                        )
                    } else {
                        Icon(
                            modifier = Modifier.size(35.dp),
                            imageVector = Icons.Outlined.AlarmOff,
                            contentDescription = "Localized description"
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
    checked: Boolean
) {

    val textColor = if (checked) LocalContentColor.current else Color.Gray

    Column(modifier = modifier) {
        Text(
            text = time,
            style = MaterialTheme.typography.headlineLarge,
            color = textColor
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = textColor
        )
    }
}

val alarmListPreview = listOf(
    Alarm(hour = "18", minute = "22", title = "new day"),
    Alarm(hour = "20", minute = "08", title = "hello world"),
    Alarm(hour = "5", minute = "00", title = "wake up"),
    Alarm(hour = "17", minute = "20")
)









