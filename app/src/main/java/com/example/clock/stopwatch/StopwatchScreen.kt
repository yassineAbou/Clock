package com.example.clock.stopwatch

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clock.ui.theme.Black100
import com.example.clock.ui.theme.ClockTheme
import com.example.clock.ui.theme.Red100
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment.Companion as Alignment1


// TODO: Design layout
// TODO: Create logic
// TODO: setup foreground service



@Preview(name = "StopwatchScreen")
@OptIn(kotlin.time.ExperimentalTime::class)
@Composable
fun DefaultPreview() {
    ClockTheme {
        StopwatchScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun LapItemPreview() {
    ClockTheme {
        LapItem(lapItem = Lap("00:00:00"), index = 1)
    }
}

@OptIn(ExperimentalAnimationApi::class, kotlin.time.ExperimentalTime::class)
@Composable
fun StopwatchScreen(
    modifier: Modifier = Modifier,
    stopwatchViewModel: StopwatchViewModel = viewModel(),
) {
    var lapHeaderLine by remember { mutableStateOf(false) }
    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    stopwatchViewModel.apply {
        Surface(modifier = modifier) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment1.CenterHorizontally
                ) {
                    stopwatchViewModel.apply {
                        Spacer(modifier = Modifier.height(122.dp))
                        StopWatchTime(
                            hours = hours,
                            minutes = minutes,
                            seconds = seconds
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        AnimatedVisibility(visible = lapHeaderLine) {
                            DividerItem()
                        }

                        LapsItems(
                            lapsItems = lapItems,
                            scrollState = scrollState
                        )
                        Buttons(
                            isPlaying = isPlaying,
                            onPause = { pause() },
                            onStart = { start() },
                            onStop = { stop() },
                            onLap = { onLap() },
                            onClear =  {onClear() },
                            onLapHeadLine = { lapHeaderLine = it },
                            scrollState = scrollState,
                            scope = scope,
                            lapsItems = lapItems
                        )

                    }
                }
            }

        }
    }



}

@Composable
private fun StopWatchTime(
    hours: String,
    minutes: String,
    seconds: String
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
    scrollState: LazyListState
) {
    LazyColumn (
        modifier = Modifier
            .height(300.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = 15.dp),
        reverseLayout = true,
        state = scrollState
        ) {
        itemsIndexed(lapsItems) {  index, lapItem ->
            LapItem(lapItem, index)
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
    onLapHeadLine: (Boolean) -> Unit,
    scrollState: LazyListState,
    scope: CoroutineScope,
    lapsItems: List<Lap>

) {
        Box {
            var startVisible by remember { mutableStateOf(true) }
            val transition = updateTransition(startVisible)

            transition.AnimatedVisibility(
                visible = { targetSelected -> targetSelected }
            ) {
                StopWatchButton(
                    onClick = {
                        onStart()
                        startVisible = false
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
                        StopWatchButton(
                            textButton = "Stop",
                            onClick = onPause,
                            color = Red100
                        )
                        StopWatchButton(
                            textButton = "Lap",
                            onClick = {
                                onLapHeadLine(true)
                                onLap()
                                scope.launch {
                                    scrollState.animateScrollToItem(index = lapsItems.lastIndex)
                                }
                            },
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    } else {
                        StopWatchButton(
                            textButton = "Resume",
                            onClick = onStart,
                            color = MaterialTheme.colorScheme.primary
                        )
                        StopWatchButton(
                            textButton = "Reset",
                            onClick = {
                                onStop()
                                onLapHeadLine(false)
                                startVisible = true
                                onClear()
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

@Composable
fun StopWatchButton(
    textButton: String,
    onClick: () -> Unit,
    color: Color,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = color
        ),
        contentPadding = PaddingValues(
            start = 40.dp,
            top = 12.dp,
            end = 40.dp,
            bottom = 12.dp
        )
    ) {
        Text(text = textButton,
            style = MaterialTheme.typography.titleMedium)
    }
}
