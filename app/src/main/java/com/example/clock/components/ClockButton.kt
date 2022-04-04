package com.example.clock.components

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
    textButton: String,
    onClick: () -> Unit,
    color: Color = MaterialTheme.colorScheme.primary,
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