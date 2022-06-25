package com.example.clock.alarm

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.clock.components.ClockAppBar
import com.example.clock.data.Alarm
import com.example.clock.ui.theme.ClockTheme
import com.google.accompanist.insets.statusBarsPadding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.saveable.rememberSaveable
import com.google.accompanist.insets.navigationBarsPadding

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
                    modifier = Modifier.weight(1f).navigationBarsPadding(),
                    alarmsList = alarmsListState,
                    onDelete = { alarmsListScreenActions.delete(it) },
                    onUpdate = { alarmsListScreenActions.update(it) },
                    navigateToCreateAlarm = navigateToCreateAlarm,
                    onSave = { alarmsListScreenActions.save(it) },
                    onChangeCreated = { alarmsListScreenActions.onChangeCreated(it) }
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
    onChangeCreated: (Boolean) -> Unit
) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()
    Box(modifier = modifier) {
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .navigationBarsPadding()
                .fillMaxSize()
        ) {
            items(alarmsList) { item ->
                var height by rememberSaveable { mutableStateOf(0) }
                var checked by rememberSaveable { mutableStateOf(item.started) }
                AnimatedSwipeDismiss(
                    item = item,
                    background = { isDismissed ->
                        Box(
                            modifier = Modifier
                                .height(height.dp)
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                                .background(Color.Red),
                        ) {
                            val alpha: Float by animateFloatAsState(if (isDismissed) 0f else 1f)
                            Icon(
                                modifier = Modifier.align(CenterEnd),
                                imageVector = Icons.Filled.Delete,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = alpha)
                            )
                        }

                    },
                    content = {
                        Alarm(
                            alarm = item,
                            onCheckedChange = {
                                 checked = it
                                onUpdate(item.copy(started = checked))
                            },
                            checked = checked,
                            setHeight = { height = it},
                            navigateToCreateAlarm = navigateToCreateAlarm,
                            onSave = { onSave(it) },
                            onChangeCreated = onChangeCreated
                        )
                    },
                    onDismiss = { onDelete(item) }
                )
            }
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Alarm(
    alarm: Alarm,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    setHeight: (Int) -> Unit,
    navigateToCreateAlarm: () -> Unit,
    onSave: (Alarm) -> Unit,
    onChangeCreated: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .onGloballyPositioned { layoutCoordinates ->
                setHeight(layoutCoordinates.size.height)
            },
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









