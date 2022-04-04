package com.example.clock.timer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clock.components.ClockButton
import com.example.clock.ui.theme.ClockTheme
import com.example.clock.ui.theme.Red100
import com.example.clock.util.parseLong

private const val TAG = "TimerScreen"
@Preview(name = "TimerScreen")
@Composable
private fun TimerScreenPreview() {
    ClockTheme {
        TimerScreen()
    }
}
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TimerScreen(
    modifier: Modifier = Modifier,
    timerViewModel: TimerViewModel = viewModel()
) {
    var timerPickerVisible by remember { mutableStateOf(true) }
    val transition = updateTransition(timerPickerVisible)

    Surface {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(150.dp))
                transition.AnimatedVisibility(
                    visible = { targetSelected -> targetSelected }
                ) {
                    UnitsOfTime(modifier = Modifier.fillMaxWidth())
                }
                transition.AnimatedVisibility(
                    visible = { targetSelected -> targetSelected }
                ) {
                    TimerPicker(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp, bottom = 140.dp),
                        setHours = {
                            timerViewModel.setTHours(it)
                        },
                        setMinutes = {
                            timerViewModel.setMinutes(it)
                        },
                        setSeconds = {
                            timerViewModel.setSeconds(it)
                        },
                        setTime = { timerViewModel.setTime() },
                    )
                }
                transition.AnimatedVisibility(
                    visible = { targetSelected -> !targetSelected }
                ) {
                    Timer(
                        modifier = Modifier
                            .size(300.dp),
                        time = timerViewModel.time.value,
                        progress =timerViewModel.progress.value
                    )
                }
                     Spacer(modifier = Modifier.height(80.dp))
                     TimerButtons(
                            modifier = Modifier.align(CenterHorizontally),
                            isPlaying = timerViewModel.isPlaying.value,
                            optionSelected = {
                                timerViewModel.handleCountDownTimer()
                            },
                            setTimerPickerVisibility = {
                                timerPickerVisible = it
                            },
                            cancelTimer =  { timerViewModel.pauseTimer() }
                        )
                    }
            }
        }

    }




@Composable
private fun UnitsOfTime( modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(text = "Hours")
        Text(text = "Minutes")
        Text(text = "Seconds")

    }
}

@Composable
private fun TimerPicker(
    modifier: Modifier = Modifier,
    setHours: (Long) -> Unit,
    setMinutes: (Long) -> Unit,
    setSeconds: (Long) -> Unit,
    setTime: () -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val textStyle = MaterialTheme.typography.displayLarge
        val background = MaterialTheme.colorScheme.surface
        var hours by remember { mutableStateOf("00") }
        var minutes by remember { mutableStateOf("00") }
        var seconds by remember { mutableStateOf("00") }
        val colors = TextFieldDefaults.textFieldColors(
            backgroundColor = background,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
        val number = KeyboardOptions(keyboardType = KeyboardType.Number)

        Surface(modifier = Modifier.weight(1f)) {
            TextField(
                value = hours,
                onValueChange = { value ->
                    if (value.length <= 2) {
                        hours = value.filter { it.isDigit() }
                        setHours(parseLong(hours))
                    }
                },
                textStyle = textStyle,
                keyboardOptions = number,
                colors = colors
            )
        }
        Text(
            text = ":",
            style = textStyle,
            modifier = Modifier.padding(top = 10.dp)
        )
        Surface(modifier = Modifier.weight(1f)) {
            TextField(
                value = minutes,
                onValueChange = { value ->
                    if (value.length <= 2) {
                        minutes = value.filter { it.isDigit() }
                        setMinutes(parseLong(minutes))
                        setTime()
                    }
                },
                textStyle = textStyle,
                keyboardOptions = number,
                colors = colors
            )
        }

        Text(
            text = ":",
            style = textStyle,
            modifier = Modifier.padding(top = 10.dp)
        )
        Surface(modifier = Modifier.weight(1f)) {
            TextField(
                value = seconds,
                onValueChange = { value ->
                    if (value.length <= 2) {
                        seconds = value.filter { it.isDigit() }
                        setSeconds(parseLong(seconds))
                    }
                },
                textStyle = textStyle,
                keyboardOptions = number,
                colors = colors
            )
        }
    }
}





@Composable
private fun Timer(
    modifier: Modifier = Modifier,
    time: String,
    progress: Float
) {

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        BackgroundIndicator(
            progress = progress,
            modifier = modifier
                .fillMaxSize()
                .scale(scaleX = -1f, scaleY = 1f),
            strokeWidth = 6.dp,
        )
        Text(
            text = time,
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Light
        )

    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun TimerButtons(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    optionSelected: () -> Unit,
    setTimerPickerVisibility: (Boolean) -> Unit,
    cancelTimer: () -> Unit
) {
    var startVisible by remember { mutableStateOf(true) }
    val transition = updateTransition(startVisible)

    Box(modifier = modifier) {

        transition.AnimatedVisibility(
            visible = { targetSelected -> targetSelected }
        ) {
            ClockButton(
                textButton = "Start",
                onClick = {
                    setTimerPickerVisibility(false)
                    optionSelected()
                    startVisible = false
                }
            )
        }

        transition.AnimatedVisibility(
            visible = { targetSelected -> !targetSelected }
        ) {
            Row (
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ){
                if (isPlaying) {
                    ClockButton(
                        textButton = "Pause",
                        onClick =  optionSelected,
                        color = Red100
                    )
                } else {
                    ClockButton(
                        textButton = "Resume",
                        onClick = optionSelected
                    )
                }
                ClockButton(
                    textButton = "Cancel",
                    onClick =  {
                        startVisible = true
                        cancelTimer()
                        setTimerPickerVisibility(true)
                    },
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

    }

}

