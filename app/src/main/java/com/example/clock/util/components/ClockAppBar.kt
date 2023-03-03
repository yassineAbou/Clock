package com.example.clock.util.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    /*
    val backgroundColors = TopAppBarDefaults.smallTopAppBarColors()
    val backgroundColor = backgroundColors.containerColor(
        scrollFraction = scrollBehavior?.scrollFraction ?: 0f,
    ).value

     */
    val foregroundColors = TopAppBarDefaults.smallTopAppBarColors(
        containerColor = Color.Transparent,
        scrolledContainerColor = Color.Transparent,
    )

    Box(
        modifier = Modifier,
    ) {
        TopAppBar(
            title = title,
            modifier = modifier.height(48.dp),
            actions = actions,
            colors = foregroundColors,
            scrollBehavior = scrollBehavior,
        )
    }
}

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
