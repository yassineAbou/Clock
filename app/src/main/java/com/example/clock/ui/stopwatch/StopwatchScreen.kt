package com.example.clock.ui.stopwatch

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clock.R
import com.example.clock.util.components.ClockButton

import com.example.clock.data.manager.StopwatchState
import com.example.clock.ui.theme.Red100
import com.example.clock.util.components.ClockAppBar
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime




private const val TAG = "StopwatchScreen"

@OptIn(ExperimentalAnimationApi::class, ExperimentalTime::class, ExperimentalTransitionApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun StopwatchScreen(
    modifier: Modifier = Modifier,
    stopwatchViewModel: StopwatchViewModel = hiltViewModel()
    ) {
    val stopwatchState: StopwatchState by stopwatchViewModel.stopwatchState.observeAsState(
        StopwatchState(second = "00", minute = "00", hour = "00", isReset = true, isPlaying = false)
    )
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarScrollState())
    var lineSeparator by rememberSaveable { mutableStateOf(stopwatchViewModel.listTimes.isNotEmpty()) }
    val scrollState = rememberLazyListState()
    var isReset by rememberSaveable { mutableStateOf(stopwatchState.isReset) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(stopwatchState.isReset, stopwatchViewModel.listTimes.isEmpty()) {
        isReset = stopwatchState.isReset
        lineSeparator = stopwatchViewModel.listTimes.isNotEmpty()
        if (stopwatchViewModel.listTimes.isNotEmpty()) {
            scrollState.animateScrollToItem(stopwatchViewModel.listTimes.lastIndex)
        }
    }


    Surface(modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize()) {
            StopwatchScreenAppBar(
                modifier = Modifier.statusBarsPadding(),
                scrollBehavior = scrollBehavior
            )
            Column(
               modifier = Modifier
                   .fillMaxWidth()
                   .fillMaxHeight(0.9f)
                   .nestedScroll(scrollBehavior.nestedScrollConnection),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Time(
                    modifier = Modifier.padding(WindowInsets.statusBars.add(WindowInsets(top = dimensionResource(id = com.intuit.sdp.R.dimen._90sdp))).asPaddingValues()),
                    hours = stopwatchState.hour,
                    minutes = stopwatchState.minute,
                    seconds = stopwatchState.second
                )
                AnimatedVisibility(visible = lineSeparator) {
                    LineSeparator(modifier = Modifier.padding(dimensionResource(id = com.intuit.sdp.R.dimen._20sdp)))
                }
                ListTimes(
                    modifier = Modifier
                        .fillMaxWidth(),
                    listTimes = stopwatchViewModel.listTimes,
                    scrollState = scrollState,
                )

            }
            Buttons(
                stopwatchViewModel = stopwatchViewModel,
                isPlaying = stopwatchState.isPlaying,
                scrollState = scrollState,
                scope = scope,
                isReset = isReset,
                onChangeIsReset = {
                    isReset = it
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(dimensionResource(id = com.intuit.sdp.R.dimen._7sdp))
            )
        }

    }

}

@Composable
private fun StopwatchScreenAppBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    ClockAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = stringResource(id = R.string.stopwatch),
                style = MaterialTheme.typography.titleLarge,
            )
        }
    )
}

@Composable
private fun Time(
    hours: String,
    minutes: String,
    seconds: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        TimeUnit(unit = hours)
        TimeUnit(unit = ":")
        TimeUnit(unit = minutes)
        TimeUnit(unit = ":")
        TimeUnit(unit = seconds)
    }
}

@Composable
private fun TimeUnit(unit: String) {
    Text(
        text = unit,
        style = MaterialTheme.typography.displayLarge,
        fontWeight = FontWeight.Light
    )
}


@Composable
private fun LineSeparator(modifier: Modifier = Modifier) {
    Divider(
        modifier = modifier,
        color =  MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    )
}

@Composable
private fun ListTimes(
    listTimes: List<String>,
    scrollState: LazyListState,
    modifier: Modifier = Modifier
) {
    val contentPadding  = dimensionResource(id = com.intuit.sdp.R.dimen._5sdp)
    val verticalPadding  = dimensionResource(id = com.intuit.sdp.R.dimen._8sdp)

    LazyColumn (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(contentPadding),
        reverseLayout = true,
        state = scrollState
        ) {
        itemsIndexed(listTimes) { index, item ->
            TimeItem(
                item = item,
                index = index,
                modifier = Modifier
                    .padding(vertical = verticalPadding)
                    .fillMaxWidth()
            )
        }

    }

}

@Composable
fun TimeItem(item: String, index: Int, modifier: Modifier = Modifier) {
    Row(
     modifier = modifier,
     horizontalArrangement = Arrangement.SpaceEvenly
    ) {
            Text(
                text = "$index",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
                )
            Text(
                text = item,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
            )
        }
}


@OptIn(ExperimentalAnimationApi::class, ExperimentalTime::class)
@Composable
private fun Buttons(
    stopwatchViewModel: StopwatchViewModel,
    isPlaying: Boolean,
    scrollState: LazyListState,
    scope: CoroutineScope,
    isReset: Boolean,
    onChangeIsReset: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val transition = updateTransition(isReset, label = stringResource(id = R.string.is_reset))
    Box(
        modifier = modifier
    ) {

        transition.AnimatedVisibility(
            visible = { isTargetReset -> isTargetReset },
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
                onClick = {
                    stopwatchViewModel.start()
                    onChangeIsReset(false)
                },
                text = stringResource(id = R.string.start),
                color = MaterialTheme.colorScheme.primary
            )
        }
        transition.AnimatedVisibility(
            visible = { isTargetReset -> !isTargetReset },
            enter = expandHorizontally(
                animationSpec = tween(
                    durationMillis = 500,
                    easing = LinearOutSlowInEasing
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
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                if (isPlaying) {
                    ClockButton(
                        text = stringResource(id = R.string.stop),
                        onClick = { stopwatchViewModel.stop() },
                        color = Red100
                    )
                    ClockButton(
                        text = stringResource(id = R.string.lap),
                        onClick = {
                            stopwatchViewModel.addTime()
                            scope.launch {
                                scrollState.animateScrollToItem(index = stopwatchViewModel.listTimes.lastIndex)
                            }
                        },
                        color = MaterialTheme.colorScheme.onSurface
                    )
                } else {
                    ClockButton(
                        text = stringResource(id = R.string.resume),
                        onClick = { stopwatchViewModel.start() },
                        color = MaterialTheme.colorScheme.primary
                    )
                    ClockButton(
                        text = stringResource(id = R.string.reset),
                        onClick = {
                            stopwatchViewModel.reset()
                            stopwatchViewModel.clearListTimes()
                            onChangeIsReset(true)
                        },
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

        }
    }
}




