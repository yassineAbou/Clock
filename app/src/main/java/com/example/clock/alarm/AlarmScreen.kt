package com.example.clock.alarm

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.tooling.preview.Preview
import com.example.clock.components.ClockAppBar
import com.example.clock.ui.theme.ClockTheme
import com.google.accompanist.insets.statusBarsPadding

@Preview
@Composable
fun AlarmScreenPreview() {
    ClockTheme {
        AlarmScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmScreen(
    modifier: Modifier = Modifier
) {
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }

    Surface(modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
            ) {
                AlarmTopAppBar(
                    modifier = Modifier.statusBarsPadding(),
                    scrollBehavior = scrollBehavior
                )
            }
        }
    }

}

@Composable
private fun AlarmTopAppBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    ClockAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = "Alarm",
                style = MaterialTheme.typography.titleLarge,
            )
        }
    )
}
