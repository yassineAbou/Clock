package com.example.clock.util.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp

@Composable
fun CustomChip(
    isChecked: Boolean,
    text: String,
    onChecked: (Boolean) -> Unit,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(
                vertical = 2.dp,
                horizontal = 4.dp,
            )
            .border(
                width = 1.dp,
                color = if (isChecked) selectedColor else MaterialTheme.colorScheme.onSurface,
                shape = CircleShape,
            )
            .background(
                color = if (isChecked) selectedColor else Transparent,
                shape = CircleShape,
            )
            .clip(shape = CircleShape)
            .clickable {
                onChecked(!isChecked)
            }
            .padding(4.dp),
    ) {
        Text(
            text = text,
            color = if (isChecked) White else Unspecified,
        )
    }
}
