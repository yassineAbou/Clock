package com.example.clock.alarm

import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.clock.util.components.NumberPicker
import com.example.clock.data.Alarm
import com.example.clock.ui.theme.ClockTheme
import com.example.clock.util.Global.current
import com.example.clock.util.Global.formatter
import com.example.clock.util.checkNumberPicker
import com.example.clock.util.components.clearFocusOnKeyboardDismiss

import com.example.clock.util.parseInt
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.regex.Pattern


@Preview()
@Composable
private fun CreateAlarmScreenPreview() {
    ClockTheme {
        CreateAlarmScreen(
            alarmState = Alarm(hour = "18", minute = "22", title = "new day"),
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
            },
        )
    }
}


@Composable
fun CreateAlarmScreen(
    modifier: Modifier = Modifier,
    alarmState: Alarm,
    alarmsListScreenActions: AlarmsListScreenActions,
    navigateToAlarmsList: () -> Unit = {},
) {

    Surface(modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize()) {

            val targetDay = remember { mutableStateListOf("") }
            var alarmTitle by remember { mutableStateOf(TextFieldValue(alarmState.title)) }
            val areSelected = remember { mutableStateListOf(
                alarmState.sunday, alarmState.monday, alarmState.tuesday, alarmState.wednesday,
                alarmState.thursday, alarmState.friday, alarmState.saturday,
            )
            }
            var index by remember { mutableStateOf(0) }
            var day by remember { mutableStateOf("") }
            var clicked by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                    val defaultTargetDay = Pattern.compile(" ").split(alarmState.targetDay).toList()
                    defaultTargetDay.forEach { targetDay.add(it) }
            }

           LaunchedEffect(clicked)  {
               when {
                   areSelected[index] && targetDay.any { it != day } -> {
                       targetDay.apply {
                           removeAll { it.contains("[0-9]".toRegex()) }
                           add(day)
                       }
                   }
                   !areSelected[index]  -> {
                       targetDay.remove(day)
                   }
               }
               if (areSelected.all { !it } && targetDay.all { !it.contains("[0-9]".toRegex()) }) {
                   targetDay.add("Today-${current.format(formatter)}")
               }
           }

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(115.dp))
                TimePicker(
                    alarmState = alarmState,
                    onChangeTargetDat = {
                        targetDay.add(it)
                        clicked != clicked
                    },
                    onClearTargetDat = { targetDay.clear() },
                    setHour = { alarmsListScreenActions.setHour(it) },
                    setMinute = { alarmsListScreenActions.setMinute(it) },
                    targetDay = targetDay
                )
                Spacer(modifier = Modifier.height(115.dp))
                CustomAlarmActions(
                    alarmState = alarmState,
                    targetDay = targetDay,
                    onChangeTargetDat = { targetDay.add(it) },
                    onClearTargetDat = {
                        targetDay.clear()
                        areSelected.replaceAll { false }
                    },
                    onChangeTitle = {
                        alarmTitle = it
                        alarmsListScreenActions.onChangeTitle(it.text)
                    },
                    areSelected = areSelected,
                    onChangeSelected = { selected, id ->
                        areSelected[id] = selected
                    },
                    onChange = { name, id ->
                        day = name
                        index = id
                        clicked = !clicked
                    },
                    alarmTitle = alarmTitle,
                )
                CreateAlarmActions(
                    onInsert = {
                        alarmsListScreenActions.onChangeDays(areSelected)
                        alarmsListScreenActions.onChangeTargetDay(targetDay.joinToString(" "))
                        alarmsListScreenActions.insert()
                    },
                    navigateToAlarmsList = navigateToAlarmsList,
                    onSave = { alarmsListScreenActions.save(it) },
                )
            }
        }
    }
 }






@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomAlarmActions(
    targetDay: List<String>,
    alarmState: Alarm,
    onChangeTargetDat: (String) -> Unit,
    onClearTargetDat: () -> Unit,
    alarmTitle: TextFieldValue,
    onChangeTitle: (TextFieldValue) -> Unit,
    onChangeSelected: (Boolean, Int) -> Unit,
    areSelected: List<Boolean>,
    onChange: (String, Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = {},
        ) {
        DatePicker(
            modifier = Modifier.fillMaxWidth(),
            onClearTargetDat = onClearTargetDat,
            onChangeTargetDat =  { onChangeTargetDat(it) },
            targetDay = targetDay
        )
        Spacer(modifier = Modifier.height(5.dp))
        Recurring(
            modifier = Modifier.fillMaxWidth(),
            areSelected = areSelected,
            onChangeSelected = onChangeSelected,
            onChangeTargetDat = onChange
        )
        AlarName(
            modifier = Modifier
                .align(Start)
                .padding(15.dp),
            title = alarmTitle,
            onChangeTitle =  onChangeTitle
        )
        Spacer(modifier = Modifier.height(140.dp))

    }

}

