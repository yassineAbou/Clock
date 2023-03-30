package com.example.clock.util.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.clock.ui.theme.ClockTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClockAppBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    TopAppBar(
        modifier = modifier.height(48.dp),
        title = title,
        actions = actions,
        scrollBehavior = scrollBehavior,
    )
}

/*
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun ClockAppBarPreview() {
    ClockTheme {
        ClockAppBar(title = { Text("Alarm") })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun ClockAppBarPreviewDark() {
    ClockTheme(darkTheme = true) {
        ClockAppBar(title = { Text("Alarm") })
    }
}

 */
