package com.example.clock.timer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.ExperimentalTransitionApi
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.clock.components.ClockAppBar
import com.example.clock.components.ClockButton
import com.example.clock.components.NumberPicker
import com.example.clock.ui.theme.ClockTheme
import com.example.clock.ui.theme.Red100
import com.example.clock.util.checkTimerInput
import com.example.clock.util.parseLong
import com.google.accompanist.insets.statusBarsPadding

private const val TAG = "TimerScreen"

@Preview(name = "TimerScreen")
@Composable
private fun TimerScreenPreview() {
    ClockTheme {
           TimerScreen(
            timerState = TimerState(
                timeInLong = 0L,
                time = "00:00:00",
                hours = 0L,
                minutes = 0L,
                seconds = 0L,
                progress = 1.00F,
                isPlaying = false,
                isDone = false
            ),
            timerScreenActions = object: TimerScreenActions {
                override fun setHours(hours: Long) {}
                override fun setMinutes(minutes: Long) {}
                override fun setSeconds(seconds: Long) {}
                override fun setTime() {}
                override fun handleCountDownTimer() {}
                override fun resetTimer() {}

            }
        )

    }
}


@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalTransitionApi::class
)
@Composable
fun TimerScreen(
    modifier: Modifier = Modifier,
    timerState: TimerState,
    timerScreenActions: TimerScreenActions
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarScrollState())
    var isTimerPickerVisible by rememberSaveable { mutableStateOf(true) }
    val isTimerPickerVisibleTransition = updateTransition(isTimerPickerVisible)
    var isStartVisible by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(timerState.isDone) {
         if (timerState.isDone) {
             isTimerPickerVisible = true
             timerScreenActions.resetTimer()
             isStartVisible = true
         }

    }

    Surface(modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                horizontalAlignment = CenterHorizontally
            ) {
                TimerTopAppBar(
                    modifier = Modifier.statusBarsPadding(),
                    scrollBehavior = scrollBehavior
                )

                Spacer(modifier = Modifier.height(95.dp))

                isTimerPickerVisibleTransition.AnimatedVisibility(
                    visible = { targetSelected -> targetSelected }
                ) {
                    UnitsOfTime(modifier = Modifier.fillMaxWidth())
                }

                isTimerPickerVisibleTransition.AnimatedVisibility(
                    visible = { targetSelected -> targetSelected }
                ) {
                    TimerPicker(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp, bottom = 132.dp),
                        setHours = {
                            timerScreenActions.setHours(it)
                        },
                        setMinutes = {
                            timerScreenActions.setMinutes(it)
                        },
                        setSeconds = {
                            timerScreenActions.setSeconds(it)
                        },
                        setTime = { timerScreenActions.setTime() },
                        time = timerState.time
                    )
                }

                isTimerPickerVisibleTransition.AnimatedVisibility(
                    visible = { targetSelected -> !targetSelected }
                ) {
                    Timer(
                        modifier = Modifier
                            .size(300.dp),
                        time = timerState.time,
                        progress = timerState.progress
                    )
                }
                Spacer(modifier = Modifier.height(72.dp))

                TimerButtons(
                    modifier = modifier,
                    isStartVisible = isStartVisible,
                    isPlaying = timerState.isPlaying,
                    optionSelected = { timerScreenActions.handleCountDownTimer() },
                    setTimerPickerVisibility = { isTimerPickerVisible = it } ,
                    resetTimer = { timerScreenActions.resetTimer() },
                    setStartVisible = {isStartVisible = it},
                    time = timerState.time
                )

            }
        }

    }
}


@Composable
private fun TimerTopAppBar(
 modifier: Modifier = Modifier,
  scrollBehavior: TopAppBarScrollBehavior? = null,
 ) {
     ClockAppBar(
      modifier = modifier,
       scrollBehavior = scrollBehavior,
        title = {
          Text(
             text = "Timer",
             style = MaterialTheme.typography.titleLarge,
            )
        }
      )
 }


@Composable
private fun UnitsOfTime(modifier: Modifier = Modifier) {
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
   time: String,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly

    ) {

        val textStyle = MaterialTheme.typography.displayLarge
        var hours by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue(time.substringBefore(":")))
        }
        var minutes by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(
                TextFieldValue(time.substringAfter(":").substringBefore(':'))
            )
        }
        var seconds by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue(time.substringAfterLast(":")))
        }

        NumberPicker(
            modifier = Modifier.weight(1f),
            number = hours,
            onNumberChange = { value ->
                if (value.text.checkTimerInput(99)) {
                    hours = value
                    setHours(hours.text.parseLong())
                    setTime()
                }
            }
        )

        Text(
            text = ":",
            style = textStyle,
            modifier = Modifier.padding(top = 10.dp)
        )

        NumberPicker(
            modifier = Modifier.weight(1f),
            number = minutes,
            onNumberChange = { value ->
                if (value.text.checkTimerInput(59)) {
                    minutes = value
                    setMinutes(minutes.text.parseLong())
                    setTime()
                }
            }
        )

        Text(
            text = ":",
            style = textStyle,
            modifier = Modifier.padding(top = 10.dp)
        )

        NumberPicker(
            modifier = Modifier.weight(1f),
            number = seconds,
            onNumberChange = { value ->
                if (value.text.checkTimerInput(59)) {
                    seconds = value
                    setSeconds(seconds.text.parseLong())
                    setTime()
                }
            }
        )

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
    isStartVisible: Boolean,
    optionSelected: () -> Unit,
    setTimerPickerVisibility: (Boolean) -> Unit,
    resetTimer: () -> Unit,
    time: String,
    setStartVisible: (Boolean) -> Unit,
) {

    val isStartVisibleTransition = updateTransition(isStartVisible)

    Box(modifier = modifier) {

        isStartVisibleTransition.AnimatedVisibility(
            visible = { targetSelected -> targetSelected }
        ) {

            ClockButton(
                textButton = "Start",
                onClick = {
                    setTimerPickerVisibility(false)
                    optionSelected()
                    setStartVisible(false)
                },
                enabled = true
            )
        }

        isStartVisibleTransition.AnimatedVisibility(
            visible = { targetSelected -> !targetSelected }
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                if (isPlaying) {
                    ClockButton(
                        textButton = "Pause",
                        onClick = optionSelected,
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
                    onClick = {
                        setTimerPickerVisibility(true)
                        resetTimer()
                        setStartVisible(true)
                    },
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

    }
}