@Composable
private fun TimePicker(
    modifier: Modifier = Modifier,
    alarmState: Alarm,
    onChangeTargetDat: (String) -> Unit,
    onClearTargetDat: () -> Unit,
    setHour: (String) -> Unit,
    setMinute: (String) -> Unit,
    targetDay: List<String>

) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        val textStyle = MaterialTheme.typography.displaySmall

        var hours by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue(alarmState.hour))
        }
        var minutes by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue(alarmState.minute))
        }
        val nextDay = LocalDateTime.now().plus(1, ChronoUnit.DAYS)



        LaunchedEffect(hours.text, minutes.text) {
            val currentTime = Calendar.getInstance()
            val timeToMatch = Calendar.getInstance()
            timeToMatch[Calendar.HOUR_OF_DAY] = hours.text.parseInt()
            timeToMatch[Calendar.MINUTE] = minutes.text.parseInt()

            when {
                currentTime < timeToMatch && targetDay.any { it.contains("-") }   -> {
                    onClearTargetDat()
                    onChangeTargetDat("Today-${current.format(formatter)}")
                }
                currentTime >= timeToMatch && targetDay.any { it.contains("-") }  -> {
                    onClearTargetDat()
                    onChangeTargetDat("Tomorrow-${nextDay.format(formatter)}")
                }
            }
        }


        NumberPicker(
            modifier = Modifier
                .weight(1f)
                .padding(start = 85.dp),
            number = hours,
            labelTimeUnit = "Hours",
            onNumberChange = { value ->
                if (value.text.checkNumberPicker(23)) {
                    hours = value
                    setHour(hours.text)
                }
            },
            textStyle = textStyle
        )

        Text(
            text = ":",
            style = textStyle,
            modifier = Modifier.padding(15.dp)
        )

        NumberPicker(
            modifier = Modifier.weight(1f),
            number = minutes,
            labelTimeUnit = "Minutes",
            textStyle = textStyle,
            onNumberChange = { value ->
                if (value.text.checkNumberPicker(59)) {
                    minutes = value
                    setMinute(minutes.text)
                }
            }
        )


    }

}



@Composable
private fun DatePicker(
    modifier: Modifier = Modifier,
    targetDay: List<String>,
    onChangeTargetDat: (String) -> Unit,
    onClearTargetDat: () -> Unit
) {
    val dialogState = rememberMaterialDialogState()


    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
            Text(
                modifier = Modifier.padding(15.dp),
                text = targetDay.joinToString(" "),
                style = MaterialTheme.typography.titleMedium
            )
        }
        /*
        IconButton(
            modifier = Modifier.padding(end = 5.dp),
            onClick = {
                    dialogState.show()
            }
        ) {
            Icon(imageVector = Icons.Filled.CalendarToday, contentDescription = null)
        }

         */
    }
    /*
    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton("Ok")
            negativeButton("Cancel")
        }
    ) {
        datepicker { date ->
            onClearTargetDat()
            onChangeTargetDat(date.format(formatter))
        }
    }

     */
}

private const val TAG = "CreateAlarmScreen"



@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Recurring(
    modifier: Modifier = Modifier,
    areSelected: List<Boolean>,
    onChangeSelected: (Boolean, Int) -> Unit,
    onChangeTargetDat: (String, Int) -> Unit
) {
    val days = listOf("Sun", "Mon", "Tue", "Whe", "Thu", "Fri", "Sat")

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        days.forEachIndexed { index, s ->
                Chip(
                    isSelected = areSelected[index],
                    text = s,
                    onChecked = { onChangeSelected(it, index)},
                    onClick = {
                         onChangeTargetDat(s, index)
                    }
                )

        }
    }

}

@Composable
private fun AlarName(
    modifier: Modifier = Modifier,
    title: TextFieldValue,
    onChangeTitle: (TextFieldValue) -> Unit
) {

    Box(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier.clearFocusOnKeyboardDismiss(),
            value = title,
            onValueChange = onChangeTitle,
            label = { Text("Alarm name") }
        )
    }

}

@Composable
private fun CreateAlarmActions(
    modifier: Modifier = Modifier,
    navigateToAlarmsList: () -> Unit,
    onInsert: () -> Unit,
    onSave: (Alarm) -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        OutlinedButton(
              modifier = Modifier
                  .weight(1f)
                  .padding(5.dp),
              onClick = {
                 navigateToAlarmsList()
              }
          ) {
              Text("Cancel")
        }
        OutlinedButton(
              modifier = Modifier
                  .weight(1f)
                  .padding(5.dp),
              onClick = {
                  onInsert()
                  navigateToAlarmsList()
              }
          ) {
              Text("Save")
        }

    }
}




