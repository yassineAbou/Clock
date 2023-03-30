package com.example.clock.ui.timer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.clock.R
import com.example.clock.data.model.TimerState
import com.example.clock.ui.theme.Red100
import com.example.clock.util.checkNumberPicker
import com.example.clock.util.components.BackgroundIndicator
import com.example.clock.util.components.ClockAppBar
import com.example.clock.util.components.ClockButton
import com.example.clock.util.components.NumberPicker
import com.example.clock.util.parseInt

/*
@Preview(device = Devices.PIXEL_4_XL)
@Composable
private fun TimerScreenPreview() {
    ClockTheme {
        TimerScreen(
            timerState = TimerState(),
            timerActions = object : TimerActions {},
        )
    }
}

@Preview(device = Devices.TABLET, uiMode = Configuration.ORIENTATION_PORTRAIT, widthDp = 768, heightDp = 1024)
@Composable
private fun TimerScreenDarkPreview() {
    ClockTheme(darkTheme = true) {
        TimerScreen(
            timerState = TimerState(isDone = false, timeText = "00:00:10"),
            timerActions = object : TimerActions {},
        )
    }
}

 */

@OptIn(
    ExperimentalAnimationApi::class,
)
@Composable
fun TimerScreen(
    modifier: Modifier = Modifier,
    timerState: TimerState,
    timerActions: TimerActions,
) {
    val isDoneTransition = updateTransition(timerState.isDone, label = stringResource(id = R.string.is_done))

    Surface(modifier = modifier) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
        ) {
            val timerPickerPaddingStart =
                if (maxWidth > 400.dp) dimensionResource(id = com.intuit.sdp.R.dimen._30sdp) else 0.dp

            TimerAppBar(modifier = Modifier.statusBarsPadding())

            isDoneTransition.AnimatedVisibility(
                visible = { isTimerDone -> isTimerDone },
                enter = scaleIn(
                    animationSpec = tween(
                        durationMillis = 1,
                        easing = FastOutLinearInEasing,
                    ),
                ),
                exit = fadeOut(),
            ) {
                TimerPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = maxHeight / 3,
                            start = timerPickerPaddingStart,
                        ),
                    timerActions = timerActions,
                    timeText = timerState.timeText,
                )
            }

            isDoneTransition.AnimatedVisibility(
                visible = { isTimerDone -> !isTimerDone },
                modifier = Modifier.align(Center),
                enter = fadeIn(),
                exit = fadeOut(
                    animationSpec = tween(
                        durationMillis = 1,
                        easing = FastOutLinearInEasing,
                    ),
                ),
            ) {
                Timer(
                    modifier = Modifier.size(dimensionResource(id = com.intuit.sdp.R.dimen._268sdp)),
                    timeText = timerState.timeText,
                    progress = timerState.progress,
                )
            }

            Buttons(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(dimensionResource(id = com.intuit.sdp.R.dimen._7sdp)),
                timerState = timerState,
                timerActions = timerActions,
                isDoneTransition = isDoneTransition,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimerAppBar(modifier: Modifier = Modifier) {
    ClockAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(id = R.string.timer),
                style = MaterialTheme.typography.titleLarge,
            )
        },
    )
}

@Composable
private fun TimerPicker(
    modifier: Modifier = Modifier,
    timerActions: TimerActions,
    timeText: String,
) {
    Row(
        modifier = modifier,
    ) {
        val textStyle = MaterialTheme.typography.displayLarge
        var hour by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue(timeText.substringBefore(":")))
        }
        var minute by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(
                TextFieldValue(timeText.substringAfter(":").substringBefore(':')),
            )
        }
        var second by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue(timeText.substringAfterLast(":")))
        }

        NumberPicker(
            modifier = Modifier.weight(1f),
            number = hour,
            timeUnit = stringResource(id = R.string.hours),
            onNumberChange = { value ->
                if (value.text.checkNumberPicker(maxNumber = 99)) {
                    hour = value
                    timerActions.setHour(hour.text.parseInt())
                    timerActions.setCountDownTimer()
                }
            },
        )

        Text(
            modifier = Modifier.padding(top = 17.dp),
            text = ":",
            style = textStyle,
        )

        NumberPicker(
            modifier = Modifier.weight(1f),
            number = minute,
            timeUnit = stringResource(id = R.string.minutes),
            onNumberChange = { value ->
                if (value.text.checkNumberPicker(maxNumber = 59)) {
                    minute = value
                    timerActions.setMinute(minute.text.parseInt())
                    timerActions.setCountDownTimer()
                }
            },
        )

        Text(
            modifier = Modifier.padding(top = 17.dp),
            text = ":",
            style = textStyle,
        )

        NumberPicker(
            modifier = Modifier.weight(1f),
            number = second,
            timeUnit = stringResource(id = R.string.seconds),
            onNumberChange = { value ->
                if (value.text.checkNumberPicker(59)) {
                    second = value
                    timerActions.setSecond(second.text.parseInt())
                    timerActions.setCountDownTimer()
                }
            },
        )
    }
}

@Composable
private fun Timer(
    modifier: Modifier = Modifier,
    timeText: String,
    progress: Float,
) {
    Box(modifier = modifier) {
        BackgroundIndicator(
            progress = progress,
            modifier = modifier
                .fillMaxSize()
                .scale(scaleX = 1f, scaleY = 1f),
            strokeWidth = 6.dp,
        )
        Text(
            modifier = Modifier.align(Center),
            text = timeText,
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Light,
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun Buttons(
    modifier: Modifier = Modifier,
    timerState: TimerState,
    timerActions: TimerActions,
    isDoneTransition: Transition<Boolean>,
) {
    Box(modifier = modifier) {
        isDoneTransition.AnimatedVisibility(
            visible = { isTimerDone -> isTimerDone },
            enter = expandHorizontally(
                animationSpec = tween(
                    durationMillis = 1,
                    easing = FastOutLinearInEasing,
                ),
            ),
            exit = shrinkHorizontally(
                animationSpec = tween(
                    durationMillis = 1,
                    easing = FastOutLinearInEasing,
                ),
            ),
        ) {
            ClockButton(
                text = stringResource(id = R.string.start),
                onClick = { timerActions.start() },
                enabled = timerState.timeInMillis != 0L,

            )
        }

        isDoneTransition.AnimatedVisibility(
            visible = { isTimerDone -> !isTimerDone },
            enter = expandHorizontally(
                animationSpec = tween(
                    durationMillis = 500,
                    easing = LinearOutSlowInEasing,
                ),
            ),
            exit = shrinkHorizontally(
                animationSpec = tween(
                    durationMillis = 1,
                    easing = FastOutLinearInEasing,
                ),
            ),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp),
            ) {
                if (timerState.isPlaying) {
                    ClockButton(
                        text = stringResource(id = R.string.pause),
                        onClick = { timerActions.pause() },
                        color = Red100,
                    )
                } else {
                    ClockButton(
                        text = stringResource(id = R.string.resume),
                        onClick = { timerActions.start() },
                    )
                }
                ClockButton(
                    text = stringResource(id = R.string.cancel),
                    onClick = { timerActions.reset() },
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}
