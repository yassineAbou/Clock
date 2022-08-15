package com.example.clock.stopwatch

import androidx.compose.animation.*
import androidx.compose.animation.core.ExperimentalTransitionApi
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.clock.components.ClockButton
import com.example.clock.ui.theme.Black100
import com.example.clock.ui.theme.ClockTheme
import com.example.clock.ui.theme.Red100
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime


@Preview(name = "StopwatchScreen")
@OptIn(kotlin.time.ExperimentalTime::class)
@Composable
fun DefaultPreview() {
    ClockTheme {
        StopwatchScreen(
            stopwatchState = StopwatchState(
                seconds = "00",
                minutes = "00",
                hours = "00",
                isPlaying = false,
                isZero = true
            ),
            stopwatchActions = object : StopwatchScreenActions {
                override fun onStart() {}
                override fun onLap() {}
                override fun onClear() {}
                override fun onPause() {}
                override fun onStop() {}

            },
            lapItems = initialLapItems
        )
    }
}




@Preview(showBackground = true)
@Composable
fun LapItemPreview() {
    ClockTheme {
        LapItem(lapItem = Lap("00:00:00"), index = 1)
    }
}

private const val TAG = "StopwatchScreen"

@OptIn(ExperimentalAnimationApi::class, ExperimentalTime::class, ExperimentalTransitionApi::class)
@Composable
fun StopwatchScreen(
    modifier: Modifier = Modifier,
    stopwatchState: StopwatchState,
    stopwatchActions: StopwatchScreenActions,
    lapItems: List<Lap>,

) {
    var lapHeaderLine by rememberSaveable { mutableStateOf(lapItems.isNotEmpty()) }
    val scrollState = rememberLazyListState()
    var isStartButtonVisible by rememberSaveable { mutableStateOf(stopwatchState.isZero) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(stopwatchState.isZero, lapItems.isEmpty()) {
        if (stopwatchState.isZero) {
            isStartButtonVisible = true
        }
            lapHeaderLine = lapItems.isNotEmpty()

    }


    Surface(modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                        Spacer(modifier = Modifier.height(122.dp))
                        StopWatchTime(
                            hours = stopwatchState.hours,
                            minutes = stopwatchState.minutes,
                            seconds = stopwatchState.seconds
                        )
                        Spacer(modifier = Modifier.height(55.dp))
                        AnimatedVisibility(visible = lapHeaderLine) {
                            DividerItem()
                        }

                        LapsItems(
                            lapsItems = lapItems,
                            scrollState = scrollState,
                        )
                        Buttons(
                            isPlaying = stopwatchState.isPlaying,
                            onPause = { stopwatchActions.onPause() },
                            onStart = { stopwatchActions.onStart() },
                            onStop = { stopwatchActions.onStop() },
                            onLap = { stopwatchActions.onLap() },
                            onClear = { stopwatchActions.onClear() },
                            scrollState = scrollState,
                            scope = scope,
                            lapsItems = lapItems,
                            onChangeLapHeaderLine = {
                                lapHeaderLine = it
                            },
                            isDone = isStartButtonVisible,
                            onChangeIsDone = {
                                isStartButtonVisible = it
                            }
                        )

                    }
                }

        }




}

@Composable
private fun StopWatchTime(
    hours: String,
    minutes: String,
    seconds: String,
) {
    Row {
        TextTime(timeUnit = hours)
        TextTime(timeUnit = ":")
        TextTime(timeUnit = minutes)
        TextTime(timeUnit = ":")
        TextTime(timeUnit = seconds)
    }
}




@Composable
private fun DividerItem() {
    Divider(
        modifier = Modifier
            .padding(start = 45.dp, end = 45.dp),
        color =  MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    )
}

@Composable
private fun LapsItems(
    lapsItems: List<Lap>,
    scrollState: LazyListState,
) {

    LazyColumn (
        modifier = Modifier
            .height(300.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = 5.dp),
        reverseLayout = true,
        state = scrollState
        ) {
        itemsIndexed(lapsItems) { index, item ->
            LapItem(lapItem = item, index = index)
        }

    }

}

@Composable
fun LapItem(lapItem: Lap, index: Int) {
    Row(
     modifier = Modifier.padding(vertical = 8.dp)
    ) {
            Text(
                text = "$index",
                modifier = Modifier.padding(end = 40.dp),
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray
                )
            Text(
                text = lapItem.currentTime,
                style = MaterialTheme.typography.titleMedium,
                color = Black100
            )
        }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun Buttons(
    isPlaying: Boolean,
    onPause: () -> Unit,
    onStop: () -> Unit,
    onStart: () -> Unit,
    onLap: () -> Unit,
    onClear: () -> Unit,
    scrollState: LazyListState,
    scope: CoroutineScope,
    lapsItems: List<Lap>,
    onChangeLapHeaderLine: (Boolean) -> Unit,
    isDone: Boolean,
    onChangeIsDone: (Boolean) -> Unit
    ) {

    val transition = updateTransition(isDone)
        Box {

            transition.AnimatedVisibility(
                visible = { targetSelected -> targetSelected }
            ) {
                ClockButton(
                    onClick = {
                        onStart()
                        onChangeIsDone(false)
                        //onChangeLapHeaderLine(true)
                    },
                    textButton = "Start",
                    color = MaterialTheme.colorScheme.primary
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
                            textButton = "Stop",
                            onClick = onPause,
                            color = Red100
                        )
                        ClockButton(
                            textButton = "Lap",
                            onClick = {
                                onLap()
                                scope.launch {
                                    scrollState.animateScrollToItem(index = lapsItems.lastIndex)
                                }
                            },
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    } else {
                        ClockButton(
                            textButton = "Resume",
                            onClick = onStart,
                            color = MaterialTheme.colorScheme.primary
                        )
                        ClockButton(
                            textButton = "Reset",
                            onClick = {
                                onStop()
                                onClear()
                                onChangeIsDone(true)
                               // onChangeLapHeaderLine(false)
                            },
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

            }
        }
}

@Composable
private fun TextTime(timeUnit: String) {
    Text(
        text = timeUnit,
        style = MaterialTheme.typography.displayLarge,
        fontWeight = FontWeight.Light
    )
}

val initialLapItems = listOf(
    Lap("00:00:00"),
    Lap("00:00:01"),
    Lap("00:00:02"),
    Lap("00:00:03"),
)


