package com.example.clock.ui.alarm

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.clock.data.model.Alarm
import com.example.clock.util.checkDate
import com.example.clock.util.checkNumberPicker
import com.example.clock.util.components.CustomChip
import com.example.clock.util.components.NumberPicker
import com.intuit.sdp.R
import java.util.regex.Pattern

@Composable
fun CreateAlarmScreen(
    modifier: Modifier = Modifier,
    alarmViewModel: AlarmViewModel,
    navigateToAlarmsList: () -> Unit = {},
) {
    val cardContainerColor = MaterialTheme.colorScheme.surface
    val createAlarmState = alarmViewModel.createAlarmState
    val descriptionListState = remember { mutableStateListOf("") }

    LaunchedEffect(Unit) {
        val splitDescriptionToList =
            Pattern.compile(" ").split(createAlarmState.description).toList()
        splitDescriptionToList.forEach { descriptionListState.add(it) }
    }

    SideEffect {
        alarmViewModel.changeCreateAlarmState(
            createAlarmState.copy(description = descriptionListState.joinToString(" ")),
        )
    }

    Surface(
        modifier = modifier,
        color = cardContainerColor
    ) {
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
                cardContainerColor = cardContainerColor,
                createAlarmState = createAlarmState,
                changeCreateAlarmState = { alarmViewModel.changeCreateAlarmState(it) },
                descriptionListState = descriptionListState,
                addToDescriptionList = { descriptionListState.add(it) },
                clearDescriptionList = { descriptionListState.clear() },
            )

            AlarmCustomActions(
                modifier = Modifier
                    .align(Center)
                    .background(color = MaterialTheme.colorScheme.surface),
                descriptionListState = descriptionListState,
                createAlarmState = createAlarmState,
                changeCreateAlarmState = { alarmViewModel.changeCreateAlarmState(it) },
                addToDescriptionList = { descriptionListState.add(it) },
                removeFromDescriptionList = { descriptionListState.remove(it) },
                clearDescriptionList = { descriptionListState.removeAll { it.contains("[0-9]".toRegex()) } },
            )
            Buttons(
                modifier = Modifier
                    .align(BottomCenter)
                    .navigationBarsPadding(),
                navigateToAlarmsList = navigateToAlarmsList,
                saveAlarm = { alarmViewModel.saveAlarm() },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlarmCustomActions(
    descriptionListState: List<String>,
    modifier: Modifier,
    createAlarmState: Alarm,
    changeCreateAlarmState: (Alarm) -> Unit,
    addToDescriptionList: (String) -> Unit,
    removeFromDescriptionList: (String) -> Unit,
    clearDescriptionList: () -> Unit,
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                Text(
                    modifier = Modifier.padding(dimensionResource(id = R.dimen._8sdp)),
                    text = descriptionListState.joinToString(" "),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen._3sdp)))
            Recurring(
                modifier = Modifier.fillMaxWidth(),
                createAlarmState = createAlarmState,
                descriptionListState = descriptionListState,
                changeCreateAlarmState = changeCreateAlarmState,
                addToDescriptionList = addToDescriptionList,
                removeFromDescriptionList = removeFromDescriptionList,
                clearDescriptionList = clearDescriptionList,
            )
            AlarmTitle(
                modifier = Modifier
                    .align(Start)
                    .padding(dimensionResource(id = R.dimen._8sdp)),
                createAlarmState = createAlarmState,
                changeCreateAlarmState = changeCreateAlarmState,
            )
        }
    }
}

@Composable
private fun AlarmPicker(
    createAlarmState: Alarm,
    changeCreateAlarmState: (Alarm) -> Unit,
    modifier: Modifier = Modifier,
    cardContainerColor: Color,
    descriptionListState: List<String>,
    clearDescriptionList: () -> Unit,
    addToDescriptionList: (String) -> Unit,
) {
    val textStyle = MaterialTheme.typography.displaySmall

    var hours by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(createAlarmState.hour))
    }
    var minutes by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(createAlarmState.minute))
    }

    LaunchedEffect(createAlarmState.hour, createAlarmState.minute) {
        if (descriptionListState.any { it.contains("[0-9]".toRegex()) }) {
            clearDescriptionList()
            addToDescriptionList(createAlarmState.checkDate())
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
                        changeCreateAlarmState(createAlarmState.copy(hour = hours.text))
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
                onNumberChange = { value ->
                    if (value.text.checkNumberPicker(maxNumber = 59)) {
                        minutes = value
                        changeCreateAlarmState(createAlarmState.copy(minute = minutes.text))
                    }
                },
                backgroundColor = cardContainerColor,
            )
        }
    }
}

private const val TAG = "CreateAlarmScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Recurring(
    descriptionListState: List<String>,
    addToDescriptionList: (String) -> Unit,
    removeFromDescriptionList: (String) -> Unit,
    clearDescriptionList: () -> Unit,
    modifier: Modifier = Modifier,
    createAlarmState: Alarm,
    changeCreateAlarmState: (Alarm) -> Unit,
) {
    val isSelectedByDay = remember {
        mutableStateMapOf(
            "Sun" to createAlarmState.isSunday,
            "Mon" to createAlarmState.isMonday,
            "Tue" to createAlarmState.isTuesday,
            "Whe" to createAlarmState.isWednesday,
            "Thu" to createAlarmState.isThursday,
            "Fri" to createAlarmState.isFriday,
            "Sat" to createAlarmState.isSaturday,
        )
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        isSelectedByDay.forEach { (day, isSelected) ->
            CustomChip(
                isSelected = isSelected,
                text = day,
                onChecked = { isChecked ->
                    isSelectedByDay[day] = isChecked

                    isSelectedByDay.apply {
                        changeCreateAlarmState(
                            createAlarmState.copy(
                                isRecurring = isSelectedByDay.any { it.value },
                                isSunday = getOrDefault("Sun", createAlarmState.isSunday),
                                isMonday = getOrDefault("Mon", createAlarmState.isMonday),
                                isTuesday = getOrDefault("Tue", createAlarmState.isTuesday),
                                isWednesday = getOrDefault("Whe", createAlarmState.isWednesday),
                                isThursday = getOrDefault("Thu", createAlarmState.isThursday),
                                isFriday = getOrDefault("Fri", createAlarmState.isFriday),
                                isSaturday = getOrDefault("Sat", createAlarmState.isSaturday),
                            ),
                        )

                        if (isSelectedByDay.all { !it.value } && descriptionListState.all {
                                !it.contains(
                                    "[0-9]".toRegex(),
                                )
                            }
                        ) {
                            addToDescriptionList(createAlarmState.checkDate())
                        }

                        when {
                            isChecked && descriptionListState.any { it != day } -> {
                                clearDescriptionList()
                                addToDescriptionList(day)
                            }
                            !isChecked -> {
                                removeFromDescriptionList(day)
                            }
                        }
                    }
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlarmTitle(
    modifier: Modifier = Modifier,
    createAlarmState: Alarm,
    changeCreateAlarmState: (Alarm) -> Unit,
) {
    var title by remember { mutableStateOf(createAlarmState.title) }

    Box(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.75f),
            value = title,
            onValueChange = {
                title = it
                changeCreateAlarmState(createAlarmState.copy(title = title))
            },
            label = { Text("Alarm name") },
        )
    }
}

@Composable
private fun Buttons(
    modifier: Modifier = Modifier,
    navigateToAlarmsList: () -> Unit,
    saveAlarm: () -> Unit,
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
                    saveAlarm()
                    navigateToAlarmsList()
                },
            ) {
                Text(text = stringResource(id = com.example.clock.R.string.save))
            }
        }
    }
}
