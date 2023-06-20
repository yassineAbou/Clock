package com.example.clock.ui.clock

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clock.R
import com.example.clock.ui.theme.ClockTheme
import com.example.clock.util.components.ClockAppBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

@Preview(device = Devices.PIXEL_4_XL)
@Composable
fun ClockScreenPreview() {
    ClockTheme {
        ClockScreen()
    }
}

@Preview(device = Devices.TABLET, uiMode = Configuration.ORIENTATION_PORTRAIT, widthDp = 768, heightDp = 1024)
@Composable
fun ClockScreenDarkPreview() {
    ClockTheme(useDarkTheme = true) {
        ClockScreen()
    }
}

@Composable
fun ClockScreen(
    modifier: Modifier = Modifier,
) {
    Surface(modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize()) {
            ClockScreenAppBar(
                modifier = Modifier.statusBarsPadding(),
            )
            SlidingClock()
        }
    }
}

@Composable
private fun ClockScreenAppBar(modifier: Modifier = Modifier) {
    ClockAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.titleLarge,
            )
        },
    )
}

data class Time(val hour: Int, val minute: Int, val second: Int)

@Composable
private fun SlidingClock() {
    fun currentTime(): Time {
        val calendar = Calendar.getInstance()
        return Time(
            hour = calendar.get(Calendar.HOUR_OF_DAY),
            minute = calendar.get(Calendar.MINUTE),
            second = calendar.get(Calendar.SECOND),
        )
    }

    var time by remember { mutableStateOf(currentTime()) }
    LaunchedEffect(0) {
        this.launch {
            while (true) {
                time = currentTime()
                delay(1000)
            }
        }
    }

    Clock(time)
}

@Composable
private fun Clock(time: Time) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val paddingSize = dimensionResource(id = com.intuit.sdp.R.dimen._2sdp)
        val padding = Modifier.padding(horizontal = paddingSize)

        NumberColumn(time.hour / 10, 0..2, padding)
        NumberColumn(time.hour % 10, 0..9, padding)

        Spacer(Modifier.size(16.dp))

        NumberColumn(time.minute / 10, 0..5, padding)
        NumberColumn(time.minute % 10, 0..9, padding)

        Spacer(Modifier.size(16.dp))

        NumberColumn(time.second / 10, 0..5, padding)
        NumberColumn(time.second % 10, 0..9, padding)
    }
}

@Composable
private fun NumberColumn(
    current: Int,
    range: IntRange,
    modifier: Modifier = Modifier,
) {
    val size = dimensionResource(id = com.intuit.sdp.R.dimen._23sdp)
    val mid = (range.last - range.first) / 2f
    val reset = current == range.first
    val offset by animateDpAsState(
        targetValue = size * (mid - current),
        animationSpec = if (reset) {
            spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow,
            )
        } else {
            tween(
                durationMillis = 300,
                easing = LinearOutSlowInEasing,
            )
        },
    )

    Column(
        modifier
            .offset(y = offset)
            .clip(RoundedCornerShape(percent = 25)),
    ) {
        range.forEach { num ->
            Number(num == current, num, Modifier.size(size))
        }
    }
}

@Composable
private fun Number(active: Boolean, value: Int, modifier: Modifier = Modifier) {
    val backgroundColor by animateColorAsState(
        if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
    )

    Box(
        modifier = modifier.background(backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        val fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._20ssp).value.sp
        Text(
            text = value.toString(),
            fontSize = fontSize,
            color = Color.White,
        )
    }
}
