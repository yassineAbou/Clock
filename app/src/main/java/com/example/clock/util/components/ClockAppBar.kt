package com.example.clock.util.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.clock.ui.theme.ClockTheme

@Composable
fun ClockAppBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    val backgroundColors = TopAppBarDefaults.smallTopAppBarColors()
    val backgroundColor = backgroundColors.containerColor(
        scrollFraction = scrollBehavior?.scrollFraction ?: 0f
    ).value
    val foregroundColors = TopAppBarDefaults.smallTopAppBarColors(
        containerColor = Color.Transparent,
        scrolledContainerColor = Color.Transparent
    )

    Box(modifier = Modifier.background(backgroundColor)) {
        SmallTopAppBar(
            modifier = modifier,
            title = title,
            scrollBehavior = scrollBehavior,
            colors = foregroundColors,
            actions = actions
        )
    }
}


@Preview
@Composable
private fun ClockAppBarPreview() {
    ClockTheme {
        ClockAppBar(title = { Text("Alarm") })
    }
}

@Preview
@Composable
private fun ClockAppBarPreviewDark() {
    ClockTheme(darkTheme = true) {
        ClockAppBar(title = { Text("Alarm") })
    }
}


