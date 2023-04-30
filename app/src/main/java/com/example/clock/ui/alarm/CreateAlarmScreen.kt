package com.example.clock.ui.alarm

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.clock.data.model.Alarm
import com.example.clock.ui.theme.Gray300
import com.example.clock.ui.theme.Purple100
import com.example.clock.util.checkDate
import com.example.clock.util.checkNumberPicker
import com.example.clock.util.components.CustomChip
import com.example.clock.util.components.NumberPicker
import com.google.gson.Gson
import com.intuit.sdp.R
import kotlinx.coroutines.launch

/*
@Preview(device = Devices.PIXEL_4_XL)
@Composable
private fun TimerScreenPreview() {
    ClockTheme {
        CreateAlarmScreen(
            createAlarmState = Alarm(),
            alarmActions = object : AlarmActions {},
        )
    }
}

@Preview(device = Devices.TABLET, uiMode = Configuration.ORIENTATION_PORTRAIT, widthDp = 768, heightDp = 1024)
@Composable
private fun TimerScreenDarkPreview() {
    ClockTheme(darkTheme = true) {
        CreateAlarmScreen(
            createAlarmState = Alarm(),
            alarmActions = object : AlarmActions {},
        )
    }
}

 */

@Composable
fun CreateAlarmScreen(
    modifier: Modifier = Modifier,
    alarmCreationState: Alarm,
    alarmActions: AlarmActions,
    navigateToAlarmsList: () -> Unit = {},
) {
    val cardContainerColor by animateColorAsState(targetValue = if (isSystemInDarkTheme()) Gray300 else Purple100)

    Surface(modifier = modifier, color = cardContainerColor) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val alarmPickerPaddingStart =
                if (maxWidth > 400.dp) {
                    dimensionResource(id = R.dimen._60sdp)
                } else {
                    dimensionResource(
                        id = R.dimen._35sdp,
                    )
                }

            AlarmPicker(
                modifier = Modifier
                    .padding(top = maxHeight / 6, start = alarmPickerPaddingStart),
                alarmCreationState = alarmCreationState,
                updateAlarmCreationState = { alarmActions.updateAlarmCreationState(it) },
                cardContainerColor = cardContainerColor,
            )

            CustomizeAlarmEvent(
                modifier = Modifier
                    .align(Center)
                    .background(color = MaterialTheme.colorScheme.surface),
                alarmCreationState = alarmCreationState,
                updateAlarmCreationState = { alarmActions.updateAlarmCreationState(it) },
            )
            Buttons(
                modifier = Modifier
                    .align(BottomCenter),
                navigateToAlarmsList = navigateToAlarmsList,
                save = { alarmActions.save() },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomizeAlarmEvent(
    modifier: Modifier,
    alarmCreationState: Alarm,
    updateAlarmCreationState: (Alarm) -> Unit,
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                Text(
                    modifier = Modifier.padding(dimensionResource(id = R.dimen._8sdp)),
                    text = alarmCreationState.description,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen._3sdp)))
            WeekDays(
                modifier = Modifier.fillMaxWidth(),
                alarmCreationState = alarmCreationState,
                updateAlarmCreationState = updateAlarmCreationState,
            )
            AlarmTitle(
                modifier = Modifier
                    .align(Start)
                    .padding(dimensionResource(id = R.dimen._8sdp)),
                alarmCreationState = alarmCreationState,
                updateAlarmCreationState = updateAlarmCreationState,
            )
        }
    }
}

@Composable
private fun AlarmPicker(
    alarmCreationState: Alarm,
    updateAlarmCreationState: (Alarm) -> Unit,
    modifier: Modifier = Modifier,
    cardContainerColor: androidx.compose.ui.graphics.Color,
) {
    val textStyle = MaterialTheme.typography.displaySmall

    var hours by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(alarmCreationState.hour))
    }
    var minutes by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(alarmCreationState.minute))
    }

    LaunchedEffect(hours, minutes) {
        launch {
            if (alarmCreationState.description.any { char -> char.isDigit() }) {
                updateAlarmCreationState(
                    alarmCreationState.copy(description = alarmCreationState.checkDate()),
                )
            }
        }
    }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            NumberPicker(
                modifier = Modifier.weight(1f),
                number = hours,
                timeUnit = "Hours",
                onNumberChange = { value ->
                    if (value.text.checkNumberPicker(maxNumber = 23)) {
                        hours = value
                        updateAlarmCreationState(alarmCreationState.copy(hour = hours.text))
                    }
                },
                textStyle = textStyle,
                backgroundColor = cardContainerColor,
            )

            Text(
                text = ":",
                style = textStyle,
                modifier = Modifier
                    .weight(0.5f)
                    .padding(top = 20.dp),
            )

            NumberPicker(
                modifier = Modifier.weight(1f),
                number = minutes,
                timeUnit = "Minutes",
                textStyle = textStyle,
                backgroundColor = cardContainerColor,
                onNumberChange = { value ->
                    if (value.text.checkNumberPicker(maxNumber = 59)) {
                        minutes = value
                        updateAlarmCreationState(alarmCreationState.copy(minute = minutes.text))
                    }
                },
            )
        }
    }
}

private const val TAG = "CreateAlarmScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WeekDays(
    modifier: Modifier = Modifier,
    alarmCreationState: Alarm,
    updateAlarmCreationState: (Alarm) -> Unit,
) {
    val daysSelected = remember {
        mutableStateMapOf<String, Boolean>().apply {
            putAll(alarmCreationState.daysSelected)
        }
    }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        daysSelected.forEach { (day, isSelected) ->
            CustomChip(
                isChecked = isSelected,
                text = day,
                onChecked = { isChecked ->
                    daysSelected[day] = isChecked
                    val activeDays = daysSelected.filterValues { it }.keys
                    val description = if (activeDays.isEmpty()) {
                        alarmCreationState.checkDate()
                    } else {
                        activeDays.joinToString(separator = " ")
                    }

                    updateAlarmCreationState(
                        alarmCreationState.copy(
                            description = description,
                            isRecurring = daysSelected.any { it.value },
                            daysSelectedJson = Gson().toJson(daysSelected),
                        ),
                    )
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlarmTitle(
    modifier: Modifier = Modifier,
    alarmCreationState: Alarm,
    updateAlarmCreationState: (Alarm) -> Unit,
) {
    var title by remember { mutableStateOf(alarmCreationState.title) }

    Box(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.75f),
            value = title,
            onValueChange = {
                title = it
                updateAlarmCreationState(alarmCreationState.copy(title = title))
            },
            label = { Text("Alarm name") },
        )
    }
}

@Composable
private fun Buttons(
    modifier: Modifier = Modifier,
    navigateToAlarmsList: () -> Unit,
    save: () -> Unit,
) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            TextButton(
                onClick = {
                    navigateToAlarmsList()
                },
            ) {
                Text(text = stringResource(id = com.example.clock.R.string.cancel))
            }
            TextButton(
                onClick = {
                    save()
                    navigateToAlarmsList()
                },
            ) {
                Text(text = stringResource(id = com.example.clock.R.string.save))
            }
        }
    }
}
