package com.example.clock.ui.stopwatch

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.clock.R
import com.example.clock.data.model.StopwatchState
import com.example.clock.ui.theme.Red100
import com.example.clock.util.components.ClockAppBar
import com.example.clock.util.components.ClockButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/*
@Preview(device = Devices.TABLET, uiMode = Configuration.ORIENTATION_PORTRAIT, widthDp = 768, heightDp = 1024)
@Composable
private fun StopwatchScreenPreview() {
    val lapTimes = mutableListOf<String>().apply {
        for (i in 0..20) {
            add("00:00:${String.format("%02d", i)}")
        }
    }.toList()
    ClockTheme {
        StopwatchScreen(
            stopwatchState = StopwatchState(second = "09", isPlaying = false, isReset = false),
            stopwatchActions = object : StopwatchActions {},
            lapTimes = lapTimes,
        )
    }
}

@Preview(device = Devices.PIXEL_4_XL)
@Composable
private fun StopwatchScreenDarkPreview() {
    ClockTheme(darkTheme = true) {
        StopwatchScreen(
            stopwatchState = StopwatchState(),
            stopwatchActions = object : StopwatchActions {},
            lapTimes = listOf(""),
        )
    }
}

 */


@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
fun StopwatchScreen(
    modifier: Modifier = Modifier,
    stopwatchState: StopwatchState,
    stopwatchActions: StopwatchActions,
    lapTimes: List<String>,
) {
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        if (lapTimes.isNotEmpty()) {
            coroutineScope.launch {
                scrollState.animateScrollToItem(lapTimes.lastIndex)
            }
        }
        onDispose {}
    }

    Surface(modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize()) {
            StopwatchScreenAppBar(
                modifier = Modifier.statusBarsPadding(),
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Stopwatch(
                    modifier = Modifier.padding(
                        WindowInsets.statusBars.add(
                            WindowInsets(
                                top = dimensionResource(id = com.intuit.sdp.R.dimen._90sdp),
                            ),
                        ).asPaddingValues(),
                    ),
                    hours = stopwatchState.hour,
                    minutes = stopwatchState.minute,
                    seconds = stopwatchState.second,
                )
                AnimatedVisibility(visible = lapTimes.isNotEmpty()) {
                    LineSeparator(modifier = Modifier.padding(dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)))
                }
                LapTimes(
                    modifier = Modifier
                        .fillMaxWidth(),
                    lapTimes = lapTimes,
                    scrollState = scrollState,
                )
            }
            Buttons(
                stopwatchActions = stopwatchActions,
                isPlaying = stopwatchState.isPlaying,
                scrollState = scrollState,
                isReset = stopwatchState.isReset,
                modifier = Modifier
                    .padding(dimensionResource(id = com.intuit.sdp.R.dimen._7sdp))
                    .align(Alignment.BottomCenter),
                coroutineScope = coroutineScope,
                lapTimes = lapTimes,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StopwatchScreenAppBar(
    modifier: Modifier = Modifier,
) {
    ClockAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(id = R.string.stopwatch),
                style = MaterialTheme.typography.titleLarge,
            )
        },
    )
}

@Composable
private fun Stopwatch(
    hours: String,
    minutes: String,
    seconds: String,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        TimeUnitDisplay(unit = hours)
        TimeUnitDisplay(unit = ":")
        TimeUnitDisplay(unit = minutes)
        TimeUnitDisplay(unit = ":")
        TimeUnitDisplay(unit = seconds)
    }
}

@Composable
private fun TimeUnitDisplay(unit: String) {
    Text(
        text = unit,
        style = MaterialTheme.typography.displayLarge,
        fontWeight = FontWeight.Light,
    )
}

@Composable
private fun LineSeparator(modifier: Modifier = Modifier) {
    Divider(
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
    )
}

@Composable
private fun LapTimes(
    lapTimes: List<String>,
    scrollState: LazyListState,
    modifier: Modifier = Modifier,
) {
    val contentPadding = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)
    val verticalPadding = dimensionResource(id = com.intuit.sdp.R.dimen._7sdp)

    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(contentPadding),
        reverseLayout = true,
        state = scrollState,
    ) {
        itemsIndexed(lapTimes) { index, item ->
            LapDuration(
                item = item,
                index = index,
                modifier = Modifier
                    .padding(vertical = verticalPadding)
                    .fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun LapDuration(item: String, index: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Text(
            text = "$index",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
        )
        Text(
            text = item,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f),
        )
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun Buttons(
    stopwatchActions: StopwatchActions,
    isPlaying: Boolean,
    scrollState: LazyListState,
    isReset: Boolean,
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope,
    lapTimes: List<String>,
) {
    val transition = updateTransition(isReset, label = stringResource(id = R.string.is_reset))
    val startButtonClicked = rememberSaveable { mutableStateOf(false) }
    Box(
       modifier = modifier,
    ) {
        transition.AnimatedVisibility(
            visible = { isStopwatchReset -> isStopwatchReset },
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
            if (!startButtonClicked.value) {
                ClockButton(
                    onClick = {
                        startButtonClicked.value = true
                        stopwatchActions.start()
                    },
                    text = stringResource(id = R.string.start),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
        transition.AnimatedVisibility(
            visible = { isStopwatchReset -> !isStopwatchReset },
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
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                if (isPlaying) {
                    ClockButton(
                        text = stringResource(id = R.string.stop),
                        onClick = {
                            stopwatchActions.stop()
                            startButtonClicked.value = false
                        },
                        color = Red100,
                    )
                    ClockButton(
                        text = stringResource(id = R.string.lap),
                        onClick = {
                            startButtonClicked.value = false
                            stopwatchActions.lap()
                            coroutineScope.launch {
                                scrollState.animateScrollToItem(index = lapTimes.lastIndex)
                            }
                        },
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                } else {
                    ClockButton(
                        text = stringResource(id = R.string.resume),
                        onClick = {
                            stopwatchActions.start()
                            startButtonClicked.value = false
                        },
                        color = MaterialTheme.colorScheme.primary,
                    )
                    ClockButton(
                        text = stringResource(id = R.string.reset),
                        onClick = {
                            startButtonClicked.value = false
                            stopwatchActions.reset()
                            stopwatchActions.clear()
                        },
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}
