package com.example.clock.world

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.clock.components.ClockAppBar
import com.example.clock.ui.theme.ClockTheme
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Preview
@Composable
fun CurrentTimeScreenPreview() {
    ClockTheme {
        CurrentTimeScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentTimeScreen(
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarScrollState())

    Surface(modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize()) {
            WorldClockTopAppBar(
                modifier = Modifier.statusBarsPadding(),
                scrollBehavior = scrollBehavior
            )
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                DigitalClock()
                AnalogClock()
            }
        }
    }

}

@Composable
private fun WorldClockTopAppBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    ClockAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = "Current Time",
                style = MaterialTheme.typography.titleLarge,
            )
        }
    )
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DigitalClock(modifier: Modifier = Modifier) {
        var time by remember { mutableStateOf("04:37:34 PM") }

        LaunchedEffect(0) {
            while (true) {
                val calendar = Calendar.getInstance()
                time = SimpleDateFormat("hh:mm:ss a").format(calendar.time)
                delay(1000)
            }
        }

        Text(
            text = " $time",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )

}

@Composable
fun AnalogClock() {
    Box(
        modifier = Modifier.size(350.dp),
        contentAlignment = Alignment.Center
    ) {
        var time by remember {
            mutableStateOf(
                TimeInFloat(
                    hour = 0f,
                    minute = 0f,
                    second = 0f
                )
            )
        }

        LaunchedEffect(0) {
               while (true) {
                   val calendar = Calendar.getInstance()

                   val hour = calendar.get(Calendar.HOUR)
                   val minute = calendar.get(Calendar.MINUTE)
                   val second = calendar.get(Calendar.SECOND)

                   time = TimeInFloat(
                       hour = ((hour + (minute / 60f)) * 6f * 5),
                       minute = minute * 6f,
                       second = second * 6f
                   )
                   delay(1000)
               }
        }



        Canvas(modifier = Modifier.size(250.dp)) {

            for (angle in 0..60) {
                rotate(angle * 6f) {
                    if (angle % 5 == 0) {
                        drawLine(
                            color = Color.Black,
                            start = Offset(size.width / 2, 0f),
                            end = Offset(size.width / 2, 40f),
                            strokeWidth = 4f
                        )
                    } else {
                        drawLine(
                            color = Color.Black,
                            start = Offset(size.width / 2, 15f),
                            end = Offset(size.width / 2, 25f),
                            strokeWidth = 4f
                        )
                    }
                }
            }

            rotate(time.hour) {
                drawLine(
                    color = Color.Black ,
                    start = Offset(size.width / 2 , size.width / 2),
                    end = Offset(size.width / 2 , 200f),
                    strokeWidth = 14f
                )
            }

            rotate(time.minute) {
                drawLine(
                    color = Color.Black ,
                    start = Offset(size.width / 2 , size.width / 2 + 40),
                    end = Offset(size.width / 2 , 75f),
                    strokeWidth = 10f
                )
            }

            rotate(time.second) {
                drawLine(
                    color = Color.Red,
                    start = Offset(size.width / 2 , size.width / 2),
                    end = Offset(size.width / 2 , 75f),
                    strokeWidth = 6f
                )
            }

            drawCircle(
                color = Color.Black ,
                radius = 20f
            )
        }
    }
}

data class TimeInFloat(
    var hour: Float,
    var minute: Float,
    var second: Float
)







