package com.example.clock.util.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ClockButton(
    text: String,
    onClick: () -> Unit,
    color: Color = MaterialTheme.colorScheme.primary,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            disabledContainerColor = color.copy(alpha = 0.1f)
        ),
        contentPadding = PaddingValues(
            start = 40.dp,
            top = 12.dp,
            end = 40.dp,
            bottom = 12.dp
        ),
        enabled = enabled
    ) {
        Text(text = text,
            style = MaterialTheme.typography.titleMedium)
    }
}