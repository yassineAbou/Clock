package com.example.clock.ui.timer

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.clock.R
import com.example.clock.data.manager.TimerState
import com.example.clock.ui.theme.Red100
import com.example.clock.util.checkNumberPicker
import com.example.clock.util.components.BackgroundIndicator
import com.example.clock.util.components.ClockAppBar
import com.example.clock.util.components.ClockButton
import com.example.clock.util.components.NumberPicker
import com.example.clock.util.parseInt
import com.google.accompanist.insets.imePadding
import com.google.accompanist.insets.statusBarsPadding

private const val TAG = "TimerScreen"
@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalTransitionApi::class
)
@Composable
fun TimerScreen(
    modifier: Modifier = Modifier,
    timerViewModel: TimerViewModel = hiltViewModel()
) {
    val timerState: TimerState by timerViewModel.timerState.observeAsState(
        TimerState(
            timeInMillis = 0L, time = "00:00:00", hour = 0, minute = 0, second = 0, progress = 0f,
            isPlaying = false, isDone = true
        )
    )
    var isDone by rememberSaveable { mutableStateOf(timerState.isDone) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarScrollState())
    val isDoneTransition = updateTransition(isDone, label = stringResource(id = R.string.is_done))
    var isStartButtonEnabled by rememberSaveable { mutableStateOf(timerState.timeInMillis != 0L) }

    LaunchedEffect(timerState.isDone, timerState.timeInMillis) {
        isDone = timerState.isDone
        isStartButtonEnabled = timerState.timeInMillis != 0L
    }

    Surface(modifier = modifier) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            val timerPickerPaddingStart = if (maxWidth > 400.dp) dimensionResource(id = com.intuit.sdp.R.dimen._30sdp) else 0.dp
            TimerAppBar(
                modifier = Modifier.statusBarsPadding(),
                scrollBehavior = scrollBehavior
            )

            isDoneTransition.AnimatedVisibility(
                    visible = { isTargetDone -> isTargetDone },
                    enter = scaleIn(
                        animationSpec = tween(
                            durationMillis = 1,
                            easing = FastOutLinearInEasing
                        )
                    ),
                    exit = fadeOut()
                ) {
                    TimerPicker(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = maxHeight / 3,
                                start = timerPickerPaddingStart
                            )
                            ,
                        timerViewModel = timerViewModel,
                        time = timerState.time
                    )
                }

                isDoneTransition.AnimatedVisibility(
                    visible = { isTargetDone -> !isTargetDone },
                    modifier = Modifier.align(Center),
                    enter = fadeIn(),
                    exit = fadeOut(
                        animationSpec = tween(
                            durationMillis = 1,
                            easing = FastOutLinearInEasing
                        )
                    )
                ) {
                        Timer(
                            modifier = Modifier.size(dimensionResource(id = com.intuit.sdp.R.dimen._268sdp)),
                            time = timerState.time,
                            progress = timerState.progress
                        )
                }
            Buttons(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(dimensionResource(id = com.intuit.sdp.R.dimen._7sdp)),
                isPlaying = timerState.isPlaying,
                optionSelected = { timerViewModel.handleCountDownTimer() },
                resetTimer = {
                    timerViewModel.resetTimer()
                },
                isDoneTransition= isDoneTransition,
                isStartButtonEnabled = isStartButtonEnabled
            )
        }

    }
}


@Composable
private fun TimerAppBar(
  modifier: Modifier = Modifier,
  scrollBehavior: TopAppBarScrollBehavior? = null,
 ) {
     ClockAppBar(
      modifier = modifier,
       scrollBehavior = scrollBehavior,
        title = {
          Text(
             text = stringResource(id = R.string.timer),
             style = MaterialTheme.typography.titleLarge,
            )
        }
      )
 }

@Composable
private fun TimerPicker(
   modifier: Modifier = Modifier,
   timerViewModel: TimerViewModel,
   time: String,
) {
    Row(
        modifier = modifier,
    ) {
        val textStyle = MaterialTheme.typography.displayLarge
        var hour by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue(time.substringBefore(":")))
        }
        var minute by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(
                TextFieldValue(time.substringAfter(":").substringBefore(':'))
            )
        }
        var second by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue(time.substringAfterLast(":")))
        }

        NumberPicker(
            modifier = Modifier.weight(1f),
            number = hour,
            labelTimeUnit = stringResource(id = R.string.hours),
            onNumberChange = { value ->
                if (value.text.checkNumberPicker(maxNumber = 99)) {
                    hour = value
                    timerViewModel.setHour(hour.text.parseInt())
                    timerViewModel.setCountDownTimer()
                }
            }
        )

        Text(
            modifier = Modifier.padding(top = 17.dp),
            text = ":",
            style = textStyle,
        )

        NumberPicker(
            modifier = Modifier.weight(1f),
            number = minute,
            labelTimeUnit = stringResource(id = R.string.minutes),
            onNumberChange = { value ->
                if (value.text.checkNumberPicker(maxNumber = 59)) {
                    minute = value
                    timerViewModel.setMinute(minute.text.parseInt())
                    timerViewModel.setCountDownTimer()
                }
            }
        )

        Text(
            modifier = Modifier.padding(top = 17.dp),
            text = ":",
            style = textStyle
        )

        NumberPicker(
            modifier = Modifier.weight(1f),
            number = second,
            labelTimeUnit = stringResource(id = R.string.seconds),
            onNumberChange = { value ->
                if (value.text.checkNumberPicker(59)) {
                    second = value
                    timerViewModel.setSecond(second.text.parseInt())
                    timerViewModel.setCountDownTimer()
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

    Box(modifier = modifier) {
        BackgroundIndicator(
            progress = progress,
            modifier = modifier
                .fillMaxSize()
                .scale(scaleX = 1f, scaleY = 1f),
            strokeWidth = 6.dp
        )
        Text(
            modifier = Modifier.align(Center),
            text = time,
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Light
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun Buttons(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    optionSelected: () -> Unit,
    resetTimer: () -> Unit,
    isStartButtonEnabled: Boolean,
    isDoneTransition: Transition<Boolean>,
) {


    Box(modifier = modifier) {

        isDoneTransition.AnimatedVisibility(
            visible = { isTargetDone -> isTargetDone },
            enter = expandHorizontally(
                animationSpec = tween(
                    durationMillis = 1,
                    easing = FastOutLinearInEasing
                )
            ),
            exit = shrinkHorizontally(
                animationSpec = tween(
                    durationMillis = 1,
                    easing = FastOutLinearInEasing
                )
            )
        ) {

            ClockButton(
                text = stringResource(id = R.string.start),
                onClick = {
                    optionSelected()
                },
                enabled = isStartButtonEnabled

            )
        }

        isDoneTransition.AnimatedVisibility(
            visible = { isTargetDone -> !isTargetDone },
            enter = expandHorizontally(
                animationSpec = tween(
                    durationMillis = 500,
                    easing = LinearOutSlowInEasing,
                )
            ),
            exit = shrinkHorizontally(
                animationSpec = tween(
                    durationMillis = 1,
                    easing = FastOutLinearInEasing
                )
            )
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                if (isPlaying) {
                    ClockButton(
                        text = stringResource(id = R.string.pause),
                        onClick = optionSelected,
                        color = Red100
                    )
                } else {
                    ClockButton(
                        text = stringResource(id = R.string.resume),
                        onClick = optionSelected
                    )
                }
                ClockButton(
                    text = stringResource(id = R.string.cancel),
                    onClick = resetTimer ,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

    }
}




